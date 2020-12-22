package com.fantechlabs.lailaa.request_models;


import lombok.Getter;

//******************************************************
@Getter
public class DocumentRequest
//******************************************************
{

    private String user_private_code;
    private int id;
    private String file;
    private String type;

    //******************************************************
    public static  DocumentRequest Builder()
    //******************************************************
    {
        return new DocumentRequest();
    }

    public void setUser_private_code(String user_private_code) {
        this.user_private_code = user_private_code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setType(String type) {
        this.type = type;
    }
}