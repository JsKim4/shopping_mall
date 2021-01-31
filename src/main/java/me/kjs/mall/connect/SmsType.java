package me.kjs.mall.connect;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.kjs.mall.common.type.EnumType;

@Getter
@RequiredArgsConstructor
public enum SmsType implements EnumType {


    PHONE_CERT("핸드폰 6자리 인증", "[그린스토어 인증 번호]\n{0} 을 입력해 주세요.", 1, "그린스토어 패밀리 인증번호", 0),
    PAYMENT_ACCEPT("결제 완료 발송",
            "[그린스토어 공식몰]\n" +
                    "안녕하세요. {0}님\n" +
                    "결제가 정상적으로 완료되었습니다.\n" +
                    "\n" +
                    "▷ 주문상품: {1}\n" +
                    "▷ 주문번호: {2}\n" +
                    "▷ 결제금액: {3}원\n" +
                    "▶ 배송안내\n" +
                    "\n" +
                    "배송에는 1-3일(영업일 기준) 소요됩니다만, 택배물량이 많거나 지역택배사 사정에 따라 배송기간에는 차이가 있을 수 있습니다.\n" +
                    "\n" +
                    "이용해주셔서 감사합니다.\n" +
                    "\n" +
                    "☎ 문의: 1544-2492(그린스토어 대표전화)" +
                    "", 4, "그린스토어 결제 완료", 0),
    DEPOSIT_INFO("입금 안내",
            "[그린스토어 입금안내]\n" +
                    "\n" +
                    "안녕하세요, {0}님\n" +
                    "주문이 정상적으로 완료되었습니다. 입금안내 도와드리겠습니다.\n" +
                    "\n" +
                    "▶은행: {1}\n" +
                    "▷입금계좌: {2}\n" +
                    "▶금액: {3}원\n" +
                    "▷입금기한: {4}\n" +
                    "▶제품명: {5}\n" +
                    "▷주문번호 : {6}\n" +
                    "\n" +
                    "※입금 기한내에 미입금시 주문이 자동취소 됩니다.\n" +
                    "※오후 3시이전 입금시 당일 출고됩니다.\n" +
                    "\n" +
                    "☎ 문의: 1544-2492(그린스토어 대표전화)", 6, "그린스토어 입금 안내", 0);

    private final String description;
    private final String text;
    private final int textParameters;

    private final String title;
    private final int titleParameters;


    public String getSmsText(String... parameters) {
        String tempText = text;
        for (int i = 0; i < textParameters; i++) {
            tempText = tempText.replace("{" + i + "}", parameters[i]);
        }
        return tempText;
    }

    public String getSmsTitle(String... parameters) {
        String tempText = title;
        for (int i = 0; i < titleParameters; i++) {
            tempText = tempText.replace("{" + i + "}", parameters[i]);
        }
        return tempText;
    }


}
