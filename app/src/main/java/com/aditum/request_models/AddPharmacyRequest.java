package com.aditum.request_models;

import lombok.Getter;

//******************************************************
@Getter
public class AddPharmacyRequest
//******************************************************
{
    private int id;
    private String first_name;
    private String last_name;
    private String user_private_code;
    private String contact_type;
    private String email;
    private String phone;
    private String address_line1;
    private String address_line2;
    private String address_line3;
    private String address_city;
    private String address_province;
    private String address_country;
    private String address_pobox;
    private String user_name;
    private String is_preferred;


    //******************************************************
    public static AddPharmacyRequest Builder()
    //******************************************************
    {
        return new AddPharmacyRequest();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setUser_private_code(String user_private_code) {
        this.user_private_code = user_private_code;
    }

    public void setContact_type(String contact_type) {
        this.contact_type = contact_type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public void setAddress_line3(String address_line3) {
        this.address_line3 = address_line3;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public void setAddress_province(String address_province) {
        this.address_province = address_province;
    }

    public void setAddress_country(String address_country) {
        this.address_country = address_country;
    }

    public void setAddress_pobox(String address_pobox) {
        this.address_pobox = address_pobox;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setIs_preferred(String is_preferred) {
        this.is_preferred = is_preferred;
    }
}
