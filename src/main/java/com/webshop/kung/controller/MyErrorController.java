package com.webshop.kung.controller;

import com.webshop.kung.entity.Category;
import com.webshop.kung.entity.Product;
import com.webshop.kung.entity.ProductDetails;
import com.webshop.kung.service.CategoryService;
import com.webshop.kung.service.ProductService;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
