package me.kjs.mall.member.part;


import lombok.*;
import me.kjs.mall.common.BaseEntity;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.member.dto.account.AccountGroupSaveDto;
import me.kjs.mall.member.type.AccountRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class AccountGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_group_id")
    private Long id;

    private String name; // 그룹명

    @Column(unique = true)
    private String alias;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<AccountRole> roles = new HashSet<>();

    public Collection<SimpleGrantedAuthority> getAuthorityRoles() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }

    public List<String> getAccountRoleNames() {
        return roles.stream().map(AccountRole::name).collect(Collectors.toList());
    }

    public static AccountGroup createAccountGroup(Collection<AccountRole> accountRoles, String name, String alias) {

        Set<AccountRole> accountRoleSet = new HashSet<>();
        accountRoleSet.addAll(accountRoles);

        return AccountGroup.builder()
                .alias(alias)
                .name(name)
                .roles(accountRoleSet)
                .build();
    }

    public void update(AccountGroupSaveDto accountGroupSaveDto) {
        if (CollectionTextUtil.isBlank(accountGroupSaveDto.getName()) == false) {
            updateName(accountGroupSaveDto.getName());
        }
        if (CollectionTextUtil.isBlank(accountGroupSaveDto.getAlias()) == false) {
            updateAlias(accountGroupSaveDto.getAlias());
        }
        if (CollectionTextUtil.isBlank(accountGroupSaveDto.getAccountRoles()) == false) {
            updateRoles(accountGroupSaveDto.getAccountRoles());
        }
    }

    private void updateRoles(List<AccountRole> accountRoles) {
        this.roles = new HashSet<>();
        roles.addAll(accountRoles);
    }

    private void updateAlias(String alias) {
        this.alias = alias;
    }

    private void updateName(String name) {
        this.name = name;
    }

    public boolean isUpdateAvail() {
        if (alias.equals("default") || alias.equals("superUser")) {
            return false;
        }
        return true;
    }

    public boolean isContainRolesTo(AccountRole accountRole) {
        return roles.contains(accountRole);
    }
}
