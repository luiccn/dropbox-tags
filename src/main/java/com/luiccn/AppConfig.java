package com.luiccn;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${dropbox.download_size}")
    private long maximumDownloadSize;

    public AppConfig() {
    }

    public long getMaximumDownloadSize() {
        return maximumDownloadSize;
    }
}
