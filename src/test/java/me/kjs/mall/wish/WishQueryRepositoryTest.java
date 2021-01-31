package me.kjs.mall.wish;

import me.kjs.mall.common.BaseTest;
import org.junit.jupiter.api.Test;

class WishQueryRepositoryTest extends BaseTest {

    @Test
    void existByMemberIdAndProductIdTest() {
        boolean b = wishQueryRepository.existByMemberIdAndProductId(1L, 1L);
    }

}