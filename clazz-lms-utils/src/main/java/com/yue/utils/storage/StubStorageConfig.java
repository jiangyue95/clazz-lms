package com.yue.utils.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.storage.type", havingValue = "stub")
public class StubStorageConfig {

    @Bean
    public FileStorage stubFileStorage() {
        return new StubFileStorage();
    }
}
