package twittergram.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.Photo;
import twittergram.entity.Story;
import twittergram.entity.Tag;
import twittergram.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepo;

    public List<Tag> saveAll(List<Tag> tags) {
        List<Tag> result = new ArrayList<>();
        for (Tag tag : tags) {
            if (tagRepo.findByText(tag.getText()) == null) {
                result.add(tagRepo.save(tag));
            } else {
                result.add(tagRepo.findByText(tag.getText()));
            }
        }
        return result;
    }
}
