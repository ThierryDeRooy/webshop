package com.webshop.utils;

import com.webshop.config.HttpSessionConfig;
import com.webshop.model.Cart;
import com.webshop.model.CartLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SessionsStock {

//    private static Map<Long , Integer> stockInBasket = new TreeMap<>();
    private static final Logger log = LoggerFactory.getLogger(SessionsStock.class);

//    public static void add(long detailsId, int quantity) {
//        log.debug("ADD TO SESSIONSSTOCK: ProductDetailsId="+detailsId+", quantity="+quantity);
//        if (stockInBasket.containsKey(detailsId)) {
//            stockInBasket.replace(detailsId, stockInBasket.get(detailsId) + quantity);
//        } else {
//            stockInBasket.put(detailsId, quantity);
//        }
//    }
//
//    public static void reset(long detailsId) {
//        log.debug("RESET SESSIONSSTOCK: ProductDetailsId="+detailsId);
//        if (stockInBasket.containsKey(detailsId))
//            stockInBasket.put(detailsId, 0);
//    }

    public static int getLimit(long detailsId, int quantity, int quantityLimit) {
        int retVal = quantity;
        int stock = 0;
        Map<Long , Integer> stockInBasket = getAllStockInSessions();
        if (stockInBasket.containsKey(detailsId)) {
            stock = stockInBasket.get(detailsId);
        }
//        if (stock > quantityLimit)
//            return 0;
        if (stock+quantity > quantityLimit) {
            retVal = quantityLimit - stock;
        }
//        add(detailsId, retVal);
        return retVal;
    }

    public static int getQuantity(long detailsId) {
        int retVal = 0;
        Map<Long , Integer> stockInBasket = getAllStockInSessions();
        if (stockInBasket.containsKey(detailsId))
            retVal = stockInBasket.get(detailsId);
        return retVal;
    }

    public static Map<Long, Integer> getSessionsStock() {
        return getAllStockInSessions();
    }

    public synchronized static Map<Long, Integer> getAllStockInSessions() {
        Map<Long , Integer> stockInBasketNew = new TreeMap<>();
        List<HttpSession> sessions = HttpSessionConfig.getActiveSessions();
        for (HttpSession session : sessions) {
            Cart cart = (Cart) session.getAttribute("myOrders");
            if (cart != null) {
                for (CartLine line : cart.getCartLines()) {
                    long key = line.getProduct().getProductDetails().getId();
                    if (stockInBasketNew.containsKey(key)) {
                        stockInBasketNew.replace(key, stockInBasketNew.get(key)+line.getQuantity());
                    } else {
                        stockInBasketNew.put(key, line.getQuantity());
                    }
                }
            }
        }
        return stockInBasketNew;
    }
}
