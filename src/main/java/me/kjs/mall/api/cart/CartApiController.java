package me.kjs.mall.api.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.cart.Cart;
import me.kjs.mall.cart.CartQuantityModifyDto;
import me.kjs.mall.cart.CartService;
import me.kjs.mall.cart.dto.CartCreateDto;
import me.kjs.mall.cart.dto.CartDto;
import me.kjs.mall.common.dto.OnlyIdsDto;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.common.util.CompareUtil;
import me.kjs.mall.member.Member;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/carts")
@Slf4j
public class CartApiController {

    private final CartService cartService;

    @GetMapping
    public ResponseDto findByMember(@CurrentMember Member member) {
        List<Cart> carts = cartService.findCartsByMemberIdFetch(member.getId());

        List<CartDto> body = carts.stream().map(CartDto::cartToDto)
                .sorted((o1, o2) -> CompareUtil.descCompare(o1.getCartId(), o2.getCartId()))
                .collect(Collectors.toList());

        return ResponseDto.ok(body);
    }

    @PostMapping
    public ResponseDto createCart(@RequestBody @Validated CartCreateDto cartCreateDto, @CurrentMember Member member, Errors errors) {
        hasErrorsThrow(errors);
        Cart cart = cartService.createCart(cartCreateDto, member);

        CartDto body = CartDto.cartToDto(cart);

        return ResponseDto.created(body);
    }

    @PutMapping("/{cartId}")
    public ResponseDto modifyCartQuantity(@RequestBody @Validated CartQuantityModifyDto cartQuantityModifyDto,
                                          @PathVariable("cartId") Long cartId,
                                          @CurrentMember Member member,
                                          Errors errors) {
        hasErrorsThrow(errors);
        cartService.modifyCartQuantity(cartId, cartQuantityModifyDto, member.getId());

        return ResponseDto.noContent();
    }

    @DeleteMapping("/{cartId}")
    public ResponseDto deleteCart(@PathVariable("cartId") Long cartId,
                                  @CurrentMember Member member) {
        cartService.removeCart(cartId, member.getId());
        return ResponseDto.noContent();
    }

    @DeleteMapping()
    public ResponseDto deleteCartList(@RequestBody OnlyIdsDto removeCartIds,
                                      @CurrentMember Member member) {
        cartService.removeCarts(removeCartIds.getIds(), member.getId());
        return ResponseDto.noContent();
    }
}
