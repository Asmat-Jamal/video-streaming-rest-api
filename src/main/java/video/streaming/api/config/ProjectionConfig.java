package video.streaming.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

public class ProjectionConfig {

    @Bean
    public SpelAwareProxyProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }
}
