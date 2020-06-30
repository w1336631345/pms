package com.kry.pms.mq.impl;

import com.kry.pms.mq.OrderMqService;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderMqServiceImpl implements OrderMqService {
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Override
    public void sendNewOrder(String topicName, String msg) {
        ActiveMQTopic destination = new ActiveMQTopic(topicName);
        jmsMessagingTemplate.convertAndSend(destination,msg);
    }
}
