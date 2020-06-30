package com.kry.pms.mq;

import org.springframework.jms.core.JmsMessagingTemplate;

import javax.annotation.Resource;

public interface OrderMqService {

    void sendNewOrder(String hotelCode, String msg);

    void sendAuditStep(String topicName, String msg);
}
