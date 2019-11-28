package com.webshop.kung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {

    @Autowired
    private CacheManager cacheManager;   // autowire cache manager

    // clear all cache using cache manager
    @RequestMapping(value = "clearCache")
    public void clearCache(){
        for(String name:cacheManager.getCacheNames()){
            cacheManager.getCache(name).clear();            // clear cache by name
        }
    }

}
