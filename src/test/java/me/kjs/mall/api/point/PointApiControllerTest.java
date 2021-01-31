package me.kjs.mall.api.point;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.point.PointState;
import me.kjs.mall.point.dto.PointGiveDto;
import me.kjs.mall.point.dto.PointSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class PointApiControllerTest extends BaseTest {


    @Test
    void queryPoints() throws Exception {

        TokenDto userTokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(userTokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);


        for (int i = 0; i < 6; i++) {
            PointGiveDto pointGiveDto = PointGiveDto.builder()
                    .amount(1000)
                    .expiredDate(LocalDate.now().plusDays(7))
                    .memberId(member.getId())
                    .misc("이것은 포인트인가 치킨인가")
                    .build();

            pointService.givePoint(pointGiveDto);
        }


        PointSearchCondition pointSearchCondition = PointSearchCondition.builder()
                .contents(5)
                .page(0)
                .pointState(PointState.ACCUMULATE)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/points/members/current")
                .param("contents", String.valueOf(pointSearchCondition.getContents()))
                .param("page", String.valueOf(pointSearchCondition.getPage()))
                .param("pointState", String.valueOf(pointSearchCondition.getPointState()))
                .header("X-AUTH-TOKEN", userTokenDto.getToken()))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.contentCount").value(pointSearchCondition.getContents()))
                .andExpect(jsonPath("data.nowPage").value(pointSearchCondition.getPage()))
                .andExpect(jsonPath("data.hasNext").value(YnType.Y.name()))
                .andExpect(jsonPath("data.contents[*].pointId").exists())
                .andExpect(jsonPath("data.contents[*].pointState").exists())
                .andExpect(jsonPath("data.contents[*].pointKind").exists())
                .andExpect(jsonPath("data.contents[*].amount").exists())
                .andExpect(jsonPath("data.contents[*].expiredDate").exists())
                .andExpect(jsonPath("data.contents[*].remainExpiredDate").exists())
                .andDo(document("query-points-current",
                        requestParameters(
                                parameterWithName("contents").description("요청할 개수(최소 5 최대 30)"),
                                parameterWithName("pointState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POINT_STATE, "null 일경우 전체검색")),
                                parameterWithName("page").description("요청할 페이지 (0 부터 시작)")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / 200"),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("요청한 콘텐츠 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.hasNext").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "다음 페이지 존재 여부")),
                                fieldWithPath("data.contents[].pointId").description("포인트 고유 번호"),
                                fieldWithPath("data.contents[].pointState").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POINT_STATE)),
                                fieldWithPath("data.contents[].pointKind").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.POINT_KIND)),
                                fieldWithPath("data.contents[].amount").description("적립 / 사용 수량"),
                                fieldWithPath("data.contents[].expiredDate").description("만료 일자"),
                                fieldWithPath("data.contents[].remainExpiredDate").description("남은 만료일"),
                                fieldWithPath("data.contents[].content").description("적립 / 사용 내역"),
                                fieldWithPath("data.contents[].pointCreateDateTime").description("생성 날짜")
                        )
                ));
    }

}