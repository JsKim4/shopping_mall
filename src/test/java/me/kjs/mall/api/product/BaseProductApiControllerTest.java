package me.kjs.mall.api.product;

import me.kjs.mall.api.documentation.util.DocumentLinkGenerator;
import me.kjs.mall.category.Category;
import me.kjs.mall.category.CategoryProduct;
import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.OnlyIdsDto;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.account.AccountGroupSaveDto;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.part.AccountGroup;
import me.kjs.mall.member.type.AccountRole;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.base.ProductProvisionType;
import me.kjs.mall.product.base.dto.BaseProductSaveDto;
import me.kjs.mall.product.base.dto.BaseProductSearchCondition;
import me.kjs.mall.product.base.dto.ImageAndOrder;
import me.kjs.mall.product.base.dto.ProvisionNoticeSaveDto;
import me.kjs.mall.product.dto.ProductDeliveryDto;
import me.kjs.mall.product.type.DeliveryType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BaseProductApiControllerTest extends BaseTest {


    @Test
    @DisplayName("/api/products/base")
    void createProductsBase() throws Exception {
        List<String> tags = Arrays.asList("tag1", "tag2", "tag3");

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
                .originPrice(30000)
                .contents(images.toString())
                .thumbnail(Arrays.asList("/image/thumbnail1.png", "/image/thumbnail2.png"))
                .build();

        TokenDto tokenDto = getTokenDto();
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/products/base")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .content(objectMapper.writeValueAsString(baseProductSaveDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data.baseProductId").exists())
                .andExpect(jsonPath("data.name").value(baseProductSaveDto.getName()))
                .andExpect(jsonPath("data.code").value(baseProductSaveDto.getCode()))
                .andExpect(jsonPath("data.status").value(CommonStatus.CREATED.name()))
                .andExpect(jsonPath("data.contents").exists())
                .andExpect(jsonPath("data.tags").isArray())
                .andExpect(jsonPath("data.productDelivery.deliveryYn").value(productDeliveryDto.getDeliveryYn().name()))
                .andExpect(jsonPath("data.productDelivery.bundleYn").value(productDeliveryDto.getBundleYn().name()))
                .andExpect(jsonPath("data.productDelivery.deliveryType").value(DeliveryType.FREE.name()))
                .andExpect(jsonPath("data.productDelivery.returnLocation").value(productDeliveryDto.getReturnLocation()))
                .andExpect(jsonPath("data.productDelivery.fee").value(0))
                .andExpect(jsonPath("data.productDelivery.feeCondition").value(0))
                .andDo(document(
                        "create-base-product",
                        requestFields(
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("code").description("상품 코드"),
                                fieldWithPath("contents").description("상품 상세 정보 html"),
                                fieldWithPath("tags").description("상품 태그 목록"),
                                fieldWithPath("thumbnail").description("상품 썸네일 이미지"),
                                fieldWithPath("originPrice").description("소비자 판매 가격"),
                                fieldWithPath("productDelivery.deliveryYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "배송 가능 여부")).optional(),
                                fieldWithPath("productDelivery.bundleYn").description("묶음 배송 가능 여부").optional().type(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE)),
                                fieldWithPath("productDelivery.deliveryType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DELIVERY_TYPE, "배송 타입")),
                                fieldWithPath("productDelivery.returnLocation").description("반품 배송지").optional(),
                                fieldWithPath("productDelivery.fee").description("배송비").optional(),
                                fieldWithPath("productDelivery.feeCondition").description("조건부 무료 최소 배송비").optional()
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 201),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.baseProductId").description("기본 상품 정보 고유 번호"),
                                fieldWithPath("data.name").description("상품 이름"),
                                fieldWithPath("data.code").description("상품 코드"),
                                fieldWithPath("data.status").description("상품 상태"),
                                fieldWithPath("data.originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data.contents").description("상품 상세 정보 html"),
                                fieldWithPath("data.tags").description("상품 태그 목록"),
                                fieldWithPath("data.thumbnail").description("상품 썸네일 이미지"),
                                fieldWithPath("data.productDelivery.deliveryYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "배송 가능 여부")),
                                fieldWithPath("data.productDelivery.bundleYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "묶음 배송 가능 여부")),
                                fieldWithPath("data.productDelivery.deliveryType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DELIVERY_TYPE, "배송 타입")),
                                fieldWithPath("data.productDelivery.returnLocation").description("반품 배송지"),
                                fieldWithPath("data.productDelivery.fee").description("배송비").optional(),
                                fieldWithPath("data.productDelivery.feeCondition").description("조건부 무료 최소 배송비"),
                                fieldWithPath("data.provisionNotice").description("상품정보 제공고시"),
                                fieldWithPath("data.provisionNotice.provisionType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PRODUCT_PROVISION_TYPE)),
                                fieldWithPath("data.provisionNotice.foodType").description("식품의 유형"),
                                fieldWithPath("data.provisionNotice.manufacturer").description("제조업소"),
                                fieldWithPath("data.provisionNotice.location").description("소재지"),
                                fieldWithPath("data.provisionNotice.manufacturingDate").description("제조년월일"),
                                fieldWithPath("data.provisionNotice.shelfLifeDate").description("유통기한"),
                                fieldWithPath("data.provisionNotice.capacityUnit").description("포장단위별 내용물의 용량"),
                                fieldWithPath("data.provisionNotice.quantityUnit").description("포장단위별 수량"),
                                fieldWithPath("data.provisionNotice.rawMaterialContents").description("원재료및 함량"),
                                fieldWithPath("data.provisionNotice.nutritionInfo").description("영양 정보"),
                                fieldWithPath("data.provisionNotice.functionInfo").description("기능 정보"),
                                fieldWithPath("data.provisionNotice.intakeNotice").description("섭취상 주의사항"),
                                fieldWithPath("data.provisionNotice.noMedicineGuidance").description("질병의 예방 및 치료를 위한 의약품이 아니라는 문구"),
                                fieldWithPath("data.provisionNotice.certificationAssociation").description("인증 기관"),
                                fieldWithPath("data.provisionNotice.certificationCode").description("인증 코드"),
                                fieldWithPath("data.provisionNotice.gmoYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "유전자 변형 식품 여부")),
                                fieldWithPath("data.provisionNotice.importYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "수입 여부")),
                                fieldWithPath("data.provisionNotice.csTel").description("소비자 상담 관련 전화번호")
                        )
                ));
    }


    @Test
    @DisplayName("/api/products/base 이미 있는 상품 코드")
    void createBaseProductFailByAlreadyCode() throws Exception {
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

        BaseProduct baseProduct = baseProductService.createBaseProduct(baseProductSaveDto);
        TokenDto tokenDto = getTokenDto();

        int expectedStatus = 4016;
        String expectedMessage = "이미 존재하는 상품 코드입니다.";

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/products/base")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .content(objectMapper.writeValueAsString(baseProductSaveDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(expectedStatus))
                .andExpect(jsonPath("message").value(expectedMessage))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "create-base-product-fail-already-code"));
    }


    @Test
    @DisplayName("/api/products/base/{baseProductId} 기본 상품 정보 수정")
    void updateBaseProductsBase() throws Exception {


        BaseProduct baseProduct = generatorBaseProduct();

        ProductDeliveryDto updateDeliveryDto = ProductDeliveryDto.builder()
                .returnLocation("RETURN LOCATION2")
                .feeCondition(60000)
                .fee(2500)
                .deliveryType(DeliveryType.CONDITION)
                .deliveryYn(YnType.Y)
                .bundleYn(YnType.Y)
                .build();


        BaseProductSaveDto updateDto = BaseProductSaveDto.builder()
                .productDelivery(updateDeliveryDto)
                .code("A000002")
                .name("PRODUCT NAME2")
                .thumbnail(Arrays.asList("/image/thumbnail1.png", "/image/thumbnail2.png"))
                .contents(baseProduct.getContent())
                .tags(baseProduct.getProductTags())
                .originPrice(30000)
                .build();
        TokenDto tokenDto = getTokenDto();

        Long baseProductId = baseProduct.getId();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/products/base/{baseProductId}", baseProductId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .content(objectMapper.writeValueAsString(updateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-base-product",
                        pathParameters(
                                parameterWithName("baseProductId").description("기본 상품 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("code").description("상품 코드"),
                                fieldWithPath("contents").description("상품 상세 정보 html"),
                                fieldWithPath("tags").description("상품 태그 목록").optional(),
                                fieldWithPath("thumbnail").description("상품 썸네일 이미지"),
                                fieldWithPath("originPrice").description("소비자 판매 가격"),
                                fieldWithPath("productDelivery.deliveryYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "배송 가능 여부")).optional(),
                                fieldWithPath("productDelivery.bundleYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "묶음 배송 가능 여부")).optional(),
                                fieldWithPath("productDelivery.deliveryType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DELIVERY_TYPE, "배송 타입")),
                                fieldWithPath("productDelivery.returnLocation").description("반품 배송지").optional(),
                                fieldWithPath("productDelivery.fee").description("배송비").optional(),
                                fieldWithPath("productDelivery.feeCondition").description("조건부 무료 최소 배송비").optional()
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 204),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null").optional()
                        )
                ));

        BaseProduct afterBaseProduct = baseProductRepository.findById(baseProduct.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(afterBaseProduct.getCode(), updateDto.getCode());
        assertEquals(afterBaseProduct.getName(), updateDto.getName());
        for (ImageAndOrder imageAndOrder : afterBaseProduct.getThumbnailImage()) {
            assertTrue(updateDto.getThumbnail().contains(imageAndOrder.getImage()));
        }
        assertEquals(afterBaseProduct.getId(), baseProductId);

    }


    @Test
    @DisplayName("/api/products/base/provision/{baseProductId} 기본 상품 제공고시 정보 수정")
    void updateBaseProductProvision() throws Exception {


        BaseProduct baseProduct = generatorBaseProduct();

        TokenDto tokenDto = getTokenDto();

        Long baseProductId = baseProduct.getId();

        ProvisionNoticeSaveDto update = ProvisionNoticeSaveDto.builder()
                .capacityUnit("15g")
                .provisionType(ProductProvisionType.HEALTHY_FOOD)
                .foodType("foodType/Lutein")
                .manufacturer("korean")
                .location("location")
                .manufacturingDate("2021-01-24")
                .shelfLifeDate("after 1 years")
                .quantityUnit("30mg x 50amount")
                .rawMaterialContents("dasdasdasd/asdasdasd/asdasda/asdasd")
                .nutritionInfo("nutrition Info")
                .functionInfo("function Info")
                .intakeNotice("intake Info")
                .noMedicineGuidance("no MedicineGuidance")
                .certificationAssociation("certificationAssociation")
                .certificationCode("156445312")
                .gmoYn(YnType.N)
                .importYn(YnType.N)
                .csTel("1599-1234")
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/products/base/provision/{baseProductId}", baseProductId)
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .content(objectMapper.writeValueAsString(update))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-base-product-provision",
                        pathParameters(
                                parameterWithName("baseProductId").description("기본 상품 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("provisionType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PRODUCT_PROVISION_TYPE)),
                                fieldWithPath("foodType").description("식품의 유형"),
                                fieldWithPath("manufacturer").description("제조업소"),
                                fieldWithPath("location").description("소재지"),
                                fieldWithPath("manufacturingDate").description("제조년월일"),
                                fieldWithPath("shelfLifeDate").description("유통기한"),
                                fieldWithPath("capacityUnit").description("포장단위별 내용물의 용량"),
                                fieldWithPath("quantityUnit").description("포장단위별 수량"),
                                fieldWithPath("rawMaterialContents").description("원재료및 함량"),
                                fieldWithPath("nutritionInfo").description("영양 정보"),
                                fieldWithPath("functionInfo").description("기능 정보"),
                                fieldWithPath("intakeNotice").description("섭취상 주의사항"),
                                fieldWithPath("noMedicineGuidance").description("질병의 예방 및 치료를 위한 의약품이 아니라는 문구"),
                                fieldWithPath("certificationAssociation").description("인증 기관"),
                                fieldWithPath("certificationCode").description("인증 코드"),
                                fieldWithPath("gmoYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "유전자 변형 식품 여부")),
                                fieldWithPath("importYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "수입 여부")),
                                fieldWithPath("csTel").description("소비자 상담 관련 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 204),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data").description("null").optional()
                        )
                ));

        BaseProduct afterBaseProduct = baseProductRepository.findById(baseProduct.getId()).orElseThrow(EntityNotFoundException::new);


    }


    @Test
    @DisplayName("/api/products/base/used/{baseProductId} 사용 상태로 변경")
    void useBaseProduct() throws Exception {

        BaseProduct baseProduct = generatorBaseProduct();

        TokenDto tokenDto = getTokenDto();

        Long baseProductId = baseProduct.getId();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/products/base/used/{baseProductId}", baseProductId)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-base-product-used",
                        pathParameters(
                                parameterWithName("baseProductId").description("기본 상품 고유 번호")
                        )
                ));

        BaseProduct afterBaseProduct = baseProductRepository.findById(baseProduct.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(afterBaseProduct.getStatus(), CommonStatus.USED);
    }

    @Test
    @DisplayName("/api/products/base/unused/{baseProductId} 사용 중지 상태로 변경")
    void unusedBaseProduct() throws Exception {

        BaseProduct baseProduct = generatorBaseProduct();

        TokenDto tokenDto = getTokenDto();

        Long baseProductId = baseProduct.getId();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/products/base/unused/{baseProductId}", baseProductId)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "update-base-product-unused",
                        pathParameters(
                                parameterWithName("baseProductId").description("기본 상품 고유 번호")
                        )
                ));

        BaseProduct afterBaseProduct = baseProductRepository.findById(baseProduct.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(afterBaseProduct.getStatus(), CommonStatus.UN_USED);
    }

    @Test
    @DisplayName("/api/products/base/{baseProductId} 상품 삭제")
    void deleteBaseProduct() throws Exception {

        BaseProduct baseProduct = generatorBaseProduct();

        TokenDto tokenDto = getTokenDto();

        Long baseProductId = baseProduct.getId();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/products/base/{baseProductId}", baseProductId)
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(document(
                        "delete-base-product",
                        pathParameters(
                                parameterWithName("baseProductId").description("기본 상품 고유 번호")
                        )
                ));

        BaseProduct afterBaseProduct = baseProductRepository.findById(baseProduct.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(afterBaseProduct.getStatus(), CommonStatus.DELETED);
    }


    @Test
    @DisplayName("/api/products/base 상품 조회")
    void findBaseProduct() throws Exception {
        List<Category> allCategoriesFetch = categoryService.findAllCategoriesFetch();
        Category category = allCategoriesFetch.get(0);
        BaseProduct baseProductB = generatorBaseProduct("B00000001");
        BaseProduct baseProductC = generatorBaseProduct("C00000001");
        BaseProduct baseProductD = generatorBaseProduct("D00000001");

        baseProductService.useBaseProduct(baseProductB.getId());
        baseProductService.useBaseProduct(baseProductC.getId());
        baseProductB.addCategory(category);
        baseProductC.addCategory(category);
        AccountGroupSaveDto accountGroupSaveDto = AccountGroupSaveDto.builder()
                .accountRoles(Arrays.asList(AccountRole.USER, AccountRole.PRODUCT))
                .alias("PRODUCT_MANAGER")
                .name("PRODUCT_MANAGER")
                .build();

        AccountGroup accountGroup = accountGroupService.createAccountGroup(accountGroupSaveDto);


        TokenDto tokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);
        accountGroupService.updateMemberGroup(member.getId(), accountGroup.getId());


        List<Category> categories = categoryService.findAllCategoriesFetch();

        BaseProductSearchCondition productSearchCondition = BaseProductSearchCondition.builder()
                .categoryId(categories.get(0).getId())
                .code("00000")
                .contents(5)
                .keyword("PRODUCT")
                .minPrice(0)
                .maxPrice(0)
                .page(0)
                .status(CommonStatus.USED)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/products/base")
                .header("X-AUTH-TOKEN", tokenDto.getToken())
                .param("categoryId", String.valueOf(productSearchCondition.getCategoryId()))
                .param("code", String.valueOf(productSearchCondition.getCode()))
                .param("contents", String.valueOf(productSearchCondition.getContents()))
                .param("keyword", String.valueOf(productSearchCondition.getKeyword()))
                .param("minPrice", String.valueOf(productSearchCondition.getMinPrice()))
                .param("maxPrice", String.valueOf(productSearchCondition.getMaxPrice()))
                .param("page", String.valueOf(productSearchCondition.getPage()))
                .param("status", String.valueOf(productSearchCondition.getStatus())))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data.contentCount").exists())
                .andExpect(jsonPath("data.totalCount").exists())
                .andExpect(jsonPath("data.nowPage").exists())
                .andExpect(jsonPath("data.maxPage").exists())
                .andExpect(jsonPath("data.contents[*].baseProductId").exists())
                .andExpect(jsonPath("data.contents[*].name").exists())
                .andExpect(jsonPath("data.contents[*].code").exists())
                .andExpect(jsonPath("data.contents[*].status").exists())
                .andExpect(jsonPath("data.contents[*].contents").isArray())
                .andExpect(jsonPath("data.contents[*].tags").isArray())
                .andExpect(jsonPath("data.contents[*].thumbnail").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.deliveryYn").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.bundleYn").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.deliveryType").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.returnLocation").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.fee").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.feeCondition").exists())
                .andDo(document(
                        "query-base-product",
                        requestParameters(
                                parameterWithName("code").description("상품 코드"),
                                parameterWithName("contents").description("가져올 개수"),
                                parameterWithName("minPrice").description("최소 금액"),
                                parameterWithName("maxPrice").description("최대 금액"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("keyword").description("상품 이름"),
                                parameterWithName("categoryId").description("카테고리 고유 번호"),
                                parameterWithName("status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS))
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("현재 이벤트 개수"),
                                fieldWithPath("data.totalCount").description("전체 이벤트 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[*].baseProductId").description("기본 상품 정보 고유 번호"),
                                fieldWithPath("data.contents[*].name").description("상품 이름"),
                                fieldWithPath("data.contents[*].code").description("상품 코드"),
                                fieldWithPath("data.contents[*].status").description("상품 상태").type(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.contents[*].contents").description("상품 상세 정보 html"),
                                fieldWithPath("data.contents[*].tags").description("상품 태그 목록"),
                                fieldWithPath("data.contents[*].thumbnail").description("상품 썸네일 이미지"),
                                fieldWithPath("data.contents[*].originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data.contents[*].productDelivery.deliveryYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "배송 가능 여부")),
                                fieldWithPath("data.contents[*].productDelivery.bundleYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "묶음 배송 가능 여부")),
                                fieldWithPath("data.contents[*].productDelivery.deliveryType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DELIVERY_TYPE, "배송 타입")),
                                fieldWithPath("data.contents[*].productDelivery.returnLocation").description("반품 배송지"),
                                fieldWithPath("data.contents[*].productDelivery.fee").description("배송비").optional(),
                                fieldWithPath("data.contents[*].productDelivery.feeCondition").description("조건부 무료 최소 배송비"),
                                fieldWithPath("data.contents[*].provisionNotice").description("상품정보 제공고시"),
                                fieldWithPath("data.contents[*].provisionNotice.provisionType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PRODUCT_PROVISION_TYPE)),
                                fieldWithPath("data.contents[*].provisionNotice.foodType").description("식품의 유형"),
                                fieldWithPath("data.contents[*].provisionNotice.manufacturer").description("제조업소"),
                                fieldWithPath("data.contents[*].provisionNotice.location").description("소재지"),
                                fieldWithPath("data.contents[*].provisionNotice.manufacturingDate").description("제조년월일"),
                                fieldWithPath("data.contents[*].provisionNotice.shelfLifeDate").description("유통기한"),
                                fieldWithPath("data.contents[*].provisionNotice.capacityUnit").description("포장단위별 내용물의 용량"),
                                fieldWithPath("data.contents[*].provisionNotice.quantityUnit").description("포장단위별 수량"),
                                fieldWithPath("data.contents[*].provisionNotice.rawMaterialContents").description("원재료및 함량"),
                                fieldWithPath("data.contents[*].provisionNotice.nutritionInfo").description("영양 정보"),
                                fieldWithPath("data.contents[*].provisionNotice.functionInfo").description("기능 정보"),
                                fieldWithPath("data.contents[*].provisionNotice.intakeNotice").description("섭취상 주의사항"),
                                fieldWithPath("data.contents[*].provisionNotice.noMedicineGuidance").description("질병의 예방 및 치료를 위한 의약품이 아니라는 문구"),
                                fieldWithPath("data.contents[*].provisionNotice.certificationAssociation").description("인증 기관"),
                                fieldWithPath("data.contents[*].provisionNotice.certificationCode").description("인증 코드"),
                                fieldWithPath("data.contents[*].provisionNotice.gmoYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "유전자 변형 식품 여부")),
                                fieldWithPath("data.contents[*].provisionNotice.importYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "수입 여부")),
                                fieldWithPath("data.contents[*].provisionNotice.csTel").description("소비자 상담 관련 전화번호")
                        )
                ));
    }


    @Test
    @DisplayName("/api/products/base 상품 조회 상품 권한만 가질 경우 used인 상품만 표시되는 테스트")
    void findBaseProductInOnlyProductRoles() throws Exception {

        List<Category> allCategoriesFetch = categoryService.findAllCategoriesFetch();
        Category category = allCategoriesFetch.get(0);
        BaseProduct baseProductB = generatorBaseProduct("B00000001");
        BaseProduct baseProductC = generatorBaseProduct("C00000001");
        BaseProduct baseProductD = generatorBaseProduct("D00000001");

        baseProductService.useBaseProduct(baseProductB.getId());
        baseProductService.useBaseProduct(baseProductC.getId());
        baseProductB.addCategory(category);
        baseProductC.addCategory(category);
        AccountGroupSaveDto accountGroupSaveDto = AccountGroupSaveDto.builder()
                .accountRoles(Arrays.asList(AccountRole.USER, AccountRole.PRODUCT))
                .alias("PRODUCT_MANAGER")
                .name("PRODUCT_MANAGER")
                .build();

        AccountGroup accountGroup = accountGroupService.createAccountGroup(accountGroupSaveDto);


        TokenDto tokenDto = getUserTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(EntityNotFoundException::new);

        accountGroupService.updateMemberGroup(member.getId(), accountGroup.getId());


        List<Category> categories = categoryService.findAllCategoriesFetch();

        BaseProductSearchCondition productSearchCondition = BaseProductSearchCondition.builder()
                .categoryId(categories.get(0).getId())
                .code("00000")
                .contents(5)
                .keyword("PRODUCT")
                .minPrice(0)
                .maxPrice(0)
                .page(0)
                .status(CommonStatus.USED)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/products/base")
                .param("categoryId", String.valueOf(productSearchCondition.getCategoryId()))
                .param("code", String.valueOf(productSearchCondition.getCode()))
                .param("contents", String.valueOf(productSearchCondition.getContents()))
                .param("keyword", String.valueOf(productSearchCondition.getKeyword()))
                .param("minPrice", String.valueOf(productSearchCondition.getMinPrice()))
                .param("maxPrice", String.valueOf(productSearchCondition.getMaxPrice()))
                .param("page", String.valueOf(productSearchCondition.getPage()))
                .param("status", String.valueOf(productSearchCondition.getStatus()))
                .header("X-AUTH-TOKEN", tokenDto.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data").exists())
                .andExpect(jsonPath("data.contentCount").exists())
                .andExpect(jsonPath("data.totalCount").exists())
                .andExpect(jsonPath("data.nowPage").exists())
                .andExpect(jsonPath("data.maxPage").exists())
                .andExpect(jsonPath("data.contents[*].baseProductId").exists())
                .andExpect(jsonPath("data.contents[*].name").exists())
                .andExpect(jsonPath("data.contents[*].code").exists())
                .andExpect(jsonPath("data.contents[*].status").exists())
                .andExpect(jsonPath("data.contents[*].contents").exists())
                .andExpect(jsonPath("data.contents[*].tags").isArray())
                .andExpect(jsonPath("data.contents[*].thumbnail").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.deliveryYn").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.bundleYn").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.deliveryType").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.returnLocation").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.fee").exists())
                .andExpect(jsonPath("data.contents[*].productDelivery.feeCondition").exists())
                .andDo(document(
                        "query-base-product-only-product-roles",
                        requestParameters(
                                parameterWithName("code").description("상품 코드"),
                                parameterWithName("contents").description("가져올 개수"),
                                parameterWithName("minPrice").description("최소 금액"),
                                parameterWithName("maxPrice").description("최대 금액"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("keyword").description("상품 이름"),
                                parameterWithName("categoryId").description("카테고리 고유 번호"),
                                parameterWithName("status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS))
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data.contentCount").description("현재 이벤트 개수"),
                                fieldWithPath("data.totalCount").description("전체 이벤트 개수"),
                                fieldWithPath("data.nowPage").description("현재 페이지"),
                                fieldWithPath("data.maxPage").description("최대 페이지"),
                                fieldWithPath("data.contents[*].baseProductId").description("기본 상품 정보 고유 번호"),
                                fieldWithPath("data.contents[*].name").description("상품 이름"),
                                fieldWithPath("data.contents[*].code").description("상품 코드"),
                                fieldWithPath("data.contents[*].status").description("상품 상태").type(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS)),
                                fieldWithPath("data.contents[*].contents").description("상품 상세 정보 html"),
                                fieldWithPath("data.contents[*].tags").description("상품 태그 목록"),
                                fieldWithPath("data.contents[*].thumbnail").description("상품 썸네일 이미지"),
                                fieldWithPath("data.contents[*].originPrice").description("상품 소비자 가격"),
                                fieldWithPath("data.contents[*].productDelivery.deliveryYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "배송 가능 여부")),
                                fieldWithPath("data.contents[*].productDelivery.bundleYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "묶음 배송 가능 여부")),
                                fieldWithPath("data.contents[*].productDelivery.deliveryType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.DELIVERY_TYPE, "배송 타입")),
                                fieldWithPath("data.contents[*].productDelivery.returnLocation").description("반품 배송지"),
                                fieldWithPath("data.contents[*].productDelivery.fee").description("배송비").optional(),
                                fieldWithPath("data.contents[*].productDelivery.feeCondition").description("조건부 무료 최소 배송비"),
                                fieldWithPath("data.contents[*].provisionNotice").description("상품정보 제공고시"),
                                fieldWithPath("data.contents[*].provisionNotice.provisionType").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PRODUCT_PROVISION_TYPE)),
                                fieldWithPath("data.contents[*].provisionNotice.foodType").description("식품의 유형"),
                                fieldWithPath("data.contents[*].provisionNotice.manufacturer").description("제조업소"),
                                fieldWithPath("data.contents[*].provisionNotice.location").description("소재지"),
                                fieldWithPath("data.contents[*].provisionNotice.manufacturingDate").description("제조년월일"),
                                fieldWithPath("data.contents[*].provisionNotice.shelfLifeDate").description("유통기한"),
                                fieldWithPath("data.contents[*].provisionNotice.capacityUnit").description("포장단위별 내용물의 용량"),
                                fieldWithPath("data.contents[*].provisionNotice.quantityUnit").description("포장단위별 수량"),
                                fieldWithPath("data.contents[*].provisionNotice.rawMaterialContents").description("원재료및 함량"),
                                fieldWithPath("data.contents[*].provisionNotice.nutritionInfo").description("영양 정보"),
                                fieldWithPath("data.contents[*].provisionNotice.functionInfo").description("기능 정보"),
                                fieldWithPath("data.contents[*].provisionNotice.intakeNotice").description("섭취상 주의사항"),
                                fieldWithPath("data.contents[*].provisionNotice.noMedicineGuidance").description("질병의 예방 및 치료를 위한 의약품이 아니라는 문구"),
                                fieldWithPath("data.contents[*].provisionNotice.certificationAssociation").description("인증 기관"),
                                fieldWithPath("data.contents[*].provisionNotice.certificationCode").description("인증 코드"),
                                fieldWithPath("data.contents[*].provisionNotice.gmoYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "유전자 변형 식품 여부")),
                                fieldWithPath("data.contents[*].provisionNotice.importYn").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.YN_TYPE, "수입 여부")),
                                fieldWithPath("data.contents[*].provisionNotice.csTel").description("소비자 상담 관련 전화번호")
                        )
                ));
    }

    @Test
    @DisplayName("/api/products/base/{baseProductId}/categories/add 기본 상품에 카테고리 추가")
    void addBaseProductCategory() throws Exception {
        List<Category> parentCategories = categoryRepository.findAllByParentCategory(null);
        List<Category> childCategories = parentCategories.get(0).getCategories();
        List<Long> categoryIds = childCategories.stream().map(Category::getId).collect(Collectors.toList());

        OnlyIdsDto onlyIdsDto = new OnlyIdsDto(categoryIds);

        BaseProduct baseProduct = generatorBaseProduct();
        //baseProductService.addCategory(baseProduct.getId(), categoryIds);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/products/base/{baseProductId}/categories/add", baseProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(onlyIdsDto))
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-base-product-add-category",
                        pathParameters(
                                parameterWithName("baseProductId").description("기본 상품 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("ids[]").description("카테고리 고유 번호")
                        )
                ));


        BaseProduct updateBaseProducts = baseProductRepository.findById(baseProduct.getId()).orElse(null);
        List<Category> baseProductCategories = updateBaseProducts.getCategoryProducts().stream().map(CategoryProduct::getCategory).collect(Collectors.toList());

        assertEquals(baseProductCategories.size(), childCategories.size());
        for (Category childCategory : childCategories) {
            assertTrue(baseProductCategories.contains(childCategory));
        }
    }

    @Test
    @DisplayName("/api/products/base/{baseProductId}/categories 기본 상품에 포함된 카테고리 조회")
    void queryBaseProductCategory() throws Exception {
        List<Category> parentCategories = categoryRepository.findAllByParentCategory(null);
        List<Category> childCategories = parentCategories.get(0).getCategories();
        List<Long> categoryIds = childCategories.stream().map(Category::getId).collect(Collectors.toList());
        BaseProduct baseProduct = generatorBaseProduct();
        baseProductService.addCategory(baseProduct.getId(), categoryIds);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/products/base/{baseProductId}/categories", baseProduct.getId())
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andExpect(jsonPath("data[0].categoryId").exists())
                .andExpect(jsonPath("data[0].categoryName").exists())
                .andExpect(jsonPath("data[0].isContain").exists())
                .andExpect(jsonPath("data[0].status").exists())
                .andDo(document("query-base-product-category",
                        pathParameters(
                                parameterWithName("baseProductId").description("기본 상품 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 상태 / " + 200),
                                fieldWithPath("message").description("응답 메시지 / " + SUCCESS_MESSAGE),
                                fieldWithPath("data[].categoryId").description("카테고리 고유 번호"),
                                fieldWithPath("data[].categoryName").description("카테고리 이름"),
                                fieldWithPath("data[].isContain").description("카테고리 포함 여부"),
                                fieldWithPath("data[].status").description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.COMMON_STATUS, "카테고리 상태"))
                        )
                ));
    }

    @Test
    @DisplayName("/api/products/base/{baseProductId}/categories/remove 기본 상품에 카테고리 추가")
    void removeBaseProductCategory() throws Exception {
        List<Category> parentCategories = categoryRepository.findAllByParentCategory(null);
        List<Category> childCategories = parentCategories.get(0).getCategories();
        List<Long> categoryIds = childCategories.stream().map(Category::getId).collect(Collectors.toList());
        BaseProduct baseProduct = generatorBaseProduct();
        baseProductService.addCategory(baseProduct.getId(), categoryIds);

        OnlyIdsDto onlyIdsDto = new OnlyIdsDto(categoryIds);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/products/base/{baseProductId}/categories/remove", baseProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(onlyIdsDto))
                .header("X-AUTH-TOKEN", getTokenDto().getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(204))
                .andExpect(jsonPath("message").value(SUCCESS_MESSAGE))
                .andDo(document("update-base-product-remove-category",
                        pathParameters(
                                parameterWithName("baseProductId").description("기본 상품 고유 번호")
                        ),
                        requestFields(
                                fieldWithPath("ids[]").description("카테고리 고유 번호")
                        )
                ));


        BaseProduct updateBaseProducts = baseProductRepository.findById(baseProduct.getId()).orElse(null);
        List<Category> baseProductCategories = updateBaseProducts.getCategoryProducts().stream().map(CategoryProduct::getCategory).collect(Collectors.toList());

        assertEquals(baseProductCategories.size(), 0);
    }


    private BaseProduct generatorBaseProduct(String code) {
        List<String> tags = Arrays.asList("tag1", "tag2", "tag3");

        List<String> images = new ArrayList<>();
        images.add("/image/image001.png");
        images.add("/image/image002.png");
        images.add("/image/image003.png");
        images.add("/image/image004.png");
        images.add("/image/image005.png");

        ProductDeliveryDto productDeliveryDto = ProductDeliveryDto.builder()
                .returnLocation("RETURN LOCATION")
                .feeCondition(0)
                .fee(0)
                .deliveryType(DeliveryType.FREE)
                .deliveryYn(YnType.Y)
                .bundleYn(YnType.Y)
                .build();

        BaseProductSaveDto baseProductSaveDto = BaseProductSaveDto.builder()
                .tags(tags)
                .productDelivery(productDeliveryDto)
                .name("PRODUCT NAME")
                .code(code)
                .contents(images.toString())
                .thumbnail(Arrays.asList("/image/thumbnail1.png", "/image/thumbnail2.png"))
                .build();
        return baseProductService.createBaseProduct(baseProductSaveDto);
    }


    private BaseProduct generatorBaseProduct() {
        return generatorBaseProduct("A0000001");
    }
}