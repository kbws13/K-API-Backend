package xyz.kbws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xyz.kbws.client.KApiClient;

/**
 * @author kbws
 * @date 2024/7/13
 * @description:
 */
@Configuration
@ConfigurationProperties("kapi.client")
@Data
@ComponentScan
public class KApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public KApiClient kApiClient() {
        return new KApiClient(accessKey, secretKey);
    }
}
