package me.kjs.mall.payment.nicepay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentApproveResponseDto {
    @JsonProperty("ResultCode")
    private String resultCode;
    @JsonProperty("ResultMsg")
    private String resultMsg;
    @JsonProperty("Amt")
    private String amt;
    @JsonProperty("MID")
    private String mID;
    @JsonProperty("Moid")
    private String moid;
    @JsonProperty("Signature")
    private String signature;
    @JsonProperty("TID")
    private String tid;
    @JsonProperty("AuthCode")
    private String authCode;
    @JsonProperty("AuthDate")
    private String authDate;
    @JsonProperty("PayMethod")
    private String payMethod;
    @JsonProperty("GoodsName")
    private String goodsName;
    @JsonProperty("VbankBankCode")
    private String bankCode;
    @JsonProperty("VbankBankName")
    private String bankName;
    @JsonProperty("VbankNum")
    private String bankNum;
    @JsonProperty("VbankExpDate")
    private String expDate;
    @JsonProperty("VbankExpTime")
    private String expTime;
}
