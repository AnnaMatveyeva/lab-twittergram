package twittergram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import twittergram.entity.Tag;
import twittergram.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;

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
