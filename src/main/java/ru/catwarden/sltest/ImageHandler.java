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

        // delete the previous photo
        deletePhoto(birthday.getPhotoPath());

        String filename = birthday.getId() + "_" + photo.getOriginalFilename();

        Path uploadPath = Paths.get("images", filename);
        Files.write(uploadPath, photo.getBytes());

        return "/images/" + filename;

    }

    public static String uploadPhoto(int id, MultipartFile photo) throws IOException{
        String filename = id + "_" + photo.getOriginalFilename();

        Path uploadPath = Paths.get("images", filename);
        Files.write(uploadPath, photo.getBytes());

        return "/images/" + filename;

    }

    public static void deletePhoto(String DBFilepath) throws IOException{
        // need to get only the filename and not the full path since the full path is searched in the disk root and not the actual working directory
        String filename = Paths.get(DBFilepath).getFileName().toString();

        // in this case the images folder is searched inside the working directory of JVM
        // otherwise if the full path from the DB is passed there, method will search this path in the disk root
        Path filepath = Paths.get("images", filename);

        Files.delete(filepath);
    }
}

