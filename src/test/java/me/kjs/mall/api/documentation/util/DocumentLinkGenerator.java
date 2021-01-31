package me.kjs.mall.api.documentation.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface DocumentLinkGenerator {

    static String generateLinkCode(DocUrl docUrl) {
        return String.format("link:/docs/%s.html[%s %s,role=\"popup\"]", docUrl.pageId, docUrl.text, "코드");
    }

    static String generateLinkCode(DocUrl docUrl, String text) {
        return String.format("link:/docs/%s.html[%s / %s %s,role=\"popup\"]", docUrl.pageId, text, docUrl.text, "코드");
    }

    static String generateText(DocUrl docUrl) {
        return String.format("%s %s", docUrl.text, "코드명");
    }

    @RequiredArgsConstructor
    @Getter
    enum DocUrl {
        DISCOUNT_TYPE("type_discount", "할인 타입"),
        HEADQUARTERS("type_headquarters", "소속 본부"),
        COMMON_STATUS("type_commonStatus", "기본 상태"),
        YN_TYPE("type_ynType", "승인/거부"),
        EXCEPTION_STATUS("type_exceptionStatus", "예외"),
        UPLOAD_PATH("type_uploadPath", "업로드 경로"),
        CONDITION_TYPE("type_conditionType", "상품 검색 타입"),
        ACCOUNT_ROLE("type_accountRole", "권한"),
        COMPANY_RANK("type_companyRank", "직급"),
        MEMBER_DIVISION("type_memberDivision", "회원 구분"),
        ORDER_STATE("type_orderState", "주문 상태"),
        PAYMENT_METHOD("type_paymentMethod", "결제 수단"),
        CARRIER("type_carrier", "배송사"),
        POINT_STATE("type_pointState", "포인트"),
        DELIVERY_TYPE("type_deliveryType", "배송 상태"),
        PRODUCT_SORT_TYPE("type_productSortType", "상품 정렬 타입"),
        ORDER_EXCHANGE_STATE("type_orderExchangeState", "상품 교환"),
        POLICY_TYPE("type_policyType", "약관 타입"),
        ACCOUNT_STATUS("type_accountStatus", "계정 상태"),
        EVENT_STATUS("type_eventStatus", "이벤트 상태"),
        POST_STATUS("type_postStatus", "게시물 게시 상태"),
        POINT_KIND("type_pointKind", "포인트 사용 / 적립 종류"),
        REVIEW_ORDER_TYPE("type_reviewOrderType", "리뷰 정렬 타입"),
        COUPON_STATUS("type_couponStatus", "쿠폰 상태"),
        GENDER("type_gender", "성별 타입"),
        SOCIAL_TYPE("type_social", "social 타입"),
        MAIN_BANNER_TYPE("type_mainBanner", "메인 베너 타입"),
        BANK("type_bank", "은행 타입"),
        PRODUCT_PROVISION_TYPE("type_productProvision", "상품군 타입");

        private final String pageId;
        @Getter
        private final String text;
    }
}
