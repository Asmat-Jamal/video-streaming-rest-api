package video.streaming.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import video.streaming.api.entity.MetaData;
import video.streaming.api.entity.Video;
import video.streaming.api.service.VideoService;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 @WebMvcTest(VideoController.class)
@ExtendWith(MockitoExtension.class)
public class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private VideoService videoService;

    @Autowired
    private ObjectMapper objectMapper;

    Video video;
    MetaData metaData;

    @BeforeEach
    public void setup(){

        video = new Video();
        video.setContent("test");
        video.setId(1L);

        metaData = new MetaData();
        metaData.setDirector("director");
        metaData.setTitle("title");
        metaData.setGenre("genre");
        metaData.setCast("cast");
        metaData.setMainActor("actor");
        metaData.setSynopsis("synopsis");
        metaData.setReleaseYear(2020);
        metaData.setVideo_id(1L);
        metaData.setDuration(400);
        metaData.setId(1L);
        video.setMetaData(metaData);

    }

    //Post Controller
    @Test
    public void saveVideoTest() throws Exception{
        // precondition
        given(videoService.saveVideo(any(Video.class))).willReturn(video);

        // action
        ResultActions response = mockMvc.perform(post("/api/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(video)));

        // verify
        response.andDo(print()).
                andExpect(status().isOk());
    }


    //Update meta data
    @Test
    public void createMetaDataTest() throws Exception{


        // precondition
        given(videoService.getVideoById(video.getId())).willReturn(Optional.of(video));

        given(videoService.savemetaData(metaData)).willReturn(metaData);

        // action
        ResultActions response = mockMvc.perform(post("/api/videos/metadata/{id}", video.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(metaData)));

        // verify
        response.andExpect(status().isOk()).andDo(print());
    }


    //get published byId controller
    @Test
    public void getPublishedVideoByIdTest() throws Exception{

        // precondition
        given(videoService.getPublishedVideoById(video.getId())).willReturn(Optional.of(video));

        // action
        ResultActions response = mockMvc.perform(get("/api/videos/{id}", video.getId()));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content", is(video.getContent())))
                .andExpect(jsonPath("$.metaData.title", is(video.getMetaData().getTitle())));

    }

}