package com.task.mobileshare.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQDirectConfig {

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("mobile.share.exchange", true, false);
    }

    @Bean
    public Queue queue() {
        return new Queue("notification", true);
    }

    @Bean
    Binding exchangeBinding(DirectExchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("notification.key");
    }
}
