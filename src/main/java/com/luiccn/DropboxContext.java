package com.luiccn;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
public class DropboxContext {

    private final String DROPBOX_TOKEN = "dropbox.token";

    @Resource
    private Environment environment;

    @Bean
    public DbxClientV2 dropboxClient() {
        String token = environment.getRequiredProperty(DROPBOX_TOKEN);

        DbxRequestConfig config = new DbxRequestConfig("mobilabs");
        return new DbxClientV2(config, token);
    }


}
