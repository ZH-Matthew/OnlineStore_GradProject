package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.GetAuthentication;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.exception.IncorrectPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AvatarService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
/**
 * <b> Сервис для работы с пользователями </b> <p>
 * Содержит CRUD методы
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final AvatarService avatarService;
    private final PasswordEncoder encoder;

    /**
     * <b>Метод изменения пароля </b> <p>
     * Принцип работы:<p>
     * Достает из контекста {@link User} , далее с помощью {@link #encoder} проверяет совпадение действующего пароля и пароля
     * аутентифицированного пользователя, если пароли равны, хеширует пароль и сохраняет его в объект пользователя,
     * далее пользователь сохраняется в БД
     * @param newPassword {@link NewPassword})
     * @param authentication {@link Authentication})
     * @throws IncorrectPasswordException не корректный действующий пароль
     */
    @Override
    public void setPassword(NewPassword newPassword, Authentication authentication) {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        if (encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(newPassword.getNewPassword()));
            repository.save(user);
            return;
        }
        throw new IncorrectPasswordException("Неверный пароль");

    }
    /**
     * <b>Метод получения пользователя </b> <p>
     * @param authentication {@link Authentication})
     * @return {@link UserDto})
     */
    @Override
    public UserDto getUser(Authentication authentication) {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        return mapper.userToUserDto(user);
    }

    /**
     *<b>Метод изменения информации о пользователе </b> <p>
     * Принцип работы:<p>
     * Достает из контекста {@link User} , меняет в пользователе: имя, фамилию, номер телефона.
     * Сохраняет пользователя.
     * @param update {@link UpdateUser} (DTO)
     * @param authentication  {@link Authentication})
     * @return {@link UpdateUser}
     */
    @Override
    public UpdateUser updateUserInfo(UpdateUser update, Authentication authentication) {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        user.setFirstName(update.getFirstName());
        user.setLastName(update.getLastName());
        user.setPhone(update.getPhone());
        repository.save(user);
        return update;
    }

    /**
     * <b>Метод изменения аватарки пользователя </b> <p>
     * Принцип работы:<p>
     * Достает из контекста {@link User} ,отдельно сохраняет действующий аватар, далее дергает метод загрузки аватарки у
     *  {@link #avatarService}, и передает файл из параметров. В конце удаляет "старую" аватарку и пересохраняет пользователя
     *
     * @param image {@link MultipartFile} аватарка (файл)
     * @param authentication  {@link Authentication}
     * @throws IOException (может выкинуть ошибки загрузки)
     */
    //Метод использует аннотацию @Transactional, которая дает понять Spring что данный метод - это не поочередные
    //самостоятельные действия внутри, а единая транзакция с возможностью отката. Spring сам реализует запросы к БД
    //и "упаковку" в транзакции. Тем самым мы обезопасим наше действие от непредвиденных сбоев и неточностей.
    @Override
    @Transactional
    public void updateUserAvatar(MultipartFile image, Authentication authentication) throws IOException {
        User user = new GetAuthentication().getAuthenticationUser(authentication.getName());
        Avatar imageFile = user.getAvatar();
        user.setAvatar(avatarService.uploadAvatar(image));
        if (imageFile != null) {
            avatarService.removeAvatar(imageFile);
        }
        repository.save(user);
    }
}
