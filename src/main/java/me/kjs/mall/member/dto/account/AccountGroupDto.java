package me.kjs.mall.member.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.part.AccountGroup;
import me.kjs.mall.member.type.AccountRole;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountGroupDto {
    private Long id;
    private String name;
    private Set<AccountRole> accountRoles;

    public static AccountGroupDto accountGroupToDto(AccountGroup accountGroup) {
        return AccountGroupDto.builder()
                .id(accountGroup.getId())
                .name(accountGroup.getName())
                .accountRoles(accountGroup.getRoles())
                .build();
    }
}
