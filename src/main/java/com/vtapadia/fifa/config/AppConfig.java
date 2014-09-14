package com.vtapadia.fifa.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class AppConfig {
    Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("${eFifa.enviroment:local}")
    String environment;

    @Value("${eFifa.port:8014}")
    Integer port;

    /**
     * Environment variable set as the application directory
     */
    @Value("${user.dir}")
    String userDirectory;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        JettyEmbeddedServletContainerFactory jetty = new JettyEmbeddedServletContainerFactory();
        jetty.setContextPath("/eFifa");
        jetty.setPort(port);
        log.debug("Environment setup for : " + environment);
        try {
            String documentRoot;
            switch(environment) {
                case "prod":
                case "test":
                    documentRoot = userDirectory + File.separator + "eFifaApp.jar";
                    break;
                case "local":
                    documentRoot = userDirectory + File.separator + "src//main//resources";
                    break;
                default:
                    throw new RuntimeException("environment not set or incorrect value");
            }
            log.debug("Setting documentRoot to {}", documentRoot);
            jetty.setDocumentRoot(new File(documentRoot));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return jetty;
    }
}
