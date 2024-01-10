package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;

public interface UserService {
    void setPassword(NewPassword newPassword, Authentication authentication);

    UserDto getUser(Authentication authentication);

    UpdateUser updateUserInfo(UpdateUser update, Authentication authentication);

}
