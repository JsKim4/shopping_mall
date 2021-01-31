package me.kjs.mall.common.exception;

import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@RequiredArgsConstructor

public enum ExceptionStatus implements EnumType {
    ILLEGAL_PASSWORD_LOGIN_FAILED(4001, "비밀번호 혹은 이메일을 확인해주세요.", "비밀번호가 맞지 않을 경우"),
    MEMBER_LOGIN_REJECT_FAIL(4002, "회원 승인이 거절되었습니다.", "회원 승인이 거절된 경우"),
    MEMBER_LOGIN_REQUEST_FAIL(4003, "회원 승인 요청 처리중 입니다.", "회원 승인이 요청중인 경우"),
    MEMBER_LOGIN_WITHDRAW_FAIL(4004, "탈퇴한 회원입니다.", "탈퇴한 회원일 경우"),
    NO_EXIST_EMAIL(4005, "비밀번호 혹은 이메일을 확인해주세요.", "입력한 이메일이 존재하지 않을 경우"),
    NOT_FOUND_MEMBER_BY_REFRESH_TOKEN(4006, "요청한 리프레시 토큰으로 사용자를 찾을 수 없습니다.", "입력한 리프레시 토큰에 해당하는 회원이 존재하지 않는경우"),
    REFRESH_TOKEN_EXPIRED(4007, "만료된 리프레시 토큰입니다.", "입력한 리프레시 토큰이 만료된 경우"),
    ALREADY_EXIST_EMAIL(4008, "이미 등록된 이메일 입니다.", "이미 가입된 이메일로 회원가입"),
    ALREADY_EXIST_EMPLOYEE_NO(4009, "이미 등록된 사번 입니다.", "이미 가입된 사번으로 회원가입"),
    ALREADY_EXIST_ALIAS(4010, "이미 등록된 ALIAS 입니다.", "중복되는 ALIAS가 존재할 경우"),
    NOT_AVAILABLE_ACCOUNT_GROUP_UPDATE(4011, "기본 그룹 혹은 최고 관리자 그룹은 수정,삭제할 수 없습니다.", "superUser 혹은 default 그룹을 수정 시도할 경우"),
    NOT_AVAILABLE_CERT_GENERATOR(4012, "입력 정보에 해당하는 회원을 찾을 수 없습니다.", "인증받기 위한 정보가 올바르지 않을경우"),
    NOT_AVAILABLE_CERTIFICATION(4013, "인증에 실패하였습니다. 재 인증 후 시도해 주세요.", "입력한 토큰으로 인증정보를 찾을 수 없는 경우"),
    NOT_AVAILABLE_BAN(4014, "이용제한이 가능한 상태가 아닙니다.", "회원 상태가 ALLOW 상태가 아닐 때 이용제한을 요청할 경우"),
    NOT_AVAILABLE_FREE(4015, "이용제한 해제가 가능한 상태가 아닙니다.", "회원 상태가 BAN 상태가 아닐 때 이용제한 해제를 요청할 경우"),
    ALREADY_EXIST_PRODUCT_CODE(4016, "이미 존재하는 상품 코드입니다.", "사용하려는 상품코드가 이미 존재할 경우"),
    PRODUCT_STOCK_NOT_ENOUGH(4017, "상품 재고 수량이 충분하지 않습니다.", "사용하려는 상품 재고 수량이 충분하지 않을 경우"),
    NOT_USED_BASE_PRODUCT(4018, "사용중인 기본 상품정보가 아닙니다.", "기본 상품 상태가 USED가 아닌 상품을 판매상품에 등록할 경우"),
    NOT_AVAILABLE_CATEGORY_PARENT(4019, "카테고리의 부모는 자신이 될 수 없습니다. 카테고리의 부모가 서로 크로싱 될수 없습니다.", "카테고리의 부모로 자기자신을 지목하거나 자신의 자식을 부모로 지정할 경우"),
    NOT_NORMAL_PRODUCT_REGISTER_WISH_LIST(4020, "일반 상품만 찜 목록에 포함시킬 수 있습니다.", "일반상품이 아닌 상품을 찜 목록에 추가시킬 경우"),
    DESTINATION_LIMIT_OVER(4021, "저장 가능한 배송지 최대 개수를 초과 하였습니다.", "등록 가능한 배송지 개수를 초과하였을 경우"),
    NOT_AVAILABLE_EXCHANGE_REQUEST(4022, "교환 요청이 불가능한 상태이거나 환불하려는 상품 개수가 남은 주문 상품 개수보다 많습니다.", "교환 요청이 불가능한 상태값인 경우"),
    PARENT_CATEGORY_NOT_TOP_NODE(4023, "최상위 노드만 자식 카테고리를 가질 수 있습니다.", ""),
    NOT_EQUALS_PRODUCT_TYPE(4024, "지정한 주문 상품 타입과 다른 상품이 포함되어 있습니다. 다시 확인해 주세요.", "지정한 주문 타입과 다른상품이 포함되어 있는 경우"),
    NOT_AVAILABLE_PRODUCT_CONTAINS(4025, "유효하지 않은 상품이 포함되어 있습니다.", "사용되고 있지않은 상품을 사용할 경우"),
    NOT_AVAILABLE_CANCEL_ORDER_STATE(4026, "결제 취소가 불가능한 상태입니다. 결제 취소는 결제 완료 상태에서만 가능합니다.", "주문 취소가 불가능한 상태인 경우"),
    PAYMENT_CANCEL_FAIL(4027, "4027 (PG사 실패메시지) / 그대로 띄워주시면 됩니다.", "주문 취소가 실패한 경우"),
    NOT_AVAILABLE_UPDATE_ORDER_SPECIFIC_DESTINATION(4028, "배송지를 변경할 수 없는 상태입니다. 배송지 변경은 결제완료 상태에서만 가능합니다.", "주문 배송지 변경이 불가능한 상태인 경우"),
    NOT_AVAILABLE_ACCEPT_ORDER_STATE(4029, "구매 확정으로 변경할 수 없는 상태입니다. 구매 확정은 배송완료 상태에서만 가능합니다.", "구매 완료 변경이 불가능한 상태인 경우"),
    NOT_AVAILABLE_DELIVERY_DOING(4030, "상품 배송중 상태로 변경할 수 없는 상태입니다. 상품 배송 상태로 변경은 결제 완료 상태에서만 가능합니다.", "SYS ERROR"),
    NOT_AVAILABLE_DELIVERY_ACCEPT(4031, "상품 배송완료 상태로 변경할 수 없는 상태입니다. 상품 배송 완료 상태로 변경은 결제 완료 및 배송중 상태에서만 가능합니다.", "SYS ERROR"),
    FILE_UPLOAD_FAIL(4032, "파일 업로드에 실패했습니다. 이미지만 업로드 가능합니다.", "업로드한 파일의 확장자가 허용되지 않은 경우"),
    FILE_CONVERT_FAIL(4033, "업로드한 파일의 변환 과정중 에러가 발생한 경우 (파일 크기 초과등)", "파일 업로드에 실패했습니다. 이미지만 업로드 가능합니다."),
    FILE_UPLOAD_SERVER(4034, "파일 서버오류 발생으로 업로드에 실패하였습니다.", "파일서버와의 통신에서 에러가 발생한 경우"),
    NOT_AVAILABLE_EXCHANGE_CANCEL(4035, "교환 요청 취소가 불가능한 상태입니다.", "교환 접수 승인 이후의 단계를 밟고 있는 경우 / 교환 요청 이전의 상태인경우"),
    NOT_AVAILABLE_ORDER_ACCOUNT_STATUS(4036, "상품 주문이 불가능한 회원입니다.", "상품 주문은 계정상태가 ALLOW에서만 가능함"),
    NOT_AVAILABLE_ORDER_PURCHASE_LIMIT(4037, "구매 한도금액을 초과하였습니다.\n당월 구매 한도 : ${1} \n당월 구매 금액 : ${2} \n현재 결제 요청 금액 : ${3} ", "구매 한도 금액을 초과한 경우"),
    Not_AVAILABLE_COUPON_UPDATE(4038, "쿠폰 업데이트가 불가능한 상태입니다. 쿠폰 업데이트는 생성 상태에서만 가능합니다.", "쿠폰 상태가 생성이 아닐때 변경 시도한 경우"),
    NOT_AVAILABLE_ORDER_PRODUCT_REVIEW(4039, "리뷰 등록이 불가능한 상태입니다. 리뷰 등록은 구매완료 상태에서만 가능합니다.", "구매완료가 아닌 상태에서 리뷰를 등록 시도할 경우"),
    NOT_AVAILABLE_MODIFY_COUPON_USE_STATUS(4040, "쿠폰을 사용상태로 변경할 수 없습니다. 조건이 하나이상 지정되어야 합니다.", "쿠폰 발급 기준이 지정되지 않았을경우"),
    ALREADY_EXIST_BEST_REVIEW_PRODUCT(4041, "이미 베스트 리뷰 상품으로 등록된 상태입니다.", "이미 베스트 리뷰에 등록된 상품일 경우"),
    NOT_REGISTER_BEST_REVIEW_PRODUCT(4042, "베스트 리뷰 상품으로 등록되지 않은 상품입니다.", "베스트 리뷰에 등록되지 않은 상품의 리뷰를 베스트리뷰로 등록할 경우"),
    BEST_REVIEW_SIZE_OVER(4043, "베스트 리뷰 등록개수가 초과하였습니다.", "베스트 리뷰 등록개수가 10개 초과일 경우"),
    ALREADY_EXIST_PRODUCT_BY_BASE_PRODUCT(4044, "이미 해당 기본 상품으로 판매중인 제품이 있습니다.", "기본상품으로 판매하고있는 상품이 이미 존재하는 경우"),
    DISCOUNT_AMOUNT_BIGGER_THEN_ORIGIN_PRICE(4045, "할인 가격은 상품 기본가격을 초과할 수 없습니다.", "상품 가격보다 할인가격이 큰 경우"),
    NOT_AVAILABLE_PLACE_ORDER_PRODUCT(4046, "주문 상태를 배송 대기중 상태로 변경할 수 없습니다. \ncause order product code : {orderProductCode} \nreason : {reason}", "발주로 상태 변경을 실패하는 경우"),
    NOT_ENOUGH_POINT(4047, "결제에 사용하려는 포인트가 부족합니다.", "포인트가 부족한 경우"),
    NOT_AVAILABLE_PAYMENT(4048, "결제하는 금액이 최소 금액 이하입니다.", "결제 금액이 부족한 경우"),
    NOT_AVAILABLE_VIRTUAL_BANK_PAYMENT_ACCEPT(4049, "가상 계좌 결제 완료가 불가능합니다.", "가상 계좌 결제 완료가 불가능한 상태일 경우"),
    ALREADY_EXIST_PHONE_NUMBER(4050, "이미 가입된 핸드폰 번호입니다.", "이미 가입된 핸드폰 번호로 가입시도할 경우"),
    ALREADY_EXIST_SOCIAL(4051, "이미 가입된 소셜 회원입니다.", "이미 가입된 소셜회원으로 가입시도할 경우"),
    NO_EXIST_PHONE_NUMBER(4052, "핸드폰 번호로 가입된 회원이 존재하지 않습니다.", "존재하지 않는 핸드폰 번호로 회원을 조회할 경우"),
    NO_EXIST_MEMBER(4053, "입력한 정보로 회원을 찾을 수 없습니다.", "존재하지 않는 조건으로 회원을 조회할 경우"),
    NOT_NON_MEMBER_ORDER(4054, "비회원 주문이 아닙니다.", "비회원 주문이 아닌 주문을 비회원 주문으로 처리할 경우"),
    KAKAO_CONNECTION(4101, "카카오 연동중 오류가 발생 하였습니다.", "카카오 연동중 오류가 발생할 경우"),
    KAKAO_NOT_REGISTER_USER(4201, "카카오 연동되지 않은 회원입니다.", "카카오로 연동되지 않은 회원인 경우"),
    RUN_TIME_EXCEPTION(500, "상상도 못한 정체", "상상도 못한 정체"),
    BAD_REQUEST(400, "잘못된 입력", "권한 에러"),
    GRANT_EXCEPTION(403, "권한 에러", "권한 에러"),
    NOT_FOUND_EXCEPTION(404, "요청한 리소스가 존재하지 않습니다.", "요청한 리소스가 존재하지 않을 경우"),
    SUCCESS(200, "성공", "요청이 성공적으로 수행되었습니다.");


    private final int status;
    private final String message;
    private final String description;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
