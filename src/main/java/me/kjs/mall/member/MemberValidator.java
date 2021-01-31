package me.kjs.mall.member;

import me.kjs.mall.member.dto.MemberQueryCondition;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class MemberValidator {

    public void validator(MemberQueryCondition memberQueryCondition, Errors errors) {
        if (memberQueryCondition.getValuesLength() != memberQueryCondition.getTypesLength()) {
            errors.rejectValue("types", "wrong value", "타입과 밸류의 길이는 같아야합니다");
            errors.rejectValue("values", "wrong value", "타입과 밸류의 길이는 같아야합니다");
        }
    }
}
