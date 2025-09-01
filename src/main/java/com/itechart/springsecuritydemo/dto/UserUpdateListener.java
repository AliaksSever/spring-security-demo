package com.itechart.springsecuritydemo.dto;

import com.itechart.springsecuritydemo.config.RabbitConfig;
import com.itechart.springsecuritydemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateListener {

    private final UserRepository userRepository;

    @RabbitListener(queues = RabbitConfig.USER_ROLE_UPDATED_QUEUE)
    public void handleRoleUpdate(UserUpdateEvent event) {
        userRepository.findByUuid(event.uuid())
                .ifPresent(user -> {
                    user.setRoles(event.newRoles());
                    userRepository.save(user);
                });
    }
}
