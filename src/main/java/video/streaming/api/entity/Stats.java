package video.streaming.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stats",
        uniqueConstraints=
@UniqueConstraint(columnNames={"video_id", "userId"}))
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "video_id")
    @JsonIgnore
    private Video video;

    private Long impressions = 0L;

    private Long views = 0L;
}
