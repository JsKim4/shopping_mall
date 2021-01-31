package me.kjs.mall.configs;

import lombok.RequiredArgsConstructor;
import me.kjs.mall.category.CategoryRepository;
import me.kjs.mall.category.CategoryService;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberQueryRepository;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.member.SignService;
import me.kjs.mall.member.part.AccountGroup;
import me.kjs.mall.member.part.AccountGroupRepository;
import me.kjs.mall.member.part.LoginInfo;
import me.kjs.mall.member.part.PointManagement;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.member.type.AccountStatus;
import me.kjs.mall.member.type.Gender;
import me.kjs.mall.product.ProductRepository;
import me.kjs.mall.product.ProductService;
import me.kjs.mall.product.base.BaseProductService;
import me.kjs.mall.system.SystemService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;
    private final MemberQueryRepository memberQueryRepository;
    private final MemberRepository memberRepository;
    private final SignService signService;
    private final AccountGroupRepository accountGroupRepository;
    private final BaseProductService baseProductService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;


    private final SystemService systemService;

    private final DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws SQLException {
        //App init;
        systemService.init();
        System.out.println(dataSource);
    }

    @PostConstruct
    private void init() {
        accountGroupInit();
        adminUserRegister();
        accountGroupRoleInit();
    }

    private void accountGroupRoleInit() {
        AccountGroup accountGroup = accountGroupRepository.findSu().orElseThrow(NoExistIdException::new);
        for (AccountRole value : AccountRole.values()) {
            if (!accountGroup.getRoles().contains(value)) {
                accountGroup.getRoles().add(value);
            }
        }
        accountGroupRepository.save(accountGroup);
    }

    private void accountGroupInit() {
        AccountGroup defaultGroup = accountGroupRepository.findDefault().orElse(null);
        if (defaultGroup == null) {
            Set<AccountRole> accountRoles = new HashSet<>();
            accountRoles.add(AccountRole.USER);
            AccountGroup newDefaultGroup = AccountGroup.createAccountGroup(accountRoles, "기본 유저", "default");
            accountGroupRepository.save(newDefaultGroup);
        }
        AccountGroup superUser = accountGroupRepository.findSu().orElse(null);
        if (superUser == null) {
            Set<AccountRole> accountRoles = new HashSet<>();
            accountRoles.addAll(Arrays.asList(AccountRole.values()));
            AccountGroup newSuGroup = AccountGroup.createAccountGroup(accountRoles, "su", "superUser");
            accountGroupRepository.save(newSuGroup);
        }
    }

    private void adminUserRegister() {
        if (memberQueryRepository.existAdmin()) {
            return;
        }
        AccountGroup accountGroup = accountGroupRepository.findSu().orElseThrow(NoExistIdException::new);
        Member member = Member.builder()
                .password(passwordEncoder.encode("a123456"))
                .pointManagement(PointManagement.initialize())
                .name("ADMIN")
                .accountGroup(accountGroup)
                .loginInfo(LoginInfo.initialize())
                .withdrawDateTime(null)
                .accountStatus(AccountStatus.ALLOW)
                .birth(LocalDate.of(1970, 1, 1).toString().replace("-", ""))
                .phoneNumber("01000000011")
                .gender(Gender.MALE)
                .email("jskim")
                .build();

        memberRepository.save(member);
    }


}
