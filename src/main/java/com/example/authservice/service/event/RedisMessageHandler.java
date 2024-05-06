package com.example.authservice.service.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RedisMessageHandler implements MessageListener {

    public static List<String> messageList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {

        log.info("TOPIC HANDLE edildi {}", message.toString());

        messageList.add(message.toString());
        log.info("Message : {}, from channel : {}", message, message.getChannel());
        messageList.forEach(System.out::println);

    }
}
