package org.solo.toyuserservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.solo.toyuserservice.dto.JoinDTO;
import org.solo.toyuserservice.entity.UserEntity;
import org.solo.toyuserservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/user/info")
    public UserEntity getUserInfo(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        return userService.getUserInfo(request, response);
    }

    @PostMapping("/user/join")
    public ResponseEntity<?> join(@RequestBody JoinDTO joinDTO) {

        System.out.println(joinDTO.getUsername());
        return userService.join(joinDTO);
    }

    @PutMapping("/user/info")
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request, @RequestBody UserEntity userEntity) throws JsonProcessingException {

        System.out.println(userEntity.getUsername());
        return userService.updateUserInfo(request, userEntity);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) throws JsonProcessingException {

        return userService.deleteUser(request);
    }
}
