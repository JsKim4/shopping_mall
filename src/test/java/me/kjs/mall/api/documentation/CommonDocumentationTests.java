package me.kjs.mall.api.documentation;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.banner.MainBannerType;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.common.type.*;
import me.kjs.mall.configs.RestDocsConfig;
import me.kjs.mall.connect.UploadPath;
import me.kjs.mall.coupon.CouponStatus;
import me.kjs.mall.event.EventStatus;
import me.kjs.mall.member.PolicyType;
import me.kjs.mall.member.dto.MemberQueryCondition;
import me.kjs.mall.member.social.SocialType;
import me.kjs.mall.member.type.*;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.specific.destination.Carrier;
import me.kjs.mall.order.specific.exchange.OrderExchangeState;
import me.kjs.mall.point.PointKind;
import me.kjs.mall.point.PointState;
import me.kjs.mall.product.base.ProductProvisionType;
import me.kjs.mall.product.type.DeliveryType;
import me.kjs.mall.product.type.DiscountType;
import me.kjs.mall.product.util.ProductSortType;
import me.kjs.mall.review.dto.ReviewOrderType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;


@SpringBootTest
@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@Transactional
public class CommonDocumentationTests extends BaseTest {
    @Test
    void discountTypeTest() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/docs"))
                .andDo(document("type-headquarters",
                        customResponseFields("custom-response",
                                beneathPath("data.headquarters").withSubsectionId("type"),
                                attributes(key("title").value("본부")),
                                enumConvertFieldDescriptor(Headquarters.values())))
                )
                .andDo(document("type-commonStatus",
                        customResponseFields("custom-response",
                                beneathPath("data.commonStatus").withSubsectionId("type"),
                                attributes(key("title").value("일반 상태")),
                                enumConvertFieldDescriptor(CommonStatus.values())))
                )
                .andDo(document("type-ynType",
                        customResponseFields("custom-response",
                                beneathPath("data.ynType").withSubsectionId("type"),
                                attributes(key("title").value("승인/거부")),
                                enumConvertFieldDescriptor(YnType.values())))
                )
                .andDo(document("type-exceptionStatus",
                        customResponseFields("custom-response",
                                beneathPath("data.exceptionStatus").withSubsectionId("type"),
                                attributes(key("title").value("예외 상태")),
                                enumConvertFieldDescriptor(ExceptionStatus.values())))
                )
                .andDo(document("type-uploadPath",
                        customResponseFields("custom-response",
                                beneathPath("data.uploadPath").withSubsectionId("type"),
                                attributes(key("title").value("업로드 경로")),
                                enumConvertFieldDescriptor(UploadPath.values())))
                )
                .andDo(document("type-conditionType",
                        customResponseFields("custom-response",
                                beneathPath("data.conditionType").withSubsectionId("type"),
                                attributes(key("title").value("검색 타입")),
                                enumConvertFieldDescriptor(MemberQueryCondition.Condition.ConditionType.values())))
                )
                .andDo(document("type-accountRole",
                        customResponseFields("custom-response",
                                beneathPath("data.accountRole").withSubsectionId("type"),
                                attributes(key("title").value("회원 권한")),
                                enumConvertFieldDescriptor(AccountRole.values())))
                )
                .andDo(document("type-companyRank",
                        customResponseFields("custom-response",
                                beneathPath("data.companyRank").withSubsectionId("type"),
                                attributes(key("title").value("직급")),
                                enumConvertFieldDescriptor(CompanyRank.values())))
                )
                .andDo(document("type-memberDivision",
                        customResponseFields("custom-response",
                                beneathPath("data.memberDivision").withSubsectionId("type"),
                                attributes(key("title").value("회원 구분")),
                                enumConvertFieldDescriptor(MemberDivision.values())))
                )
                .andDo(document("type-orderState",
                        customResponseFields("custom-response",
                                beneathPath("data.orderState").withSubsectionId("type"),
                                attributes(key("title").value("주문 상태")),
                                enumConvertFieldDescriptor(OrderState.values())))
                )
                .andDo(document("type-paymentMethod",
                        customResponseFields("custom-response",
                                beneathPath("data.paymentMethod").withSubsectionId("type"),
                                attributes(key("title").value("결제 수단")),
                                enumConvertFieldDescriptor(PaymentMethod.values())))
                )
                .andDo(document("type-discount",
                        customResponseFields("custom-response",
                                beneathPath("data.discountType").withSubsectionId("type"),
                                attributes(key("title").value("할인 타입")),
                                enumConvertFieldDescriptor(DiscountType.values())))
                )
                .andDo(document("type-carrier",
                        customResponseFields("custom-response",
                                beneathPath("data.carrier").withSubsectionId("type"),
                                attributes(key("title").value("배송 회사")),
                                enumConvertFieldDescriptor(Carrier.values())))
                )
                .andDo(document("type-pointState",
                        customResponseFields("custom-response",
                                beneathPath("data.pointState").withSubsectionId("type"),
                                attributes(key("title").value("포인트 구분")),
                                enumConvertFieldDescriptor(PointState.values())))
                )
                .andDo(document("type-deliveryType",
                        customResponseFields("custom-response",
                                beneathPath("data.deliveryType").withSubsectionId("type"),
                                attributes(key("title").value("배송 타입")),
                                enumConvertFieldDescriptor(DeliveryType.values())))
                )
                .andDo(document("type-productSortType",
                        customResponseFields("custom-response",
                                beneathPath("data.productSortType").withSubsectionId("type"),
                                attributes(key("title").value("상품 정렬 타입")),
                                enumConvertFieldDescriptor(ProductSortType.values())))
                )
                .andDo(document("type-orderExchangeState",
                        customResponseFields("custom-response",
                                beneathPath("data.orderExchangeState").withSubsectionId("type"),
                                attributes(key("title").value("상품 교환 상태")),
                                enumConvertFieldDescriptor(OrderExchangeState.values())))
                )
                .andDo(document("type-accountStatus",
                        customResponseFields("custom-response",
                                beneathPath("data.accountStatus").withSubsectionId("type"),
                                attributes(key("title").value("계정 상태")),
                                enumConvertFieldDescriptor(AccountStatus.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.POLICY_TYPE.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.policyType").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.POLICY_TYPE.getText())),
                                enumConvertFieldDescriptor(PolicyType.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.EVENT_STATUS.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.eventStatus").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.EVENT_STATUS.getText())),
                                enumConvertFieldDescriptor(EventStatus.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.POST_STATUS.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.postStatus").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.POST_STATUS.getText())),
                                enumConvertFieldDescriptor(PostStatus.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.REVIEW_ORDER_TYPE.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.reviewOrderType").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.REVIEW_ORDER_TYPE.getText())),
                                enumConvertFieldDescriptor(ReviewOrderType.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.POINT_KIND.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.pointKind").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.POINT_KIND.getText())),
                                enumConvertFieldDescriptor(PointKind.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.COUPON_STATUS.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.couponStatus").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.COUPON_STATUS.getText())),
                                enumConvertFieldDescriptor(CouponStatus.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.PRODUCT_PROVISION_TYPE.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.productProvision").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.PRODUCT_PROVISION_TYPE.getText())),
                                enumConvertFieldDescriptor(ProductProvisionType.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.GENDER.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.gender").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.GENDER.getText())),
                                enumConvertFieldDescriptor(Gender.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.SOCIAL_TYPE.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.socialType").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.SOCIAL_TYPE.getText())),
                                enumConvertFieldDescriptor(SocialType.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.MAIN_BANNER_TYPE.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.mainBanner").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.MAIN_BANNER_TYPE.getText())),
                                enumConvertFieldDescriptor(MainBannerType.values())))
                )
                .andDo(document(DocumentLinkGenerator.DocUrl.BANK.getPageId(),
                        customResponseFields("custom-response",
                                beneathPath("data.bank").withSubsectionId("type"),
                                attributes(key("title").value(DocumentLinkGenerator.DocUrl.BANK.getText())),
                                enumConvertFieldDescriptor(Bank.values())))
                );

    }

    private FieldDescriptor[] enumConvertFieldDescriptor(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
                .map(enumType -> fieldWithPath(enumType.name()).description(enumType.getDescription()))
                .toArray(FieldDescriptor[]::new);
    }

    @Primary
    public static CustomResponseFieldsSnippet customResponseFields(String type,
                                                                   PayloadSubsectionExtractor<?> subsectionExtractor,
                                                                   Map<String, Object> attributes, FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes
                , true);
    }
}


