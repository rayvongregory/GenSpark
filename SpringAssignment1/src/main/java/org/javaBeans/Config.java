package org.javaBeans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// this class contains the bean configurations
public class Config {
    // we can define specific beans here like we would in the XML config, but it'll look like hot garbage
    // no component scanning
    @Bean
    public Student getStudent() {
        return new Student();
    }
    @Bean
    public Address getAddress() {
        return new Address();
    }
    @Bean
    public Phone getPhone() {
        return new Phone();
    }

}
