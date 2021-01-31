package me.kjs.mall.partial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.partial.NewProductCalendar;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductCalendarDto {
    private Integer calendarId;
    private List<NewProductDto> newProducts;

    public static NewProductCalendarDto newProductCalendarToDto(NewProductCalendar newProductCalendar) {
        return NewProductCalendarDto.builder()
                .calendarId(newProductCalendar.getId())
                .newProducts(newProductCalendar.getNewProducts().stream().map(NewProductDto::newProductToDto).collect(Collectors.toList()))
                .build();
    }
}
