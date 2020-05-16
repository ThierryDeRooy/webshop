package com.webshop.service;

import com.webshop.entity.Order;
import com.webshop.model.Cart;
import com.webshop.model.CartLine;

import java.security.Principal;
import java.util.Map;

public interface CartService {
    void setTransportCost(Cart cartInfo);
    void updateQuantitiesCartLine(Cart sessionCart, CartLine sessionLine, CartLine line);
    void updateQuantitiesCart(Cart sessionCart, Cart formCart);
    Map<Long, Integer> getStockShoppingCart(Cart cart);
    Order saveCartToOrder(Cart myCart, Principal principal);
}
