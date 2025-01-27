package video.streaming.api.repository;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import video.streaming.api.entity.MetaData;
import video.streaming.api.entity.Video;
import video.streaming.api.projection.ProjectedVideoMetaData;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2,replace= AutoConfigureTestDatabase.Replace.NONE)
public class MetaDataRepositoryTest {

    @Autowired
    private MetaDataRepository metaDataRepository;

    @Autowired
    private TestEntityManager entityManager;


    @BeforeEach
    public void setUp() {

        Video video = new Video();
        video.setContent("test content");
        video.setPublished(false);
        entityManager.persist(video);

        MetaData metaData = new MetaData();
        metaData.setVideo_id(video.getId());
        metaData.setTitle("test title");
        metaData.setSynopsis("test synopsis");
        metaData.setCast("test cast");
        metaData.setDirector("test director");
        metaData.setMainActor("test main actor");
        metaData.setReleaseYear(2020);
        metaData.setGenre("test genre");
        metaData.setDuration(60);
        entityManager.persist(metaData);

        video.setMetaData(metaData);
        video.setPublished(true);
        entityManager.persist(video);
    }


    // Find By ID
    @Test
    public void whenFindByPublishedVideo_thenReturnVideo() {

        // when
        Page<ProjectedVideoMetaData> found = metaDataRepository.getProjectedVideoMetaData(true, Pageable.unpaged());

        // then
        assertEquals(1,found.stream().count());
    }


    // Find By ID Not published video
    @Test
    public void whenFindByNonPublishedVideo_thenReturnNull() {

        // when
        Page<ProjectedVideoMetaData> found = metaDataRepository.getProjectedVideoMetaData(false, Pageable.ofSize(1));

        // then
        assertEquals(0, found.stream().count());
    }


}