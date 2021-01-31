package me.kjs.mall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryNonMemberDto {
    @NotBlank
    private String name;
    @NotBlank
    private String orderCode;
}
