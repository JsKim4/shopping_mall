package me.kjs.mall.cart;

import me.kjs.mall.cart.dto.CartCreateDto;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.GrantException;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityExistsException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest extends BaseTest {

    @Test
    @DisplayName("장바구니 생성 성공 케이스")
    void createCart() {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();
        List<Product> list = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .productId(list.get(0).getId())
                .quantity(10)
                .build();

        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityExistsException::new);

        Cart cart = cartService.createCart(cartCreateDto, member);
        assertNotNull(cart.getId());
        assertEquals(member, cart.getMember());
        assertEquals(cartCreateDto.getQuantity(), cart.getQuantity());
        assertEquals(cartCreateDto.getProductId(), cart.getProduct().getId());
    }

    @Test
    @DisplayName("장바구니 생성 성공 케이스 / 동일한 상품을 추가할 경우")
    void createCartAddQuantity() {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();
        List<Product> list = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .productId(list.get(0).getId())
                .quantity(10)
                .build();

        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityExistsException::new);

        Cart cart = cartService.createCart(cartCreateDto, member);

        Cart cart2 = cartService.createCart(cartCreateDto, member);

        assertEquals(cart.getId(), cart2.getId());
        assertEquals(member, cart2.getMember());
        assertEquals(cartCreateDto.getQuantity() * 2, cart2.getQuantity());
        assertEquals(cartCreateDto.getProductId(), cart2.getProduct().getId());
    }

    @Test
    @DisplayName("장바구니 생성 실패 케이스 / 사용 중지된 상품이거나 삭제된 상품일 경우")
    void createCartFailByNotUsedProduct() {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();
        List<Product> list = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        Long productId = list.get(0).getId();
        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .productId(productId)
                .quantity(10)
                .build();

        productService.unUseProduct(productId);

        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityExistsException::new);

        NoExistIdException noExistIdException = assertThrows(NoExistIdException.class, () -> {
            Cart cart = cartService.createCart(cartCreateDto, member);
        });
    }

    @Test
    @DisplayName("장바구니 생성 실패 케이스 / 존재하지 않는 상품일경우")
    void createCartFailByNotExist() {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();
        List<Product> list = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        Long productId = list.get(0).getId();
        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .productId(-1L)
                .quantity(10)
                .build();

        productService.unUseProduct(productId);

        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityExistsException::new);

        NoExistIdException noExistIdException = assertThrows(NoExistIdException.class, () -> {
            Cart cart = cartService.createCart(cartCreateDto, member);
        });
    }

    @Test
    @DisplayName("장바구니 개수 변경 성공 케이스")
    void modifyCartQuantity() {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();
        List<Product> list = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .productId(list.get(0).getId())
                .quantity(10)
                .build();

        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityExistsException::new);

        Cart cart = cartService.createCart(cartCreateDto, member);

        CartQuantityModifyDto cartQuantityModifyDto = CartQuantityModifyDto.builder()
                .quantity(30)
                .build();

        cartService.modifyCartQuantity(cart.getId(), cartQuantityModifyDto, member.getId());

        assertNotNull(cart.getId());
        assertEquals(member, cart.getMember());
        assertEquals(cartQuantityModifyDto.getQuantity(), cart.getQuantity());
        assertEquals(cartCreateDto.getProductId(), cart.getProduct().getId());
    }

    @Test
    @DisplayName("장바구니 개수 변경 실패 케이스 / 장바구니의 주인이 아닌경우")
    void modifyCartQuantityFailByNotOwner() {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();
        List<Product> list = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .productId(list.get(0).getId())
                .quantity(10)
                .build();

        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityExistsException::new);

        Cart cart = cartService.createCart(cartCreateDto, member);

        Member member2 = memberRepository.findByEmail("user001").orElse(memberRepository.findByEmail("user002").orElse(null));

        CartQuantityModifyDto cartQuantityModifyDto = CartQuantityModifyDto.builder()
                .quantity(30)
                .build();

        GrantException grantException = assertThrows(GrantException.class, () -> {
            cartService.modifyCartQuantity(cart.getId(), cartQuantityModifyDto, member2.getId());
        });
    }


    @Test
    @DisplayName("장바구니 삭제 성공 케이스")
    void removeCartTest() {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();
        List<Product> list = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .productId(list.get(0).getId())
                .quantity(10)
                .build();

        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityExistsException::new);

        Cart cart = cartService.createCart(cartCreateDto, member);
        cartService.removeCart(cart.getId(), member.getId());
        NoExistIdException noExistIdException = assertThrows(NoExistIdException.class, () -> {
            cartRepository.findById(cart.getId()).orElseThrow(NoExistIdException::new);
        });
    }

    @Test
    @DisplayName("장바구니 삭제 성공 케이스")
    void removeCartTestFailByNotOwner() {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();
        List<Product> list = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        CartCreateDto cartCreateDto = CartCreateDto.builder()
                .productId(list.get(0).getId())
                .quantity(10)
                .build();

        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityExistsException::new);

        Member member2 = memberRepository.findByEmail("00000001").orElse(memberRepository.findByEmail("00000002").orElse(null));
        Cart cart = cartService.createCart(cartCreateDto, member2);
        GrantException grantException = assertThrows(GrantException.class, () -> {
            cartService.removeCart(cart.getId(), member.getId());
        });
    }


    @Test
    @DisplayName("장바구니 조회 성공 케이스")
    void findByMemberIdTest() {
        ProductSearchCondition productSearchCondition = ProductSearchCondition.builder()
                .build();
        List<Product> list = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();

        CartCreateDto cartCreateDto1 = CartCreateDto.builder()
                .productId(list.get(0).getId())
                .quantity(10)
                .build();

        CartCreateDto cartCreateDto2 = CartCreateDto.builder()
                .productId(list.get(1).getId())
                .quantity(10)
                .build();

        CartCreateDto cartCreateDto3 = CartCreateDto.builder()
                .productId(list.get(2).getId())
                .quantity(10)
                .build();

        CartCreateDto cartCreateDto4 = CartCreateDto.builder()
                .productId(list.get(3).getId())
                .quantity(10)
                .build();

        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityExistsException::new);
        cartService.createCart(cartCreateDto1, member);
        cartService.createCart(cartCreateDto2, member);
        cartService.createCart(cartCreateDto3, member);
        cartService.createCart(cartCreateDto4, member);

        List<Cart> carts = cartService.findCartsByMemberIdFetch(member.getId());
        assertTrue(carts.stream().anyMatch(cart -> cart.getProduct().getId().equals(cartCreateDto1.getProductId())));
        assertTrue(carts.stream().anyMatch(cart -> cart.getProduct().getId().equals(cartCreateDto2.getProductId())));
        assertTrue(carts.stream().anyMatch(cart -> cart.getProduct().getId().equals(cartCreateDto3.getProductId())));
        assertTrue(carts.stream().anyMatch(cart -> cart.getProduct().getId().equals(cartCreateDto4.getProductId())));
    }

}