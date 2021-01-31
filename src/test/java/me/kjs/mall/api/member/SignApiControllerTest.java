package me.kjs.mall.api.member;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.cert.dto.CertTokenDto;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.PolicyType;
import me.kjs.mall.member.dto.MemberDetailDto;
import me.kjs.mall.member.dto.sign.*;
import me.kjs.mall.member.social.SocialConnectorDto;
import me.kjs.mall.member.social.SocialType;
import me.kjs.mall.member.type.AccountStatus;
import me.kjs.mall.member.type.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class SignApiControllerTest extends BaseTest {

    @Test
    @DisplayName("/api/members 회원 가입 성공 케이스")
    void memberRegisterSuccess() throws Exception {

        List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());

        String phoneNumber = "01000000013";
        CertTokenDto certTokenDto = certService.generatorRegisterCertKey(phoneNumber);
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .name("KJS")
                .password("a123456")
                .email("jskim@kjs-mall.co.kr")
                .policies(policyAndAcceptDtos)
                .birth(LocalDate.of(1970, 1, 1))
                .certKey(certTokenDto.getToken())
                .phoneNumber(phoneNumber)
                .gender(Gender.MALE)
                .build();
        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members")
                .content(objectMapper.writeValueAsString(memberJoinDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value("요청이 성공적으로 수행되었습니다."))
                .andExpect(jsonPath("data.memberId").exists())
                .andExpect(jsonPath("data.name").value(memberJoinDto.getName()))
                .andExpect(jsonPath("data.email").value(memberJoinDto.getEmail()))
                .andExpect(jsonPath("data.accountStatus").value(AccountStatus.ALLOW.name()))
                .andExpect(jsonPath("data.pointsHeld").value(0))
                .andExpect(jsonPath("data.accumulatePoint").value(0))
                .andExpect(jsonPath("data.pointsUsed").value(0))
                .andDo(document(
                        "create-member-success",
                        requestFields(
                                fieldWithPath("email").description("가입할 회원의 이메일"),
                                fieldWithPath("password").description("가입할 회원의 비밀번호"),
                                fieldWithPath("name").description("가입할 회원의 이름"),
                                fieldWithPath("phoneNumber").description("핸드폰 번호"),
                                fieldWithPath("certKey").description("핸드폰 번호 인증키"),
                                fieldWithPath("socialType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.SOCIAL_TYPE)).optional(),
                                fieldWithPath("socialId").description("소셜아이디").optional(),
                                fieldWithPath("gender").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.GENDER)),
                                fieldWithPath("birth").description("생년월일(yyyy-mm-dd)").type("data"),
                                fieldWithPath("policies[].policyType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POLICY_TYPE)),
                                fieldWithPath("policies[].acceptType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE))
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / 201"),
                                fieldWithPath("message").description("응답 메세지 / 요청이 성공적으로 수행되었습니다."),
                                fieldWithPath("data.memberId").description("회원 고유 번호"),
                                fieldWithPath("data.name").description("회원 이름"),
                                fieldWithPath("data.email").description("회원 이메일"),
                                fieldWithPath("data.accountStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ACCOUNT_STATUS)),
                                fieldWithPath("data.pointsHeld").description("사용 가능 포인트"),
                                fieldWithPath("data.accumulatePoint").description("적립 총 포인트"),
                                fieldWithPath("data.pointsUsed").description("사용 총 포인트"),
                                fieldWithPath("data.accountGroupId").description("권한 그룹 고유 번호"),
                                fieldWithPath("data.accountGroupName").description("권한 그룹 이름"),
                                fieldWithPath("data.phoneNumber").description("핸드폰 번호"),
                                fieldWithPath("data.gender").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.GENDER)),
                                fieldWithPath("data.birth").description("생년월일").type("date")
                        )
                ));
    }

    @Test
    @DisplayName("/api/members/withdraw 회원 탈퇴")
    void memberDelete() throws Exception {

        List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());

        String phoneNumber = "01900000013";
        CertTokenDto certTokenDto = certService.generatorRegisterCertKey(phoneNumber);
        String password = "a123456";
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .name("KJS")
                .password(password)
                .email("jskim@kjs-mall.co.kr")
                .policies(policyAndAcceptDtos)
                .birth(LocalDate.of(1970, 1, 1))
                .certKey(certTokenDto.getToken())
                .phoneNumber(phoneNumber)
                .gender(Gender.MALE)
                .build();

        Member join = signService.join(memberJoinDto);

        String token = jwtTokenProvider.createToken(join.getEmail(), join.getAccountRoleNames());

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/withdraw")
                .content(objectMapper.writeValueAsString(OnlyPasswordDto.builder().password(password).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document(
                        "delete-member",
                        requestFields(
                                fieldWithPath("password").description("회원 비밀번호")
                        )
                ));
    }


    @Test
    @DisplayName("/api/members 회원 가입 이미 등록된 이메일로 실패 케이스")
    void memberRegisterFailByExistEmail() throws Exception {
        List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());
        String phoneNumber = "01000000013";
        CertTokenDto certTokenDto = certService.generatorRegisterCertKey(phoneNumber);
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .name("KJS")
                .password("a123456")
                .email("jskim@kjs-mall.co.kr")
                .policies(policyAndAcceptDtos)
                .birth(LocalDate.of(1970, 1, 1))
                .certKey(certTokenDto.getToken())
                .phoneNumber(phoneNumber)
                .gender(Gender.MALE)
                .build();
        SIgnService.join(memberJoinDto);
        TokenDto tokenDto = getTokenDto();
        int expectedValue = 4008;
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members")
                .content(objectMapper.writeValueAsString(memberJoinDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedValue))
                .andExpect(jsonPath("message").value("이미 등록된 이메일 입니다."))
                .andDo(document(
                        "create-member-fail-already-register-email",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + expectedValue),
                                fieldWithPath("message").description("응답 메세지 / 이미 등록된 이메일 입니다."),
                                fieldWithPath("data").description("null")
                        )
                ));
    }


    @Test
    @DisplayName("/api/members/login 회원 로그인 성공")
    void memberLoginSuccess() throws Exception {
        List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .name("KJS")
                .password("a123456")
                .email("jskim@kjs-mall.co.kr")
                .policies(policyAndAcceptDtos)
                .birth(LocalDate.of(1970, 1, 1))
                .certKey("certKey")
                .phoneNumber("01000000013")
                .gender(Gender.MALE)
                .build();
        SIgnService.join(memberJoinDto);

        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email(memberJoinDto.getEmail())
                .password(memberJoinDto.getPassword())
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberLoginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.token").exists())
                .andExpect(jsonPath("data.refreshToken").exists())
                .andExpect(jsonPath("data.tokenExpireSecond").exists())
                .andExpect(jsonPath("data.refreshTokenExpireSecond").exists())
                .andDo(document("create-login-token-success",
                        requestFields(
                                fieldWithPath("email").description("로그인할 사용자 이메일"),
                                fieldWithPath("password").description("로그인할 사용자 패스워드")
                        ),
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
    @DisplayName("/api/members/login 회원 로그인 가입된 이메일이 존재하지 않아서 실패")
    void memberLoginFailByNotRegistered() throws Exception {
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .name("KJS")
                .password("a123456")
                .email("jskim@kjs-mall.co.kr")
                .build();

        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email(memberJoinDto.getEmail())
                .password(memberJoinDto.getPassword())
                .build();

        int expectedValue = 4005;
        String expectedString = "비밀번호 혹은 이메일을 확인해주세요.";
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberLoginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedValue))
                .andExpect(jsonPath("message").value(expectedString))
                .andDo(document("create-login-token-fail-not-exist-email",
                        requestFields(
                                fieldWithPath("email").description("로그인할 사용자 이메일"),
                                fieldWithPath("password").description("로그인할 사용자 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + expectedValue),
                                fieldWithPath("message").description("응답 메시지 / " + expectedString),
                                fieldWithPath("data").description("null")
                        )));
    }

    @Test
    @DisplayName("/api/members/login 회원 로그인 비밀번호 불일치로 실패")
    void memberLoginFailByIllegalPassword() throws Exception {
        List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .name("KJS")
                .password("a123456")
                .email("jskim@kjs-mall.co.kr")
                .policies(policyAndAcceptDtos)
                .birth(LocalDate.of(1970, 1, 1))
                .certKey("certKey")
                .phoneNumber("01000000013")
                .gender(Gender.MALE)
                .build();

        SIgnService.join(memberJoinDto);

        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email(memberJoinDto.getEmail())
                .password(memberJoinDto.getPassword() + "aa")
                .build();

        int expectedValue = 4001;
        String expectedString = "비밀번호 혹은 이메일을 확인해주세요.";
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberLoginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedValue))
                .andExpect(jsonPath("message").value(expectedString))
                .andDo(document("create-login-token-fail-illegal-password",
                        requestFields(
                                fieldWithPath("email").description("로그인할 사용자 이메일"),
                                fieldWithPath("password").description("로그인할 사용자 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + expectedValue),
                                fieldWithPath("message").description("응답 메시지 / " + expectedString),
                                fieldWithPath("data").description("null")
                        )));
    }

    @Test
    @DisplayName("/api/members/login 회원 로그인 탈퇴한 회원이므로 실패")
    void memberLoginFailByWithdrawMember() throws Exception {
        List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .name("KJS")
                .password("a123456")
                .email("jskim@kjs-mall.co.kr")
                .policies(policyAndAcceptDtos)
                .birth(LocalDate.of(1970, 1, 1))
                .certKey("certKey")
                .phoneNumber("01000000013")
                .gender(Gender.MALE)
                .build();

        Member saveMember = SIgnService.join(memberJoinDto);
        MemberDetailDto memberDetailDto = MemberDetailDto.memberToDetailDto(saveMember);
        Member member = memberRepository.findById(memberDetailDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        member.memberWithdraw();
        memberRepository.save(member);

        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email(memberJoinDto.getEmail())
                .password(memberJoinDto.getPassword())
                .build();

        int expectedValue = 4004;
        String expectedString = "탈퇴한 회원입니다.";
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberLoginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedValue))
                .andExpect(jsonPath("message").value(expectedString))
                .andDo(document("create-login-token-fail-withdraw-member",
                        requestFields(
                                fieldWithPath("email").description("로그인할 사용자 이메일"),
                                fieldWithPath("password").description("로그인할 사용자 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + expectedValue),
                                fieldWithPath("message").description("응답 메시지 / " + expectedString),
                                fieldWithPath("data").description("null")
                        )));
    }

    @Test
    @DisplayName("/api/members/login 회원 로그인 입력 유효성 검사에서 실패")
    void memberLoginFailByValidationError() throws Exception {

        List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .name("KJS")
                .password("a123456")
                .email("jskim@kjs-mall.co.kr")
                .policies(policyAndAcceptDtos)
                .birth(LocalDate.of(1970, 1, 1))
                .certKey("certKey")
                .phoneNumber("01000000013")
                .gender(Gender.MALE)
                .build();

        Member saveMember = SIgnService.join(memberJoinDto);
        MemberDetailDto memberDetailDto = MemberDetailDto.memberToDetailDto(saveMember);
        Member member = memberRepository.findById(memberDetailDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        member.memberWithdraw();
        memberRepository.save(member);

        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .email("")
                .password("")
                .build();

        int expectedValue = 400;
        String expectedString = "입력값을 확인해 주세요.";
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberLoginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedValue))
                .andExpect(jsonPath("message").value(expectedString))
                .andDo(document("create-login-token-fail-validation-error"));
    }


    @Test
    @DisplayName("/api/members/refresh 토큰 재발급 성공")
    void tokenRefreshSuccess() throws Exception {
        TokenDto tokenDto = getTokenDto();

        TokenRefreshDto tokenRefreshDto = TokenRefreshDto.builder()
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRefreshDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.token").exists())
                .andExpect(jsonPath("data.refreshToken").value(tokenRefreshDto.getRefreshToken()))
                .andExpect(jsonPath("data.tokenExpireSecond").exists())
                .andExpect(jsonPath("data.refreshTokenExpireSecond").exists())
                .andDo(document("create-refresh-token-success",
                        requestFields(
                                fieldWithPath("refreshToken").description("로그인시 발급받은 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.token").description("실제 통신에 필요한 token"),
                                fieldWithPath("data.refreshToken").description("token 재 발급에 필요한 토큰 재 발급용 토큰"),
                                fieldWithPath("data.tokenExpireSecond").description("토큰 만료까지 남은 시간"),
                                fieldWithPath("data.refreshTokenExpireSecond").description("재 발급영 토큰 만료까지 남은 시간")
                        )));
    }


    @Test
    @DisplayName("/api/members/refresh 존재하지 않는 토큰으로 인한 실패")
    void tokenRefreshFailByNotFoundByRefreshToken() throws Exception {
        TokenDto tokenDto = getTokenDto();

        TokenRefreshDto tokenRefreshDto = TokenRefreshDto.builder()
                .refreshToken(tokenDto.getRefreshToken() + "adsasda")
                .build();


        int expectedStatus = 4006;
        String expectedMessage = "요청한 리프레시 토큰으로 사용자를 찾을 수 없습니다.";

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRefreshDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("create-refresh-token-fail-notfound-member-token",
                        requestFields(
                                fieldWithPath("refreshToken").description("로그인시 발급받은 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + expectedStatus),
                                fieldWithPath("message").description("응답 메시지 / " + expectedMessage),
                                fieldWithPath("data").description("null")
                        )));
    }

    @Test
    @DisplayName("/api/members/refresh 토큰이 만료되어 실패")
    void tokenRefreshFailByExpiredRefreshToken() throws Exception {
        TokenDto tokenDto = getTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElse(null);

        member.refreshTokenExpire();

        TokenRefreshDto tokenRefreshDto = TokenRefreshDto.builder()
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        int expectedStatus = 4007;
        String expectedMessage = "만료된 리프레시 토큰입니다.";

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRefreshDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("create-refresh-token-fail-expired-token",
                        requestFields(
                                fieldWithPath("refreshToken").description("로그인시 발급받은 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + expectedStatus),
                                fieldWithPath("message").description("응답 메시지 / " + expectedMessage),
                                fieldWithPath("data").description("null")
                        )));
    }


    @Test
    @DisplayName("/api/members/connect 소셜 아이디 연결")
    void connectSocial() throws Exception {
        TokenDto tokenDto = getTokenDto();

        SocialConnectorDto socialConnectorDto = SocialConnectorDto.builder()
                .socialId("1111111111")
                .socialType(SocialType.KAKAO)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/connect")
                .header("X-AUTH-TOKEN", getUserTokenDto().getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socialConnectorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("create-connection-social",
                        requestFields(
                                fieldWithPath("socialId").description("소셜 로그인 실패시 받은 id"),
                                fieldWithPath("socialType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.SOCIAL_TYPE))
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null")
                        )));
    }


}