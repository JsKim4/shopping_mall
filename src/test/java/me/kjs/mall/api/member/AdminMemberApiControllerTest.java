package me.kjs.mall.api.member;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.MemberBanCauseDto;
import me.kjs.mall.member.dto.sign.OnlyPasswordDto;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.type.AccountStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMemberApiControllerTest extends BaseTest {


    @Test
    @DisplayName("/api/members/password/{memberId} 회원 정보 수정 성공 케이스 변경 없음")
    void initializePassword() throws Exception {
        Member admin = memberQueryRepository.findAdmin();
        Long memberId = admin.getId();
        TokenDto tokenDto = getTokenDto();

        OnlyPasswordDto pass = new OnlyPasswordDto("password123");


        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/password/{memberId}", memberId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pass)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-member-admin-password-initialize",
                        requestFields(
                                fieldWithPath("password").description("초기화 비밀번호")
                        ),
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유 번호")
                        )
                ));
        Member member = memberRepository.findById(memberId).orElseThrow(EntityExistsException::new);

        assertTrue(passwordEncoder.matches(pass.getPassword(), member.getPassword()));
    }


    @Test
    @DisplayName("/api/members/ban/{memberId} 회원 이용 제한")
    void memberBan() throws Exception {


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        Long memberId = member.getId();


        MemberBanCauseDto memberBanCauseDto = MemberBanCauseDto.builder()
                .causeMessage("is ben test!!")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/ban/{memberId}", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberBanCauseDto))
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-member-ban",
                        requestFields(
                                fieldWithPath("causeMessage").description("벤 사유")
                        ),
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유 번호")
                        )
                ));

        Member afterMember = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        assertEquals(afterMember.getAccountStatus(), AccountStatus.BAN);
        assertTrue(afterMember.getMemberBanHistories().size() > 0);
        assertEquals(afterMember.getBanCause().getCauseMessage(), memberBanCauseDto.getCauseMessage());
    }

    @Test
    @DisplayName("/api/members/ban/{memberId} 회원 아용 제한이 불 가능한 상태")
    void memberBanFailNotAvailableStatus() throws Exception {

        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        Long memberId = member.getId();


        MemberBanCauseDto memberBanCauseDto = MemberBanCauseDto.builder()
                .causeMessage("is ben test!!")
                .build();
        memberService.banMember(memberId, memberBanCauseDto);

        int expectedStatus = 4014;
        String expectedMessage = "이용제한이 가능한 상태가 아닙니다.";

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/ban/{memberId}", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberBanCauseDto))
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-member-ban-fail-not-available-status"
                ));
    }

    @Test
    @DisplayName("/api/members/free/{memberId} 회원 이용제한 해제")
    void memberFree() throws Exception {


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        Long memberId = member.getId();


        MemberBanCauseDto memberBanCauseDto = MemberBanCauseDto.builder()
                .causeMessage("is ben test!!")
                .build();

        memberService.banMember(memberId, memberBanCauseDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/free/{memberId}", memberId)
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-member-free",
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유 번호")
                        )
                ));

        Member afterMember = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        assertEquals(afterMember.getAccountStatus(), AccountStatus.ALLOW);
        assertTrue(afterMember.getMemberBanHistories().size() > 0);
        assertEquals(afterMember.getBanCause().getCauseMessage(), "이용제한 회원이 아닙니다.");
    }


    @Test
    @DisplayName("/api/members/free/{memberId} 회원 이용 제한 해체 실패")
    void memberFreeFailByNotAvailableStatus() throws Exception {


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        Long memberId = member.getId();


        int expectedStatus = 4015;
        String expectedMessage = "이용제한 해제가 가능한 상태가 아닙니다.";

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/free/{memberId}", memberId)
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-member-free-fail-not-available-status",
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유 번호")
                        )
                ));

    }

}