package twittergram.service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twittergram.exception.FileStorageException;

@Service
public class FileService {

    private final Path absolutePath = Paths.get("src/main/resources/uploads/").toAbsolutePath();

    public String uploadFile(MultipartFile file, int imageId, String nickname) {

        try {

            String fileName = String.valueOf(imageId);
            String resultPath = absolutePath + "\\" + nickname + "\\" + fileName;
            File filePath = new File(resultPath);
            if (!filePath.exists()) {
                filePath.getParentFile().mkdir();
            }
            FileOutputStream outputStream = new FileOutputStream(resultPath);
            outputStream.write(file.getBytes());
            return resultPath;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                + ". Please try again!");
        }
    }

}