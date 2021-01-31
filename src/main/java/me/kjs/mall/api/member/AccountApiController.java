package me.kjs.mall.api.member;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.OnlyIdDto;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.member.dto.account.AccountGroupDto;
import me.kjs.mall.member.dto.account.AccountGroupSaveDto;
import me.kjs.mall.member.part.AccountGroup;
import me.kjs.mall.member.part.AccountGroupRepository;
import me.kjs.mall.member.part.AccountGroupService;
import me.kjs.mall.member.type.AccountRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;
import static me.kjs.mall.member.dto.account.AccountGroupDto.accountGroupToDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members/accounts")
@PreAuthorize("hasRole('ROLE_ACCOUNT_GROUP')")
public class AccountApiController {

    private final AccountGroupRepository accountGroupRepository;

    private final AccountGroupService accountGroupService;

    @GetMapping("/roles")
    public ResponseDto findRoles() {
        List<AccountRole> accountRoles = Arrays.asList(AccountRole.values());
        return ResponseDto.ok(accountRoles);
    }

    @GetMapping("/groups")
    public ResponseDto findGroups() {
        List<AccountGroup> accountGroups = accountGroupRepository.findAllByAccountFetchRoles().stream().distinct().collect(Collectors.toList());

        List<AccountGroupDto> accountGroupDtos = accountGroups.stream().map(AccountGroupDto::accountGroupToDto).collect(Collectors.toList());

        return ResponseDto.ok(accountGroupDtos);
    }

    @PostMapping("/groups")
    public ResponseDto createGroup(@RequestBody @Validated AccountGroupSaveDto accountGroupSaveDto, Errors errors) {
        hasErrorsThrow(errors);

        AccountGroup saveAccountGroup = accountGroupService.createAccountGroup(accountGroupSaveDto);

        return ResponseDto.created(accountGroupToDto(saveAccountGroup));
    }

    @PutMapping("/groups/{groupId}")
    public ResponseDto updateGroups(@PathVariable("groupId") Long groupId, @RequestBody @Validated AccountGroupSaveDto accountGroupSaveDto, Errors errors) {
        hasErrorsThrow(errors);
        accountGroupService.update(groupId, accountGroupSaveDto);

        return ResponseDto.noContent();
    }

    @PutMapping("/{memberId}")
    public ResponseDto updateMemberGroups(@PathVariable("memberId") Long memberId, @RequestBody @Validated OnlyIdDto onlyIdDto, Errors errors) {
        hasErrorsThrow(errors);

        accountGroupService.updateMemberGroup(memberId, onlyIdDto.getId());

        return ResponseDto.noContent();
    }
}
