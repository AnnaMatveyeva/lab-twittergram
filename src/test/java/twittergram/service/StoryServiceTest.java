package twittergram.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import twittergram.entity.Like;
import twittergram.entity.Story;
import twittergram.entity.Tag;
import twittergram.exception.StoryNotFoundException;
import twittergram.model.StoryDTO;
import twittergram.repository.StoryRepository;
import twittergram.service.mapper.StoryMapper;

class StoryServiceTest {

    @InjectMocks
    private StoryService storyService;
    @Mock
    private StoryRepository storyRepository;
    @Mock
    private StoryMapper mapper;
    @Mock
    private TagService tagService;
    @Mock
    private LikeService likeService;
    @Mock
    private Pageable pageable;
    @Mock
    private Sort sort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findById_positive_test() {
        when(storyRepository.findById(1L)).thenReturn(java.util.Optional.of(new Story()));
        assertNotNull(storyService.findById(1L));
        verify(storyRepository).findById(1L);
    }

    @Test
    void findById_negative_test() {
        when(storyRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(StoryNotFoundException.class, () -> storyService.findById(1L));
        verify(storyRepository).findById(1L);
    }

    @Test
    void findDTOById_positive_test() {
        when(storyRepository.findById(1L)).thenReturn(java.util.Optional.of(new Story()));
        doCallRealMethod().when(mapper).toDTO(any(Story.class));

        assertNotNull(storyService.findDTOById(1L));
        verify(storyRepository).findById(1L);
        verify(mapper).toDTO(any());
    }

    @Test
    void findDTOById_negative_test() {
        when(storyRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        doCallRealMethod().when(mapper).toDTO(any(Story.class));
        assertThrows(StoryNotFoundException.class, () -> storyService.findDTOById(1L));
        verify(storyRepository).findById(1L);
        verify(mapper, times(0)).toDTO(any());
    }

    @Test
    void create_test() {
        when(storyRepository.save(any(Story.class))).thenReturn(new Story());
        doCallRealMethod().when(mapper).toDTO(any(Story.class));

        assertNotNull(storyService.create(new StoryDTO(), 1L));
        verify(storyRepository).save(any(Story.class));
    }

    @Test
    void addTags_emptyList_test() {
        List<Tag> tags = Arrays.asList(new Tag());
        when(tagService.saveAll(tags)).thenReturn(tags);

        Story storyFromService = storyService.addTags(tags, new Story());
        assertEquals(tags.size(), storyFromService.getTags().size());
    }

//    @Test
//    void addTags_notEmptyList_test() {
//        Story story = new Story();
//        Tag tag = new Tag();
//        List<Tag> tags = Arrays.asList(tag);
//        story.setTags(tags);
//
//        when(tagService.saveAll(tags)).thenReturn(tags);
//        when(tagService.addTagStory(tag, story)).thenReturn(tag);
//        Story storyFromService = storyService.addTags(tags, story);
//
//        assertEquals(tags.size(), storyFromService.getTags().size());
//    }

    @Test
    void update_test() {
        Story story = new Story();
        when(storyRepository.findById(1L)).thenReturn(java.util.Optional.of(story));
        StoryDTO storyDTO = new StoryDTO();
        storyDTO.setText("text");

        when(storyRepository.save(story)).thenReturn(story);
        doCallRealMethod().when(mapper).toDTO(story);

        assertEquals("text", storyService.update(1L, storyDTO).getText());
        verify(storyRepository).save(story);

    }

    @Test
    void update_notFound_test() {
        when(storyRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        StoryDTO storyDTO = new StoryDTO();
        storyDTO.setText("text");

        assertThrows(StoryNotFoundException.class, () -> storyService.update(1L, storyDTO));
        verify(storyRepository).findById(1L);

    }

//    @Test
//    void addLike_test() {
//        Story story = new Story();
//        story.setLikes(new ArrayList<>());
//        when(storyRepository.findById(1L)).thenReturn(java.util.Optional.of(story));
//        when(likeService.setLike(story, 1L)).thenReturn(new Like());
//        when(storyRepository.save(story)).thenReturn(story);
//        doCallRealMethod().when(mapper).toDTO(story);
//        StoryDTO storyDTO = storyService.addLike(1L, 1L);
//
//        assertEquals(1, storyDTO.getLikes().size());
//        verify(storyRepository).findById(1L);
//        verify(storyRepository).save(story);
//    }
//
//    @Test
//    void add_existing_like_test() {
//        Story story = new Story();
//        Like like = new Like();
//        story.setLikes(Arrays.asList(like));
//
//        when(storyRepository.findById(1L)).thenReturn(java.util.Optional.of(story));
//        when(likeService.setLike(story, 1L)).thenReturn(like);
//        when(storyRepository.save(story)).thenReturn(story);
//        doCallRealMethod().when(mapper).toDTO(story);
//
//        StoryDTO storyDTO = storyService.addLike(1L, 1L);
//
//        assertEquals(1, storyDTO.getLikes().size());
//        verify(storyRepository).findById(1L);
//    }
//
//    @Test
//    void delete_test() {
//        Story story = new Story();
//        Like like = new Like();
//        Tag tag = new Tag();
//        story.setLikes(Arrays.asList(like));
//        story.setTags(Arrays.asList(tag));
//        doNothing().when(likeService).removeStory(like, story);
//        doNothing().when(tagService).removeStory(tag, story);
//
//        storyService.delete(story);
//        verify(storyRepository).delete(story);
//    }
//
//    @Test
//    void delete_list_test() {
//        Story story = new Story();
//        Like like = new Like();
//        Tag tag = new Tag();
//        story.setLikes(Arrays.asList(like));
//        story.setTags(Arrays.asList(tag));
//        doNothing().when(likeService).removeStory(like, story);
//        doNothing().when(tagService).removeStory(tag, story);
//
//        storyService.deleteList(Arrays.asList(story, story));
//        verify(storyRepository, times(2)).delete(story);
//    }

//    @Test
//    void deleteUserLikes_test() {
//        Story story = new Story();
//        List<Story> stories = Arrays.asList(story);
//        when(storyRepository.findByLikes_UserId(1L)).thenReturn(stories);
//        doNothing().when(likeService).removeFromStories(stories, 1L);
//
//        storyService.deleteUserLikes(1L);
//        verify(storyRepository).findByLikes_UserId(1L);
//    }

}