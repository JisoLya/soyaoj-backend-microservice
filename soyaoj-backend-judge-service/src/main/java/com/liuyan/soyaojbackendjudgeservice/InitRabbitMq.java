package com.liuyan.soyaojbackendjudgeservice;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InitRabbitMq {

    public static void doInit() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String exchangeName = "code_exchange";
            channel.exchangeDeclare(exchangeName, "direct");

            String queueName = "code_queue";
            channel.queueDeclare(queueName, false, false, false, null);
            channel.queueBind(queueName, exchangeName, "my_routKey");
            log.info("消息队列启动成功");
        }catch (Exception e) {
            log.error("消息队列启动失败");
        }
    }

    public static void main(String[] args) {
        doInit();
    }
}
