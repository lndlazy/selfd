package net.xnzn.app.selfdevice.login.bean.request;

import java.io.Serializable;

public class UserLoginRequest implements Serializable {

    private Long user_id;
    private String username;
    private String password;
    private String scope;
    private String grant_type;
    private String role;

    public UserLoginRequest() {
    }

    public UserLoginRequest(String username, String password, String scope, String grant_type, String role) {
        this.username = username;
        this.password = password;
        this.scope = scope;
        this.grant_type = grant_type;
        this.role = role;
    }


    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

} 