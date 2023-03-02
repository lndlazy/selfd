package net.xnzn.app.selfdevice.login.bean.request;

import java.io.Serializable;

public class LoginUserResp implements Serializable {


    /**
     * id : 103307597204959232
     * merchantId : 103307466409783296
     * canteenEffId : -1
     * custEffId : -1
     * dishesEffId : -1
     * username : 13900000000
     * user_id : 103307597204959232
     * merchant_id : 103307466409783296
     */

    private String id;
    private String merchantId;
    private String canteenEffId;
    private String custEffId;
    private String dishesEffId;
    private String username;
    private String user_id;
    private String merchant_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCanteenEffId() {
        return canteenEffId;
    }

    public void setCanteenEffId(String canteenEffId) {
        this.canteenEffId = canteenEffId;
    }

    public String getCustEffId() {
        return custEffId;
    }

    public void setCustEffId(String custEffId) {
        this.custEffId = custEffId;
    }

    public String getDishesEffId() {
        return dishesEffId;
    }

    public void setDishesEffId(String dishesEffId) {
        this.dishesEffId = dishesEffId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    @Override
    public String toString() {
        return "LoginUserResp{" +
                "id='" + id + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", canteenEffId='" + canteenEffId + '\'' +
                ", custEffId='" + custEffId + '\'' +
                ", dishesEffId='" + dishesEffId + '\'' +
                ", username='" + username + '\'' +
                ", user_id='" + user_id + '\'' +
                ", merchant_id='" + merchant_id + '\'' +
                '}';
    }
}