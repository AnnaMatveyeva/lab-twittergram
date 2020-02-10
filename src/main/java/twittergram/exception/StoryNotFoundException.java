package twittergram.exception;

public class StoryNotFoundException extends RuntimeException {

    public StoryNotFoundException() {
        super("Story not found");
    }

}