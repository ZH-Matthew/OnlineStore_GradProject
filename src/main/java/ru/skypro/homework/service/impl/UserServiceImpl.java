package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public void setPassword(NewPassword newPassword, Authentication authentication) {

    }

    @Override
    public UserDto getUser(Authentication authentication) {
        return null;
    }

    @Override
    public UpdateUser updateUserInfo(UpdateUser update, Authentication authentication) {
        return null;
    }
}
