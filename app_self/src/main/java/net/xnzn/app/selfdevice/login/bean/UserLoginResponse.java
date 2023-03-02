package net.xnzn.app.selfdevice.login.bean;

import java.io.Serializable;

public class UserLoginResponse implements Serializable {
    private long merchant_id;
    private long user_id;
    private String username;

    public long getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(long merchant_id) {
        this.merchant_id = merchant_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserLoginResponse{" +
                "merchant_id=" + merchant_id +
                ", user_id=" + user_id +
                ", username='" + username + '\'' +
                '}';
    }
}
