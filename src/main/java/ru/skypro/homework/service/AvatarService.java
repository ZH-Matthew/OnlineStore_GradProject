package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Avatar;

import java.io.IOException;

public interface AvatarService {

    Avatar uploadAvatar(MultipartFile image) throws IOException;

    void removeAvatar(Avatar avatar);

    Avatar getAvatar(Long id);
}
