package com.itechart.springsecuritydemo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    public static final String USER_ROLE_UPDATED_QUEUE = "user.role.updated";
    public static final String USER_ROLE_UPDATED_EXCHANGE = "user.role.exchange";

    @Bean
    public Queue userRoleUpdatedQueue() {
        return new Queue(USER_ROLE_UPDATED_QUEUE, true);
    }

    @Bean
    public TopicExchange userRoleExchange() {
        return new TopicExchange(USER_ROLE_UPDATED_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue userRoleUpdatedQueue, TopicExchange userRoleExchange) {
        return BindingBuilder.bind(userRoleUpdatedQueue)
                .to(userRoleExchange)
                .with("user.role.updated");
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

}
