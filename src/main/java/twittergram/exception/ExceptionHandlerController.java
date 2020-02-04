package twittergram.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({FileStorageException.class, StoryNotFoundException.class,
        PhotoNotFoundException.class, UserNotFoundException.class, UserValidationException.class,
        UserNotActiveException.class})
    public void handleException(HttpServletResponse response, Exception ex)
        throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }
}