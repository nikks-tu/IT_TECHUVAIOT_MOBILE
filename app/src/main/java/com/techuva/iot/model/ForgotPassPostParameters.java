package com.techuva.iot.model;

public class ForgotPassPostParameters {
    private String EmailId;


    public ForgotPassPostParameters(String emailId) {
        EmailId = emailId;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }


}
