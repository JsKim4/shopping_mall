package me.kjs.mall.order.specific.destination.dto;

import lombok.Builder;
import lombok.Getter;
import me.kjs.mall.order.specific.destination.OrderDestination;

@Getter
@Builder
public class OrderDestinationDto {
    private String recipient;
    private String addressSimple;
    private String addressDetail;
    private String tel1;
    private String tel2;
    private String zipcode;
    private String message;

    public static OrderDestinationDto orderDestinationToDto(OrderDestination orderDestination) {
        return OrderDestinationDto.builder()
                .recipient(orderDestination.getRecipient())
                .addressSimple(orderDestination.getAddress())
                .addressDetail(orderDestination.getAddressDetail())
                .tel1(orderDestination.getTel1())
                .tel2(orderDestination.getTel2())
                .zipcode(orderDestination.getZipcode())
                .message(orderDestination.getMessage())
                .build();
    }
}
