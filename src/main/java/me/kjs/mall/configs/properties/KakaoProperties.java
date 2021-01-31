package me.kjs.mall.configs.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("kjs-mall.login.kakao")
@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KakaoProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
    private String tokenRequestUrl;
    private String tokenRequestGrantType;
    private String userRequestHeaderType;
    private String userRequestUrl;
    private String disconnectRequestUrl;

    public String getBearerToken(String accessToken) {
        return "Bearer " + accessToken;
    }
}
