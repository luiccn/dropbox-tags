package com.luiccn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Import({ SolrContext.class})
public class DropboxTagsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DropboxTagsApplication.class, args);
	}

	@RequestMapping("/")
	public String home() {

		return "Hello Docker World";
	}



}
