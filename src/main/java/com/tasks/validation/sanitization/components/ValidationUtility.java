package com.tasks.validation.sanitization.components;

import com.tasks.validation.sanitization.domain.Error;
import com.tasks.validation.sanitization.exception.SanitizationException;
import com.tasks.validation.sanitization.utils.Constants;
import com.tasks.validation.sanitization.utils.UuidValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;


public class ValidationUtility {

    private final Sanitizer validator;

    public ValidationUtility(Sanitizer validator) {
        this.validator = validator;
    }

    public List<Error> getErrors(Object objectToValidate, String... uuidSToValidate)
    {
        List<Error> errors  = new ArrayList<>();
        if(objectToValidate != null) {
            try {
                errors = validator.validate(objectToValidate, s -> s.getClass(), errors, "");
            } catch (Throwable e) {
 
                throw new RuntimeException(e);
            }
        }

        for(String id: uuidSToValidate) {
            String[] fieldAndValue = id.split(" =",2);
            String field = null;
            String uuid;
            if(fieldAndValue.length == 2) {
                field = fieldAndValue[0];
                uuid = fieldAndValue[1];
            }else
                uuid = fieldAndValue[0];

            if(!UuidValidator.isValidUuid(uuid))
                errors.add(new Error(field, "Is not a valid uuid"));
        }
        return errors;
    }

    public void validateOrThrowException(Object objectToValidate, String... uuidSToValidate){
        List<Error> errors = getErrors(objectToValidate, uuidSToValidate);

        if (!CollectionUtils.isEmpty(errors))
            throw new SanitizationException("Validation of Inputs failed", errors);
    }
    public void validateOrThrowException(Map<String, String> objectToValidate){
        List<Error> errors = new ArrayList<>();
        for(String key: objectToValidate.keySet()){
            String value = objectToValidate.get(key);
            if (value == null)
                errors.add(new Error(key, "is invalid"));
            else if (!Pattern.compile(Constants.STRING_PATTERN).matcher(value).matches()){
                errors.add(new Error(key, "is invalid"));
            }
        }

        if (!CollectionUtils.isEmpty(errors))
            throw new SanitizationException("Validation of query Inputs failed", errors);
    }
}