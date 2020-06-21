package twittergram.exception;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

	@ExceptionHandler({FileStorageException.class, StoryNotFoundException.class,
			PhotoNotFoundException.class, UserNotFoundException.class, UserValidationException.class,
			UserNotActiveException.class})
	public void handleException(HttpServletResponse response, Exception ex)
			throws IOException {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
		log.debug(ex.getMessage());
	}

	@SneakyThrows
	@ExceptionHandler(Throwable.class)
	public void handleThrowable(HttpServletResponse response,Throwable ex){
		response.sendError(HttpServletResponse.SC_BAD_REQUEST,ex.getMessage());
		log.debug(ex.getMessage());
	}
}