package com.task.mobileshare.output.impl;

import com.task.mobileshare.output.NotificationSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitNotificationSender implements NotificationSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitNotificationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendNotification(String message) {
        rabbitTemplate.convertAndSend("mobile.share.exchange", "notification.key", message);
    }
}
