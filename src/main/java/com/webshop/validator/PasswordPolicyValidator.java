package com.webshop.validator;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class PasswordPolicyValidator implements ConstraintValidator<PasswordPolicy, String> {
    private static final int MIN_COMPLEX_RULES = 3;
    private static final int MIN_UPPER_CASE_CHARS = 1;
    private static final int MIN_LOWER_CASE_CHARS = 1;
    private static final int MIN_DIGIT_CASE_CHARS = 1;
    private static final int MIN_SPECIAL_CASE_CHARS = 1;
    private static final int MAX_REPETATIVE_CHARS = 3;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<String> errorList = getErrors(password);
        if (errorList.isEmpty())
            return true;
        for (String error : errorList) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation();
        }
        return false;
//        return isValid(password);
    }

//    public boolean isValid (String password) {
    public List<String> getErrors (String password) {
        if (password == null)
            password = "";
        List<Rule> passwordRules = new ArrayList<>();
        passwordRules.add(new LengthRule(10, 128));
        CharacterCharacteristicsRule passwordChars = new CharacterCharacteristicsRule(MIN_COMPLEX_RULES,
                new CharacterRule(EnglishCharacterData.UpperCase,MIN_UPPER_CASE_CHARS),
                new CharacterRule(EnglishCharacterData.LowerCase,MIN_LOWER_CASE_CHARS),
                new CharacterRule(EnglishCharacterData.Digit,MIN_DIGIT_CASE_CHARS),
                new CharacterRule(EnglishCharacterData.Special, MIN_SPECIAL_CASE_CHARS));
        passwordRules.add(passwordChars);
        passwordRules.add(new RepeatCharacterRegexRule(MAX_REPETATIVE_CHARS));
        PasswordValidator passwordValidator = new PasswordValidator(passwordRules);
        PasswordData passwordData = new PasswordData(password);
        RuleResult ruleResult = passwordValidator.validate(passwordData);

        List<String> errorList = new ArrayList<>();
        for (RuleResultDetail ruleResultDetail : ruleResult.getDetails())
            if (!"INSUFFICIENT_CHARACTERISTICS".equalsIgnoreCase(ruleResultDetail.getErrorCode()))
                errorList.add("{"+ruleResultDetail.getErrorCode()+"}");
        return errorList;
//        return ruleResult.isValid();
    }

}
