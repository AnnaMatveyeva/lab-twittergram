package twittergram.exception;

public class UserDeletedException extends RuntimeException {

    public UserDeletedException(String mess) {
        super(mess);
    }

}