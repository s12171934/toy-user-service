package org.solo.toyuserservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.solo.toyuserservice.dto.JoinDTO;
import org.solo.toyuserservice.entity.UserEntity;
import org.solo.toyuserservice.repository.UserRepository;
import org.solo.toyuserservice.util.PassportUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PassportUtil passportUtil;

    public UserService(UserRepository userRepository, PassportUtil passportUtil) {

        this.userRepository = userRepository;
        this.passportUtil = passportUtil;
    }

    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    public ResponseEntity<?> join(JoinDTO joinDTO) {

        UserEntity userEntity = new UserEntity();

        boolean checkPassword = joinDTO.getPassword().equals(joinDTO.getPasswordCheck());
        if(!checkPassword) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.UNAUTHORIZED);
        }

        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        if(isUser) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        userEntity.setUsername(joinDTO.getUsername());
        userEntity.setPassword(bCryptPasswordEncoder().encode(joinDTO.getPassword()));
        userEntity.setEmail(joinDTO.getEmail());
        userEntity.setName(joinDTO.getName());
        userEntity.setRole("ROLE_USER");

        userRepository.save(userEntity);

        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    public UserEntity getUserInfo(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        String passportJson = request.getHeader("passport");
        if(passportJson == null) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        UserEntity userEntity = userRepository.findByUsername(passportUtil.getUsername(passportJson));

        if(userEntity == null) {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        return userEntity;
    }

    @Transactional
    public ResponseEntity<?> updateUserInfo(HttpServletRequest request, UserEntity userEntity) throws JsonProcessingException {

        String passportJson = request.getHeader("passport");
        if(passportJson == null) {

            return new ResponseEntity<>("Passport not set", HttpStatus.UNAUTHORIZED);
        }

        String username = passportUtil.getUsername(passportJson);
        userRepository.updateByUsername(username, userEntity.getName(), userEntity.getEmail());

        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteUser(HttpServletRequest request) throws JsonProcessingException {

        String passportJson = request.getHeader("passport");
        if(passportJson == null) {

            return new ResponseEntity<>("Passport not set", HttpStatus.UNAUTHORIZED);
        }

        String username = passportUtil.getUsername(passportJson);
        userRepository.deleteByUsername(username);

        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }
}
