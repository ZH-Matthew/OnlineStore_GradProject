package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdMapper {
    public AdDto adToAdDto(Ad ad) {
        AdDto adDto = new AdDto();
        adDto.setAuthor(ad.getAuthor().getId());
        adDto.setImage(ad.getImage().getFilePath());
        adDto.setPk(ad.getId());
        adDto.setPrice(ad.getPrice());
        adDto.setTitle(ad.getTitle());
        return adDto;
    }

    public Ad adDtoToAd(AdDto adDto, User user) {
        Ad ad = new Ad();
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setAuthor(user);
        return ad;
    }

    public AdsDto adListToAds(List<Ad> list) { //без обратного метода
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(list.size());
        List<AdDto> adDtoList = new ArrayList<>();
        for (Ad ad : list) {
            adDtoList.add(adToAdDto(ad));
        }
        adsDto.setResults(adDtoList);
        return adsDto;
    }

    public ExtendedAd toExtendedAd(Ad ad) { //без обратного метода
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(ad.getId());
        extendedAd.setAuthorFirstName(ad.getAuthor().getFirstName());
        extendedAd.setAuthorLastName(ad.getAuthor().getLastName());
        extendedAd.setDescription(ad.getDescription());
        extendedAd.setEmail(ad.getAuthor().getEmail());
        extendedAd.setImage(ad.getImage().getFilePath());
        extendedAd.setPhone(ad.getAuthor().getPhone());
        extendedAd.setPrice(ad.getPrice());
        extendedAd.setTitle(ad.getTitle());
        return extendedAd;
    }

    public Ad createOrUpdateAdToAd(CreateOrUpdateAd createOrUpdateAd){
        Ad ad = new Ad();
        ad.setDescription(createOrUpdateAd.getDescription());
        ad.setPrice(createOrUpdateAd.getPrice());
        ad.setTitle(createOrUpdateAd.getTitle());
        return ad;
    }

    public CreateOrUpdateAd adToCreateOrUpdateAd(Ad ad){
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setDescription(ad.getDescription());
        createOrUpdateAd.setTitle(ad.getTitle());
        createOrUpdateAd.setPrice(ad.getPrice());
        return createOrUpdateAd;
    }
}
