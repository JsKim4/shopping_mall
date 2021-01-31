package me.kjs.mall.connect;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsResultDto {
    private boolean success;
    private String message;

    public static SmsResultDto ok(String message) {
        return SmsResultDto.builder()
                .success(true)
                .message(message)
                .build();
    }

    public static SmsResultDto ok() {
        return SmsResultDto.builder()
                .success(true)
                .build();
    }

    public static SmsResultDto fail(String message) {
        return SmsResultDto.builder()
                .success(false)
                .message(message)
                .build();
    }
}
