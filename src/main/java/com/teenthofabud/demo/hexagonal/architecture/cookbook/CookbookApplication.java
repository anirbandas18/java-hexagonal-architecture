package com.teenthofabud.demo.hexagonal.architecture.cookbook;

import com.teenthofabud.core.common.configuration.TOABMongoAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {TOABMongoAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class })
public class CookbookApplication {
    public static void main(String[] args) {
        SpringApplication.run(CookbookApplication.class);
    }

}
