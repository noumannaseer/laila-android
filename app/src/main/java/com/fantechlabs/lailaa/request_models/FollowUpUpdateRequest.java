package com.fantechlabs.lailaa.request_models;


//******************************************************
public class FollowUpUpdateRequest
//******************************************************
{

    private String user_private_code;
    private String medication_id;
    private String log_DateTime;
    private String status;
    private int followup_id;


    //******************************************************
    public static FollowUpUpdateRequest Builder()
    //******************************************************
    { return new FollowUpUpdateRequest(); }

    public void setUser_private_code(String user_private_code) {
        this.user_private_code = user_private_code;
    }

    public void setMedication_id(String medication_id) {
        this.medication_id = medication_id;
    }

    public void setLog_DateTime(String log_DateTime) {
        this.log_DateTime = log_DateTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFollowup_id(int followup_id) {
        this.followup_id = followup_id;
    }
}