package me.kjs.mall.destination.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationSaveDto {
    @NotBlank
    private String destinationName;
    @NotBlank
    private String recipient;
    @NotBlank
    private String tel1;
    private String tel2;
    @NotBlank
    private String addressSimple;
    @NotBlank
    private String addressDetail;
    @NotBlank
    private String zipcode;
}
