package com.runnersteam.mail;

import com.runnersteam.mail.messaging.RunnersMailBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
public class RunnersmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunnersmailApplication.class, args);
	}

}
