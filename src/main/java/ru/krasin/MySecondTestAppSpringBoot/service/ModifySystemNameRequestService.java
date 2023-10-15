package ru.krasin.MySecondTestAppSpringBoot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.krasin.MySecondTestAppSpringBoot.model.Request;

@Service
public class ModifySystemNameRequestService implements ModifyRequestService{
    private static final Logger logger = LoggerFactory.getLogger(ModifySystemNameRequestService.class);

    @Override
    public void modify(Request request) {
        long startTime = System.currentTimeMillis(); // Засекаем время начала
        request.setSystemName("Service 1");

        HttpEntity<Request> httpEntity = new HttpEntity<>(request);

        // Отправляем запрос

        long endTime = System.currentTimeMillis(); // Засекаем время окончания
        long executionTime = endTime - startTime; // Вычисляем время выполнения в миллисекундах
        logger.info("Time elapsed in Service 1: {} ms", executionTime); // Выводим время в лог


        new RestTemplate().exchange("http://localhost:8090/feedback",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                });
    }
}
