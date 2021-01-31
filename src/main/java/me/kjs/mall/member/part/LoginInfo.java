package me.kjs.mall.member.part;

import lombok.*;
import me.kjs.mall.configs.properties.SecurityProperties;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginInfo {
    private LocalDateTime lastLoginDateTime;
    private LocalDateTime lastTokenIssueDateTime;
    private LocalDateTime refreshTokenExpireDateTime;
    private String refreshToken;

    public static LoginInfo initialize() {
        return LoginInfo.builder()
                .lastLoginDateTime(null)
                .lastTokenIssueDateTime(null)
                .refreshTokenExpireDateTime(LocalDateTime.now().plusDays(14))
                .refreshToken(UUID.randomUUID().toString())
                .build();
    }

    public LoginInfo login(SecurityProperties securityProperties) {
        LocalDateTime now = LocalDateTime.now();
        return LoginInfo.builder()
                .lastLoginDateTime(now)
                .lastTokenIssueDateTime(now)
                .refreshTokenExpireDateTime(now.plusDays(securityProperties.getRefreshTokenExpiredDate()))
                .refreshToken(UUID.randomUUID().toString())
                .build();
    }

    public LoginInfo tokenRefresh() {
        return LoginInfo.builder()
                .lastLoginDateTime(lastLoginDateTime)
                .lastTokenIssueDateTime(LocalDateTime.now())
                .refreshTokenExpireDateTime(refreshTokenExpireDateTime)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean isExpiredRefreshToken() {
        return refreshTokenExpireDateTime.isBefore(LocalDateTime.now());
    }

    public void refreshTokenExpire() {
        refreshTokenExpireDateTime = LocalDateTime.now().minusMinutes(1);
    }

    public void expired() {
        refreshToken = null;
        refreshTokenExpireDateTime = LocalDateTime.now();
    }
}
