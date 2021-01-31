package me.kjs.mall.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kjs.mall.api.test.TestService;
import me.kjs.mall.banner.MainBannerRepository;
import me.kjs.mall.banner.MainBannerService;
import me.kjs.mall.cart.CartRepository;
import me.kjs.mall.cart.CartService;
import me.kjs.mall.category.CategoryRepository;
import me.kjs.mall.category.CategoryService;
import me.kjs.mall.cert.CertService;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.configs.RestDocsConfig;
import me.kjs.mall.configs.properties.SecurityProperties;
import me.kjs.mall.coupon.CouponService;
import me.kjs.mall.coupon.IssueCouponRepository;
import me.kjs.mall.destination.DestinationRepository;
import me.kjs.mall.destination.DestinationService;
import me.kjs.mall.event.EventService;
import me.kjs.mall.member.*;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.part.AccountGroupRepository;
import me.kjs.mall.member.part.AccountGroupService;
import me.kjs.mall.notice.NoticeService;
import me.kjs.mall.order.OrderExchangeService;
import me.kjs.mall.order.OrderQueryRepository;
import me.kjs.mall.order.OrderRepository;
import me.kjs.mall.order.OrderService;
import me.kjs.mall.order.sheet.OrderSheetService;
import me.kjs.mall.order.specific.OrderSpecificRepository;
import me.kjs.mall.partial.BestReviewProductRepository;
import me.kjs.mall.partial.BestReviewService;
import me.kjs.mall.partial.BestSellerRepository;
import me.kjs.mall.partial.PartialService;
import me.kjs.mall.point.PointRepository;
import me.kjs.mall.point.PointService;
import me.kjs.mall.point.PointSpecificQueryRepository;
import me.kjs.mall.product.ProductQueryRepository;
import me.kjs.mall.product.ProductRepository;
import me.kjs.mall.product.ProductService;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.base.BaseProductRepository;
import me.kjs.mall.product.base.BaseProductService;
import me.kjs.mall.product.base.dto.BaseProductSaveDto;
import me.kjs.mall.product.dto.ProductDeliveryDto;
import me.kjs.mall.product.type.DeliveryType;
import me.kjs.mall.qna.QnaRepository;
import me.kjs.mall.qna.QnaService;
import me.kjs.mall.review.ReviewRepository;
import me.kjs.mall.review.ReviewService;
import me.kjs.mall.security.JwtTokenProvider;
import me.kjs.mall.story.StoryService;
import me.kjs.mall.system.SystemService;
import me.kjs.mall.system.seller.SellerRepository;
import me.kjs.mall.system.version.VersionRepository;
import me.kjs.mall.wish.WishQueryRepository;
import me.kjs.mall.wish.WishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@Transactional
@Disabled
public class BaseTest {
    protected final String SUCCESS_MESSAGE = "요청이 성공적으로 수행되었습니다.";
    @Autowired
    protected MainBannerRepository mainBannerRepository;
    @Autowired
    protected MainBannerService mainBannerService;

    @Autowired
    protected SignService signService;
    @Autowired
    protected PointRepository pointRepository;
    @Autowired
    protected PointSpecificQueryRepository pointSpecificQueryRepository;
    @Autowired
    protected IssueCouponRepository issueCouponRepository;
    @Autowired
    protected EntityManager em;
    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected BestReviewService bestReviewService;
    @Autowired
    protected BestReviewProductRepository bestReviewProductRepository;
    @Autowired
    protected BestSellerRepository bestSellerRepository;
    @Autowired
    protected PartialService partialService;
    @Autowired
    protected PointService pointService;

    @Autowired
    protected QnaRepository qnaRepository;
    @Autowired
    protected QnaService qnaService;
    @Autowired
    protected ReviewService reviewService;

    @Autowired
    protected CouponService couponService;

    @Autowired
    protected NoticeService noticeService;
    @Autowired
    protected StoryService storyService;
    @Autowired
    protected EventService eventService;

    @Autowired
    protected OrderExchangeService orderExchangeService;

    @Autowired
    protected TestService testService;
    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderSpecificRepository orderSpecificRepository;
    @Autowired
    protected OrderQueryRepository orderQueryRepository;

    @Autowired
    protected OrderService orderService;
    @Autowired
    protected SystemService systemService;
    @Autowired
    protected OrderSheetService orderSheetService;
    @Autowired
    protected DestinationRepository destinationRepository;
    @Autowired
    protected DestinationService destinationService;
    @Autowired
    protected WishService wishService;
    @Autowired
    protected WishQueryRepository wishQueryRepository;
    @Autowired
    protected CartRepository cartRepository;
    @Autowired
    protected CartService cartService;
    @Autowired
    protected CategoryService categoryService;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected SignService SIgnService;
    @Autowired
    protected VersionRepository versionRepository;
    @Autowired
    protected SellerRepository sellerRepository;
    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected MemberService memberService;
    @Autowired
    protected SecurityProperties securityProperties;
    @Autowired
    protected CertService certService;
    //@Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MemberQueryRepository memberQueryRepository;
    @Autowired
    protected AccountGroupRepository accountGroupRepository;
    @Autowired
    protected AccountGroupService accountGroupService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    protected BaseProductRepository baseProductRepository;
    @Autowired
    protected BaseProductService baseProductService;
    @Autowired
    protected JwtTokenProvider jwtTokenProvider;
    @Autowired
    private WebApplicationContext ctx;
    protected RestDocumentationResultHandler restDocs;
    @Autowired
    protected ProductQueryRepository productQueryRepository;

    /*
        @Autowired
        private TestDataRunnerInTest testDataRunnerInTest;*/
    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.restDocs = document(
                "{class-name}/{method-name}",
                preprocessResponse(prettyPrint())
        );
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .apply(springSecurity())
                .alwaysDo(print())
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris().host("192.168.0.251").port(15080), prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();

        /*testDataRunnerInTest.setUp();*/
    }

    protected TokenDto getUserTokenDto() {
        return getTokenDto("user001");
    }

    protected TokenDto getTokenDto() {
        Member admin = memberQueryRepository.findAdmin();
        return getTokenDto(admin);
    }

    protected TokenDto getTokenDto(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return getTokenDto(member);
    }

    protected TokenDto getTokenDto(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return getTokenDto(member);
    }

    private TokenDto getTokenDto(Member member) {
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getAccountRoleNames());
        return TokenDto.tokenAndMemberToTokenDto(token, jwtTokenProvider.getSecurityProperties().getTokenValidSecond(), member);
    }


    protected BaseProduct generatorBaseProductTemp() {
        List<String> tags = Arrays.asList("태그1", "태그2", "태그3");

        ProductDeliveryDto productDeliveryDto = ProductDeliveryDto.builder()
                .returnLocation("RETURN LOCATION")
                .feeCondition(0)
                .fee(0)
                .deliveryType(DeliveryType.FREE)
                .deliveryYn(YnType.Y)
                .bundleYn(YnType.Y)
                .build();

        List<String> images = new ArrayList<>();
        images.add("/image/image001.png");
        images.add("/image/image002.png");
        images.add("/image/image003.png");
        images.add("/image/image004.png");
        images.add("/image/image005.png");

        BaseProductSaveDto baseProductSaveDto = BaseProductSaveDto.builder()
                .tags(tags)
                .productDelivery(productDeliveryDto)
                .name("PRODUCT NAME")
                .code("A000001")
                .contents(images.toString())
                .thumbnail(Arrays.asList("/image/thumbnail1.png", "/image/thumbnail2.png"))
                .build();

        BaseProduct baseProduct = BaseProduct.createBaseProduct(baseProductSaveDto);
        return baseProductRepository.save(baseProduct);
    }
}
