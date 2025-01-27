package video.streaming.api.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MetaDataModel {

    @NotBlank(message = "Title is mandatory")
    public String title;

    @NotBlank(message = "Synopsis is mandatory")
    public String synopsis;

    @NotBlank(message = "Cast is mandatory")
    public String cast;

    @NotBlank(message = "Director is mandatory")
    public String director;

    @NotBlank(message = "Main Actor is mandatory")
    public String mainActor;

    @NotNull(message = "Release Year is mandatory")
    @Min(value=1800, message="Release Year: min value 1800")
    @Max(value=2100, message="Release Year: max value is 2100 ")
    public Integer releaseYear;

    @NotBlank(message = "Genre is mandatory")
    public String genre;

    @NotNull(message = "Duration is mandatory")
    @Min(value=300, message="Duration: min value 300 seconds")
    public Integer duration;
}
