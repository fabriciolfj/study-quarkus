package com.github.fabriciolfj.bank.infra;

import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigProperties(prefix = "bank-support")
public class BankSupportConfig {

    private String phone;

    private String email;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
}
