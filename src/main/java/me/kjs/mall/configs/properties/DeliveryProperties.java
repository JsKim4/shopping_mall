package me.kjs.mall.configs.properties;

import me.kjs.mall.order.specific.destination.Carrier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class DeliveryProperties {

    private static String rootApiUrl;


    @Value("${kjs-mall.delivery.root-api-url}")
    protected void setRootApiUrl(String rootApiUrl) {
        DeliveryProperties.rootApiUrl = rootApiUrl;
    }


    public static String getDeliveryApi(Carrier carrier, String invoiceNumber) {
        return rootApiUrl.replace("{carrier}", carrier.getCode()).replace("{invoiceNumber}", invoiceNumber);
    }

}
