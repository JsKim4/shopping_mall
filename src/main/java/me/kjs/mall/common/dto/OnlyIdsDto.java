package me.kjs.mall.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlyIdsDto {
    @NotNull
    @Builder.Default
    private List<Long> ids = new ArrayList<>();
}
