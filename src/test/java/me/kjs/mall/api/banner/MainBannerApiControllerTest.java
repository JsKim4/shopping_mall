package me.kjs.mall.api.banner;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.banner.MainBanner;
import me.kjs.mall.banner.MainBannerSaveDto;
import me.kjs.mall.banner.MainBannerType;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.CommonSearchCondition;
import me.kjs.mall.common.exception.NoExistIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class MainBannerApiControllerTest extends BaseTest {

    @Test
    @DisplayName("메인 배너 생성 테스트")
    void mainBannerCreateTest() throws Exception {

        MainBannerSaveDto mainBannerSaveDto = MainBannerSaveDto.builder()
                .itemId(1L)
                .bannerType(MainBannerType.EVENT)
                .link(null)
                .mobileImage("/upload/mall/banner/image/" + UUID.randomUUID().toString())
                .pcImage("/upload/mall/banner/image/" + UUID.randomUUID().toString())
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/banners")
                .header("X-AUTH-TOKEN", getTokenDto().getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mainBannerSaveDto)))
                .andExpect(jsonPath("status").value(201))
                .andDo(document("create-banner",
                        requestFields(
                                fieldWithPath("itemId").description("관련 id"),
                                fieldWithPath("bannerType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.MAIN_BANNER_TYPE)),
                                fieldWithPath("link").description("관련 링크"),
                                fieldWithPath("mobileImage").description("모바일 이미지"),
                                fieldWithPath("pcImage").description("pc 이미지")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.mainBannerId").description("메인 배너 고유 번호"),
                                fieldWithPath("data.pcImage").description("pc 이미지"),
                                fieldWithPath("data.mobileImage").description("mobile 이미지"),
                                fieldWithPath("data.sortNumber").description("정렬 번호"),
                                fieldWithPath("data.link").description("관련 링크").optional(),
                                fieldWithPath("data.itemId").description("관련 번호").optional(),
                                fieldWithPath("data.bannerType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.MAIN_BANNER_TYPE)),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS))
                        )

                ));

    }


    @Test
    @DisplayName("메인 배너 삭제 테스트")
    void mainBannerDeleteTest() throws Exception {

        MainBannerSaveDto mainBannerSaveDto = MainBannerSaveDto.builder()
                .itemId(1L)
                .bannerType(MainBannerType.EVENT)
                .link(null)
                .mobileImage("/upload/mall/banner/image/" + UUID.randomUUID().toString())
                .pcImage("/upload/mall/banner/image/" + UUID.randomUUID().toString())
                .build();

        MainBanner mainBanner = mainBannerService.createMainBanner(mainBannerSaveDto);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/banners/{mainBannerId}", mainBanner.getId())
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("delete-banner",
                        pathParameters(
                                parameterWithName("mainBannerId").description("메인 베너 고유 번호")
                        )
                ));

        NoExistIdException noExistIdException = assertThrows(NoExistIdException.class, () -> {
            MainBanner byId = mainBannerRepository.findById(mainBanner.getId()).orElseThrow(NoExistIdException::new);
        });
    }

    @Test
    @DisplayName("메인 배너 조회 테스트")
    void mainBannerQueryTest() throws Exception {

        MainBannerType[] values = MainBannerType.values();
        for (int i = 1; i <= 5; i++) {
            MainBannerSaveDto mainBannerSaveDto = MainBannerSaveDto.builder()
                    .itemId((long) i)
                    .bannerType(values[i % values.length])
                    .link(null)
                    .mobileImage("/upload/mall/banner/image/" + UUID.randomUUID().toString())
                    .pcImage("/upload/mall/banner/image/" + UUID.randomUUID().toString())
                    .build();

            MainBanner mainBanner = mainBannerService.createMainBanner(mainBannerSaveDto);
        }
        CommonSearchCondition commonSearchCondition = CommonSearchCondition.builder()
                .page(0)
                .contents(4)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/banners/")
                .param("page", String.valueOf(commonSearchCondition.getPage()))
                .param("contents", String.valueOf(commonSearchCondition.getContents()))
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(jsonPath("status").value(200))
                .andDo(document("query-banners",
                        requestParameters(
                                parameterWithName("contents").description("요청할 개수(최소 1 최대 100)"),
                                parameterWithName("page").description("요청할 페이지 (0 부터 시작)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("현재 이벤트 개수"),
                                fieldWithPath("data.totalCount").description("전체 이벤트 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[*].mainBannerId").description("메인 배너 고유 번호"),
                                fieldWithPath("data.contents[*].pcImage").description("pc 이미지"),
                                fieldWithPath("data.contents[*].mobileImage").description("mobile 이미지"),
                                fieldWithPath("data.contents[*].sortNumber").description("정렬 번호"),
                                fieldWithPath("data.contents[*].link").description("관련 링크").optional(),
                                fieldWithPath("data.contents[*].itemId").description("관련 번호").optional(),
                                fieldWithPath("data.contents[*].bannerType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.MAIN_BANNER_TYPE)),
                                fieldWithPath("data.contents[*].status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS))

                        )
                ));

    }

}