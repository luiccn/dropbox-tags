package com.luiccn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Import({ SolrContext.class, DropboxContext.class})
public class DropboxTagsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DropboxTagsApplication.class, args);
	}
}
