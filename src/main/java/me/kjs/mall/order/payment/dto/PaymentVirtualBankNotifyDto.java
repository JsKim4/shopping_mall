package me.kjs.mall.order.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVirtualBankNotifyDto {
    private String Amt;
    private String AuthCode;
    private String AuthDate;
    private String BuyerAuthNum;
    private String BuyerEmail;
    private String FnCd;
    private String FnName;
    private String GoodsName;
    private String MallUserID;
    private String MerchantKey;
    private String MID;
    private String MOID;
    private String Name;
    private String PayMethod;
    private String RcptAuthCode;
    private String RcptTID;
    private String RcptType;
    private String ReceitType;
    private String ResultCode;
    private String ResultMsg;
    private String StateCd;
    private String TID;
    private String VbankInputName;
    private String VbankName;
    private String VbankNum;
}
