package me.kjs.mall.member.social.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoOauthResponseDto {
    private String code;
    private String state;
    private String error;
    private String error_description;
}
