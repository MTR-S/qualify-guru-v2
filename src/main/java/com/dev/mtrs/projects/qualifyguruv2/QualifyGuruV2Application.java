package com.dev.mtrs.projects.qualifyguruv2;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;

@SpringBootApplication
public class QualifyGuruV2Application {

	public static void main(String[] args) {
		SpringApplication.run(QualifyGuruV2Application.class, args);
	}

}
