package me.kjs.mall.cert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.cert.Cert;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertTokenDto {
    private String token;
    private long tokenExpireSecond;

    public static CertTokenDto certToTokenDto(Cert cert) {
        return CertTokenDto.builder()
                .token(cert.getToken())
                .tokenExpireSecond(cert.getRemainExpiredDateTime())
                .build();
    }
}
