package me.kjs.mall.member.part;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.member.dto.account.AccountGroupSaveDto;
import me.kjs.mall.member.exception.account.AlreadyExistAliasException;
import me.kjs.mall.member.exception.account.NotAvailableAccountGroupUpdateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class AccountGroupService {
    private final AccountGroupRepository accountGroupRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public AccountGroup createAccountGroup(AccountGroupSaveDto accountGroupSaveDto) {
        if (accountGroupRepository.existsByAlias(accountGroupSaveDto.getAlias())) {
            throw new AlreadyExistAliasException();
        }
        AccountGroup accountGroup = AccountGroup.createAccountGroup(accountGroupSaveDto.getAccountRoles(), accountGroupSaveDto.getName(), accountGroupSaveDto.getAlias());
        return accountGroupRepository.save(accountGroup);
    }

    @Transactional
    public void update(Long groupId, AccountGroupSaveDto accountGroupSaveDto) {

        AccountGroup accountGroup = accountGroupRepository.findById(groupId).orElseThrow(NoExistIdException::new);

        if (CollectionTextUtil.isBlank(accountGroupSaveDto.getAlias()) == false && accountGroupRepository.existsByAlias(accountGroupSaveDto.getAlias())) {
            if (accountGroup.getAlias().equals(accountGroupSaveDto.getAlias()) == false) {
                throw new AlreadyExistAliasException();
            }
        }

        if (accountGroup.isUpdateAvail() == false) {
            throw new NotAvailableAccountGroupUpdateException();
        }

        accountGroup.update(accountGroupSaveDto);
    }

    @Transactional
    public void updateMemberGroup(Long memberId, Long groupId) {
        AccountGroup accountGroup = accountGroupRepository.findById(groupId).orElseThrow(NoExistIdException::new);

        Member member = memberRepository.findById(memberId).orElseThrow(NoExistIdException::new);

        member.updateGroup(accountGroup);
    }
}
