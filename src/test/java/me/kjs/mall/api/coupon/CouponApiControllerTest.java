package me.kjs.mall.api.coupon;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.coupon.Coupon;
import me.kjs.mall.coupon.CouponStatus;
import me.kjs.mall.coupon.IssueCoupon;
import me.kjs.mall.coupon.dto.CouponConditionUpdateDto;
import me.kjs.mall.coupon.dto.CouponSaveDto;
import me.kjs.mall.coupon.dto.CouponSearchCondition;
import me.kjs.mall.coupon.dto.IssueCouponSearchCondition;
import me.kjs.mall.coupon.exception.NotAvailableCouponUpdateException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.product.type.DiscountType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class CouponApiControllerTest extends BaseTest {

    @Test
    void couponCreateTest() throws Exception {
        CouponSaveDto couponSaveDto = CouponSaveDto.builder()
                .content("coupon content")
                .discountAmount(3000)
                .discountType(DiscountType.FLAT_RATE)
                .maxDiscountPrice(100000)
                .maxPeriod(300)
                .minPrice(3100)
                .title("coupon title")
                .build();

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/coupons")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponSaveDto)))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.content").value(couponSaveDto.getContent()))
                .andExpect(jsonPath("data.discountAmount").value(couponSaveDto.getDiscountAmount()))
                .andExpect(jsonPath("data.discountType").value(couponSaveDto.getDiscountType().name()))
                .andExpect(jsonPath("data.maxDiscountPrice").value(couponSaveDto.getDiscountAmount()))
                .andExpect(jsonPath("data.maxPeriod").value(couponSaveDto.getMaxPeriod()))
                .andExpect(jsonPath("data.minPrice").value(couponSaveDto.getMinPrice()))
                .andExpect(jsonPath("data.title").value(couponSaveDto.getTitle()))
                .andDo(document("create-coupon",
                        requestFields(
                                fieldWithPath("title").description("쿠폰 제목"),
                                fieldWithPath("content").description("쿠폰 내용"),
                                fieldWithPath("discountAmount").description("쿠폰 할인률 / 할인 금액"),
                                fieldWithPath("discountType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DISCOUNT_TYPE)),
                                fieldWithPath("maxDiscountPrice").description("최대 할인 금액 / 0일 경우 무제한 / 정액 할인일 경우 X").optional(),
                                fieldWithPath("maxPeriod").description("쿠폰 최대 유지기한"),
                                fieldWithPath("minPrice").description("최소 결제 금액")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 201),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.couponId").description("쿠폰 고유 번호"),
                                fieldWithPath("data.title").description("쿠폰 제목"),
                                fieldWithPath("data.content").description("쿠폰 내용"),
                                fieldWithPath("data.discountType").description(DocumentLinkGenerator.generateText(DocumentLinkGenerator.DocUrl.DISCOUNT_TYPE)),
                                fieldWithPath("data.discountAmount").description("쿠폰 할인률 / 할인 금액"),
                                fieldWithPath("data.maxDiscountPrice").description("최대 할인 금액"),
                                fieldWithPath("data.minPrice").description("최소 결제 금액"),
                                fieldWithPath("data.status").description("상태값"),
                                fieldWithPath("data.maxPeriod").description("최대 유지기간")
                        )
                ));
    }

    @Test
    void queryCoupons() throws Exception {
        for (int i = 0; i < 5; i++) {
            CouponSaveDto couponSaveDto = CouponSaveDto.builder()
                    .content("coupon content")
                    .discountAmount(3000)
                    .discountType(DiscountType.FLAT_RATE)
                    .maxDiscountPrice(100000)
                    .maxPeriod(300)
                    .minPrice(3100)
                    .title("coupon title")
                    .build();
            couponService.createCoupon(couponSaveDto);
        }
        TokenDto tokenDto = getTokenDto();

        CouponSearchCondition couponSearchCondition = CouponSearchCondition.builder()
                .contents(5)
                .page(0)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/coupons")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .param("contents", String.valueOf(couponSearchCondition.getContents()))
                .param("page", String.valueOf(couponSearchCondition.getPage())))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.contents[*].couponId").exists())
                .andExpect(jsonPath("data.contents[*].title").exists())
                .andExpect(jsonPath("data.contents[*].content").exists())
                .andExpect(jsonPath("data.contents[*].discountType").exists())
                .andExpect(jsonPath("data.contents[*].discountAmount").exists())
                .andExpect(jsonPath("data.contents[*].maxDiscountPrice").exists())
                .andExpect(jsonPath("data.contents[*].minPrice").exists())
                .andExpect(jsonPath("data.contents[*].status").exists())
                .andExpect(jsonPath("data.contents[*].maxPeriod").exists())
                .andDo(document("query-coupons",
                        requestParameters(
                                parameterWithName("contents").description("가져올 개수"),
                                parameterWithName("page").description("가져올 페이지 0 부터 시작")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 200),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("현재 이벤트 개수"),
                                fieldWithPath("data.totalCount").description("전체 이벤트 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[*].couponId").description("쿠폰 고유 번호"),
                                fieldWithPath("data.contents[*].title").description("쿠폰 제목"),
                                fieldWithPath("data.contents[*].content").description("쿠폰 내용"),
                                fieldWithPath("data.contents[*].discountType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DISCOUNT_TYPE)),
                                fieldWithPath("data.contents[*].discountAmount").description("할인률 / 할인 금액"),
                                fieldWithPath("data.contents[*].maxDiscountPrice").description("최대 할인 금액"),
                                fieldWithPath("data.contents[*].minPrice").description("최소 결제 금액"),
                                fieldWithPath("data.contents[*].status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.contents[*].maxPeriod").description("최대 유지 기간")
                        )));
    }

    @Test
    void queryCoupon() throws Exception {

        CouponSaveDto couponSaveDto = CouponSaveDto.builder()
                .content("coupon content")
                .discountAmount(3000)
                .discountType(DiscountType.FLAT_RATE)
                .maxDiscountPrice(3000)
                .maxPeriod(300)
                .minPrice(3100)
                .title("coupon title")
                .build();
        Coupon coupon = couponService.createCoupon(couponSaveDto);

        TokenDto tokenDto = getTokenDto();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/coupons/{couponId}", coupon.getId())
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.couponId").exists())
                .andExpect(jsonPath("data.title").value(couponSaveDto.getTitle()))
                .andExpect(jsonPath("data.content").value(couponSaveDto.getContent()))
                .andExpect(jsonPath("data.discountType").value(couponSaveDto.getDiscountType().name()))
                .andExpect(jsonPath("data.discountAmount").value(couponSaveDto.getDiscountAmount()))
                .andExpect(jsonPath("data.maxDiscountPrice").value(couponSaveDto.getDiscountAmount()))
                .andExpect(jsonPath("data.minPrice").value(couponSaveDto.getMinPrice()))
                .andExpect(jsonPath("data.status").value(CommonStatus.CREATED.name()))
                .andExpect(jsonPath("data.maxPeriod").value(couponSaveDto.getMaxPeriod()))
                .andDo(document("query-coupon",
                        pathParameters(
                                parameterWithName("couponId").description("쿠폰 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 200),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.couponId").description("쿠폰 고유 번호"),
                                fieldWithPath("data.title").description("쿠폰 제목"),
                                fieldWithPath("data.content").description("쿠폰 내용"),
                                fieldWithPath("data.discountType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DISCOUNT_TYPE)),
                                fieldWithPath("data.discountAmount").description("할인률 / 할인 금액"),
                                fieldWithPath("data.maxDiscountPrice").description("최대 할인 금액"),
                                fieldWithPath("data.minPrice").description("최소 결제 금액"),
                                fieldWithPath("data.status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.maxPeriod").description("최대 유지 기간")
                        )));
    }

    @Test
    void updateCoupon() throws Exception {
        CouponSaveDto couponSaveDto = CouponSaveDto.builder()
                .content("coupon content")
                .discountAmount(3000)
                .discountType(DiscountType.FLAT_RATE)
                .maxDiscountPrice(100000)
                .maxPeriod(300)
                .minPrice(3100)
                .title("coupon title")
                .build();
        Coupon coupon = couponService.createCoupon(couponSaveDto);

        Long couponId = coupon.getId();

        CouponSaveDto couponUpdateDto = CouponSaveDto.builder()
                .content("coupon update content")
                .discountAmount(10)
                .discountType(DiscountType.PERCENT)
                .maxDiscountPrice(10000)
                .maxPeriod(300)
                .minPrice(5000)
                .title("coupon update title")
                .build();

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/coupons/{couponId}", couponId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponUpdateDto)))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("update-coupon",
                        pathParameters(
                                parameterWithName("couponId").description("쿠폰 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("title").description("쿠폰 제목"),
                                fieldWithPath("content").description("쿠폰 내용"),
                                fieldWithPath("discountAmount").description("쿠폰 할인률 / 할인 금액"),
                                fieldWithPath("discountType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DISCOUNT_TYPE)),
                                fieldWithPath("maxDiscountPrice").description("최대 할인 금액 / 0일 경우 무제한 / 정액 할인일 경우 X").optional(),
                                fieldWithPath("maxPeriod").description("쿠폰 최대 유지기한"),
                                fieldWithPath("minPrice").description("최소 결제 금액")
                        )
                ));

        assertEquals(coupon.getDiscountAmount(), couponUpdateDto.getDiscountAmount());
        assertEquals(coupon.getDiscountType(), couponUpdateDto.getDiscountType());
        assertEquals(coupon.getMinPrice(), couponUpdateDto.getMinPrice());
        assertEquals(coupon.getContent(), couponUpdateDto.getContent());
        assertEquals(coupon.getMaxPeriod(), couponUpdateDto.getMaxPeriod());
        assertEquals(coupon.getTitle(), couponUpdateDto.getTitle());
        assertEquals(coupon.getMaxDiscountPrice(), couponUpdateDto.getMaxDiscountPrice());
    }

    @Test
    void updateFailCoupon() throws Exception {
        CouponSaveDto couponSaveDto = CouponSaveDto.builder()
                .content("coupon content")
                .discountAmount(3000)
                .discountType(DiscountType.FLAT_RATE)
                .maxDiscountPrice(100000)
                .maxPeriod(300)
                .minPrice(3100)
                .title("coupon title")
                .build();
        Coupon coupon = couponService.createCoupon(couponSaveDto);
        coupon.updateStatus(CommonStatus.USED);
        Long couponId = coupon.getId();

        CouponSaveDto couponUpdateDto = CouponSaveDto.builder()
                .content("coupon update content")
                .discountAmount(10)
                .discountType(DiscountType.PERCENT)
                .maxDiscountPrice(10000)
                .maxPeriod(300)
                .minPrice(5000)
                .title("coupon update title")
                .build();

        TokenDto tokenDto = getTokenDto();
        int expectedStatus = new NotAvailableCouponUpdateException().getStatus();
        assert expectedStatus == 4038;
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/coupons/{couponId}", couponId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponUpdateDto)))
                .andExpect(jsonPath("status").value(expectedStatus));
    }

    @Test
    void useCoupon() throws Exception {
        CouponSaveDto couponSaveDto = CouponSaveDto.builder()
                .content("coupon content")
                .discountAmount(3000)
                .discountType(DiscountType.FLAT_RATE)
                .maxDiscountPrice(100000)
                .maxPeriod(300)
                .minPrice(3100)
                .title("coupon title")
                .build();
        Coupon coupon = couponService.createCoupon(couponSaveDto);
        coupon.addCouponCondition(CouponConditionUpdateDto.builder()
                .useDateTime(true)
                .beginDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusDays(1))
                .build());
        Long couponId = coupon.getId();

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/coupons/used/{couponId}", couponId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponSaveDto)))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("update-coupon-used",
                        pathParameters(
                                parameterWithName("couponId").description("쿠폰 고유 번호")
                        )
                ));

        assertEquals(coupon.getStatus(), CommonStatus.USED);
    }

    @Test
    void unUseCoupon() throws Exception {
        CouponSaveDto couponSaveDto = CouponSaveDto.builder()
                .content("coupon content")
                .discountAmount(3000)
                .discountType(DiscountType.FLAT_RATE)
                .maxDiscountPrice(100000)
                .maxPeriod(300)
                .minPrice(3100)
                .title("coupon title")
                .build();
        Coupon coupon = couponService.createCoupon(couponSaveDto);

        Long couponId = coupon.getId();

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/coupons/unused/{couponId}", couponId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponSaveDto)))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("update-coupon-unused",
                        pathParameters(
                                parameterWithName("couponId").description("쿠폰 고유 번호")
                        )
                ));

        assertEquals(coupon.getStatus(), CommonStatus.UN_USED);
    }


    @Test
    void deleteCoupon() throws Exception {
        CouponSaveDto couponSaveDto = CouponSaveDto.builder()
                .content("coupon content")
                .discountAmount(3000)
                .discountType(DiscountType.FLAT_RATE)
                .maxDiscountPrice(100000)
                .maxPeriod(300)
                .minPrice(3100)
                .title("coupon title")
                .build();
        Coupon coupon = couponService.createCoupon(couponSaveDto);

        Long couponId = coupon.getId();

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/coupons/{couponId}", couponId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(couponSaveDto)))
                .andExpect(jsonPath("status").value(204))
                .andDo(document("delete-coupon",
                        pathParameters(
                                parameterWithName("couponId").description("쿠폰 고유 번호")
                        )
                ));

        assertThrows(NoExistIdException.class, () -> {
            couponService.findCouponById(couponId);
        });
    }

    @Test
    void issueCouponQueryTest() throws Exception {
        CouponSaveDto couponSaveDto = CouponSaveDto.builder()
                .content("coupon content")
                .discountAmount(3000)
                .discountType(DiscountType.FLAT_RATE)
                .maxDiscountPrice(100000)
                .maxPeriod(300)
                .minPrice(3100)
                .title("coupon title")
                .build();
        Coupon coupon1 = couponService.createCoupon(couponSaveDto);
        Coupon coupon2 = couponService.createCoupon(couponSaveDto);
        coupon1.addCouponCondition(CouponConditionUpdateDto.builder()
                .useDateTime(true)
                .beginDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusDays(1))
                .build());
        Long couponId = coupon1.getId();
        couponService.useCoupon(couponId);
        Member member = memberRepository.findByRefreshToken(getTokenDto().getRefreshToken()).orElseThrow(NoExistIdException::new);
        IssueCoupon issueCoupon1 = IssueCoupon.issueCoupon(coupon1, member, LocalDate.now().plusDays(20));
        IssueCoupon issueCoupon2 = IssueCoupon.issueCoupon(coupon2, member, LocalDate.now().plusDays(20));

        issueCouponRepository.save(issueCoupon1);
        issueCouponRepository.save(issueCoupon2);

        IssueCouponSearchCondition issueCouponSearchCondition = IssueCouponSearchCondition.builder()
                .couponStatus(CouponStatus.ISSUE)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/coupons/issue/current")
                .param("couponStatus", String.valueOf(issueCouponSearchCondition.getCouponStatus()))
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andDo(document("query-issue-coupon-current",
                        requestParameters(
                                parameterWithName("couponStatus").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COUPON_STATUS))
                        ),
                        responseFields(
                                fieldWithPath("status").description("상태값 / " + 200),
                                fieldWithPath("message").description("메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data[*].title").description("쿠폰 제목"),
                                fieldWithPath("data[*].content").description("쿠폰 발급 내용"),
                                fieldWithPath("data[*].expiredDate").description("쿠폰 만료일"),
                                fieldWithPath("data[*].remainUsePeriod").description("남은 기간 (일수)"),
                                fieldWithPath("data[*].discountType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DISCOUNT_TYPE)),
                                fieldWithPath("data[*].discountAmount").description("할인률 / 금액"),
                                fieldWithPath("data[*].maxDiscountPrice").description("최대 할인 금액"),
                                fieldWithPath("data[*].minPrice").description("최소 결제금액")
                        )
                ));

    }


}