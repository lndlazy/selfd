package net.xnzn.app.selfdevice.login.bean.request;


public class UserLoginBean  {
    private String serialNum;
    private String  custId;
    private String authCode;
    private String merchantId;
    private String aliUid;
    private String aliAuthCode;

    public UserLoginBean() {
    }

    public String getAliUid() {
        return aliUid;
    }

    public void setAliUid(String aliUid) {
        this.aliUid = aliUid;
    }

    public String getAliAuthCode() {
        return aliAuthCode;
    }

    public void setAliAuthCode(String aliAuthCode) {
        this.aliAuthCode = aliAuthCode;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}