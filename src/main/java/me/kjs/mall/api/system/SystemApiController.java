package me.kjs.mall.api.system;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.system.GlobalSettingValue;
import me.kjs.mall.system.SystemService;
import me.kjs.mall.system.dto.AppInitValueDto;
import me.kjs.mall.system.dto.SellerUpdateDto;
import me.kjs.mall.system.dto.VersionCreateDto;
import me.kjs.mall.system.dto.VersionUpdateDto;
import me.kjs.mall.system.seller.GlobalSeller;
import me.kjs.mall.system.version.GlobalVersion;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/global")
@Slf4j
public class SystemApiController {

    private final SystemService systemService;


    @GetMapping("/{packageName}")
    public ResponseDto findVersion(@PathVariable("packageName") String packageName) {
        AppInitValueDto body = GlobalSettingValue.getGlobalValue(packageName);

        return ResponseDto.ok(body);
    }

    @GetMapping("/versions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDto queryVersions() {

        List<GlobalVersion> globalVersions = GlobalSettingValue.getGlobalVersions();

        return ResponseDto.ok(globalVersions);
    }


    @PostMapping("/versions")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDto createVersion(@RequestBody @Validated VersionCreateDto versionCreateDto) {
        systemService.createVersion(versionCreateDto);
        systemService.init();
        GlobalVersion globalVersion = GlobalSettingValue.getVersion(versionCreateDto.getPackageName());

        return ResponseDto.created(globalVersion);
    }

    @PutMapping("/versions/{packageName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDto updateVersion(@RequestBody VersionUpdateDto versionUpdateDto, @PathVariable("packageName") String packageName) {
        systemService.updateVersion(versionUpdateDto, packageName);
        systemService.init();
        return ResponseDto.noContent();
    }

    @GetMapping("/seller")
    public ResponseDto querySellerInfo() {
        GlobalSeller seller = GlobalSettingValue.getSeller();
        return ResponseDto.ok(seller);
    }

    @PutMapping("/seller")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDto updateSellerInfo(@RequestBody @Validated SellerUpdateDto sellerUpdateDto) {
        systemService.updateSeller(sellerUpdateDto);
        systemService.init();
        return ResponseDto.noContent();
    }


}
