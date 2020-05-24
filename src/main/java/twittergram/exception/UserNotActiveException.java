package twittergram.exception;

public class UserNotActiveException extends RuntimeException {

	public UserNotActiveException(String mess) {
		super(mess);
	}

}