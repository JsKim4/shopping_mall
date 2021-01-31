package me.kjs.mall.api.documentation;

import me.kjs.mall.banner.MainBannerType;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.exception.ExceptionStatus;
import me.kjs.mall.common.type.*;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class EnumViewController {

    @GetMapping("/docs")
    public ResponseDto<Docs> findAll() {

        Map<String, String> discountType = getDocs(DiscountType.values());
        Map<String, String> headquarters = getDocs(Headquarters.values());
        Map<String, String> commonStatus = getDocs(CommonStatus.values());
        Map<String, String> ynType = getDocs(YnType.values());
        Map<String, String> exceptionStatus = getDocs(ExceptionStatus.values());
        Map<String, String> uploadPath = getDocs(UploadPath.values());
        Map<String, String> conditionType = getDocs(MemberQueryCondition.Condition.ConditionType.values());
        Map<String, String> accountRole = getDocs(AccountRole.values());
        Map<String, String> companyRank = getDocs(CompanyRank.values());
        Map<String, String> memberDivision = getDocs(MemberDivision.values());
        Map<String, String> orderState = getDocs(OrderState.values());
        Map<String, String> paymentMethod = getDocs(PaymentMethod.values());
        Map<String, String> carrier = getDocs(Carrier.values());
        Map<String, String> pointState = getDocs(PointState.values());
        Map<String, String> deliveryType = getDocs(DeliveryType.values());
        Map<String, String> productSortType = getDocs(ProductSortType.values());
        Map<String, String> orderExchangeState = getDocs(OrderExchangeState.values());
        Map<String, String> accountStatus = getDocs(AccountStatus.values());
        Map<String, String> policyType = getDocs(PolicyType.values());
        Map<String, String> eventStatus = getDocs(EventStatus.values());
        Map<String, String> postStatus = getDocs(PostStatus.values());
        Map<String, String> reviewOrderType = getDocs(ReviewOrderType.values());
        Map<String, String> pointKind = getDocs(PointKind.values());
        Map<String, String> couponStatus = getDocs(CouponStatus.values());
        Map<String, String> productProvision = getDocs(ProductProvisionType.values());
        Map<String, String> socialType = getDocs(SocialType.values());
        Map<String, String> gender = getDocs(Gender.values());
        Map<String, String> mainBanner = getDocs(MainBannerType.values());
        Map<String, String> bank = getDocs(Bank.values());

        return ResponseDto.ok(
                Docs.builder()
                        .discountType(discountType)
                        .headquarters(headquarters)
                        .commonStatus(commonStatus)
                        .ynType(ynType)
                        .exceptionStatus(exceptionStatus)
                        .uploadPath(uploadPath)
                        .conditionType(conditionType)
                        .accountRole(accountRole)
                        .companyRank(companyRank)
                        .memberDivision(memberDivision)
                        .orderState(orderState)
                        .paymentMethod(paymentMethod)
                        .carrier(carrier)
                        .pointState(pointState)
                        .deliveryType(deliveryType)
                        .productSortType(productSortType)
                        .orderExchangeState(orderExchangeState)
                        .accountStatus(accountStatus)
                        .policyType(policyType)
                        .eventStatus(eventStatus)
                        .postStatus(postStatus)
                        .reviewOrderType(reviewOrderType)
                        .pointKind(pointKind)
                        .couponStatus(couponStatus)
                        .productProvision(productProvision)
                        .gender(gender)
                        .mainBanner(mainBanner)
                        .socialType(socialType)
                        .bank(bank)
                        .build()
        );
    }

    private Map<String, String> getDocs(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
                .collect(Collectors.toMap(EnumType::name, EnumType::getDescription));
    }
}