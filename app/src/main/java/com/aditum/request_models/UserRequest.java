package com.aditum.request_models;

import lombok.Getter;

//******************************************************
@Getter
public class UserRequest
//******************************************************
{
    private int userId;
    private String email;
    private String password;
    private String name;


    //******************************************************
    public static UserRequest Builder()
    //******************************************************
    {
        return new UserRequest();
    }

    //******************************************************
    public UserRequest setUserId(int userId)
    //******************************************************
    {
        this.userId = userId;
        return this;
    }

    //******************************************************
    public UserRequest setEmail(String email)
    //******************************************************
    {
        this.email = email;
        return this;
    }

    //******************************************************
    public UserRequest setPassword(String password)
    //******************************************************
    {
        this.password = password;
        return this;
    }

    public UserRequest setName(String name)
    {
        this.name = name;
        return this;
    }
    
}
