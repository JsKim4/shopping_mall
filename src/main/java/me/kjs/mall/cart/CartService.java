package me.kjs.mall.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.cart.dto.CartCreateDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.ThrowUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public Cart createCart(CartCreateDto cartCreateDto, Member member) {
        Product product = findProductById(cartCreateDto);
        Cart cart = cartRepository.findTopByMemberAndProduct(member, product).orElse(Cart.createCart(product, member));
        cart.updateQuantity(cart.getQuantity() + cartCreateDto.getQuantity());
        return cartRepository.save(cart).loading();
    }

    @Transactional
    public void modifyCartQuantity(Long cartId, CartQuantityModifyDto cartQuantityModifyDto, Long memberId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NoExistIdException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notOwnerThrow(cart, member);
        cart.updateQuantity(cartQuantityModifyDto.getQuantity());
    }

    @Transactional
    public void removeCart(Long cartId, Long memberId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NoExistIdException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notOwnerThrow(cart, member);
        cartRepository.delete(cart);
    }

    @Transactional
    public void removeCarts(List<Long> cartIds, Long memberId) {
        List<Cart> carts = cartRepository.findAllById(cartIds);
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        for (Cart cart : carts) {
            ThrowUtil.notOwnerThrow(cart, member);
            cartRepository.delete(cart);
        }
    }


    public List<Cart> findCartsByMemberIdFetch(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        List<Cart> carts = cartRepository.findAllByMember(member);
        for (Cart cart : carts) {
            cart.loading();
        }
        return carts;
    }


    private Product findProductById(CartCreateDto cartCreateDto) {
        Product product = productRepository.findById(cartCreateDto.getProductId()).orElseThrow(NoExistIdException::new);
        ThrowUtil.notUsedThrow(product);
        return product;
    }
}
