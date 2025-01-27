package video.streaming.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "metaData")
@AllArgsConstructor
public class MetaData extends BaseEntity {

    @Id
    @Column(name = "video_id", nullable = false)
    private Long video_id;

    private String title;

    private String synopsis;

    private String cast;

    private String director;

    private String mainActor;

    private Integer releaseYear;

    private String genre;

    private Integer duration =  0;
    private Boolean published = false;

    @OneToOne
    @JoinColumn(name = "video_id")
    @JsonIgnore
    private Video video;


    // Getters and setters
    public Long getId() {
        return video_id;
    }

    public void setId(Long id) {
        this.video_id = id;
    }

    public MetaData() {
    }
}
