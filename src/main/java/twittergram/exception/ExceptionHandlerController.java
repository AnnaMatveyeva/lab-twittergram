package twittergram.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(FileStorageException.class)
    public void handleFileException(HttpServletResponse response, FileStorageException ex)
        throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(StoryNotFoundException.class)
    public void handleStoryNotFoundException(HttpServletResponse response,
        StoryNotFoundException ex)
        throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    public void handlePhotoNotFoundException(HttpServletResponse response,
        PhotoNotFoundException ex)
        throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }
}