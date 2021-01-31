package me.kjs.mall.point;

import me.kjs.mall.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("select p from Point p where p.member = :member order by p.id desc")
    List<Point> findAllByMember(@Param("member") Member member);
}
