package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.skypro.homework.config.GetAuthentication;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;

/**
 * <b>Сервис объявлений</b>
 * Хранит основную логику по работе с объявлениями
 */
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;

    /**
     * <b>Метод получения всех объявлений </b> <p>
     * (В виде объекта DTO {@link AdsDto} )
     */
    @Override
    public AdsDto getAllAds() {
        List<Ad> ads = adRepository.findAll();
        return adMapper.adListToAds(ads);
    }

    /**
     * <b>Метод добавления объявления в БД </b> <p>
     * Принцип работы:<p>
     * На основе минимальной информации DTO {@link CreateOrUpdateAd},
     * создать полноценный объект объявления {@link Ad},
     * на основе данных аутентификации создать объект пользователя {@link User},
     * добавить созданного пользователя и картинку из параметра в объект объявления,
     * сохранить получившийся объект с помощью репозитория {@link AdRepository},
     *
     * @param  createOrUpdateAd DTO объявления {@link CreateOrUpdateAd} <p>
     * @param  image картинка товара {@link MultipartFile} <p>
     * @param  authentication переменная аутентификации хранящая данные об авторизованном пользователе {@link Authentication} <p>
     */
    @Override
    public AdDto addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, Authentication authentication) throws IOException {
        Ad ad = adMapper.createOrUpdateAdToAd(createOrUpdateAd);
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        System.out.println(ad.getId());
        ad.setAuthor(user);
        adRepository.save(ad);
        ad.setImage(imageService.uploadImage(ad.getId(), image));
        adRepository.save(ad);
        return adMapper.adToAdDto(ad);
    }

    /**
     * <b>Метод получения объявления по ID </b> <p>
     * Запрошенный {@link Ad} преобразуется в DTO {@link ExtendedAd} и возвращается методом.
     */
    @Override
    public ExtendedAd getAd(long id) {
        return adMapper.toExtendedAd(adRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление с ID = " + id + " не найдено ")));
    }

    /**
     * <b> Метод удаления объявления </b> <p>
     * Метод использует аннотацию {@link Transactional} <p>
     * Принцип работы:<p>
     * 1) По переданному ID находится объявление <p>
     * 2) Проводится проверка на доступ к редактированию объявления
     * через метод {@link #checkPermit(Ad, Authentication)} <p>
     * 3) В репозиториях {@link CommentRepository} , {@link ImageRepository} , {@link AdRepository} - вызываются методы удаления
     * комментариев объявления, картинки объявления, и данных об объявлении соответственно.
     *
     * @param id id объявления (long)
     * @param authentication объект аутентификации с данными текущего пользователя
     */
    //Метод использует аннотацию @Transactional, которая дает понять Spring что данный метод - это не поочередные
    //самостоятельные действия внутри, а единая транзакция с возможностью отката. Spring сам реализует запросы к БД
    //и "упаковку" в транзакции. Тем самым мы обезопасим наше удаление от непредвиденных сбоев и неточностей.

    @Override
    @Transactional
    public void deleteAd(long id, Authentication authentication) {

        Ad ad = adRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление с ID" + id + "не найдено"));

        checkPermit(ad, authentication);
        commentRepository.deleteCommentsByAdId(id);
        imageRepository.deleteById(ad.getImage().getId());
        adRepository.deleteById(id);
    }

    /**
     * <b> Метод изменения данных объявления </b> <p>
     * (Названия, описания и цены)  {@link CreateOrUpdateAd} <p>
     * Принцип работы:<p>
     * Находит по ID объявление, уточняет есть ли у пользователя доступ к редактированию {@link #checkPermit(Ad, Authentication)} ,
     *  далее меняет данные (Названия, описания и цены), сохраняет объявление, возвращает DTO только что измененного
     *  объекта {@link AdDto}
     * @param id id объявления (long)
     * @param createOrUpdateAd DTO объявления {@link CreateOrUpdateAd}
     * @param authentication объект аутентификации с данными текущего пользователя
     * @return {@link AdDto} DTO объявления
     */
    @Override
    public AdDto updateAd(long id, CreateOrUpdateAd createOrUpdateAd, Authentication authentication) {
        Ad ad = adRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление с ID" + id + "не найдено"));
        checkPermit(ad, authentication);
        ad.setTitle(createOrUpdateAd.getTitle());
        ad.setDescription(createOrUpdateAd.getDescription());
        ad.setPrice(createOrUpdateAd.getPrice());
        adRepository.save(ad);
        return adMapper.adToAdDto(ad);
    }

    /**
     * <b> Метод возвращающий все объявления пользователя </b> <p>
     * Принцип работы:<p>
     * Из {@link Authentication} достает пользователя, по данному пользователю находим все его объявления.
     * Маппер пакует лист объявлений в  {@link AdsDto} и возвращает DTO
     * @param authentication объект аутентификации с данными текущего пользователя
     * @return {@link AdsDto} DTO всех объявлений пользователя
     */
    @Override
    public AdsDto getAdsMe(Authentication authentication) {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        List<Ad> adList = adRepository.findAdByAuthorId(user.getId());
        return adMapper.adListToAds(adList);
    }

    /**
     * <b> Метод изменения изображения объявления </b> <p>
     * Метод использует аннотацию {@link Transactional} <p>
     * Принцип работы:<p>
     * Найти объявление по ID, далее проверить доступ к редактированию, из объявления взять текущую картинку,
     * сохранить её отдельно, на её место сохранить новую, не актуальную картинку - удалить, сохранить объект объявления,
     * в котором уже будет новая картинка.
     * @param id id объявления (long)
     * @param image файл  {@link MultipartFile} (картинка объявления)
     * @param authentication объект аутентификации с данными текущего пользователя
     * @throws IOException (может выкинуть ошибки загрузки)
     */
    //Метод использует аннотацию @Transactional, которая дает понять Spring что данный метод - это не поочередные
    //самостоятельные действия внутри, а единая транзакция с возможностью отката. Spring сам реализует запросы к БД
    //и "упаковку" в транзакции. Тем самым мы обезопасим наше удаление от непредвиденных сбоев и неточностей.
    @Override
    @Transactional
    public void updateAdImage(Long id, MultipartFile image, Authentication authentication) throws IOException {
        Ad ad = adRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Объявление с ID" + id + "не найдено"));
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        checkPermit(ad, authentication);
        Image imageFile = ad.getImage();
        ad.setImage(imageService.uploadImage(ad.getId(), image));
        imageService.removeImage(imageFile);
        adRepository.save(ad);
    }
    /**
     * <b> Метод проверки доступа к редактированию объявления </b> <p>
     * Служебный внутренний метод принимающий на вход: <p> {@link Ad} и {@link Authentication} <p>
     * далее сравнивает автора объявления и текущего пользователя, а также проверяет, является ли пользователь Админом.
     * Если текущий пользователь не автор объявления и не админ, то будет выброшено  {@link AccessDeniedException}
     *
     * @param ad объявление
     * @param authentication объект аутентификации с данными текущего пользователя
     */
    public void checkPermit(Ad ad, Authentication authentication) {
        if (!ad.getAuthor().getEmail().equals(authentication.getName())
                && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new AccessDeniedException("Вы не можете редактировать или удалять чужое объявление");
        }
    }
}
