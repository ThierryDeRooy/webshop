package com.webshop.model;

import com.webshop.entity.OrderCost;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart {

    @Valid
    private List<CartLine> cartLines = new ArrayList<>();
    @Valid
    private CustomerInfo customerInfo;
    private Locale locale;

    private List<OrderCost> orderCosts = new ArrayList<>();

    public Cart(){}

    public List<CartLine> getCartLines() {
        return cartLines;
    }

    public void setCartLines(List<CartLine> cartLines) {
        this.cartLines = cartLines;
    }

    public void addCartLine(CartLine cartLine) {
        CartLine line = this.findLineByCode(cartLine.getProduct().getProductDetails().getCode());

        if (line == null) {
            cartLines.add(cartLine);
        } else {
            line.setQuantity(line.getQuantity() + cartLine.getQuantity());
        }
    }

    public void removeCartLine(CartLine cartLine){
        cartLines.remove(cartLine);
    }

    public void removeCartLine(long id) {
        for (CartLine cartLine : cartLines) {
            if (id == cartLine.getProduct().getProductDetails().getId()) {
                removeCartLine(cartLine);
                break;
            }
        }
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totPrice = new BigDecimal(0);
        for (CartLine cartLine: cartLines) {
            totPrice = totPrice.add(cartLine.getTotalPrice());
        }
        for (OrderCost orderCost : orderCosts){
            totPrice = totPrice.add(orderCost.getTotalPrice());
        }
        return totPrice;
    }
    public BigDecimal getTotalPriceInclBtw() {
        BigDecimal totPrice = new BigDecimal(0);
        for (CartLine cartLine: cartLines) {
            totPrice = totPrice.add(cartLine.getTotalPriceInclBtw());
        }
        for (OrderCost orderCost : orderCosts){
            totPrice = totPrice.add(orderCost.getTotalPriceInclVat());
        }
        return totPrice;
    }

    public int getNumberOfProducts() {
        int quantity = 0;
        for (CartLine line : cartLines) {
            quantity = quantity + line.getQuantity();
        }
        return quantity;
//        return cartLines.size();
    }

//    public void updateQuantity(Cart cartForm) {
//        if (cartForm != null) {
//            List<CartLine> lines = cartForm.getCartLines();
//            for (CartLine line : lines) {
//                this.updateProduct(line.getProduct().getProductDetails().getCode(), line.getQuantity());
//            }
//        }
//
//    }

//    public void updateProduct(String code, int quantity) {
//        CartLine line = this.findLineByCode(code);
//
//        if (line != null) {
//            if (quantity <= 0) {
//                this.cartLines.remove(line);
//            } else {
//                line.setQuantity(quantity);
//            }
//        }
//    }


    public CartLine findLineByCode(String code) {
        for (CartLine line : this.cartLines) {
            if (line.getProduct().getProductDetails().getCode().equals(code)) {
                return line;
            }
        }
        return null;
    }


    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public void addOrderCost(OrderCost orderCost) {
        orderCosts.add(orderCost);
    }

    public void setOrderCost(List<OrderCost> orderCosts) {
        this.orderCosts = orderCosts;
    }

    public List<OrderCost> getOrderCosts() {
        return orderCosts;
    }

}
