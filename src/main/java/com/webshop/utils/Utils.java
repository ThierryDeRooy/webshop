package com.webshop.utils;

import com.webshop.config.HttpSessionConfig;
import com.webshop.entity.Order;
import com.webshop.model.Cart;
import com.webshop.model.CartLine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Utils {


    public static Cart getCartInSession(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute("myOrders");

        if (cart==null) {
            cart = new Cart();
            request.getSession().setAttribute("myOrders", cart);
        }
        return cart;
    }

    public static void removeCartInSession(HttpServletRequest request) {
        removeCartFromSessionsStock(request.getSession());
        request.getSession().removeAttribute("myOrders");
    }

    public static void removeCartFromSessionsStock(HttpSession session){
        Cart cart = (Cart) session.getAttribute("myOrders");
        if (cart != null) {
            for (CartLine line : cart.getCartLines()) {
                SessionsStock.add(line.getProduct().getProductDetails().getId(), -line.getQuantity());
            }
        }
    }

    public static void storeLastOrderedCartInSession(HttpServletRequest request, Cart cartInfo) {
        request.getSession().setAttribute("lastOrderedCart", cartInfo);
    }

    public static Cart getLastOrderedCartInSession(HttpServletRequest request) {
        return (Cart) request.getSession().getAttribute("lastOrderedCart");
    }


    public static void setOrderInSession(HttpServletRequest request, Order order) {
        request.getSession().setAttribute("chosenOrder", order);
    }
    public static Order getOrderInSession(HttpServletRequest request) {
        return (Order) request.getSession().getAttribute("chosenOrder");
    }

    public static void removeOrderInSession(HttpServletRequest request) {
        request.getSession().removeAttribute("chosenOrder");
    }

    public static Object getOrSetAttributeInSession(HttpServletRequest request, String attributeName, Object value, Object defaultvalue) {
        if (value != null) {
            request.getSession().setAttribute(attributeName, value);
            return value;
        }
        Object retVal = request.getSession().getAttribute(attributeName);
        if (retVal != null) {
            return retVal;
        } else {
            request.getSession().setAttribute(attributeName, defaultvalue);
            return defaultvalue;
        }
    }

    public static Map<Long, Integer> getSessionsStock() {
        Map<Long, Integer> sessionsStock = new TreeMap<>();
        List<HttpSession> sessions = HttpSessionConfig.getActiveSessions();
        for (HttpSession session : sessions) {
            Cart cart = (Cart) session.getAttribute("myOrder");
            if (cart != null) {
                for (CartLine line : cart.getCartLines()) {
                    long key = line.getProduct().getProductDetails().getId();
                    if (sessionsStock.containsKey(key)) {
                        sessionsStock.replace(key, sessionsStock.get(key)+line.getQuantity());
                    } else {
                        sessionsStock.put(key, line.getQuantity());
                    }
                }
            }
        }
        return sessionsStock;
    }
}
