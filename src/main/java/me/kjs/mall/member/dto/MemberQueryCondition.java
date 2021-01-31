package me.kjs.mall.member.dto;

import lombok.*;
import me.kjs.mall.common.type.EnumType;
import me.kjs.mall.common.type.QueryCondition;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberQueryCondition implements QueryCondition {
    private List<Condition> conditions;

    private Condition.ConditionType types[];
    private String values[];
    private String searchWord;
    @Min(0)
    private int page = 0;
    @Min(5)
    @Max(100)
    private int contents = 10;

    public int getTypesLength() {
        if (types == null)
            return 0;
        return types.length;
    }

    public int getValuesLength() {
        if (values == null)
            return 0;
        return values.length;
    }

    public void init() {
        conditions = new ArrayList<>();
        for (int i = 0; i < getTypesLength() || i < getValuesLength(); i++) {
            conditions.add(Condition.builder()
                    .value(values[i])
                    .type(types[i])
                    .build());
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Condition {

        private String value;

        private ConditionType type;

        @Getter
        @RequiredArgsConstructor
        public enum ConditionType implements EnumType {
            CompanyRank("직급"),
            Headquarters("소속 본부"),
            AccountStatus("계정 상태");
            private final String description;
        }
    }

    public boolean isBlank() {
        return searchWord == null || searchWord.trim().isEmpty();
    }

    public String getSearchWord() {
        return searchWord.trim();
    }

    public boolean isContainCondition(Condition.ConditionType conditionType) {
        if (conditions == null) {
            return false;
        }

        if (conditions.stream().anyMatch(ct -> ct.type == conditionType)) {
            return true;
        }
        return false;
    }

    public String getContainValue(Condition.ConditionType conditionType) {
        return conditions.stream().filter(ct -> ct.type == conditionType).findFirst().orElse(null).value;
    }


}
