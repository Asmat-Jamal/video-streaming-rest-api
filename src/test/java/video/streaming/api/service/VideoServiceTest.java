package video.streaming.api.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import video.streaming.api.entity.MetaData;
import video.streaming.api.entity.Video;
import video.streaming.api.projection.ProjectedVideoMetaData;
import video.streaming.api.repository.MetaDataRepository;
import video.streaming.api.repository.VideoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {


    @Mock
    private VideoRepository mockedVideoRepository;
    @Mock
    private MetaDataRepository mockedMetaDataRepository;


    @InjectMocks
    private VideoService entityService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveVideo() {
        // Given

        Video e = new Video();
        e.setContent("content");
        when(mockedVideoRepository.save(e)).thenReturn(e);

        // When
        final Video result = entityService.saveVideo(e);

        // Then
        assertNotNull(result);
        assertEquals(e.getContent(), result.getContent());

    }

    @Test
    void savemetaData() {
        // Given

        MetaData e = new MetaData();
        e.setTitle("title");
        e.setDirector("director");
        when(mockedMetaDataRepository.save(e)).thenReturn(e);

        // When
        final MetaData result = entityService.savemetaData(e);

        // Then
        assertNotNull(result);
        assertEquals(e.getTitle(), result.getTitle());
        assertEquals(e.getDirector(), result.getDirector());


    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void getAllVideosMetaData() {

        List<ProjectedVideoMetaData> companies = new ArrayList<>();
        Page<ProjectedVideoMetaData> pagedResponse = new PageImpl<>(companies);
        Mockito.when(mockedMetaDataRepository.getProjectedVideoMetaData(true, Pageable.ofSize(1))).thenReturn(pagedResponse);

    }



}