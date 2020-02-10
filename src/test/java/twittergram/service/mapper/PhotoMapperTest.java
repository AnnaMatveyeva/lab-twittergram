package twittergram.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doCallRealMethod;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twittergram.entity.Photo;
import twittergram.model.PhotoDTO;

class PhotoMapperTest {

    @Mock
    private PhotoMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void toDTO_test() {
        Photo photo = new Photo();
        photo.setPath("/path");
        photo.setImage(1);
        photo.setDate(LocalDate.now());
        photo.setLikes(new ArrayList<>());
        photo.setDescription("description");

        doCallRealMethod().when(mapper).toDTO(photo);
        PhotoDTO photoDTO = mapper.toDTO(photo);
        assertEquals(photo.getDescription(), photoDTO.getDescription());
        assertEquals(photo.getImage(), photoDTO.getImage());
        assertEquals(photo.getDate(), photoDTO.getDate());
        assertEquals(photo.getPath(), photoDTO.getPath());
    }
}