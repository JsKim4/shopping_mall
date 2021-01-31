package me.kjs.mall.banner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MainBannerService {

    private final MainBannerRepository mainBannerRepository;

    @Transactional
    public MainBanner createMainBanner(MainBannerSaveDto mainBannerSaveDto) {
        MainBanner mainBanner = MainBanner.createMainBanner(mainBannerSaveDto);
        return mainBannerRepository.save(mainBanner);
    }

    @Transactional
    public void deleteMainBanner(Long bannerId) {
        mainBannerRepository.deleteById(bannerId);
    }
}
