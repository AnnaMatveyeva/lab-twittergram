package twittergram.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.Story;
import twittergram.repository.StoryRepository;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepo;
    private final UserService userService;

    public Story create(String text, String nickname) {
        Story story = new Story();
        story.setText(text);
        story.setDate(LocalDate.now());
        story.setUser(userService.findByNickname(nickname));
        return storyRepo.save(story);
    }

}
