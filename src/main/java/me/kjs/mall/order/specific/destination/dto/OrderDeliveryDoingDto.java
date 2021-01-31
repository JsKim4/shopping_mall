package me.kjs.mall.order.specific.destination.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.order.specific.destination.Carrier;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryDoingDto {
    private Carrier carrier = Carrier.CJ;
    private String invoiceNumber;
}
