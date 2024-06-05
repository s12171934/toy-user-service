package org.solo.toyuserservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.*;
import org.solo.toyuserservice.dto.JoinDTO;
import org.solo.toyuserservice.entity.UserEntity;
import org.solo.toyuserservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("")
    public UserEntity getUserInfo(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        return userService.getUserInfo(request, response);
    }

    @PostMapping("")
    public ResponseEntity<?> join(@RequestBody JoinDTO joinDTO) {

        System.out.println(joinDTO.getUsername());
        return userService.join(joinDTO);
    }

    @PutMapping("")
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request, @RequestBody UserEntity userEntity) throws JsonProcessingException {

        System.out.println(userEntity.getUsername());
        return userService.updateUserInfo(request, userEntity);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) throws JsonProcessingException {

        return userService.deleteUser(request);
    }
}
