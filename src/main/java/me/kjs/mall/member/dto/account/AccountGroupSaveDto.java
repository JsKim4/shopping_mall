package me.kjs.mall.member.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.member.type.AccountRole;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountGroupSaveDto {
    private String name;
    private String alias;
    private List<AccountRole> accountRoles;
}
