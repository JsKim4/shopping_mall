package me.kjs.mall.api.member;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.cert.dto.CertTokenDto;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.OnlyTokenDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.MemberBanCauseDto;
import me.kjs.mall.member.dto.MemberQueryCondition;
import me.kjs.mall.member.dto.MemberUpdateDto;
import me.kjs.mall.member.dto.sign.MemberPasswordUpdateDto;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.type.CompanyRank;
import me.kjs.mall.member.type.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberApiControllerTest extends BaseTest {

    @Test
    @DisplayName("/api/members/current 현재 로그인한 사용자 확인")
    void queryCurrentMemberSuccessOnEmployee() throws Exception {
        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/current")
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data.memberId").exists())
                .andExpect(jsonPath("data.name").exists())
                .andExpect(jsonPath("data.email").exists())
                .andExpect(jsonPath("data.accountStatus").exists())
                .andExpect(jsonPath("data.pointsHeld").exists())
                .andExpect(jsonPath("data.accumulatePoint").exists())
                .andExpect(jsonPath("data.pointsUsed").exists())
                .andDo(document("query-member-current",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
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
                        )));
    }


    @Test
    @DisplayName("/api/members 회원 조회 성공 케이스")
    void findMembersSuccess() throws Exception {
        TokenDto tokenDto = getTokenDto();

        List<MemberQueryCondition.Condition> conditionList = new ArrayList<>();
        conditionList.add(MemberQueryCondition.Condition.builder()
                .type(MemberQueryCondition.Condition.ConditionType.CompanyRank)
                .value(CompanyRank.ASSISTANT_MANAGER.name())
                .build());

        MemberQueryCondition condition = MemberQueryCondition.builder()
                .conditions(conditionList)
                .contents(50)
                .page(0)
                .searchWord("일반")
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .param("types[0]", MemberQueryCondition.Condition.ConditionType.CompanyRank.name())
                .param("values[0]", CompanyRank.ASSISTANT_MANAGER.name())
                .param("page", "0")
                .param("contents", "10")
                .param("searchWord", "일반"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("query-members",
                        requestParameters(
                                parameterWithName("contents").description("페이지 하나에 검색될 개수 (_기본 10)"),
                                parameterWithName("page").description("페이지 번호 (0번부터 시작_기본 0)"),
                                parameterWithName("searchWord").description("이름 검색"),
                                parameterWithName("types[0]").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.CONDITION_TYPE) + "리스트"),
                                parameterWithName("values[0]").description("검색 조건 타입에 맞는 값")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("페이지 콘텐츠 개수"),
                                fieldWithPath("data.totalCount").description("전체 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[].memberId").description("회원 고유 번호"),
                                fieldWithPath("data.contents[].name").description("회원 이름"),
                                fieldWithPath("data.contents[].email").description("회원 이메일"),
                                fieldWithPath("data.contents[].accountStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ACCOUNT_STATUS)),
                                fieldWithPath("data.contents[].pointsHeld").description("사용 가능 포인트"),
                                fieldWithPath("data.contents[].accumulatePoint").description("적립 총 포인트"),
                                fieldWithPath("data.contents[].pointsUsed").description("사용 총 포인트"),
                                fieldWithPath("data.contents[].accountGroupId").description("권한 그룹 고유 번호"),
                                fieldWithPath("data.contents[].accountGroupName").description("권한 그룹 이름"),
                                fieldWithPath("data.contents[].phoneNumber").description("핸드폰 번호"),
                                fieldWithPath("data.contents[].gender").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.GENDER)),
                                fieldWithPath("data.contents[].birth").description("생년월일").type("date")
                        )
                ));
    }

    @Test
    @DisplayName("/api/members 회원 정보 수정 성공")
    void updateMember() throws Exception {
        TokenDto tokenDto = getUserTokenDto();

        MemberUpdateDto memberUpdateDto = MemberUpdateDto.builder()
                .birth(LocalDate.of(1970, 01, 01))
                .currentPassword("a123456")
                .gender(Gender.MALE)
                .name("HONG")
                .password("a1234567")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberUpdateDto))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-member",
                        requestFields(
                                fieldWithPath("birth").description("변경할 생년월일").type("date"),
                                fieldWithPath("currentPassword").description("현재 비밀번호"),
                                fieldWithPath("gender").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.GENDER)),
                                fieldWithPath("name").description("변경할 이름"),
                                fieldWithPath("password").description("변경할 비밀번호").optional()
                        )
                ));
    }


    @Test
    @DisplayName("/api/members/password 회원 비밀번호 변경 성공 케이스")
    void updateMemberPasswordSuccess() throws Exception {

        Member member = memberRepository.findByRefreshToken(getUserTokenDto().getRefreshToken()).orElseThrow(NoExistIdException::new);

        CertTokenDto certTokenDto = certService.generatorPasswordModifyCertKey(member);
        String token = certTokenDto.getToken();
        String password = "asd123456789";
        MemberPasswordUpdateDto updateDto = MemberPasswordUpdateDto.builder()
                .password(password)
                .token(token)
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-member-password",
                        requestFields(
                                fieldWithPath("password").description("변경할 비밀번호"),
                                fieldWithPath("token").description("비밀번호 변경 인증 토큰")
                        )));


        Member user = memberRepository.findByRefreshToken(getUserTokenDto().getRefreshToken()).orElseThrow(NoExistIdException::new);

        assertTrue(passwordEncoder.matches(updateDto.getPassword(), user.getPassword()));
    }

    @Test
    @DisplayName("/api/members/find/email 회원 비밀번호 변경 성공 케이스")
    void findEmail() throws Exception {

        Member member = memberRepository.findByRefreshToken(getUserTokenDto().getRefreshToken()).orElseThrow(NoExistIdException::new);

        CertTokenDto certTokenDto = certService.generatorFindEmailCertKey(member.getPhoneNumber());


        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/find/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(OnlyTokenDto.builder().token(certTokenDto.getToken()).build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("create-find-email",
                        requestFields(
                                fieldWithPath("token").description("이메일 찾기용 토큰")
                        )
                        , responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.email").description("사용자 이메일")
                        )
                ));

    }

    @Test
    @DisplayName("/api/members/current/ban 회원 이용 제한 사유")
    void queryCurrentMemberBanCause() throws Exception {

        TokenDto tokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        Long memberId = member.getId();


        MemberBanCauseDto memberBanCauseDto = MemberBanCauseDto.builder()
                .causeMessage("is ben test!!")
                .build();

        memberService.banMember(memberId, memberBanCauseDto);


        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/current/ban")
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data.causeMessage").value(memberBanCauseDto.getCauseMessage()))
                .andExpect(jsonPath("data.beginDate").exists())
                .andDo(document("query-member-current-ban-cause",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.causeMessage").description("이용 제한 사유"),
                                fieldWithPath("data.beginDate").description("이용 제한 기록 시작일 / 제한 아닐 경우 null").optional()
                        )));


    }


}