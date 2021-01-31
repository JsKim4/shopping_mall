package me.kjs.mall.connect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.util.DateTimeUtil;
import me.kjs.mall.configs.properties.FileServerProperties;
import me.kjs.mall.connect.exception.FileConvertFailException;
import me.kjs.mall.connect.exception.FileUploadFailException;
import me.kjs.mall.order.cancel.dto.PaymentCancelResponseDto;
import me.kjs.mall.order.payment.dto.OrderPaymentVirtualBankResultDto;
import me.kjs.mall.payment.nicepay.PaymentApproveRequestDto;
import me.kjs.mall.payment.nicepay.PaymentApproveResponseDto;
import me.kjs.mall.payment.nicepay.PaymentCancelRequestDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectService {

    private final RestTemplate restTemplate;

    private final FileServerProperties fileServerProperties;
    private final ObjectMapper objectMapper;
    private final SmsService smsService;

    public PaymentApproveResponseDto paymentApproveRequest(PaymentApproveRequestDto paymentApproveRequestDto) throws JsonProcessingException {

        log.info("paymentApproveRequestDto = " + paymentApproveRequestDto);
        Map parameters = createPaymentApproveRequestHashMap(paymentApproveRequestDto);
        HttpEntity formEntity = headerAndParameterSetting(parameters);

        ResponseEntity<String> responseEntity = restTemplate
                .postForEntity(paymentApproveRequestDto.getNextApproveUrl(), formEntity, String.class);

        PaymentApproveResponseDto paymentApproveResponseDto = objectMapper.readValue(responseEntity.getBody(), PaymentApproveResponseDto.class);
        log.info("===========================================================");
        log.info("paymentApproveResponseDto = " + paymentApproveResponseDto);
        log.info("===========================================================");
        return paymentApproveResponseDto;
    }

    public PaymentCancelResponseDto paymentCancelRequest(PaymentCancelRequestDto paymentCancelRequestDto) throws JsonProcessingException {
        Map parameters = createPaymentCancelRequestHashMap(paymentCancelRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "x-www-form-urlencoded", Charset.forName("EUC-KR")));
        HttpEntity<Map> formEntity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> responseEntity = restTemplate
                .postForEntity(paymentCancelRequestDto.getCancelUrl(), formEntity, String.class);

        PaymentCancelResponseDto paymentCancelResponseDto = objectMapper.readValue(responseEntity.getBody(), PaymentCancelResponseDto.class);
        log.info("===========================================================");
        log.info("paymentCancelResponseDto = " + paymentCancelResponseDto);
        log.info("===========================================================");
        return paymentCancelResponseDto;
    }

    private HttpEntity headerAndParameterSetting(Map parameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(parameters, headers);
    }

    private Map createPaymentCancelRequestHashMap(PaymentCancelRequestDto paymentCancelRequestDto) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("TID", paymentCancelRequestDto.getTID());
        parameters.add("MID", paymentCancelRequestDto.getMID());
        parameters.add("Moid", paymentCancelRequestDto.getMoid());
        parameters.add("CancelAmt", paymentCancelRequestDto.getCancelAmt());
        parameters.add("CancelMsg", paymentCancelRequestDto.getCancelMsg());
        parameters.add("PartialCancelCode", paymentCancelRequestDto.getPartialCancelCode());
        parameters.add("EdiDate", paymentCancelRequestDto.getEdiDate());
        parameters.add("SignData", paymentCancelRequestDto.getSignData());
        parameters.add("CharSet", paymentCancelRequestDto.getCharSet());
        parameters.add("RefundAcctNo", paymentCancelRequestDto.getRefundAcctNo());
        parameters.add("RefundBankCd", paymentCancelRequestDto.getRefundBankCd());
        parameters.add("RefundAcctNm", paymentCancelRequestDto.getRefundAcctNm());
       /*try {
            parameters.add("RefundAcctNm", URLEncoder.encode(paymentCancelRequestDto.getRefundAcctNm(),"EUC-KR"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        return parameters;
    }

    private Map createPaymentApproveRequestHashMap(PaymentApproveRequestDto paymentApproveRequestDto) {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

        parameters.add("TID", paymentApproveRequestDto.getTID());
        parameters.add("AuthToken", paymentApproveRequestDto.getAuthToken());
        parameters.add("MID", paymentApproveRequestDto.getMid());
        parameters.add("Amt", paymentApproveRequestDto.getAmt());
        parameters.add("SignData", paymentApproveRequestDto.getSignData());
        parameters.add("CharSet", paymentApproveRequestDto.getCharSet());
        parameters.add("EdiDate", paymentApproveRequestDto.getEdiDate());
        return parameters;
    }

    public String uploadImage(MultipartFile file, UploadPath uploadPath) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        ByteArrayResource requestEntity;
        try {
            requestEntity = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (IOException e) {
            throw new FileConvertFailException();
        }
        parts.add("file", requestEntity);
        String fileUploadPath = fileServerProperties.getUploadPath(uploadPath);
        FileUploadResponse result;
        try {
            String body = restTemplate.postForObject(fileUploadPath, parts, String.class);
            result = Optional.of(objectMapper.readValue(body, FileUploadResponse.class)).orElseThrow(FileUploadFailException::new);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploadServerException();
        }
        if (!result.isSuccess()) {
            throw new FileUploadFailException();
        }
        return result.getPath();
    }

    private SmsResultDto sendSms(String target, String title, String message) {
        return smsService.send(target, title, message, 0, LocalDateTime.now());
    }

    public SmsResultDto sendPhoneCertSms(String phoneNumber, String token) {
        SmsType phoneCert = SmsType.PHONE_CERT;
        String message = phoneCert.getSmsText(token);
        String title = phoneCert.getSmsTitle();
        return sendSms(phoneNumber, title, message);
    }

    public SmsResultDto sendPaymentAcceptSms(String phoneNumber, String orderName, String orderCode, String orderMemberName, int paymentPrice) {
        SmsType paymentAccept = SmsType.PAYMENT_ACCEPT;
        String message = paymentAccept.getSmsText(orderMemberName, orderName, orderCode, String.valueOf(paymentPrice));
        String title = paymentAccept.getSmsTitle();
        return sendSms(phoneNumber, title, message);
    }

    public SmsResultDto sendPaymentBankInfoSms(String phoneNumber, String orderMemberName, OrderPaymentVirtualBankResultDto paymentVirtualBank, int paymentPrice, String orderName, String orderCode) {
        SmsType depositInfo = SmsType.DEPOSIT_INFO;
        String message = depositInfo.getSmsText(orderMemberName, paymentVirtualBank.getBankName(), paymentVirtualBank.getBankCode(), String.valueOf(paymentPrice),
                DateTimeUtil.formatYYMMDDHHMMSSKR(paymentVirtualBank.getBankExpiredDateTime()), orderName, orderCode);
        String title = depositInfo.getSmsTitle();
        return sendSms(phoneNumber, title, message);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileUploadResponse {
        private Integer status;
        private String message;
        private List<String> data;

        public boolean isSuccess() {
            return status != null && status == 200 && data.size() > 0;
        }

        public String getPath() {
            return data.get(0);
        }
    }
}
