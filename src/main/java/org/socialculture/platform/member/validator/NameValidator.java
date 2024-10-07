package org.socialculture.platform.member.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NameValidator {

    // 3자이상 10자이하, (초성,모음 제한)한글, 영어, 숫자 가능
    private static final String NAME_REGEX = "^(?=.{3,10}$)(?!.*[ㄱ-ㅎㅏ-ㅣ])[a-zA-Z0-9가-힣]*$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    public boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name).matches();
    }
}
