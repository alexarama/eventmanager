package com.example.eventmanager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/api/users/authenticate")
    Map<String, String> authenticate(@RequestBody Map<String, String> credentials);

    @GetMapping("/api/users")
    List<Map<String, Object>> findAll();
}