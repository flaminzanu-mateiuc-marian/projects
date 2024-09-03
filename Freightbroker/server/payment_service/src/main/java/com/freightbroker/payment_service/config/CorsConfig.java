package com.freightbroker.payment_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.DatagramSocket;
import java.net.InetAddress;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    public static String getIp() {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://192.168.59.253:3000", "http://"+getIp()+":3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTION")
                .allowedHeaders("*");
    }
}
