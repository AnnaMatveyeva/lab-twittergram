package twittergram.model;

import java.util.ArrayList;
import java.util.List;


public enum InvalidTokens {
    INSTANCE;

    List<String> tokens = new ArrayList<>();

    public List<String> getTokensList() {
        return tokens;
    }
}
