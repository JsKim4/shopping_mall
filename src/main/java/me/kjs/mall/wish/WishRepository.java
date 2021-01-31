package me.kjs.mall.wish;


import me.kjs.mall.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByMember(Member member);
}
