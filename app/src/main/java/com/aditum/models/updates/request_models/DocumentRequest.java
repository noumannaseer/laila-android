package com.aditum.models.updates.request_models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//**********************************************************
public class DocumentRequest
//**********************************************************
{
    private int user_id;
    private String file;
    private String type;
    private String token;

    public static DocumentRequest Builder() {
        return new DocumentRequest();
    }
}
