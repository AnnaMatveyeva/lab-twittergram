package twittergram.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InvalidTokens {

    private List<String> tokens = new ArrayList<>();

    public List<String> getTokensList() {
        return tokens;
    }
}
