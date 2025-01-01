package com.movie.tdmb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TdmbApplication {
	public static void main(String[] args) {
		SpringApplication.run(TdmbApplication.class, args);
	}
}
