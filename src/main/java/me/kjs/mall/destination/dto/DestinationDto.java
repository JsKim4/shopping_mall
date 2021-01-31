package me.kjs.mall.destination.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.destination.Destination;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDto {
    private Long destinationId;
    private String destinationName;
    private String recipient;
    private String tel1;
    private String tel2;
    private String addressSimple;
    private String addressDetail;
    private String zipcode;

    public static DestinationDto destinationToDto(Destination destination) {
        return DestinationDto.builder()
                .destinationId(destination.getId())
                .destinationName(destination.getName())
                .recipient(destination.getRecipient())
                .tel1(destination.getTel1())
                .tel2(destination.getTel2())
                .addressSimple(destination.getAddressSimple())
                .addressDetail(destination.getAddressDetail())
                .zipcode(destination.getZipcode())
                .build();
    }
}
