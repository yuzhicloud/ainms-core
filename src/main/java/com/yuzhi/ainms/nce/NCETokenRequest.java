package com.yuzhi.ainms.nce;

public class NCETokenRequest {
    private String userName;
    private String password;

    public NCETokenRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
