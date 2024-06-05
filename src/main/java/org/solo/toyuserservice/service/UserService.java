package org.solo.toyuserservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.solo.toyuserservice.dto.JoinDTO;
import org.solo.toyuserservice.entity.UserEntity;
import org.solo.toyuserservice.kafka.KafkaProducerService;
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
    private final KafkaProducerService kafkaProducerService;

    public UserService(UserRepository userRepository,
                       PassportUtil passportUtil,
                       KafkaProducerService kafkaProducerService) {

        this.userRepository = userRepository;
        this.passportUtil = passportUtil;
        this.kafkaProducerService = kafkaProducerService;
    }

    public BCryptPasswordEncoder bCryptPasswordEncoder() { //회원가입시 비밀번호 암호화하여 DB에 저장시 필요

        return new BCryptPasswordEncoder();
    }

    public ResponseEntity<?> join(JoinDTO joinDTO) {

        UserEntity userEntity = new UserEntity();

        //비밀번호 확인 검증
        boolean checkPassword = joinDTO.getPassword().equals(joinDTO.getPasswordCheck());
        if(!checkPassword) {
            return new ResponseEntity<>("Passwords do not match", HttpStatus.UNAUTHORIZED);
        }

        //이미 존재하는 username인지 중복 검증
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        if(isUser) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        //검증 완료 후 DB에 user 저장
        userEntity.setUsername(joinDTO.getUsername());
        userEntity.setPassword(bCryptPasswordEncoder().encode(joinDTO.getPassword()));
        userEntity.setEmail(joinDTO.getEmail());
        userEntity.setName(joinDTO.getName());
        userEntity.setRole("ROLE_USER");

        userRepository.save(userEntity);

        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    public UserEntity getUserInfo(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        //로그인 상태일시 본인 정보 조회를 위해 passport 검증
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

        //로그인 상태일시 본인 정보 조회를 위해 passport 검증
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

        //로그인 상태일시 본인 정보 조회를 위해 passport 검증
        String passportJson = request.getHeader("passport");
        if(passportJson == null) {

            return new ResponseEntity<>("Passport not set", HttpStatus.UNAUTHORIZED);
        }

        String username = passportUtil.getUsername(passportJson);
        userRepository.deleteByUsername(username);

        //kafka topic - user-delete에 삭제된 username 전송
        kafkaProducerService.sendMessage("user-delete",username);

        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }
}
