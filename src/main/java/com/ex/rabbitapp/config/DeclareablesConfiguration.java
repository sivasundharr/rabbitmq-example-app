package com.ex.rabbitapp.config;

import com.ex.rabbitapp.model.Receiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeclareablesConfiguration {
    @Value("${topic.queueName.one}")
    private String queueOneName;
    @Value("${topic.queueName.two}")
    private String queueTwoName;
    @Value("${topicExchange1}")
    private String topicExchangeName;

    @Value("${topic.exchange.routing-key.one}")
    private String routingKeyOne;

    @Value("${topic.exchange.routing-key.two}")
    private String routingKeyTwo;
    @Bean
    public Queue queueOne(){
        return new Queue(queueOneName,false);
    }

    @Bean
    public Queue queueTwo(){
        return new Queue(queueTwoName,false);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queueOne())
                .to(topicExchange()).with(routingKeyOne);
    }

    @Bean
    public Binding bindingTwo(){
        return BindingBuilder.bind(queueTwo())
                .to(topicExchange()).with(routingKeyTwo);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(Receiver receiver){
        return new MessageListenerAdapter(receiver,"receiveMessage");
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter){

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueOneName);
        container.setMessageListener(listenerAdapter);
        return container;

    }

}
