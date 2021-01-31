package me.kjs.mall.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlaceOrderDownloadForErpResultDto {
    private List<PlaceOrderDownloadForErpItemSuccessDto> successes;
    private List<PlaceOrderDownloadForErpItemFailDto> failures;

    public static PlaceOrderDownloadForErpResultDto createPlaceOrderDownloadForErpResultDto(List<PlaceOrderDownloadForErpItemSuccessDto> successes, List<PlaceOrderDownloadForErpItemFailDto> failures) {
        return PlaceOrderDownloadForErpResultDto.builder()
                .successes(successes)
                .failures(failures)
                .build();
    }
}
