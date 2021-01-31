package me.kjs.mall.payment.nicepay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCertificationResponseDto {
    private String authResultCode;
    private String authResultMsg;
    private String payMethod;
    private String authToken;
    private String mID;
    private String moid;
    private String amt;
    private String signature;
    private String txTid;
    private String nextAppURL;
    private String netCancelURL;
}
