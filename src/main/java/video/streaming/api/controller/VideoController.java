package video.streaming.api.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import video.streaming.api.entity.MetaData;
import video.streaming.api.entity.Stats;
import video.streaming.api.entity.Video;
import video.streaming.api.models.MetaDataModel;
import video.streaming.api.models.VideoModel;
import video.streaming.api.projection.ProjectedVideoMetaData;
import video.streaming.api.service.VideoService;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);


    @Autowired
    private VideoService videoService;

    // Create
    @PostMapping
    public ResponseEntity<?> createVideo(@Valid @RequestBody VideoModel video) {
        Video newObject = new Video();
        newObject.setContent(video.content);
        Video newVideo = videoService.saveVideo(newObject);
        if (newVideo != null) {
            return ResponseEntity.ok(newVideo);
        } else {
            return ResponseEntity.internalServerError().build();
        }

    }

     // Create or update meta data
    @PostMapping("/metadata/{id}")
    public ResponseEntity<MetaData> createMetaData(@PathVariable Long id, @Valid @RequestBody MetaDataModel metaData) {

        MetaData newMetaDataObj = new MetaData();

        return videoService.getVideoById(id)
                .map(video -> {
                    newMetaDataObj.setId(video.getId());
                    newMetaDataObj.setTitle(metaData.title);
                    newMetaDataObj.setSynopsis(metaData.synopsis);
                    newMetaDataObj.setDirector(metaData.director);
                    newMetaDataObj.setMainActor(metaData.mainActor);
                    newMetaDataObj.setReleaseYear(metaData.releaseYear);
                    newMetaDataObj.setGenre(metaData.genre);
                    newMetaDataObj.setDuration(metaData.duration);
                    newMetaDataObj.setCast(metaData.cast);
                    MetaData updatedMetaData = videoService.savemetaData(newMetaDataObj);
                    return ResponseEntity.ok(updatedMetaData);
                })
                .orElse(ResponseEntity.notFound().build());

    }


    // Update Video
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVideo(@PathVariable Long id, @Valid @RequestBody VideoModel newVideoData) {

        return videoService.getVideoById(id)
                .map(video -> {
                    video.setContent(newVideoData.content);
                    Video updatedVideo = videoService.saveVideo(video);
                    return ResponseEntity.ok(updatedVideo);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // Read all videos based on published
    @GetMapping("getall/{published}")
    public ResponseEntity<?> getAllVideos(@PathVariable boolean published, Pageable pageable) {

        Page<ProjectedVideoMetaData> videos = videoService.getAllVideosMetaData(published, pageable);
        return ResponseEntity.ok(videos);


    }


    // Read all by director
    @GetMapping("search/{director}")
    public ResponseEntity<?> getAllVideosByDirector(@PathVariable String director, Pageable pageable) {

        Page<ProjectedVideoMetaData> videos = videoService.getAllVideosMetaDataByDirector(director, pageable);
        return ResponseEntity.ok(videos);
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long id) {
        return videoService.getPublishedVideoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // get  video stats
    @GetMapping("stats/{id}")
    public ResponseEntity<List<Stats>> getVideoStatsById(@PathVariable Long id) {
        return videoService.getVideoStatsByVideoId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // play video
    @GetMapping("play/{id}")
    public ResponseEntity<Video> playVideoById(@PathVariable Long id) {
        return videoService.playPublishedVideoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/publish/{id}")
    public ResponseEntity<?> publishVideo(@PathVariable Long id) {

        return videoService.getVideoById(id)
                .map(video -> {

                    MetaData metaData = video.getMetaData();
                    if (metaData != null) {
                        video.setPublished(true);

                        Video updatedVideo = videoService.saveVideo(video);
                        return ResponseEntity.ok(updatedVideo);
                    } else {
                        return ResponseEntity.internalServerError().body("meta data not found");
                    }

                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delistVideo(@PathVariable Long id) {

        return videoService.getVideoById(id)
                .map(video -> {
                    MetaData metaData = video.getMetaData();
                    if (metaData != null) {
                        video.setPublished(false);

                        Video updatedVideo = videoService.saveVideo(video);
                        return ResponseEntity.ok(updatedVideo);
                    } else {
                        return ResponseEntity.internalServerError().body("meta data not found");
                    }
                }).orElse(ResponseEntity.notFound().build());
    }
}


