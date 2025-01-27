package video.streaming.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import video.streaming.api.entity.MetaData;
import video.streaming.api.entity.Stats;
import video.streaming.api.entity.Video;
import video.streaming.api.projection.ProjectedVideoMetaData;
import video.streaming.api.repository.MetaDataRepository;
import video.streaming.api.repository.StatsRepository;
import video.streaming.api.repository.VideoRepository;

import java.util.*;

@Service
public class VideoService {

    @Autowired
    private final VideoRepository videoRepository;
    private final MetaDataRepository metaDataRepository;
    private final StatsRepository statsRepository;

    public VideoService(VideoRepository videoRepository,MetaDataRepository metaDataRepository,StatsRepository statsRepository) {

        this.videoRepository = videoRepository;
        this.metaDataRepository = metaDataRepository;
        this.statsRepository =statsRepository;
    }

    // Create or Update a Video
    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    // Create or Update a Video MetaData
    public MetaData savemetaData(MetaData metaData) {
        return metaDataRepository.save(metaData);
    }


    // Read: Get all videos
    public Page<ProjectedVideoMetaData> getAllVideosMetaData(Boolean published, Pageable pageable) {
        return metaDataRepository.getProjectedVideoMetaData(published,pageable);
    }

    // Read: Get all videos by director
    public Page<ProjectedVideoMetaData> getAllVideosMetaDataByDirector(String director,Pageable pageable) {
        return metaDataRepository.getProjectedVideoMetaDataByDirectorContainsIgnoreCase(director,pageable);
    }

    // Read: Get a published video
    public Optional<Video> getPublishedVideoById(Long id) {
        Optional<Video> video = videoRepository.findById(id);

        // return published only video
        if (video.isPresent() && video.get().getPublished()){

            // update stats
            Optional<MetaData> metaData =  metaDataRepository.findById(id);
            metaData.ifPresent(video.get()::setMetaData);
            this.updateVideoStatsViews(video.get());

            return video;
        }else{
            return Optional.empty() ;
        }

    }

    // Read: play video
    public Optional<Video> playPublishedVideoById(Long id) {
        Optional<Video> video = videoRepository.findById(id);

        // return published only video
        if (video.isPresent() && video.get().getPublished()){

            // update stats
            Optional<MetaData> metaData =  metaDataRepository.findById(id);
            metaData.ifPresent(video.get()::setMetaData);
            this.updateVideoStatsImpressions(video.get());

            return video;
        }else{
            return Optional.empty() ;
        }

    }

    // get video stats
    public Optional<List<Stats>> getVideoStatsByVideoId(Long id) {
        Optional<Video> video = videoRepository.findById(id);

        // return published only video
        if (video.isPresent() && video.get().getPublished()){

            List<Stats> stats  = video.get().getStats().stream().toList();
            return Optional.of(stats);
        }else{
            return Optional.empty() ;
        }

    }


    // Read: Get a published video
    public Optional<Video> getVideoById(Long id) {
       return videoRepository.findById(id);
    }

    // Read: Get a specific video
    public Optional<MetaData> getVideoMetaDataById(Long id) {
        return metaDataRepository.findById(id);
    }

    private void updateVideoStatsViews(Video video) {

       List<Stats> stats  = video.getStats().stream()
                .filter(p -> Objects.equals(p.getUserId(), 1L)).toList();

       if (stats.size() == 1){
           Stats statsObj = stats.getFirst();

            statsObj.setViews(statsObj.getViews() + 1);
            statsRepository.save(statsObj);

       }else{
            Stats statsObj = new Stats();
            statsObj.setViews(1L);
            statsObj.setUserId(1L);
            statsObj.setVideo(video);
            statsRepository.save(statsObj);

       }
    }

    private void updateVideoStatsImpressions(Video video) {

        List<Stats> stats  = video.getStats().stream()
                .filter(p -> Objects.equals(p.getUserId(), 1L)).toList();

        if (stats.size() == 1){
            Stats statsObj = stats.getFirst();
            statsObj.setImpressions(statsObj.getImpressions() + 1);
            statsRepository.save(statsObj);

        }else{
            Stats statsObj = new Stats();
            statsObj.setImpressions(1L);
            statsObj.setUserId(1L);
            statsObj.setVideo(video);
            statsRepository.save(statsObj);
        }
    }

}
