package com.webshop.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.*;

public class Constants {

//    languages
    public static final List<String> LANGUAGES = new ArrayList<>();
    static {
        LANGUAGES.add("NL");
        LANGUAGES.add("EN");
    }
    public static final Locale DEFAULT_LOCALE = new Locale("EN");

//  Product status
    public static final int PRODUCT_ACTIVE = 0;
    public static final int PRODUCT_NOT_AVAILABLE = 1;
    public static final Map<Integer, String> PRODUCT_STATES = new HashMap<Integer, String>();
    static {
        PRODUCT_STATES.put(PRODUCT_ACTIVE, "ACTIVE");
        PRODUCT_STATES.put(PRODUCT_NOT_AVAILABLE, "NOT AVAILABLE");
    }

//  product price per unit
    public static final int PER_UNIT = 0;
    public static final int PER_WEIGHT_KG = 1;
    public static final int PER_WEIGHT_100G = 2;
    public static final Map<Integer, String> PRODUCT_UNITS = new HashMap<Integer, String>();
    static {
        PRODUCT_UNITS.put(PER_UNIT, "PIECE");
        PRODUCT_UNITS.put(PER_WEIGHT_KG, "KG");
        PRODUCT_UNITS.put(PER_WEIGHT_100G, "100GRAM");
    }

//  order states
    public static final int ORDER_CREATED = 0;
    public static final int ORDER_PAYED = 1;
    public static final int ORDER_READY_FOR_TRANSPORT = 2;
    public static final int ORDER_SENT = 3;
    public static final int ORDER_ARRIVED = 4;
    public static final int ORDER_ARCHIVED = 5;
    public static final Map<Integer, String> ORDER_STATES = new HashMap<Integer, String>();
    static {
        ORDER_STATES.put(ORDER_CREATED, "ORDER_CREATED");
        ORDER_STATES.put(ORDER_PAYED, "ORDER_PAYED");
        ORDER_STATES.put(ORDER_READY_FOR_TRANSPORT, "ORDER_READY_FOR_TRANSPORT");
        ORDER_STATES.put(ORDER_SENT, "ORDER_SENT");
        ORDER_STATES.put(ORDER_ARRIVED, "ORDER_ARRIVED");
        ORDER_STATES.put(ORDER_ARCHIVED, "ORDER_ARCHIVED");
    }


}
