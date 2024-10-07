package org.socialculture.platform.member.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.validator.EmailValidator;
import org.socialculture.platform.member.validator.NameValidator;
import org.socialculture.platform.member.validator.PasswordValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberValidatorService {

    private final EmailValidator emailValidator;
    private final NameValidator nameValidator;
    private final PasswordValidator passwordValidator;

    public void validateEmail(String email) {
        if (!emailValidator.isValidEmail(email)) {
            throw new GeneralException(ErrorStatus.EMAIL_INVALID);
        }
    }

    public void validateName(String name) {
        if (!nameValidator.isValidName(name)) {
            throw new GeneralException(ErrorStatus.NAME_INVALID);
        }
    }

    public void validatePassword(String password) {
        if (!passwordValidator.isValidPassword(password)) {
            throw new GeneralException(ErrorStatus.PASSWORD_INVALID);
        }
    }





}
