package me.kjs.mall.order.cancel.dto;

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
public class PaymentCancelResponseDto {
    @JsonProperty("ResultCode")
    private String resultCode;
    @JsonProperty("ResultMsg")
    private String resultMsg;
    @JsonProperty("ErrorCD")
    private String errorCd;
    @JsonProperty("ErrorMsg")
    private String errorMsg;
    @JsonProperty("CancelAmt")
    private String cancelAmt;
    @JsonProperty("MID")
    private String mid;
    @JsonProperty("Moid")
    private String moid;
    @JsonProperty("Signature")
    private String signature;
    @JsonProperty("TID")
    private String tID;
    @JsonProperty("PayMethod")
    private String payMethod;
    @JsonProperty("CancelDate")
    private String cancelDate;
    @JsonProperty("CancelTime")
    private String cancelTime;
    @JsonProperty("CancelNum")
    private String cancelNum;
    @JsonProperty("remainAmt")
    private String remainAmt;
}

































































