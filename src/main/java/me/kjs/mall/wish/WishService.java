package me.kjs.mall.wish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class WishService {
    private final WishRepository wishRepository;

    private final WishQueryRepository wishQueryRepository;

    private final MemberRepository memberRepository;

    private final ProductRepository productRepository;

    @Transactional
    public Wish createWish(Long memberId, Long productId) {
        boolean isExist = wishQueryRepository.existByMemberIdAndProductId(memberId, productId);
        if (isExist) {
            return wishQueryRepository.findWishByMemberIdAndProductId(memberId, productId).loading();
        }
        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);
        Product product = productRepository.findById(productId).orElseThrow(NoExistIdException::new);
        ThrowUtil.notUsedThrow(product);

        Wish wish = Wish.createWish(member, product);
        return wishRepository.save(wish).loading();
    }

    @Transactional
    public void deleteWish(Long memberId, Long productId) {
        boolean isExist = wishQueryRepository.existByMemberIdAndProductId(memberId, productId);
        if (isExist == false) {
            return;
        }
        Wish wish = wishQueryRepository.findWishByMemberIdAndProductId(memberId, productId);
        wishRepository.delete(wish);
    }

    public List<Wish> findWish(Member member) {
        List<Wish> wishes = wishRepository.findAllByMember(member);
        for (Wish wish : wishes) {
            wish.loading();
        }
        return wishes;
    }
}
