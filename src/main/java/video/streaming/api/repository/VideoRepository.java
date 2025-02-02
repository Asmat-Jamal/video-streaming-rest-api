package video.streaming.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import video.streaming.api.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

}



