package me.kjs.mall.point;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import org.junit.jupiter.api.Test;

class PointServiceTest extends BaseTest {

    @Test
    void expiredTest() {
        pointService.expiredPoint();
    }

    @Test
    void availablePoint() {
        Member member = memberRepository.findByEmail("user003").orElseThrow(NoExistIdException::new);
        int i = pointService.availableUsePoint(member);
    }
}