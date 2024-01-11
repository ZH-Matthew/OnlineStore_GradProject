package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;

import java.awt.datatransfer.Clipboard;

public interface ImageService {
    Image uploadImage(MultipartFile imageFile);

    void updateAdImage(long id, MultipartFile image, Authentication authentication);


    void removeImage(Image image);

    Image getImage(long id);
}
