package com.teenthofabud.demo.hexagonal.architecture.cookbook;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = { "com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.repository" })
@EnableTransactionManagement
public class CookbookConfiguration {

    @Profile("!test")
    @Bean
    public OpenAPI cookbookServiceAPI(@Value("${spring.application.name}") String applicationName,
                                      @Value("${res.cookbook.description}") String applicationDescription,
                                      @Value("${res.cookbook.version}") String applicationVersion) {
        return new OpenAPI()
                .info(new Info().title(applicationName)
                        .description(applicationDescription)
                        .version(applicationVersion));
    }

}
