package me.kjs.mall.api.member;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.OnlyIdDto;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.account.AccountGroupSaveDto;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.part.AccountGroup;
import me.kjs.mall.member.type.AccountRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AccountApiControllerTest extends BaseTest {


    @Test
    @DisplayName("/aa")
    void queetest() {

    }

    @Test
    @DisplayName("/api/members/accounts/roles 권한 리스트 조회")
    void queryAccountRolesTest() throws Exception {

        TokenDto tokenDto = getTokenDto();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/accounts/roles")
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").isArray())
                .andDo(document(
                        "query-member-account-roles",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ACCOUNT_ROLE))
                        )
                ));
    }

    @Test
    @DisplayName("/api/members/accounts/groups 권한 그룹 리스트 조회")
    void queryAccountGroups() throws Exception {
        TokenDto tokenDto = getTokenDto();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/members/accounts/groups")
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").isArray())
                .andExpect(jsonPath("data[0].id").exists())
                .andExpect(jsonPath("data[0].name").exists())
                .andExpect(jsonPath("data[0].accountRoles").isArray())
                .andDo(document(
                        "query-member-account-groups",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("권한 그룹 리스트"),
                                fieldWithPath("data[].id").description("그룹 아이디 고유 번호"),
                                fieldWithPath("data[].name").description("그룹 이름"),
                                fieldWithPath("data[].accountRoles").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ACCOUNT_ROLE))
                        )
                ));
    }

    @Test
    @DisplayName("/api/members/accounts/groups 권한 그룹 생성")
    void createAccountGroups() throws Exception {
        TokenDto tokenDto = getTokenDto();

        AccountGroupSaveDto accountGroupSaveDto = AccountGroupSaveDto.builder()
                .accountRoles(Arrays.asList(AccountRole.ADMIN, AccountRole.ACCOUNT_GROUP, AccountRole.USER))
                .alias("GROUP_MANAGER")
                .name("GROUP")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/accounts/groups")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountGroupSaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.name").value(accountGroupSaveDto.getName()))
                .andExpect(jsonPath("data.accountRoles").isArray())
                .andDo(document(
                        "create-member-account-groups",
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("alias").description("약어"),
                                fieldWithPath("accountRoles").description("권한 리스트")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.id").description("그룹 아이디 고유 번호"),
                                fieldWithPath("data.name").description("그룹 이름"),
                                fieldWithPath("data.accountRoles").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ACCOUNT_ROLE))
                        )
                ));
    }

    @Test
    @DisplayName("/api/members/accounts/groups 권한 그룹 생성")
    void createAccountGroupsFailAlreadyAlias() throws Exception {
        TokenDto tokenDto = getTokenDto();

        AccountGroupSaveDto accountGroupSaveDto = AccountGroupSaveDto.builder()
                .accountRoles(Arrays.asList(AccountRole.ADMIN, AccountRole.ACCOUNT_GROUP, AccountRole.USER))
                .alias("superUser")
                .name("GROUP_MANAGER")
                .build();

        int expectedStatus = 4010;
        String expectedMessage = "이미 등록된 ALIAS 입니다.";

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/members/accounts/groups")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountGroupSaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "create-member-account-groups-fail-already-alias",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + expectedStatus),
                                fieldWithPath("message").description("응답 메시지 / " + expectedMessage),
                                fieldWithPath("data").description("null").optional()
                        )
                ));
    }

    @Test
    @DisplayName("/api/members/accounts/groups/{groupId} 그룹 수정")
    void updateAccountGroupsSuccess() throws Exception {
        TokenDto tokenDto = getTokenDto();

        AccountGroupSaveDto accountGroupSaveDto = AccountGroupSaveDto.builder()
                .accountRoles(Arrays.asList(AccountRole.ADMIN, AccountRole.ACCOUNT_GROUP, AccountRole.USER))
                .alias("GROUP_MANAGER")
                .name("GROUP_MANAGER")
                .build();

        AccountGroup accountGroup = accountGroupService.createAccountGroup(accountGroupSaveDto);

        AccountGroupSaveDto updateAccountGroupDto = AccountGroupSaveDto.builder()
                .accountRoles(Arrays.asList(AccountRole.ADMIN, AccountRole.ACCOUNT_GROUP, AccountRole.USER, AccountRole.MEMBER_ADD))
                .alias("GROUP_MANAGER2")
                .name("GROUP_MANAGER2")
                .build();


        Long groupId = accountGroup.getId();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/accounts/groups/{groupId}", groupId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountGroupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document("update-member-account-groups",
                        pathParameters(
                                parameterWithName("groupId").description("권한 그룹 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("accountRoles").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.ACCOUNT_ROLE)).optional(),
                                fieldWithPath("alias").description("권한 그룹 약어 / null 이거나 공백일 경우 업데이트 안함").optional(),
                                fieldWithPath("name").description("권한 그룹 이름 / null 이거나 공백일 경우 업데이트 안함").optional())
                ));

        AccountGroup afterAccountGroup = accountGroupRepository.findById(groupId).orElseThrow(EntityExistsException::new);

        assertEquals(afterAccountGroup.getRoles().size(), updateAccountGroupDto.getAccountRoles().size());

        for (AccountRole role : afterAccountGroup.getRoles()) {
            assertTrue(updateAccountGroupDto.getAccountRoles().contains(role));
        }
        assertEquals(afterAccountGroup.getName(), updateAccountGroupDto.getName());
        assertEquals(afterAccountGroup.getAlias(), updateAccountGroupDto.getAlias());
    }


    @Test
    @DisplayName("/api/members/accounts/groups/{groupId} 그룹 수정")
    void updateAccountGroupsSuccessNotModified() throws Exception {
        TokenDto tokenDto = getTokenDto();

        AccountGroupSaveDto accountGroupSaveDto = AccountGroupSaveDto.builder()
                .accountRoles(Arrays.asList(AccountRole.ADMIN, AccountRole.ACCOUNT_GROUP, AccountRole.USER))
                .alias("GROUP_MANAGER")
                .name("GROUP_MANAGER")
                .build();

        AccountGroup accountGroup = accountGroupService.createAccountGroup(accountGroupSaveDto);

        AccountGroupSaveDto updateAccountGroupDto = AccountGroupSaveDto.builder()
                .accountRoles(new ArrayList<>())
                .alias("")
                .name("")
                .build();


        Long groupId = accountGroup.getId();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/accounts/groups/{groupId}", groupId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountGroupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist());
        AccountGroup afterAccountGroup = accountGroupRepository.findById(groupId).orElseThrow(EntityExistsException::new);

        assertEquals(afterAccountGroup.getRoles().size(), accountGroupSaveDto.getAccountRoles().size());

        for (AccountRole role : afterAccountGroup.getRoles()) {
            assertTrue(accountGroupSaveDto.getAccountRoles().contains(role));
        }
        assertEquals(afterAccountGroup.getName(), accountGroupSaveDto.getName());
        assertEquals(afterAccountGroup.getAlias(), accountGroupSaveDto.getAlias());
    }


    @Test
    @DisplayName("/api/members/accounts/groups/{groupId} 중복되는 alias가 존재해서 실패")
    void updateAccountGroupsFailByAlreadyAlias() throws Exception {
        TokenDto tokenDto = getTokenDto();

        AccountGroupSaveDto accountGroupSaveDto = AccountGroupSaveDto.builder()
                .accountRoles(Arrays.asList(AccountRole.ADMIN, AccountRole.ACCOUNT_GROUP, AccountRole.USER))
                .alias("GROUP_MANAGER")
                .name("GROUP_MANAGER")
                .build();

        AccountGroup accountGroup = accountGroupService.createAccountGroup(accountGroupSaveDto);

        AccountGroupSaveDto updateAccountGroupDto = AccountGroupSaveDto.builder()
                .accountRoles(new ArrayList<>())
                .alias("superUser")
                .name("")
                .build();


        Long groupId = accountGroup.getId();

        int expectedStatus = 4010;
        String expectedMessage = "이미 등록된 ALIAS 입니다.";
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/accounts/groups/{groupId}", groupId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountGroupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-member-account-groups-fail-already-alias",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + expectedStatus),
                                fieldWithPath("message").description("응답 메시지 / " + expectedMessage),
                                fieldWithPath("data").description("null").optional()
                        )
                ));
    }


    @Test
    @DisplayName("/api/members/accounts/groups/{groupId} 그룹 수정 기본, 슈퍼 계정 그룹은 수정할 수 없어서 실패")
    void updateAccountGroupsFailByDefaultOrSuperUserModifyTry() throws Exception {
        TokenDto tokenDto = getTokenDto();

        AccountGroup accountGroup = accountGroupRepository.findSu().orElseThrow(EntityNotFoundException::new);
        AccountGroupSaveDto updateAccountGroupDto = AccountGroupSaveDto.builder()
                .accountRoles(new ArrayList<>())
                .alias("superUser")
                .name("")
                .build();

        Long groupId = accountGroup.getId();

        int expectedStatus = 4011;
        String expectedMessage = "기본 그룹 혹은 최고 관리자 그룹은 수정,삭제할 수 없습니다.";
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/accounts/groups/{groupId}", groupId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAccountGroupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-member-account-groups-fail-not-avail-try",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + expectedStatus),
                                fieldWithPath("message").description("응답 메시지 / " + expectedMessage),
                                fieldWithPath("data").description("null").optional()
                        )
                ));
    }


    @Test
    @DisplayName("/api/members/accounts/{memberId} 회원 권한 그룹 수정")
    void updateMemberAccountGroup() throws Exception {
        TokenDto tokenDto = getTokenDto();

        AccountGroup accountGroup = accountGroupRepository.findSu().orElseThrow(EntityNotFoundException::new);

        Long groupId = accountGroup.getId();
        OnlyIdDto onlyIdDto = new OnlyIdDto(groupId);


        Member member = memberRepository.findByEmail("user002").orElseThrow(EntityNotFoundException::new);
        Long memberId = member.getId();


        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/members/accounts/{memberId}", member.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(onlyIdDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-member-groups",
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("id").description("권한 그룹 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 204),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null").optional()
                        )
                ));

        Member afterMember = memberRepository.findById(member.getId()).orElseThrow(EntityExistsException::new);

        assertEquals(afterMember.getAccountGroup(), accountGroup);
    }
}
