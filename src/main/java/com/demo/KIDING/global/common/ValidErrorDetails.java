package com.demo.KIDING.global.common;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ValidErrorDetails {

    public Map<String, String> validateHandler(Errors errors) {
        Map<String, String> errorMap = new HashMap<>();

        for (FieldError error: errors.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        return errorMap;

    }
}
