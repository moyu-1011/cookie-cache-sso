package com.moyu.sc.validation;

import com.moyu.sc.domain.dto.UserDto;
import com.moyu.sc.validation.annotation.ValidPasswordMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<ValidPasswordMatch, UserDto> {

    @Override
    public void initialize(ValidPasswordMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext constraintValidatorContext) {
        return userDto.getPassword().equals(userDto.getMatchPassword());
    }
}
