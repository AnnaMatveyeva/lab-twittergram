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

    public Tag addTagPhoto(Tag tag, Photo photo) {
        tag.getPhotos().add(photo);
        tagRepo.save(tag);
        return tag;
    }

    public List<Tag> addTagsPhoto(List<Tag> tags, Photo photo) {
        for (Tag tag : tags) {
            tag.getPhotos().add(photo);
        }
        return tagRepo.saveAll(tags);
    }

    public List<Tag> addTagsStory(List<Tag> tags, Story story) {
        for (Tag tag : tags) {
            tag.getStories().add(story);
        }
        return tagRepo.saveAll(tags);
    }

    public Tag addTagStory(Tag tag, Story story) {
        tag.getStories().add(story);
        tagRepo.save(tag);
        return tag;
    }

    public void addTags(List<Tag> tags, Photo photo) {
        tags = saveAll(tags);
        if (photo.getTags().isEmpty()) {
            photo.setTags(tags);
            addTagsPhoto(tags, photo);
        } else {
            for (Tag tag : tags) {
                if (!photo.getTags().contains(tag)) {
                    photo.getTags().add(tag);
                    addTagPhoto(tag, photo);
                }
            }
        }
    }

    public void removeStory(Tag tag, Story story) {
        tag.getStories().remove(story);
        tagRepo.save(tag);
    }

    public void removePhoto(Tag tag, Photo photo) {
        tag.getPhotos().remove(photo);
        tagRepo.save(tag);
    }

}
