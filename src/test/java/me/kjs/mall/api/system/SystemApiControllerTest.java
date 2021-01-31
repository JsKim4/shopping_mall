package me.kjs.mall.api.system;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.system.dto.SellerUpdateDto;
import me.kjs.mall.system.dto.VersionCreateDto;
import me.kjs.mall.system.dto.VersionUpdateDto;
import me.kjs.mall.system.version.AppVersion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SystemApiControllerTest extends BaseTest {

    @Test
    @DisplayName("/api/global/{packageName} 기본 정보 조회 성공 케이스")
    void queryGlobalTest() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/global/{packageName}", "kr.co.kjs-mall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.version.packageName").exists())
                .andExpect(jsonPath("data.version.os").exists())
                .andExpect(jsonPath("data.version.latelyCode").exists())
                .andExpect(jsonPath("data.version.latelyVersion").exists())
                .andExpect(jsonPath("data.version.minVersion").exists())
                .andExpect(jsonPath("data.version.minCode").exists())
                .andExpect(jsonPath("data.seller.companyName").exists())
                .andExpect(jsonPath("data.seller.corporateRegistrationNumber").exists())
                .andExpect(jsonPath("data.seller.companyLocation").exists())
                .andExpect(jsonPath("data.seller.companyType").exists())
                .andExpect(jsonPath("data.seller.netSaleReportNumber").exists())
                .andExpect(jsonPath("data.seller.ceoName").exists())
                .andExpect(jsonPath("data.seller.forwardingAddress").exists())
                .andExpect(jsonPath("data.seller.returnAddress").exists())
                .andExpect(jsonPath("data.seller.csCenterTel").exists())
                .andExpect(jsonPath("data.seller.deliveryFee").exists())
                .andExpect(jsonPath("data.seller.deliveryCondition").exists())
                .andExpect(jsonPath("data.seller.returnFee").exists())
                .andExpect(jsonPath("data.seller.companyFax").exists())
                .andDo(document("query-global",
                        pathParameters(
                                parameterWithName("packageName").description("프로젝트 패키지 이름"))
                        ,
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.version.latelyCode").description("최신 버전 코드"),
                                fieldWithPath("data.version.latelyVersion").description("최신 버전"),
                                fieldWithPath("data.version.minCode").description("최소 버전 코드"),
                                fieldWithPath("data.version.minVersion").description("최소 버전 "),
                                fieldWithPath("data.version.packageName").description("패키지 이름"),
                                fieldWithPath("data.version.os").description("운영체제"),
                                fieldWithPath("data.seller.companyName").description("상호명"),
                                fieldWithPath("data.seller.corporateRegistrationNumber").description("사업자 등록 번호"),
                                fieldWithPath("data.seller.companyLocation").description("시압징 소재지"),
                                fieldWithPath("data.seller.companyType").description("사업자 구분"),
                                fieldWithPath("data.seller.netSaleReportNumber").description("통신판매업 신고번호"),
                                fieldWithPath("data.seller.ceoName").description("대표자"),
                                fieldWithPath("data.seller.companyFax").description("FAX"),
                                fieldWithPath("data.seller.forwardingAddress").description("출고지 주소"),
                                fieldWithPath("data.seller.returnAddress").description("반품지 주소"),
                                fieldWithPath("data.seller.csCenterTel").description("고객센터"),
                                fieldWithPath("data.seller.deliveryFee").description("기본 배송비"),
                                fieldWithPath("data.seller.deliveryCondition").description("기본 배송비 조건"),
                                fieldWithPath("data.seller.returnFee").description("기본 반품 배송비")
                        )));
    }


    @Test
    @DisplayName("/api/global/version 버전 생성 성공")
    void createVersionTest() throws Exception {


        VersionCreateDto versionCreateDto = VersionCreateDto.builder()
                .latelyCode("1")
                .latelyVersion("1.0.0")
                .minCode("1")
                .minVersion("1.0.0")
                .os("linux")
                .packageName("me.kjs.mall")
                .build();

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/global/versions")
                .content(objectMapper.writeValueAsString(versionCreateDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.latelyCode").value(versionCreateDto.getLatelyCode()))
                .andExpect(jsonPath("data.latelyVersion").value(versionCreateDto.getLatelyVersion()))
                .andExpect(jsonPath("data.minCode").value(versionCreateDto.getMinCode()))
                .andExpect(jsonPath("data.minVersion").value(versionCreateDto.getMinVersion()))
                .andExpect(jsonPath("data.os").value(versionCreateDto.getOs()))
                .andExpect(jsonPath("data.packageName").value(versionCreateDto.getPackageName()))
                .andDo(document(
                        "create-version",
                        requestFields(
                                fieldWithPath("latelyCode").description("최신 버전 코드"),
                                fieldWithPath("latelyVersion").description("최신 버전"),
                                fieldWithPath("minCode").description("최소 버전 코드"),
                                fieldWithPath("minVersion").description("최소 버전"),
                                fieldWithPath("os").description("운영체제"),
                                fieldWithPath("packageName").description("패키지 이름")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.latelyCode").description("최신 버전 코드"),
                                fieldWithPath("data.latelyVersion").description("최신 버전"),
                                fieldWithPath("data.minCode").description("최소 버전 코드"),
                                fieldWithPath("data.minVersion").description("최소 버전"),
                                fieldWithPath("data.os").description("운영체제"),
                                fieldWithPath("data.packageName").description("패키지 이름")
                        )
                ));
    }


    @Test
    @DisplayName("/api/global/version/{packageName} 버전 수정 성공")
    void updateVersionTest() throws Exception {
        VersionCreateDto versionCreateDto = VersionCreateDto.builder()
                .latelyCode("1")
                .latelyVersion("1.0.0")
                .minCode("1")
                .minVersion("1.0.0")
                .os("linux")
                .packageName("me.kjs.mall")
                .build();

        TokenDto tokenDto = getTokenDto();

        AppVersion version = systemService.createVersion(versionCreateDto);

        VersionUpdateDto versionUpdateDto = VersionUpdateDto.builder()
                .latelyCode("2")
                .latelyVersion("2.0.0")
                .minCode("2")
                .minVersion("2.0.0")
                .os("linux")
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/global/versions/{packageName}", versionCreateDto.getPackageName())
                .content(objectMapper.writeValueAsString(versionUpdateDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-version",
                        pathParameters(
                                parameterWithName("packageName").description("패키지 이름")
                        ),
                        requestFields(
                                fieldWithPath("latelyCode").description("최신 버전 코드").optional(),
                                fieldWithPath("latelyVersion").description("최신 버전").optional(),
                                fieldWithPath("minCode").description("최소 버전 코드").optional(),
                                fieldWithPath("minVersion").description("최소 버전").optional(),
                                fieldWithPath("os").description("운영체제").optional()
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 204),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null").optional()
                        )
                ));

        assertEquals(versionUpdateDto.getLatelyCode(), version.getLatelyCode());
        assertEquals(versionUpdateDto.getLatelyVersion(), version.getLatelyVersion());
        assertEquals(versionUpdateDto.getMinCode(), version.getMinCode());
        assertEquals(versionUpdateDto.getMinVersion(), version.getMinVersion());
        assertEquals(versionUpdateDto.getOs(), version.getOs());
    }

    @Test
    @DisplayName("/api/global/version 버전 조회 성공")
    void queryVersionTest() throws Exception {
        TokenDto tokenDto = getTokenDto();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/global/versions")
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").exists())
                .andDo(document(
                        "query-versions",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data[].latelyCode").description("최신 버전 코드"),
                                fieldWithPath("data[].latelyVersion").description("최신 버전"),
                                fieldWithPath("data[].minCode").description("최소 버전 코드"),
                                fieldWithPath("data[].minVersion").description("최소 버전"),
                                fieldWithPath("data[].os").description("운영체제"),
                                fieldWithPath("data[].packageName").description("패키지 이름")
                        )
                ));

    }


    @Test
    @DisplayName("/api/global/seller 판매자 정보 조회")
    void querySellerTest() throws Exception {
        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/global/seller"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").exists())
                .andDo(document(
                        "query-seller",
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.companyName").description("상호명"),
                                fieldWithPath("data.corporateRegistrationNumber").description("사업자 등록 번호"),
                                fieldWithPath("data.companyLocation").description("시압징 소재지"),
                                fieldWithPath("data.companyType").description("사업자 구분"),
                                fieldWithPath("data.netSaleReportNumber").description("통신판매업 신고번호"),
                                fieldWithPath("data.ceoName").description("대표자"),
                                fieldWithPath("data.companyFax").description("FAX"),
                                fieldWithPath("data.forwardingAddress").description("출고지 주소"),
                                fieldWithPath("data.returnAddress").description("반품지 주소"),
                                fieldWithPath("data.csCenterTel").description("고객센터"),
                                fieldWithPath("data.deliveryFee").description("기본 배송비"),
                                fieldWithPath("data.deliveryCondition").description("기본 배송비 조건"),
                                fieldWithPath("data.returnFee").description("기본 반품 배송비")
                        )
                ));
    }

    @Test
    @DisplayName("/api/global/seller 판매자 정보 변경")
    void updateSellerTest() throws Exception {


        TokenDto tokenDto = getTokenDto();

        SellerUpdateDto seller = SellerUpdateDto.builder()
                .corporateRegistrationNumber("138-81-66285")
                .companyLocation("(13558) asss")
                .companyType("aaaa")
                .netSaleReportNumber("2016-sungnam-0615")
                .ceoName("aaa,nnn")
                .forwardingAddress("(U : 54576) asdasd")
                .returnAddress("(U : 13558) asdasd")
                .csCenterTel("CRM CENTER1544-2492")
                .deliveryFee(2500)
                .deliveryCondition(30000)
                .returnFee(3000)
                .companyFax("031-718-703222")
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/global/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seller))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").exists())
                .andDo(document(
                        "update-seller",
                        requestFields(
                                fieldWithPath("corporateRegistrationNumber").description("사업자 등록 번호"),
                                fieldWithPath("companyLocation").description("시압징 소재지"),
                                fieldWithPath("companyType").description("사업자 구분"),
                                fieldWithPath("netSaleReportNumber").description("통신판매업 신고번호"),
                                fieldWithPath("ceoName").description("대표자"),
                                fieldWithPath("companyFax").description("FAX"),
                                fieldWithPath("forwardingAddress").description("출고지 주소"),
                                fieldWithPath("returnAddress").description("반품지 주소"),
                                fieldWithPath("csCenterTel").description("고객센터"),
                                fieldWithPath("deliveryFee").description("기본 배송비"),
                                fieldWithPath("deliveryCondition").description("기본 배송비 조건"),
                                fieldWithPath("returnFee").description("기본 반품 배송비")
                        )
                ));
    }
}