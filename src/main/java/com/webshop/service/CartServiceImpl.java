package com.webshop.service;

import com.webshop.constants.Constants;
import com.webshop.entity.*;
import com.webshop.model.Cart;
import com.webshop.model.CartLine;
import com.webshop.model.CustomerInfo;
import com.webshop.repository.OrderCostRepository;
import com.webshop.repository.TransportCostRepository;
import com.webshop.utils.SessionsStock;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final TransportCostRepository transportCostRepository;
    private final MessageSource messageSource;
    private final ProductService productService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final OrderDetailsService orderDetailsService;
    private final OrderCostRepository orderCostRepository;
    private final CacheManager cacheManager;

    @Override
    public void setTransportCost(Cart cartInfo) {
        if (cartInfo.getCustomerInfo()== null || cartInfo.getCustomerInfo().getCountry()==null)
            return;;
        TransportCost transportCost = transportCostRepository.findByCountryCode(cartInfo.getCustomerInfo().getCountry().getCode());
        if (transportCost==null)
            return;
        OrderCost orderCost = new OrderCost();
        BigDecimal transportCounter = new BigDecimal(0);
        for (CartLine line : cartInfo.getCartLines()){
            transportCounter = transportCounter.add(line.getProduct().getProductDetails().getTransportPoints().multiply(new BigDecimal(line.getQuantity())));
        }
        orderCost.setPrice(transportCost.getCostPrice());
        int quantityCost = transportCounter.divide(transportCost.getPoints(), 0, RoundingMode.CEILING).intValue();
        orderCost.setQuantity(quantityCost);
        orderCost.setVat(transportCost.getVat());
        orderCost.setDescription(messageSource.getMessage("pakket.zegel", null, LocaleContextHolder.getLocale()));
        List<OrderCost> orderCosts = new ArrayList<>();
        orderCosts.add(orderCost);
        cartInfo.setOrderCost(orderCosts);
    }

    @Override
    public synchronized void updateQuantitiesCartLine(Cart sessionCart, CartLine sessionLine, CartLine line) {
        ProductDetails prdDetails = productService.findById(line.getProduct().getProductDetails().getId());
        if (prdDetails == null) {
            return;
        }
        if (sessionLine != null && line.getQuantity()>=0) {
            int newQuantity = SessionsStock.getLimit(line.getProduct().getProductDetails().getId(), line.getQuantity() - sessionLine.getQuantity(), prdDetails.getStock());
            if (sessionLine.getQuantity() + newQuantity > 0) {
                sessionLine.setQuantity(sessionLine.getQuantity() + newQuantity);
                sessionLine.getProduct().setProductDetails(prdDetails);
            } else {
                sessionCart.removeCartLine(sessionLine);
            }
        } else if (line.getQuantity()>=0) {
            int newQuantity = SessionsStock.getLimit(line.getProduct().getProductDetails().getId(), line.getQuantity(), prdDetails.getStock());
            if (newQuantity>0) {
                line.setQuantity(newQuantity);
                sessionCart.addCartLine(line);
            }
        }

    }

    @Override
    public synchronized void updateQuantitiesCart(Cart sessionCart, Cart formCart) {
        if (formCart != null) {
            List<CartLine> lines = formCart.getCartLines();
            for (CartLine line : lines) {
                CartLine sessionLine = sessionCart.findLineByCode(line.getProduct().getProductDetails().getCode());
                updateQuantitiesCartLine(sessionCart, sessionLine, line);
            }
            setTransportCost(sessionCart);
        }

    }

    @Override
    public synchronized Map<Long, Integer> getStockShoppingCart(Cart cart) {
        Map<Long, Integer> cartStock = new TreeMap<>();
        for (CartLine line : cart.getCartLines()) {
            ProductDetails prodDet = productService.findById(line.getProduct().getProductDetails().getId());
            int stock= prodDet.getStock() - SessionsStock.getQuantity(prodDet.getId()) + line.getQuantity();
            cartStock.put(line.getProduct().getProductDetails().getId(), stock);
        }
        return cartStock;
    }

    @Override
    public synchronized Order saveCartToOrder(Cart myCart, Principal principal) {
        Date today = new Date();
        CustomerInfo customer = myCart.getCustomerInfo();
        Order order = new Order(today, customer.getName(), customer.getAddress(), customer.getPostCode(), customer.getCity(),
                customer.getCountry(), customer.getEmail(), customer.getPhone(), customer.getBtwNr());
        if (principal != null && principal.getName() != null) {
            Customer regCustomer = customerService.findByUsername(principal.getName());
            order.setCustomer(regCustomer);
        }
        order.setStatus(Constants.ORDER_CREATED);
        orderService.saveOrder(order);
        for (CartLine cartLine : myCart.getCartLines()) {
            BigDecimal discount = new BigDecimal(0);
            OrderDetails orderDetails = new OrderDetails(order, cartLine.getProduct(), cartLine.getProduct().getName(),
                    cartLine.getProduct().getProductDetails().getPrice(), cartLine.getProduct().getProductDetails().getBtw(),
                    cartLine.getProduct().getProductDetails().getUnit(),discount, cartLine.getQuantity());
            orderDetailsService.addOrderDetails(orderDetails);
            order.addOrderDetail(orderDetails);
        }
        for (OrderCost orderCost : myCart.getOrderCosts()) {
            orderCost.setOrder(order);
            order.addOrderCost(orderCost);
            orderCostRepository.save(orderCost);
        }
        updateStocksAfterOrder(order);
        return order;
    }

    private synchronized void updateStocksAfterOrder(Order order) {
        for (OrderDetails orderDetails : order.getOrderDetails()) {
            productService.updateStock(orderDetails.getProduct().getProductDetails(), -orderDetails.getQuantity());
        }
        clearCache();
    }

    private void clearCache(){
        for(String name:cacheManager.getCacheNames()){
            cacheManager.getCache(name).clear();            // clear cache by name
        }

    }


}
