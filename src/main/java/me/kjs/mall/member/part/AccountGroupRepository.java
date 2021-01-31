package me.kjs.mall.member.part;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountGroupRepository extends JpaRepository<AccountGroup, Long> {

    @Query("select ag from AccountGroup ag where ag.alias='default'")
    Optional<AccountGroup> findDefault();


    @Query("select ag from AccountGroup ag join fetch ag.roles where ag.alias='superUser'")
    Optional<AccountGroup> findSu();


    @Query("select ag from AccountGroup ag join fetch ag.roles")
    List<AccountGroup> findAllByAccountFetchRoles();

    boolean existsByAlias(String alias);

}
