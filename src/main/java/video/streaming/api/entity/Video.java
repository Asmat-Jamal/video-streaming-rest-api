package video.streaming.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "video")
public class Video extends BaseEntity {

    // Getters and setters
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private Boolean published = false;
    @Setter
    @Getter
    private String content;

    @Setter
    @Getter
    @OneToOne(mappedBy = "video", cascade = CascadeType.ALL)
    private MetaData metaData;

    @Setter
    @Getter
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL,fetch = FetchType.LAZY )
    @JsonIgnore
    private Set<Stats> stats;

    public Video() {
    }



    public void setPublished(Boolean published) {
        this.published = published;
        if (this.metaData != null) {
            this.metaData.setPublished(published);
        }
    }


}


