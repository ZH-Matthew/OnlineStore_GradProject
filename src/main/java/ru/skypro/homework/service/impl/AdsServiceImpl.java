package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.GetAuthentication;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import java.util.List;
import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;
    @Override
    public AdsDto getAllAds() {
        List<Ad> ads = adRepository.findAll();
        return adMapper.adListToAds(ads);
    }

    @Override
    public AdDto addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, Authentication authentication) {
        Ad ad = adMapper.createOrUpdateAdToAd(createOrUpdateAd);
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());

        ad.setAuthor(user);
        ad.setImage(imageService.uploadImage(image));
        adRepository.save(ad);

        return adMapper.adToAdDto(ad);
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
