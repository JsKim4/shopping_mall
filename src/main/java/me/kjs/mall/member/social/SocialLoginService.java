package me.kjs.mall.member.social;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.OnlyIdDto;
import me.kjs.mall.configs.properties.KakaoProperties;
import me.kjs.mall.member.social.exception.KakaoConnectionException;
import me.kjs.mall.member.social.kakao.KakaoOauthResponseDto;
import me.kjs.mall.member.social.kakao.KakaoTokenRequestDto;
import me.kjs.mall.member.social.kakao.KakaoTokenResponseDto;
import me.kjs.mall.member.social.kakao.KakaoUserResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService {

    private final ObjectMapper objectMapper;
    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    public KakaoTokenResponseDto connectKakao(KakaoOauthResponseDto kakaoOauthResponseDto) {
        KakaoTokenRequestDto kakaoTokenRequestDto = KakaoTokenRequestDto.kakaoOauthToTokenRequestDto(kakaoOauthResponseDto, kakaoProperties);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            Map<String, String> map = objectMapper.convertValue(kakaoTokenRequestDto, new TypeReference<Map<String, String>>() {
            }); // (3)
            body.setAll(map);
            HttpEntity<KakaoTokenRequestDto> request = new HttpEntity(body, headers);
            ResponseEntity<KakaoTokenResponseDto> result = restTemplate.postForEntity(kakaoProperties.getTokenRequestUrl(), request, KakaoTokenResponseDto.class);
            log.info("result.getHeaders() {}", result.getHeaders());
            log.info("result.getStatusCode() {}", result.getStatusCode());
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new KakaoConnectionException();
        }
    }

    public KakaoUserResponseDto connectKakaoUser(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            headers.add(kakaoProperties.getUserRequestHeaderType(), kakaoProperties.getBearerToken(accessToken));
            HttpEntity request = new HttpEntity(headers);
            ResponseEntity<KakaoUserResponseDto> result = restTemplate.postForEntity(kakaoProperties.getUserRequestUrl(), request, KakaoUserResponseDto.class);
            return result.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new KakaoConnectionException();
        }
    }

    private void disconnectKakao(String targetId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            headers.add("Authorization", "KakaoAK 773027ad2446e6b38e3a43d96a9f1c99");
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            Map<String, String> map = new HashMap<>();
            map.put("target_id_type", "user_id");
            map.put("target_id", targetId);
            body.setAll(map);
            HttpEntity request = new HttpEntity(body, headers);

            ResponseEntity<OnlyIdDto> result = restTemplate.postForEntity(kakaoProperties.getDisconnectRequestUrl(), request, OnlyIdDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            /*throw new KakaoConnectionException();*/
        }
    }

    public void disconnect(SocialConnectorDto socialConnector) {
        switch (socialConnector.getSocialType()) {
            case KAKAO:
                disconnectKakao(socialConnector.getSocialId());
            default:
                return;
        }
    }

}
