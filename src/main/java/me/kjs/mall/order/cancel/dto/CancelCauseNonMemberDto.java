package me.kjs.mall.order.cancel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.Bank;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelCauseNonMemberDto implements CancelOrderInterface {
    private String cause = "cause000";
    @NotBlank
    private String phoneNumber;
    private Bank bank;
    @NotBlank
    private String name;
    private String accountNo;
}
