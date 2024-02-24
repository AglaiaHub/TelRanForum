package java51.forum.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ModelMapper getmodelMapper () {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()  //https://modelmapper.org/user-manual/configuration/
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        ;
        return modelMapper;
    }
}
