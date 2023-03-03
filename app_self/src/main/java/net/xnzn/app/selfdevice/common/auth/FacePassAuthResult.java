package net.xnzn.app.selfdevice.common.auth;

public class FacePassAuthResult {
    private boolean isAuth;
    private String message = "授权异常.";


    public FacePassAuthResult() {

    }


    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "FacePassAuthResult{" +
                "isAuth=" + isAuth +
                ", message='" + message + '\'' +
                '}';
    }
}