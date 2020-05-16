package com.webshop.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return MultipartFile.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MultipartFile multipartFile = (MultipartFile) target;
        String mimeType = multipartFile.getContentType();
        String type = mimeType.split("/")[0];
        if (!type.equalsIgnoreCase("image")) {
            errors.rejectValue("file", "lang.error.fileNoImage");;
        }
        if (multipartFile.getSize()> 1280000 )
            errors.rejectValue("file", "lang.error.fileToLarge");


    }
}
