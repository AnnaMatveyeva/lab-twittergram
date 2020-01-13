package twittergram.service;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twittergram.exception.FileStorageException;

@Service
public class FileService {

    private final Path absolutePath = Paths.get("src/main/resources/uploads/").toAbsolutePath();

    public String uploadFile(MultipartFile file, int imageId) {

        try {
            String fileName = String.valueOf(imageId);
            FileOutputStream outputStream = new FileOutputStream(absolutePath + "\\" + fileName);
            outputStream.write(file.getBytes());
            return absolutePath + "\\" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                + ". Please try again!");
        }
    }

}