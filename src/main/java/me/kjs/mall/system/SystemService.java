package me.kjs.mall.system;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.util.CollectionTextUtil;
import me.kjs.mall.system.dto.SellerUpdateDto;
import me.kjs.mall.system.dto.VersionCreateDto;
import me.kjs.mall.system.dto.VersionUpdateDto;
import me.kjs.mall.system.seller.Seller;
import me.kjs.mall.system.seller.SellerRepository;
import me.kjs.mall.system.version.AppVersion;
import me.kjs.mall.system.version.VersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SystemService {

    private final VersionRepository versionRepository;

    private final SellerRepository sellerRepository;

    @Transactional
    public void init() {
        initVersions();
        initSellers();
    }

    private void initSellers() {
        Optional<Seller> seller = sellerRepository.findTop();
        if (!seller.isPresent()) {
            GlobalSettingValue.initGlobalSeller(getDefaultSeller());
        } else {
            GlobalSettingValue.initGlobalSeller(seller.get());
        }
    }

    private Seller getDefaultSeller() {
        Seller seller = Seller.builder()
                .companyName("jskim")
                .corporateRegistrationNumber("000-00-000000")
                .companyLocation("(00000) 서울특별시 중구 황학동")
                .companyType("법인사업자")
                .netSaleReportNumber("0000-경기성남-0000")
                .ceoName("김준섭")
                .forwardingAddress("(우 : 0000) 서울특별시 중구 황학동")
                .returnAddress("(우 : 0000)  서울특별시 중구 황학동")
                .csCenterTel("고객센터 0000-0000")
                .deliveryFee(2500)
                .deliveryCondition(30000)
                .returnFee(3000)
                .companyFax("000-000-000000")
                .build();
        return sellerRepository.save(seller);
    }

    private void initVersions() {
        List<AppVersion> versions = versionRepository.findAll();
        if (CollectionTextUtil.isBlank(versions)) {
            versions.add(getDefaultAndroidVersion());
            versions.add(getDefaultIosVersion());
        }
        GlobalSettingValue.initGlobalVersion(versions);
    }

    private AppVersion getDefaultIosVersion() {
        AppVersion android = AppVersion.builder()
                .latelyVersion("1.0.0")
                .latelyCode("1")
                .minVersion("1.0.0")
                .minCode("1")
                .os("android")
                .packageName("kr.co.kjs-mall")
                .build();
        return versionRepository.save(android);
    }

    private AppVersion getDefaultAndroidVersion() {
        AppVersion ios = AppVersion.builder()
                .latelyVersion("1.0.0")
                .latelyCode("1")
                .minVersion("1.0.0")
                .minCode("1")
                .os("ios")
                .packageName("app.kr.co.kjs-mall.KjsMall")
                .build();
        return versionRepository.save(ios);
    }

    @Transactional
    public AppVersion createVersion(VersionCreateDto versionCreateDto) {
        AppVersion appVersion = AppVersion.createAppVersion(versionCreateDto);

        return versionRepository.save(appVersion);
    }

    @Transactional
    public void updateVersion(VersionUpdateDto versionUpdateDto, String packageName) {
        AppVersion appVersion = versionRepository.findById(packageName).orElseThrow(NoExistIdException::new);
        appVersion.update(versionUpdateDto);
    }

    @Transactional
    public void updateSeller(SellerUpdateDto sellerUpdateDto) {
        Optional<Seller> seller = sellerRepository.findTop();
        if (!seller.isPresent()) {
            getDefaultSeller();
        }
        seller.get().update(sellerUpdateDto);
    }
}
