package com.webshop.config;

import com.webshop.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class HttpSessionConfig {

    private static final Map<String, HttpSession> sessions = new Hashtable<>();
    private static final Logger log = LoggerFactory.getLogger(HttpSessionConfig.class);

//    private final ProductService productService;
//    private final CacheManager cacheManager;

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent hse) {
//                hse.getSession().setMaxInactiveInterval(5);
                sessions.put(hse.getSession().getId(), hse.getSession());
                log.info("SESSION CREATED: " + hse.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent hse) {
                log.info((new Date()).toString() + " SESSION DESTROYED: " + hse.getSession().getId() + " time="
                        + new Date(hse.getSession().getLastAccessedTime()));
//                removeReservedStock(hse.getSession());
                Utils.removeCartFromSessionsStock(hse.getSession());
                sessions.remove(hse.getSession().getId());

            }
        };
    }

    public static List<HttpSession> getActiveSessions() {
        return new ArrayList<>(sessions.values());
    }


//    private void removeReservedStock(HttpSession httpSession) {
//        Cart cart = (Cart) httpSession.getAttribute("myOrders");
//        if (cart != null) {
//            for (CartLine line : cart.getCartLines()) {
//                log.info("Return Basket of Product: " + line.getProduct().getName() + " - " + line.getQuantity()+ " units");
//                productService.updateReservedStock(line.getProduct().getProductDetails(), 0, line.getQuantity());
//                clearCache();
//            }
//        }
//    }
//
//    private void clearCache(){
//        for(String name:cacheManager.getCacheNames()){
//            cacheManager.getCache(name).clear();            // clear cache by name
//        }
//
//    }


}
