package com.webshop.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Controller
public class MyErrorController extends ResponseEntityExceptionHandler {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor ste = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, ste);
//        dataBinder.setValidator(fileValidator);
    }

    @ExceptionHandler(MultipartException.class)
    //   @ResponseBody
//    @ModelAttribute
    public RedirectView handleFileException(HttpServletRequest request, MultipartException ex, Model model) {
  //       return "Photo file exceeds its MAX size  <a href='../addNewProduct'>BACK TO PRODUCT ADMINISTRATION</a>";
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("saveProduct");
//        mav.addObject("model", model);
//        return mav;

        RedirectView redirectView = new RedirectView("addNewProduct");
        redirectView.addStaticAttribute("model", model);
        FlashMap flash = RequestContextUtils.getOutputFlashMap(request);
        flash.put("generalError", "Please enter a photo file NOT larger than 1280KB !");
//        flash.put("model", "lang.error.fileRejected");
//        String retVal = "addNewProduct";
        return redirectView;
    }


}
