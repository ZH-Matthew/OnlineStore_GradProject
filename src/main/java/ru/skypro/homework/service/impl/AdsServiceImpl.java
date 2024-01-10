package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;

@Service
public class AdsServiceImpl implements AdsService {
    @Override
    public AdsDto getAllAds() {
        return null;
    }

    @Override
    public AdDto addAd(CreateOrUpdateAd createOrUpdateAdDTO, MultipartFile image, Authentication authentication) {
        return null;
    }

    @Override
    public ExtendedAd getAd(long id) {
        return null;
    }

    @Override
    public void deleteAd(long id, Authentication authentication) {

    }

    @Override
    public AdDto updateAd(long id, CreateOrUpdateAd createOrUpdateAd, Authentication authentication) {
        return null;
    }

    @Override
    public AdsDto getAdsMe(Authentication authentication) {
        return null;
    }
}
