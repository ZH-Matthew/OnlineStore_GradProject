package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

import java.util.ArrayList;
import java.util.List;


@Mapper(componentModel = "spring")
public interface AdMapper {


//    @Mapping(source = "author.id", target = "author")
//    @Mapping(source = "id", target = "pk")
//    @Mapping(source = "image.filepath", target = "image")
//    AdDto adToDTO(Ad ad);
//
//    @Mapping(target = "id", source = "pk")
//    @Mapping(target = "author.id", source = "author")
//    @Mapping(source = "image.filepath", target = "image")
//    Ad adDtoToModel(AdDto adDto);
//
//    @Mapping(source = "id", target = "pk")
//    @Mapping(source = "user.firstName", target = "authorFirstName")
//    @Mapping(source = "user.lastName", target = "authorFirstName")
//    @Mapping(source = "user.email", target = "email")
//    @Mapping(source = "image.filepath", target = "image")
//    @Mapping(source = "user.phone", target = "phone")
//    ExtendedAd toExtendedAd(Ad ad);

    default AdDto adToDto(Ad ad) {
        AdDto adDto = new AdDto();
        adDto.setAuthor(ad.getUser().getId());
        adDto.setImage(ad.getImage().getFilePath());
        adDto.setPk(ad.getId());
        adDto.setPrice(ad.getPrice());
        adDto.setTitle(ad.getTitle());
        return adDto;
    }

    default Ad adDtoToAd(AdDto adDto, User user) {
        Ad ad = new Ad(adDto.getPrice(), adDto.getTitle(), null, user, null);
        return ad;
    }

    default ExtendedAd toExtendedAd(Ad ad) {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(ad.getId());
        extendedAd.setAuthorFirstName(ad.getUser().getFirstName());
        extendedAd.setAuthorLastName(ad.getUser().getLastName());
        extendedAd.setDescription(ad.getDescription());
        extendedAd.setEmail(ad.getUser().getEmail());
        extendedAd.setImage(ad.getImage().getFilePath());
        extendedAd.setPhone(ad.getUser().getPhone());
        extendedAd.setPrice(ad.getPrice());
        extendedAd.setTitle(ad.getTitle());
        return extendedAd;
    }

    Ad createOrUpdateAdToAd(CreateOrUpdateAd createOrUpdateAd);

    CreateOrUpdateAd adToCreateOrUpdateAd(Ad ad);


    default AdsDto adListToAds(List<Ad> list) {
        AdsDto adsDto = new AdsDto();
        adsDto.setCount(list.size());
        List<AdDto> adDtoList = new ArrayList<>();
        for (Ad ad : list) {
            adDtoList.add(adToDto(ad));
        }
        adsDto.setResults(adDtoList);
        return adsDto;
    }
}
