package com.test.gateway.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="app")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppConfig {
    private long maxDeviceCount;
}