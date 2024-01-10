package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.service.AvatarService;

@Service
public class AvatarServiceImpl implements AvatarService {
    @Override
    public void updateUserAvatar(MultipartFile image, Authentication authentication) {

    }

    @Override
    public Avatar getAvatar(long id) {
        return null;
    }
}
