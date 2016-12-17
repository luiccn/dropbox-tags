package com.luiccn;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${dropbox.downloadsize}")
    private long maximumDownloadSize;

    public AppConfig() {
    }

    public long getMaximumDownloadSize() {
        return maximumDownloadSize;
    }
}
