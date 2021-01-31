package me.kjs.mall.order.cancel.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.Bank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelCauseDto implements CancelOrderInterface {
    private String cause = "cause000";
    private Bank bank;
    private String name;
    private String accountNo;
}
