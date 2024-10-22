package com.malvin.assetregister.request;

import lombok.Data;

@Data
public class CreateUserReq {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
