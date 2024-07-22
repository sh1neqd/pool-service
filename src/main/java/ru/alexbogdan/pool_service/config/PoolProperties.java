package ru.alexbogdan.pool_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pool.working-hours")
@Data
public class PoolProperties {
    private String start;
    private String end;
}