package me.kjs.mall.configs.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("kjs-mall.security")
@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SecurityProperties {
    private long tokenValidSecond = 300;//1800; 테스트 위해서 300
    private String secretKey = "temp";
    private int refreshTokenExpiredDate = 14;

    public long getTokenValidTime() {
        return tokenValidSecond * 1000;
    }

    public long getRefreshTokenExpiredSecond() {
        return refreshTokenExpiredDate * 60 * 60 * 24;
    }
}
