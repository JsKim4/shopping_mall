package me.kjs.mall.configs;

import lombok.RequiredArgsConstructor;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.CategoryRepository;
import me.kjs.mall.category.CategoryService;
import me.kjs.mall.category.dto.CategorySaveDto;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.*;
import me.kjs.mall.member.dto.sign.MemberJoinDto;
import me.kjs.mall.member.dto.sign.PolicyAndAcceptDto;
import me.kjs.mall.member.part.AccountGroup;
import me.kjs.mall.member.part.AccountGroupRepository;
import me.kjs.mall.member.part.LoginInfo;
import me.kjs.mall.member.part.PointManagement;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.member.type.AccountStatus;
import me.kjs.mall.member.type.Gender;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.ProductRepository;
import me.kjs.mall.product.ProductService;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.base.BaseProductService;
import me.kjs.mall.product.base.dto.BaseProductSaveDto;
import me.kjs.mall.product.dto.ProductCreateDto;
import me.kjs.mall.product.dto.ProductDeliveryDto;
import me.kjs.mall.product.type.DeliveryType;
import me.kjs.mall.product.type.DiscountType;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Profile({"test-server", "default"})
@RequiredArgsConstructor
public class TestDataRunner {
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


    @PostConstruct
    public void setUp() {
        accountGroupInit();
        adminUserRegister();
        if (categoryRepository.count() == 0) {
            categoryRegister();
        }
        if (productRepository.count() == 0) {
            productRegister();
        }

        if (!memberRepository.findByEmail("user001").isPresent()) {
            customUserRegister();
        }

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

    private void categoryRegister() {
        String top[] = {"기능별", "성분별", "대상별"};
        List<String> list[] = new List[3];
        list[0] = Arrays.asList("혈행,콜레스테롤", "높은 혈압,항산화", "뼈건강, 관절건강", "다이어트, 피부", "눈 건강", "장 건강"
                , "혈당관리", "두뇌 영양공급", "갱년기", "면역력", "스트레스", "피로개선", "에너지대사, 종합비타민", "질 건강");
        list[1] = Arrays.asList("오메가3", "비타민&미네랄", "칼슘,MSM", "프로폴리스", "엽산", "아연", "마그네슘", "유산균", "루테인,비타민A", "다이어트,피부"
                , "코엔자임큐텐", "철분", "크롬", "호박", "테아닌,미강", "녹용,홍삼");
        list[2] = Arrays.asList("여성용", "남성용", "노년용", "청소년용", "어린이용", "임산부용");
        for (int i = 0; i < top.length; i++) {
            CategorySaveDto parentCategoryDto = CategorySaveDto.builder()
                    .name(top[i])
                    .productContainable(YnType.N)
                    .build();
            Category parentCategory = categoryService.createCategory(parentCategoryDto);
            parentCategory.neverDeleted();
            categoryRepository.save(parentCategory);
            categoryService.useCategory(parentCategory.getId());

            for (String s : list[i]) {
                CategorySaveDto childCategoryDto = CategorySaveDto.builder()
                        .name(s)
                        .productContainable(YnType.Y)
                        .parentCategoryId(parentCategory.getId())
                        .build();
                Category category = categoryService.createCategory(childCategoryDto);
                categoryService.useCategory(category.getId());
            }
        }
    }

    public void productRegister() {
        List<Category> parentCategory = categoryRepository.findAllByParentCategory(null);
        List<Long> categories1 = categoryRepository.findAllByParentCategory(parentCategory.get(0)).stream().map(Category::getId).collect(Collectors.toList());
        List<Long> categories2 = categoryRepository.findAllByParentCategory(parentCategory.get(1)).stream().map(Category::getId).collect(Collectors.toList());
        List<Long> categories3 = categoryRepository.findAllByParentCategory(parentCategory.get(2)).stream().map(Category::getId).collect(Collectors.toList());

        List<String> productNames = Arrays.asList(
                "수면엔", "혈당엔", "콜라겐", "프로바이오틱스", "아이브라이트"
                , "시데랄", "클린오메가", "활력엔", "마그네슘", "아스타잔티"
                , "우먼케어", "그린 츄어블 아연", "회화춘", "피로면역엔", "피로개선엔"
        );

        for (int i = 0; i < 5; i++) {
            BaseProduct baseProduct = generatorBaseProduct("B0000000F" + i, productNames.get(i), i + 1);
            baseProductService.addCategory(baseProduct.getId(), categories1);
            BaseProduct baseProduct1 = generatorBaseProduct("C0000000F" + i, productNames.get(i + 5), i + 10);
            baseProductService.addCategory(baseProduct1.getId(), categories2);
            BaseProduct baseProduct2 = generatorBaseProduct("D0000000F" + i, productNames.get(i + 10), i + 100);
            baseProductService.addCategory(baseProduct2.getId(), categories3);
            generatorProduct(baseProduct);
            generatorProduct(baseProduct1);
            generatorProduct(baseProduct2);
        }
    }

    private void generatorProduct(BaseProduct baseProduct) {

        baseProductService.useBaseProduct(baseProduct.getId());

        ProductCreateDto normalProduct = ProductCreateDto.builder()
                .baseProductId(baseProduct.getId())
                .stock(10)
                .salesEndDate(LocalDateTime.now().plusMonths(1))
                .salesBeginDate(LocalDateTime.now())
                .discountType(DiscountType.PERCENT)
                .discountAmount(10)
                .build();
        Product product = productService.createProduct(normalProduct);
        productService.useProduct(product.getId());
    }

    private void customUserRegister() {

        List<PolicyAndAcceptDto> policyAndAcceptDtos = Arrays.asList(PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.MARKETING).build(),
                PolicyAndAcceptDto.builder().acceptType(YnType.Y).policyType(PolicyType.PRIVACY).build());
        for (int i = 1; i <= 5; i++) {
            String email = "user00" + i;
            if (memberRepository.existsByEmail(email) == false) {
                MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                        .name("일반 유저" + i)
                        .email(email)
                        .password("a123456")
                        .birth(LocalDate.of(1970, 1, 1))
                        .certKey("certKey")
                        .phoneNumber("0100000000" + i)
                        .gender(Gender.MALE)
                        .policies(policyAndAcceptDtos)
                        .build();

                signService.join(memberJoinDto);

            }
        }


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
                .phoneNumber("01000000000")
                .gender(Gender.MALE)
                .email("jskim")
                .build();

        memberRepository.save(member);
    }

    private BaseProduct generatorBaseProduct(String code, String productName, int i) {
        List<String> tags = Arrays.asList("tag1", "tag2", "tag3");

        List<String> images = new ArrayList<>();
        images.add("/upload/mall/product/2020-12-30/image001.png");
        images.add("/upload/mall/product/2020-12-30/image002.png");
        images.add("/upload/mall/product/2020-12-30/image003.png");
        images.add("/upload/mall/product/2020-12-30/image004.png");
        images.add("/upload/mall/product/2020-12-30/image005.png");

        ProductDeliveryDto productDeliveryDto = ProductDeliveryDto.builder()
                .returnLocation("RETURN LOCATION")
                .feeCondition(60000)
                .fee(3000)
                .deliveryType(DeliveryType.CONDITION)
                .deliveryYn(YnType.Y)
                .bundleYn(YnType.Y)
                .build();

        BaseProductSaveDto baseProductSaveDto = BaseProductSaveDto.builder()
                .originPrice(i * 100)
                .tags(tags)
                .productDelivery(productDeliveryDto)
                .name(productName)
                .code(code)
                .contents(images.toString())
                .thumbnail(Arrays.asList("/upload/mall/product/2020-12-30/thumbnail1.png", "/upload/mall/product/2020-12-30/thumbnail2.png"))
                .build();
        return baseProductService.createBaseProduct(baseProductSaveDto);
    }
}
