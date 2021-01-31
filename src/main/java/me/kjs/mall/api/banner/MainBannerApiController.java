package me.kjs.mall.api.banner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.banner.*;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.CommonSearchCondition;
import me.kjs.mall.common.dto.ResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banners")
@Slf4j
public class MainBannerApiController {

    private final MainBannerService mainBannerService;

    private final MainBannerQueryRepository mainBannerQueryRepository;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_BANNER')")
    public ResponseDto createMainBanner(@RequestBody @Validated MainBannerSaveDto mainBannerSaveDto, Errors errors) {
        hasErrorsThrow(errors);
        MainBanner mainBanner = mainBannerService.createMainBanner(mainBannerSaveDto);
        MainBannerDto mainBannerDto = MainBannerDto.mainBannerToDto(mainBanner);
        return ResponseDto.created(mainBannerDto);
    }

    @DeleteMapping("/{mainBannerId}")
    @PreAuthorize("hasRole('ROLE_BANNER')")
    public ResponseDto deleteMainBanner(@PathVariable("mainBannerId") Long mainBannerId) {
        mainBannerService.deleteMainBanner(mainBannerId);
        return ResponseDto.noContent();
    }

    @GetMapping
    public ResponseDto queryMainBanner(CommonSearchCondition commonSearchCondition) {
        CommonPage<MainBanner> mainBannerCommonPage = mainBannerQueryRepository.findMainBannerBySearchCondition(commonSearchCondition);
        CommonPage body = mainBannerCommonPage.updateContent(mainBannerCommonPage.getContents().stream().map(MainBannerDto::mainBannerToDto).collect(Collectors.toList()));
        return ResponseDto.ok(body);
    }

}
