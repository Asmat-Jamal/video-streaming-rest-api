package video.streaming.api.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import video.streaming.api.entity.Video;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2,replace= AutoConfigureTestDatabase.Replace.NONE)
public class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TestEntityManager entityManager;



    public void setUp() {

        Video video = new Video();
        video.setContent("test content");
        video.setPublished(false);
        entityManager.persist(video);
    }


    // Find By ID
    @Test
    public void whenFindByID_thenReturnVideo() {

        Video newVideo = new Video();
        newVideo.setContent("new test content");
        newVideo.setPublished(false);
        entityManager.persist(newVideo);
        Optional<Video> retrievedVideo = videoRepository.findById(newVideo.getId());
        assertThat(retrievedVideo).contains(newVideo);
    }

    // Find By NonExisting ID
    @Test
    public void whenFindByNonExistingID_thenReturnNull() {

        // given
        Long Id = 2L;

        // when
        Optional<Video> found = videoRepository.findById(Id);

        // then
        assertFalse(found.isPresent());
    }


    // save video
    @Test
    public void givenNewVideo_whenSave_thenSuccess() {
        Video newVideo = new Video();
        newVideo.setContent("test content");
        newVideo.setPublished(false);
        Video insertedVideo = videoRepository.save(newVideo);
        assertThat(entityManager.find(Video.class, insertedVideo.getId()) ).isEqualTo(insertedVideo);
    }
}
