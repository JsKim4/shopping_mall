package me.kjs.mall.api.member;

import me.kjs.mall.cert.dto.CertTokenDto;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.EmailOnlyDto;
import me.kjs.mall.common.dto.EmailPhoneNumberDto;
import me.kjs.mall.common.dto.PhoneNumberCertDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.connect.SmsResultDto;
import me.kjs.mall.connect.SmsService;
import me.kjs.mall.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CertApiControllerTest extends BaseTest {

    @MockBean
    private SmsService smsService;


    @Test
    @DisplayName("/api/certs/join/phone 회원가입용 핸드폰 인증 번호 발급")
    void certJoinPhoneTest() throws Exception {
        Map map = new HashMap();
        map.put("phoneNumber", "01026420239");

        SmsResultDto resultDto = SmsResultDto.builder()
                .success(true)
                .message("message")
                .build();
        when(smsService.send(any(), any(), any(), anyInt(), any())).thenReturn(resultDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/certs/join/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("create-member-join-phone-cert-key",
                        requestFields(fieldWithPath("phoneNumber").description("핸드폰 번호")),
                        responseFields(
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("null")
                        )
                ));
    }

    @Test
    @DisplayName("/api/certs/password/phone 비밀번호 찾기 인증 번호 발급")
    void certPasswordPhoneTest() throws Exception {
        Member member = memberRepository.findByRefreshToken(getUserTokenDto().getRefreshToken()).orElseThrow(NoExistIdException::new);
        EmailPhoneNumberDto emailPhoneNumberDto = EmailPhoneNumberDto.builder()
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .build();

        SmsResultDto resultDto = SmsResultDto.builder()
                .success(true)
                .message("message")
                .build();
        when(smsService.send(any(), any(), any(), anyInt(), any())).thenReturn(resultDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/certs/password/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailPhoneNumberDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("create-member-password-phone-cert-key",
                        requestFields(
                                fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                fieldWithPath("email").description("회원이메일")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("null")
                        )
                ));
    }

    @Test
    @DisplayName("/api/certs/email/phone 이메일 찾기 인증 번호 발급")
    void certEmailPhoneTest() throws Exception {
        Map map = new HashMap();
        map.put("phoneNumber", "01000000001");

        SmsResultDto resultDto = SmsResultDto.builder()
                .success(true)
                .message("message")
                .build();
        when(smsService.send(any(), any(), any(), anyInt(), any())).thenReturn(resultDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/certs/email/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("create-member-email-phone-cert-key",
                        requestFields(fieldWithPath("phoneNumber").description("핸드폰 번호")),
                        responseFields(
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").description("null")
                        )
                ));
    }

    @Test
    @DisplayName("/api/certs/check/phone 회원가입용 핸드폰 확인 및 회원가입용 발급")
    void certCheckPhoneTest() throws Exception {
        Map map = new HashMap();
        map.put("phoneNumber", "01026420239");
        CertTokenDto certTokenDto = certService.generatorPhoneNumberCertKey("01026420239");
        PhoneNumberCertDto phoneNumberCertDto = PhoneNumberCertDto.builder()
                .phoneNumber("01026420239")
                .token(certTokenDto.getToken())
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/certs/check/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(phoneNumberCertDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.token").exists())
                .andExpect(jsonPath("data.tokenExpireSecond").exists())
                .andDo(document("create-member-join-cert-key",
                        requestFields(
                                fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                fieldWithPath("token").description("핸드폰 번호 인증후 발급받은 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.token").description("회원가입용 토큰"),
                                fieldWithPath("data.tokenExpireSecond").description("회원가입 토큰 만료 시간(초)")
                        )
                ));
    }

    @Test
    @DisplayName("/api/certs/password/check/phone 비밀번호 찾기 인증 번호 발급")
    void certPasswordCheckPhoneTest() throws Exception {

        SmsResultDto resultDto = SmsResultDto.builder()
                .success(true)
                .message("message")
                .build();
        when(smsService.send(any(), any(), any(), anyInt(), any())).thenReturn(resultDto);

        String phoneNumber = "01000000001";
        CertTokenDto certTokenDto = certService.generatorPhoneNumberCertKey(phoneNumber);
        PhoneNumberCertDto phoneNumberCertDto = PhoneNumberCertDto.builder()
                .token(certTokenDto.getToken())
                .phoneNumber(phoneNumber)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/certs/password/check/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(phoneNumberCertDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("create-member-password-cert-key",
                        requestFields(
                                fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                fieldWithPath("token").description("핸드폰 인증으로 받은 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.token").description("회원가입용 토큰"),
                                fieldWithPath("data.tokenExpireSecond").description("회원가입 토큰 만료 시간(초)")
                        )
                ));
    }

    @Test
    @DisplayName("/api/certs/email/check/phone 이메일 찾기 인증 번호 발급")
    void certEmailCheckPhoneTest() throws Exception {
        SmsResultDto resultDto = SmsResultDto.builder()
                .success(true)
                .message("message")
                .build();
        when(smsService.send(any(), any(), any(), anyInt(), any())).thenReturn(resultDto);

        String phoneNumber = "01000000001";
        CertTokenDto certTokenDto = certService.generatorPhoneNumberCertKey(phoneNumber);
        PhoneNumberCertDto phoneNumberCertDto = PhoneNumberCertDto.builder()
                .token(certTokenDto.getToken())
                .phoneNumber(phoneNumber)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/certs/email/check/phone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(phoneNumberCertDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("create-member-email-cert-key",
                        requestFields(
                                fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                fieldWithPath("token").description("핸드폰 인증으로 받은 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.token").description("회원가입용 토큰"),
                                fieldWithPath("data.tokenExpireSecond").description("회원가입 토큰 만료 시간(초)")
                        )
                ));
    }


    @Test
    @DisplayName("/api/certs/check/email 이메일 확인")
    void certCheckEmailTest() throws Exception {
        EmailOnlyDto emailOnlyDto = EmailOnlyDto.builder()
                .email("user006")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/certs/check/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailOnlyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("create-check-email",
                        requestFields(
                                fieldWithPath("email").description("이메일")
                        )
                ));
    }

}