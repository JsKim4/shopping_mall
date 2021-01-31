package me.kjs.mall.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSaveDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String thumbnailImage;
    @NotEmpty
    private List<@NotNull String> contents;
    @NotNull
    private LocalDate beginDate;
    @NotNull
    private LocalDate endDate;

}
