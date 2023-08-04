package org.pl.deenes.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class SpringConfiguration {

    @Bean
    public Double lastKilometer() {
        return 0.0;
    }

    @Bean
    public File file() {
        return new File("src/main/resources/RJ_SKRJ_666401_464028_9.pdf");
    }
}
