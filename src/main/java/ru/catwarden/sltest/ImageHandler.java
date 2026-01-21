package ru.catwarden.sltest;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageHandler {
    public static String editPhoto(Birthday birthday, MultipartFile photo) throws IOException {
        if(photo==null || photo.isEmpty() ){
            return birthday.getPhotoPath();

        }

        String filename = birthday.getId() + "_" + photo.getOriginalFilename();

        Path uploadPath = Paths.get("images", filename);
        Files.write(uploadPath, photo.getBytes());

        return "/images/" + filename;

    }
}

