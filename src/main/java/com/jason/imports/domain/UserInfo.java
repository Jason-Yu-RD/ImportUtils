package com.jason.imports.domain;

import java.sql.Timestamp;

/**
 * Created by JasonAuto on 2016/9/1.
 */
public class UserInfo {

    private String pin;

    private String email;

    private String phone;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
