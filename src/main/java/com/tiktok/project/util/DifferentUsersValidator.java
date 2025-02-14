package com.tiktok.project.util;

import com.tiktok.project.dto.request.ChatRoomRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentUsersValidator implements ConstraintValidator<DifferentUsers, ChatRoomRequest> {

    @Override
    public boolean isValid(ChatRoomRequest request, ConstraintValidatorContext context) {
        return request.getUserOneId() != null
                && request.getUserTwoId() != null
                && !request.getUserOneId().equals(request.getUserTwoId());
    }
}
