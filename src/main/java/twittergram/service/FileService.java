package twittergram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import twittergram.exception.FileStorageException;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileService {

	private final Path absolutePath = Paths.get("src/main/resources/uploads/");

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
			log.trace("File was created at " + resultPath);
			return resultPath;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FileStorageException("Could not store file " + file.getOriginalFilename()
					+ ". Please try again!");
		}
	}

	public void deleteFile(String path) {
		File file = new File(path);
		file.getAbsolutePath();
		File abFile = new File(file.getAbsolutePath());
		if (abFile.delete()) {
			log.trace("File with path " + path + " was deleted");
		}

	}
}