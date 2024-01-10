package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public void updateAdImage(long id, MultipartFile image, Authentication authentication) {

    }

    @Override
    public Image getImage(long id) {
        return null;
    }
}
