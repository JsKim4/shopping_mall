package me.kjs.mall.member.social.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.configs.properties.KakaoProperties;

@Getter
@Builder
public class KakaoTokenRequestDto {
    @JsonProperty("grant_type")
    private String grantType;
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @JsonProperty("code")
    private String code;
    @JsonProperty("client_secret")
    private String clientSecret;

    public static KakaoTokenRequestDto kakaoOauthToTokenRequestDto(KakaoOauthResponseDto kakaoOauthResponseDto, KakaoProperties kakaoProperties) {
        return KakaoTokenRequestDto.builder()
                .grantType(kakaoProperties.getTokenRequestGrantType())
                .clientId(kakaoProperties.getClientId())
                .clientSecret(kakaoProperties.getClientSecret())
                .redirectUri(kakaoProperties.getRedirectUrl())
                .code(kakaoOauthResponseDto.getCode())
                .build();
    }
}
