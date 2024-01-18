package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository repository;
    public static String uploadDirectory = System.getProperty("user.dir") + "/images";

    /**
     * Метод для загрузки изображения
     *
     * @param id id объявления
     * @param imageFile изображение
     * @throws IOException
     */
    @Override
    public Image uploadImage(long id, MultipartFile imageFile) throws IOException {

        Path filePath = Path.of(uploadDirectory,"ad_" + id + "." + getExtensions(imageFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Image image = new Image();
        image.setFilePath(filePath.toString());
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        image.setData(imageFile.getBytes());
        repository.save(image);
        return image;
    }
    /**
     * Метод для удаления изображения из репозитория
     *
     * @param image
     *
     */
    @Override
    public void removeImage(Image image) {
        repository.delete(image);

    }
    /**
     * Метод для поиска изображения по id в репозитории
     *
     * @param id
     *
     */
    @Override
    public Image getImage(Long id) {
        return repository.findById(id).orElse(new Image());
    }

    /**
     * Вспомогательный метод для получения формата файла
     *
     * @param fileName
     *
     */
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
