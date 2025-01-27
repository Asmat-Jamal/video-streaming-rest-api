package video.streaming.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import video.streaming.api.entity.Stats;
import video.streaming.api.projection.ProjectedVideoMetaData;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {


}
