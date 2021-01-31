package me.kjs.mall.configs.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class PaymentProperties {
    private static String returnUrl;
    private static String merchantId;
    private static String merchantKey;
    private static String rootApiUrl;
    private static String certificationRequestUrl;
    private static String certificationRequestWebviewUrl;

    private static String redirectResultPage;
    private static String cancelApiUrl;

    public static String getRedirectResultPageForSuccess() {
        return getRedirectResultPage();
    }

    @Value("${kjs-mall.payment.nicePay.return-url}")
    protected void setReturnUrl(String returnUrl) {
        PaymentProperties.returnUrl = returnUrl;
    }


    @Value("${kjs-mall.payment.nicePay.merchant-id}")
    protected void setMerchantId(String merchantId) {
        PaymentProperties.merchantId = merchantId;
    }

    @Value("${kjs-mall.payment.nicePay.root-api-url}")
    protected void setRootApiUrl(String rootApiUrl) {
        PaymentProperties.rootApiUrl = rootApiUrl;
    }

    @Value("${kjs-mall.payment.nicePay.certification-request-url}")
    protected void setCertificationRequestUrl(String certificationRequestUrl) {
        PaymentProperties.certificationRequestUrl = certificationRequestUrl;
    }

    @Value("${kjs-mall.payment.nicePay.merchant-key}")
    protected void setMerchantKey(String merchantKey) {
        PaymentProperties.merchantKey = merchantKey;
    }

    @Value("${kjs-mall.payment.nicePay.certification-request-webview-url}")
    protected void setCertificationRequestWebviewUrl(String certificationRequestWebviewUrl) {
        PaymentProperties.certificationRequestWebviewUrl = certificationRequestWebviewUrl;
    }

    @Value("${kjs-mall.payment.nicePay.redirect-result-page}")
    private void setRedirectResultPage(String redirectResultPage) {
        PaymentProperties.redirectResultPage = redirectResultPage;
    }

    @Value("${kjs-mall.payment.nicePay.cancel-api-url}")
    private void setCancelApiUrl(String cancelApiUrl) {
        PaymentProperties.cancelApiUrl = cancelApiUrl;
    }

    public static String getMerchantKey() {
        return merchantKey;
    }

    public static String getReturnUrl() {
        return returnUrl;
    }


    public static String getMerchantId() {
        return merchantId;
    }

    public static String getRootApiUrl() {
        return rootApiUrl;
    }

    public static String getRedirectResultPage() {
        return redirectResultPage;
    }

    public static String getCertificationRequestUrl() {
        return certificationRequestUrl;
    }

    public static String getCertificationRequestWebviewUrl() {
        return certificationRequestWebviewUrl;
    }

    public static String getCancelApiUrl() {
        return cancelApiUrl;
    }
}
