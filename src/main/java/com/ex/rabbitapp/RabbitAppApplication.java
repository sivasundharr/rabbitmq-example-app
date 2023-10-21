package com.ex.rabbitapp;

import com.ex.rabbitapp.model.Receiver;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class RabbitAppApplication {
	private final RabbitTemplate rabbitTemplate;
	private final Receiver receiver;

	@Value("${topic.exchange.name1}")
	private String exchangeName;

	public RabbitAppApplication(Receiver receiver, RabbitTemplate rabbitTemplate) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
	}
	@Bean
	public CommandLineRunner run(){
		return args->{
			System.out.println("sending message...");

			rabbitTemplate.convertAndSend(exchangeName,"hello.some.world",
					"Hello iam siva");

			receiver.getLatch().await(1000, TimeUnit.MILLISECONDS);
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(RabbitAppApplication.class, args);
	}

}
