package com.xapi.xapiinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author 15304
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class XapiInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XapiInterfaceApplication.class, args);
    }

}
