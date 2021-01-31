package me.kjs.mall.api;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.social.SocialRegisterDto;
import me.kjs.mall.member.type.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class DocumentationTest extends BaseTest {

    @Test
    void kakaoResponseLoginTest() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/kakao/response/login"))
                .andDo(document("interface-response-social-login",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.token").description("실제 통신에 필요한 token"),
                                fieldWithPath("data.refreshToken").description("token 재 발급에 필요한 토큰 재 발급용 토큰"),
                                fieldWithPath("data.tokenExpireSecond").description("토큰 만료까지 남은 시간"),
                                fieldWithPath("data.refreshTokenExpireSecond").description("재 발급영 토큰 만료까지 남은 시간")

                        )));
    }

    @Test
    void kakaoResponseRegisterTest() throws Exception {
        ExceptionStatus kakaoNotRegisterUser = ExceptionStatus.KAKAO_NOT_REGISTER_USER;
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/kakao/response/register"))
                .andDo(document("interface-response-social-register",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + kakaoNotRegisterUser.getStatus()),
                                fieldWithPath("message").description("응답 메시지 / " + kakaoNotRegisterUser.getMessage()),
                                fieldWithPath("data.socialId").description("사용자 소셜 아이디(소셜 고유값)"),
                                fieldWithPath("data.name").description("사용자 이름 (없을 경우 공백)"),
                                fieldWithPath("data.socialType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.SOCIAL_TYPE)),
                                fieldWithPath("data.gender").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.GENDER))
                        )));
    }

    @GetMapping("/test/kakao/response/login")
    public ResponseDto responseLoginInterface() {
        TokenDto tokenDto = TokenDto.builder()
                .refreshToken(UUID.randomUUID().toString())
                .refreshTokenExpireSecond(60 * 60 * 24 * 14)
                .token("jsdhkasjd.asdhkjhdkjas.asdlkhaksd")
                .tokenExpireSecond(60 * 60 * 2)
                .build();
        return ResponseDto.ok(tokenDto);
    }

    @GetMapping("/test/kakao/response/register")
    public ResponseDto responseRegisterInterface() {
        SocialRegisterDto socialRegisterDto = SocialRegisterDto.builder()
                .gender(Gender.MALE)
                .name("홍길동")
                .socialId("11111111")
                .build();

        return ResponseDto.builder()
                .message(ExceptionStatus.KAKAO_NOT_REGISTER_USER.getMessage())
                .status(ExceptionStatus.KAKAO_NOT_REGISTER_USER.getStatus())
                .data(socialRegisterDto)
                .build();
    }


}
