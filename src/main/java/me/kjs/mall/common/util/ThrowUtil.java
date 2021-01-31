package me.kjs.mall.common.util;

import me.kjs.mall.common.exception.GrantException;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.exception.ValidationErrorException;
import me.kjs.mall.common.type.AvailableCheck;
import me.kjs.mall.common.type.OwnerCheck;
import me.kjs.mall.member.Member;
import org.springframework.validation.Errors;


public class ThrowUtil {


    public static void notAvailableThrow(AvailableCheck availableCheck) {
        if (availableCheck.isAvailable() == false) {
            throw new NoExistIdException();
        }
    }

    public static void hasErrorsThrow(Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationErrorException(errors);
        }
    }

    public static void notOwnerThrow(OwnerCheck item, Member member) {
        if (item.isOwner(member) == false) {
            throw new GrantException("grant Exception");
        }
    }

    public static void notUsedThrow(AvailableCheck availableCheck) {
        if (availableCheck.isUsed() == false) {
            throw new NoExistIdException();
        }
    }
}
