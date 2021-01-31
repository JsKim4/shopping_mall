package me.kjs.mall.cert;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static me.kjs.mall.cert.KeyGeneratorType.UUID_GENERATOR;

@Getter
@RequiredArgsConstructor
public enum CertType {
    MEMBER_PASSWORD_MODIFY_TOKEN(600, UUID_GENERATOR, 1),
    PHONE_CERT_TOKEN(180, KeyGeneratorType.ONLY_NUMBER_GENERATOR, 6),
    MEMBER_REGISTER_TOKEN(600, UUID_GENERATOR, 1),
    EMAIL_FIND_TOKEN(600, UUID_GENERATOR, 1);

    private final int validTime; // 유효 시간 (초)
    private final KeyGeneratorType keyGeneratorType; // 키 생성 타입
    private final int keyRepeat; // 키 생성 반복 횟수

    public String generatorKey() {
        return keyGeneratorType.generatorKey(keyRepeat);
    }

    public LocalDateTime getExpiredDateTime() {
        return LocalDateTime.now().plusSeconds(validTime);
    }
}
