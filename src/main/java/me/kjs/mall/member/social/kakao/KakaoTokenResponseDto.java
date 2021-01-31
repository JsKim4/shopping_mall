package me.kjs.mall.member.social.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.social.SocialType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoTokenResponseDto {
    @JsonProperty(value = "token_type")
    private String tokenType;
    @JsonProperty(value = "access_token")
    private String accessToken;
    @JsonProperty(value = "expires_in")
    private Long expiresIn;
    @JsonProperty(value = "refresh_token")
    private String refreshToken;
    @JsonProperty(value = "refresh_token_expires_in")
    private Long refreshTokenExpiresIn;
    @JsonProperty(value = "scope")
    private String scope;

    public Long getExpiresIn() {
        return expiresIn == null ? 0 : expiresIn;
    }

    public Long getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn == null ? 0 : refreshTokenExpiresIn;
    }

    public SocialType getTokenType() {
        return SocialType.KAKAO;
    }

}
