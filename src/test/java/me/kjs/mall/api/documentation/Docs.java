package me.kjs.mall.api.documentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class Docs {
    Map<String, String> discountTypes;
    Map<String, String> headquarters;
    Map<String, String> commonStatus;
    Map<String, String> ynType;
    Map<String, String> exceptionStatus;
    Map<String, String> uploadPath;
    Map<String, String> conditionType;
    Map<String, String> accountRole;
    Map<String, String> companyRank;
    Map<String, String> memberDivision;
    Map<String, String> orderState;
    Map<String, String> paymentMethod;
    Map<String, String> carrier;
    Map<String, String> pointState;
    Map<String, String> discountType;
    Map<String, String> deliveryType;
    Map<String, String> productSortType;
    Map<String, String> orderExchangeState;
    Map<String, String> accountStatus;
    Map<String, String> policyType;
    Map<String, String> eventStatus;
    Map<String, String> postStatus;
    Map<String, String> reviewOrderType;
    Map<String, String> pointKind;
    Map<String, String> couponStatus;
    Map<String, String> productProvision;
    Map<String, String> socialType;
    Map<String, String> gender;
    Map<String, String> mainBanner;
    Map<String, String> bank;


}
