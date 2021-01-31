package me.kjs.mall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderNonMemberInfoDto {
    @NotEmpty
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String name;
}
