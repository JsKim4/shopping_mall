package me.kjs.mall.order.specific.destination;


import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.configs.properties.DeliveryProperties;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.dto.OrderDeliveryDoingDto;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;

import javax.persistence.*;
import java.time.LocalDateTime;

import static me.kjs.mall.common.util.CollectionTextUtil.isBlank;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderDestination extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_destination_id")
    private Long id;
    private String recipient;
    private String address;
    private String addressDetail;
    private String tel1;
    private String tel2;
    private String zipcode;
    private String message;
    @OneToOne(mappedBy = "orderDestination")
    private OrderSpecific orderSpecific;

    @Enumerated(EnumType.STRING)
    private Carrier carrier;

    private String invoiceNumber;

    private LocalDateTime doingDateTime;

    private LocalDateTime acceptDateTime;

    public static OrderDestination createOrderDestination(OrderDestinationSaveDto destinationDto) {
        return OrderDestination.builder()
                .recipient(destinationDto.getRecipient())
                .address(destinationDto.getAddressSimple())
                .addressDetail(destinationDto.getAddressDetail())
                .tel1(destinationDto.getTel1())
                .tel2(destinationDto.getTel2())
                .zipcode(destinationDto.getZipcode())
                .message(destinationDto.getMessage())
                .build();
    }

    public OrderDestination loading() {
        getMessage();
        return this;
    }

    public String getDestinationAddress() {
        return address + " " + addressDetail;
    }

    public void update(OrderDestinationSaveDto orderDestinationSaveDto) {
        if (CollectionTextUtil.isNotBlank(orderDestinationSaveDto.getRecipient())) {
            recipient = orderDestinationSaveDto.getRecipient();
        }
        if (CollectionTextUtil.isNotBlank(orderDestinationSaveDto.getTel1())) {
            tel1 = orderDestinationSaveDto.getTel1();
        }
        tel2 = orderDestinationSaveDto.getTel2();
        tel2 = isBlank(tel2) ? null : tel2;
        if (CollectionTextUtil.isNotBlank(orderDestinationSaveDto.getAddressSimple())) {
            address = orderDestinationSaveDto.getAddressSimple();
        }
        if (CollectionTextUtil.isNotBlank(orderDestinationSaveDto.getAddressDetail())) {
            addressDetail = orderDestinationSaveDto.getAddressDetail();
        }
        if (CollectionTextUtil.isNotBlank(orderDestinationSaveDto.getZipcode())) {
            zipcode = orderDestinationSaveDto.getZipcode();
        }
        if (CollectionTextUtil.isNotBlank(orderDestinationSaveDto.getMessage())) {
            message = orderDestinationSaveDto.getMessage();
        }
    }

    public void deliveryDoingOrder(OrderDeliveryDoingDto orderDeliveryDoingDto) {
        invoiceNumber = orderDeliveryDoingDto.getInvoiceNumber();
        carrier = orderDeliveryDoingDto.getCarrier();
        doingDateTime = LocalDateTime.now();
    }

    public void deliveryAcceptOrder() {
        acceptDateTime = LocalDateTime.now();
    }

    public String getDeliveryApiUrl() {
        if (carrier == null || isBlank(invoiceNumber)) {
            return null;
        }
        return DeliveryProperties.getDeliveryApi(carrier, invoiceNumber);
    }
}
