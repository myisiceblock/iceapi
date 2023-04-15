package com.iceblock.client;

import com.iceblock.client.client.IceApiClient;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("ice.client")
@Data
@ComponentScan

public class IceApiClientConfig {
    private String accessKey;

    private String secretKey;

    @Bean
    public IceApiClient iceApiClient() {
        return new IceApiClient(accessKey, secretKey);
    }

}
