package me.kjs.mall.order.specific.destination.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDestinationSaveDto {
    @NotBlank
    private String recipient;
    @NotBlank
    private String addressSimple;
    @NotBlank
    private String addressDetail;
    @NotBlank
    private String tel1;
    private String tel2;
    @NotBlank
    private String zipcode;
    @NotNull
    private String message;
}