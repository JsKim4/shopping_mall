package me.kjs.mall.member.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.PolicyType;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyAndAcceptDto {
    @NotNull
    private PolicyType policyType;
    @NotNull
    private YnType acceptType;
}
