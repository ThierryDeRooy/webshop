package com.webshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class WebErrorController implements ErrorController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(path="/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            log.error(statusCode + ": " + request.getAttribute(RequestDispatcher.ERROR_EXCEPTION) + " - " + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "error-404";
                return "error";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
 //               return "error-500";
                return "error";
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
