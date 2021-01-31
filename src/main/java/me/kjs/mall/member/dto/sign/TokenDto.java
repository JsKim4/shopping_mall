package me.kjs.mall.member.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.Member;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String token;
    private String refreshToken;
    private long tokenExpireSecond;
    private long refreshTokenExpireSecond;

    public static TokenDto tokenAndMemberToTokenDto(String token, long tokenExpireSecond, Member member) {
        return TokenDto.builder()
                .token(token)
                .refreshToken(member.getRefreshToken())
                .tokenExpireSecond(tokenExpireSecond)
                .refreshTokenExpireSecond(member.getRefreshTokenExpireSecond(LocalDateTime.now()))
                .build();

    }
}
