package com.webshop.kung.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhotoFileValidator implements ConstraintValidator<PhotoFile, MultipartFile> {
    @Override
    public void initialize(PhotoFile constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile==null || multipartFile.getOriginalFilename()==null || multipartFile.getOriginalFilename().isEmpty())
            return true;
        String mimeType = multipartFile.getContentType();
        String type = mimeType.split("/")[0];
        if (!type.equalsIgnoreCase("image")) {
            return false;
        }
        if (multipartFile.getSize()> 1280000 )
            return false;
        return true;
    }
}
