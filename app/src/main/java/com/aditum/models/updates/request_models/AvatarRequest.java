package com.aditum.models.updates.request_models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//**********************************************************
public class AvatarRequest
//**********************************************************
{
    private int user_id;
    private String token;
    private String file;

    public static AvatarRequest Builder() {
        return new AvatarRequest();
    }
}
