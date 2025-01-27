package video.streaming.api.models;

import jakarta.validation.constraints.NotBlank;

public class VideoModel {
    @NotBlank(message = "Content is mandatory")
    public String content;
}
