package me.kjs.mall.configs.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Data
@ConfigurationProperties("kjs-mall.sms.purrio")
public class PurrioProperties {
    private String userId;
    private String senderPhone;
    private String sendApiUrl;
    private String cancelApiUrl;
}
