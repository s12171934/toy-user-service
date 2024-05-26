package org.solo.toyuserservice.dto;

import lombok.Data;

@Data
public class JoinDTO {

    private String username;
    private String password;
    private String passwordCheck;
    private String name;
    private String email;
}
