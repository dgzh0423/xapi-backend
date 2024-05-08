package com.xapi.xapiclientsdk.config;

import com.xapi.xapiclientsdk.client.XApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 15304
 */
@Configuration
@ConfigurationProperties("xapi.client")
@Data
@ComponentScan
public class XApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public XApiClient xApiClient(){
        return new XApiClient(accessKey, secretKey);
    }

}
