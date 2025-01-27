package video.streaming.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import video.streaming.api.entity.MetaData;
import video.streaming.api.projection.ProjectedVideoMetaData;


@Repository
public interface MetaDataRepository extends JpaRepository<MetaData, Long> {

    @Query("SELECT v.video_id as id,v.mainActor as mainActor,v.director as director,v.duration as duration ,v.genre as genre,v.title as title FROM MetaData AS v WHERE v.published = :published")
    Page<ProjectedVideoMetaData> getProjectedVideoMetaData(Boolean published, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT v.video_id as id,v.mainActor as mainActor,v.director as director,v.duration as duration ,v.genre as genre,v.title as title FROM MetaData AS v WHERE  v.published = true and lower(v.director) like :director")
    Page<ProjectedVideoMetaData> getProjectedVideoMetaDataByDirectorContainsIgnoreCase(String director, org.springframework.data.domain.Pageable pageable);

}

