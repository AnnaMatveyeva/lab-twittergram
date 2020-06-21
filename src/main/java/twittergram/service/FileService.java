package twittergram.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twittergram.exception.FileStorageException;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

	public static File base_Path = new File("C:\\uploads");
	private final Path absolutePath = base_Path.toPath();
	private static final Logger log = LoggerFactory.getLogger(FileService.class);

	public String uploadFile(MultipartFile file, int imageId, String nickname) {


		String fileName = String.valueOf(imageId);
		String resultPath = absolutePath + "\\" + nickname + "\\" + fileName;
		try {
			File filePath = new File(resultPath);
			if (!filePath.exists()) {
				filePath.getParentFile().mkdir();
			}
			FileOutputStream outputStream = new FileOutputStream(resultPath);
			outputStream.write(file.getBytes());
			log.debug("File was created at {}", resultPath);
			outputStream.close();
			return resultPath;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new FileStorageException("Could not store file " + file.getOriginalFilename()
					+ ". Please try again!");
		}
	}

	@SneakyThrows
	public void deleteFile(String path) {
		if (Files.deleteIfExists(Path.of(path))) {
			log.debug("File with path {} was deleted", path);
		}

	}
}