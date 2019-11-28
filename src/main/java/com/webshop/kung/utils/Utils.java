package com.webshop.kung.utils;

import com.webshop.kung.entity.Order;
import com.webshop.kung.model.Cart;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    // Products in the cart, stored in Session.

//    public static Order getCartInSession(HttpServletRequest request) {
//        Order order = (Order) request.getSession().getAttribute("myOrders");
//
//        if (order==null) {
//            order = new Order();
//            request.getSession().setAttribute("myOrders", order);
//        }
//        return order;
//    }
//
//    public static void removeCartInSession(HttpServletRequest request) {
//        request.getSession().removeAttribute("myOrders");
//    }

    public static Cart getCartInSession(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute("myOrders");

        if (cart==null) {
            cart = new Cart();
            request.getSession().setAttribute("myOrders", cart);
        }
        return cart;
    }

    public static void removeCartInSession(HttpServletRequest request) {
        request.getSession().removeAttribute("myOrders");
    }

    public static void storeLastOrderedCartInSession(HttpServletRequest request, Cart cartInfo) {
        request.getSession().setAttribute("lastOrderedCart", cartInfo);
    }

    public static Cart getLastOrderedCartInSession(HttpServletRequest request) {
        return (Cart) request.getSession().getAttribute("lastOrderedCart");
    }


}
