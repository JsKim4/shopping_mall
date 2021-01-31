package me.kjs.mall.api.wish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.member.Member;
import me.kjs.mall.wish.Wish;
import me.kjs.mall.wish.WishService;
import me.kjs.mall.wish.dto.WishProductDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/wish")
@RequiredArgsConstructor
@Slf4j
public class WishApiController {

    private final WishService wishService;

    @GetMapping
    public ResponseDto findByMemberId(@CurrentMember Member member) {
        List<Wish> wish = wishService.findWish(member);

        List<WishProductDto> body = wish.stream().map(WishProductDto::wishToWishProductDto).collect(Collectors.toList());

        return ResponseDto.ok(body);
    }

    @PostMapping("/{productId}")
    public ResponseDto createWish(@PathVariable("productId") Long productId,
                                  @CurrentMember Member member) {
        Wish wish = wishService.createWish(member.getId(), productId);
        WishProductDto wishProductDto = WishProductDto.wishToWishProductDto(wish);
        return ResponseDto.created(wishProductDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseDto deleteWish(@PathVariable("productId") Long productId,
                                  @CurrentMember Member member) {
        wishService.deleteWish(member.getId(), productId);

        return ResponseDto.noContent();
    }

}
