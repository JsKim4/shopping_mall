package me.kjs.mall.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("select m from Member m where m.loginInfo.refreshToken = :refreshToken")
    Optional<Member> findByRefreshToken(@Param("refreshToken") String refreshToken);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("select m from Member m join fetch m.accountGroup ag join fetch ag.roles where m.email = :email")
    Optional<Member> findByEmailFetchGroup(@Param("email") String email);

    Optional<Member> findByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumberAndEmail(String phoneNumber, String email);
}
