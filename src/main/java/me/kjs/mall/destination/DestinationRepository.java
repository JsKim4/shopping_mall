package me.kjs.mall.destination;

import me.kjs.mall.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByMemberOrderByIdDesc(Member member);
}
