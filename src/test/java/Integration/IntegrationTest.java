package Integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import video.streaming.api.VideoApiApplication;
import video.streaming.api.models.MetaDataModel;
import video.streaming.api.models.VideoModel;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes= VideoApiApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    VideoModel video;
    MetaDataModel metaData;

     @Container
     static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");

     @DynamicPropertySource
     static void configureProperties(DynamicPropertyRegistry registry) {
         registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
         registry.add("spring.datasource.username", mySQLContainer::getUsername);
         registry.add("spring.datasource.password", mySQLContainer::getPassword);
     }

     @BeforeAll
     static void beforeAll() {
         mySQLContainer.start();
     }

     @AfterAll
     static void afterAll() {
         mySQLContainer.stop();
     }


    @BeforeEach
    public void setup(){


        video = new VideoModel();
        video.content = "test content";

        metaData = new MetaDataModel();
        metaData.director = "director";
        metaData.title = "title";
        metaData.genre = "genre";
        metaData.cast = "cast";
        metaData.mainActor = "actor";
        metaData.synopsis = "synopsis";

        metaData.releaseYear = 2020;
        metaData.duration = 400;

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        System.out.println(mySQLContainer.getDatabaseName());
        System.out.println(mySQLContainer.getJdbcUrl());
        System.out.println(mySQLContainer.getUsername());
        System.out.println(mySQLContainer.getPassword());

    }



    //Save Video Test
    @Test
    @Order(1)
    public void saveVideoTest() throws Exception {

        // Action and Verify
        mockMvc.perform(post("/api/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))

                .andExpect(status().isOk())
                .andDo(print());
    }

     @Test
     @Order(2)
     public void saveMetaDataTest() throws Exception {
         // precondition

         // Action and Verify
         mockMvc.perform(post("/api/videos/metadata/{id}",1L)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(metaData)))

                 .andExpect(status().isOk())
                 .andDo(print());
     }

     // Publish Video
     @Test
     @Order(3)
     public void publicVideoTest() throws Exception {
         // precondition

         // Action and Verify
         mockMvc.perform(post("/api/videos/publish/{id}",1L)
                         .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andDo(print());
     }

    //Get All videos Test
    @Test
    @Order(4)
    public void getAllPublishedVideos() throws Exception {

        // Action and Verify
        mockMvc.perform(get("/api/videos/getall/true"))

                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalElements",is(1)));

    }

    //Load video by id
    @Test
    @Order(5)
    @Transactional
    public void loadVideoById() throws Exception{

        // Action and Verify
        mockMvc.perform(get("/api/videos/{id}", 1))

                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.metaData.title", is(metaData.title)));

    }

     //Load video by id
     @Test
     @Order(6)
     @Transactional
     public void playVideoById() throws Exception{

         // Action and Verify
         mockMvc.perform(get("/api/videos/play/{id}", 1))

                 .andExpect(status().isOk())
                 .andDo(print())
                 .andExpect(jsonPath("$.content", is(video.content)));

     }

}
