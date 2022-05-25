package org.java;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "org.java")
// this class contains the bean configurations
public class Config {
    // we can either define specific beans here like we would in the XML config, but it'll look like hot garbage
    // so we use the @ComponentScan annotation instead like we would in the annotation/xml approach

}
