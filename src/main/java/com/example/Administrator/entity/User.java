package com.example.Administrator.entity;

//import lombok.AllArgsConstructor;
//import lombok.Builder;
import lombok.Data;
//import lombok.NoArgsConstructor;

//auto generate setter and getter
@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
public class User {
    private int id;
    private String name;
    private String password;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private String token;



}
