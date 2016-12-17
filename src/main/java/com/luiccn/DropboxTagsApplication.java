package com.luiccn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ SolrContext.class})
public class DropboxTagsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DropboxTagsApplication.class, args);
	}



}
