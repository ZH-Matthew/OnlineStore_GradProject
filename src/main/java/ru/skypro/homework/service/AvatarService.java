package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Avatar;

import java.awt.datatransfer.Clipboard;

public interface AvatarService {
    void updateUserAvatar(MultipartFile image, Authentication authentication);

    Avatar getAvatar(long id);
}
