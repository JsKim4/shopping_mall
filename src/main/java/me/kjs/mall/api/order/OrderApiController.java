package me.kjs.mall.api.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonSlice;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.Member;
import me.kjs.mall.order.Order;
import me.kjs.mall.order.OrderExchangeService;
import me.kjs.mall.order.OrderService;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.dto.*;
import me.kjs.mall.order.dto.create.OrderCreateDto;
import me.kjs.mall.order.sheet.OrderSheetCreateDto;
import me.kjs.mall.order.sheet.OrderSheetDto;
import me.kjs.mall.order.sheet.OrderSheetService;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.order.specific.dto.OrderSpecificDetailDto;
import me.kjs.mall.order.specific.exchange.dto.OrderExchangeDto;
import me.kjs.mall.order.specific.product.dto.OrderExchangeRequestDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderApiController {

    private final OrderSheetService orderSheetService;
    private final OrderService orderService;
    private final OrderExchangeService orderExchangeService;

    @PostMapping("/sheet")
    public ResponseDto createOrderSheet(@RequestBody @Validated OrderSheetCreateDto orderSheetCreateDto,
                                        Errors errors) {
        hasErrorsThrow(errors);
        validation(orderSheetCreateDto, errors);
        hasErrorsThrow(errors);

        OrderSheetDto orderSheet = orderSheetService.createOrderSheet(orderSheetCreateDto);

        return ResponseDto.created(orderSheet);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto createOrder(@RequestBody @Validated OrderCreateDto orderCreateDto,
                                   @CurrentMember Member member,
                                   Errors errors) {
        hasErrorsThrow(errors);
        validation(orderCreateDto, errors);
        hasErrorsThrow(errors);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        return ResponseDto.created(OrderDetailDto.orderToResultDetailDto(order));
    }

    @PostMapping("/non")
    public ResponseDto createOrderByNonMember(@RequestBody @Validated OrderCreateByNonMemberDto orderCreateByNonMemberDto,
                                              Errors errors) {
        hasErrorsThrow(errors);
        validation(orderCreateByNonMemberDto, errors);
        hasErrorsThrow(errors);
        Order order = orderService.createOrder(orderCreateByNonMemberDto);
        return ResponseDto.created(OrderDetailDto.orderToResultDetailDto(order));
    }

    @GetMapping("/non")
    public ResponseDto getNonMemberOrder(@Validated OrderQueryNonMemberDto orderQueryNonMemberDto, Errors errors) {
        hasErrorsThrow(errors);
        OrderSpecific orderSpecific = orderService.queryOrderByNonMember(orderQueryNonMemberDto);
        return ResponseDto.ok(OrderSpecificDetailDto.orderSpecificToDetailDto(orderSpecific));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto queryOrderSpecifics(@Validated OrderSearchConditionDto orderSearchConditionDto,
                                           @CurrentMember Member member,
                                           Errors errors) {
        hasErrorsThrow(errors);
        validation(orderSearchConditionDto, errors);
        hasErrorsThrow(errors);

        List<OrderSpecific> orderSpecifics = orderService.queryOrders(orderSearchConditionDto, member);
        List<OrderSpecificSimpleDto> result = orderSpecifics.stream().map(OrderSpecificSimpleDto::orderSpecificToSimpleDto).collect(Collectors.toList());
        CommonSlice body = new CommonSlice(result, orderSearchConditionDto);
        return ResponseDto.ok(body);
    }

    private void validation(OrderSearchConditionDto orderSearchConditionDto, Errors errors) {
        if (orderSearchConditionDto.getOrderState() == OrderState.ORDER_CREATE) {
            errors.rejectValue("orderState", "wrong value", "orderState does not OrderCreate");
        }
    }

    @GetMapping("/{orderSpecificId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto queryOrder(@PathVariable("orderSpecificId") Long orderSpecificId,
                                  @CurrentMember Member member) {
        OrderSpecific orderSpecific = orderService.findOrderById(orderSpecificId, member.getId());
        OrderSpecificDetailDto body = OrderSpecificDetailDto.orderSpecificToDetailDto(orderSpecific);
        return ResponseDto.ok(body);
    }

    @PutMapping("/accept/{orderSpecificId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto acceptOrder(@PathVariable("orderSpecificId") Long orderSpecificId,
                                   @CurrentMember Member member) {
        orderService.acceptOrder(orderSpecificId, member.getId());

        return ResponseDto.noContent();
    }

    @PutMapping("/destination/{orderSpecificId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto updateOrderDestination(@PathVariable("orderSpecificId") Long orderSpecificId,
                                              @RequestBody OrderDestinationSaveDto orderDestinationSaveDto,
                                              @CurrentMember Member member) {
        orderService.updateDestination(orderSpecificId, member.getId(), orderDestinationSaveDto);
        return ResponseDto.noContent();
    }

    @PutMapping("/exchange/{orderSpecificId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto orderExchangeRequest(@PathVariable("orderSpecificId") Long orderSpecificId,
                                            @RequestBody OrderExchangeRequestDto orderExchangeRequestDto,
                                            @CurrentMember Member member) {
        orderExchangeService.orderExchangeRequest(orderSpecificId, orderExchangeRequestDto, member);

        return ResponseDto.noContent();
    }

    @GetMapping("/exchange/{orderSpecificId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto queryOrderExchange(@PathVariable("orderSpecificId") Long orderSpecificId,
                                          @CurrentMember Member member) {
        OrderSpecific orderSpecific = orderService.findOrderById(orderSpecificId, member.getId());

        OrderExchangeDto body = OrderExchangeDto.orderSpecificToOrderExchangeDto(orderSpecific);

        return ResponseDto.ok(body);
    }

    @DeleteMapping("/exchange/{orderSpecificId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseDto deleteOrderExchange(@PathVariable("orderSpecificId") Long orderSpecificId,
                                           @CurrentMember Member member) {
        orderExchangeService.orderExchangeCancel(orderSpecificId, member);

        return ResponseDto.noContent();
    }


    private void validation(OrderSheetCreateDto orderSheetCreateDto, Errors errors) {
        long distinctSize = orderSheetCreateDto.getProducts().stream().distinct().count();
        long originalSize = orderSheetCreateDto.getProducts().size();
        if (distinctSize != originalSize) {
            errors.rejectValue("products", "wrong value", "product Id can't overlap");
        }
    }

    private void validation(OrderCreateByNonMemberDto orderCreateByNonMemberDto, Errors errors) {
        long distinctSize = orderCreateByNonMemberDto.getProducts().stream().distinct().count();
        long originalSize = orderCreateByNonMemberDto.getProducts().size();
        if (distinctSize != originalSize) {
            errors.rejectValue("products", "wrong value", "product Id can't overlap");
        }
        if (orderCreateByNonMemberDto.getOrderMultipleYn() == YnType.N && orderCreateByNonMemberDto.getDestination().size() > 1) {
            errors.rejectValue("destination", "wrong value", "if order multiple equals N then destination size can't more then 1");
        }
    }


    private void validation(OrderCreateDto orderCreateDto, Errors errors) {
        long distinctSize = orderCreateDto.getProducts().stream().distinct().count();
        long originalSize = orderCreateDto.getProducts().size();
        if (distinctSize != originalSize) {
            errors.rejectValue("products", "wrong value", "product Id can't overlap");
        }

        if (orderCreateDto.getOrderMultipleYn() == YnType.N && orderCreateDto.getDestination().size() > 1) {
            errors.rejectValue("destination", "wrong value", "if order multiple equals N then destination size can't more then 1");
        }
    }
}
