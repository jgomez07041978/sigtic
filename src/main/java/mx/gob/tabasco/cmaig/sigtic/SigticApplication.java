package mx.gob.tabasco.cmaig.sigtic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class SigticApplication {

	public static void main(String[] args) {
		SpringApplication.run(SigticApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-6"));
		System.out.println("Date in UTC: " + new Date().toString());
	}

}
