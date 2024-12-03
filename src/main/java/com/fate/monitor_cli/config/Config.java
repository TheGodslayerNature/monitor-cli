package com.fate.monitor_cli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import oshi.SystemInfo;

@Configuration
public class Config {
    @Bean
    public SystemInfo systemInfo(){
        return new SystemInfo();
    }
}
