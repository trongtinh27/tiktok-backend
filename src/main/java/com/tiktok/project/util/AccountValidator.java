package com.tiktok.project.util;

import com.tiktok.project.auth.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class AccountValidator implements ConstraintValidator<ValidAccount, RegisterRequest> {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_REGEX = "^[0-9]{10}$"; // Số điện thoại 10-11 chữ số

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getAccount() == null) {
            return false; // Không được để account null
        }

        boolean isEmail = request.isEmail();
        String account = request.getAccount();

        if (isEmail) {
            return Pattern.matches(EMAIL_REGEX, account); // Kiểm tra email hợp lệ
        } else {
            return Pattern.matches(PHONE_REGEX, account); // Kiểm tra số điện thoại hợp lệ
        }
    }
}
