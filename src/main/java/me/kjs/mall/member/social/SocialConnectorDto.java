package me.kjs.mall.member.social;

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
public class SocialConnectorDto implements SocialAccountCreateDto {
    @NotEmpty
    private String socialId;
    @NotNull
    private SocialType socialType;
}
