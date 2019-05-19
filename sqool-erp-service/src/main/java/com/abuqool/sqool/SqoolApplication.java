package com.abuqool.sqool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class SqoolApplication {
    public static void main(String[] args) {
        SpringApplication.run(SqoolApplication.class, args);
    }
//
//    /**
//     * 同时支持HTTP和HTTPS
//     */
//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        // connector.setScheme("http");
//        // connector.setSecure(false);
//        // connector.setRedirectPort(8443);
//        connector.setPort(80);
//
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        tomcat.addAdditionalTomcatConnectors(connector);
//        return tomcat;
//    }

}