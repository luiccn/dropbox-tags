package com.luiccn;

import com.dropbox.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
@RestController
@Import({SolrContext.class, DropboxContext.class})
public class DropboxTagsApplication {

    public static void main(String[] args) throws IOException, DbxException {

        // Open properties, we are using it to save the app token
        FileInputStream in = new FileInputStream("application.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();

        if (props.getProperty("dropbox.token").isEmpty()) {
            DbxRequestConfig requestConfig = new DbxRequestConfig(props.getProperty("dropbox.appname"));
            DbxAppInfo appInfo = new DbxAppInfo(props.getProperty("dropbox.key"), props.getProperty("dropbox.secret"));
            DbxWebAuth auth = new DbxWebAuth(requestConfig, appInfo);
            DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                    .withNoRedirect()
                    .build();

            String authorizeUrl = auth.authorize(authRequest);
            System.out.println("1. Go to " + authorizeUrl);
            System.out.println("2. Click \"Allow\" (you might have to log in first).");
            System.out.println("3. Copy the authorization code.");
            System.out.print("Enter the authorization code here: ");

            Scanner scan = new Scanner(System.in);
            String code = scan.nextLine();

            if (code != null) {
                code = code.trim();
                DbxAuthFinish authFinish = auth.finishFromCode(code);
                String accessToken = authFinish.getAccessToken();

                System.out.println("Access token:");
                System.out.println(accessToken);

                FileOutputStream out = new FileOutputStream("application.properties");
                props.setProperty("dropbox.token", accessToken);
                props.store(out, null);
                out.close();
            }
        }

        SpringApplication.run(DropboxTagsApplication.class, args);
    }
}