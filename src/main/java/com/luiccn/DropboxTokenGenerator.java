package com.luiccn;

import com.dropbox.core.*;
import com.dropbox.core.json.JsonReader;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class DropboxTokenGenerator {

    public static void main(String[] args) throws JsonReader.FileLoadException, DbxException, IOException {

        FileInputStream in = new FileInputStream("application.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();

        DbxRequestConfig requestConfig = new DbxRequestConfig(props.getProperty("dropbox.appname"));
        //DbxAppInfo appInfo = new DbxAppInfo("i659xd69chxiq39", "4g1pyg395bqvick");
        DbxAppInfo appInfo = new DbxAppInfo(args[0], args [1]);
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


}