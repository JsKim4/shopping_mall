package me.kjs.mall.order.dto.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.interfaces.OrderCreatorFirst;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto implements OrderCreatorFirst {
    @NotNull
    @NotEmpty
    @Valid
    private List<@NotNull OrderDestinationSaveDto> destination;
    @NotNull
    @NotEmpty
    @Valid
    private List<@NotNull ProductIdAndQuantityDto> products;
    @NotNull
    private YnType orderMultipleYn = YnType.N;
    @NotNull
    private PaymentMethod paymentMethod;
    private int point;

    @Override
    @JsonIgnore
    public List<Long> getProductIds() {
        return products.stream().map(ProductIdAndQuantityDto::getProductId).collect(Collectors.toList());
    }
}