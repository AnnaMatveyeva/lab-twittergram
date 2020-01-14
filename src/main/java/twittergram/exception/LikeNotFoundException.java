package twittergram.exception;

public class LikeNotFoundException extends RuntimeException {

    public LikeNotFoundException() {
        super("Like not found");
    }

}