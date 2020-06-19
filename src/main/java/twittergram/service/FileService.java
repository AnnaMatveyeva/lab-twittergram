package twittergram.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twittergram.exception.FileStorageException;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileService {

	private final Path absolutePath = Paths.get("src/main/resources/uploads/");

	public String uploadFile(MultipartFile file, int imageId, String nickname) {
		String fileName = String.valueOf(imageId);
		String resultPath = absolutePath + "\\" + nickname + "\\" + fileName;
		try(FileOutputStream outputStream = new FileOutputStream(resultPath)) {
			File filePath = new File(resultPath);
			if (!filePath.exists()) {
				filePath.getParentFile().mkdir();
			}
			outputStream.write(file.getBytes());
			log.trace("File was created at " + resultPath);
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
			log.trace("File with path " + path + " was deleted");
		}

	}
}