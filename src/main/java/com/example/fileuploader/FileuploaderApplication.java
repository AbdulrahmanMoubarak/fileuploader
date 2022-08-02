package com.example.fileuploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FileuploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileuploaderApplication.class, args);
		System.out.println("Started");
	}


	//1659450907243
}
