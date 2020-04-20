package com.techuva.iot.model;

public class LoginPostParameters {
    private String EmailId;
    private String Password;


    public LoginPostParameters(String emailId, String password) {
        EmailId = emailId;
        Password = password;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


}
