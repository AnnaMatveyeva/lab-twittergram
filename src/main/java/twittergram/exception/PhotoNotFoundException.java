package twittergram.exception;

public class PhotoNotFoundException extends RuntimeException {

	public PhotoNotFoundException() {
		super("Photo not found");
	}

}