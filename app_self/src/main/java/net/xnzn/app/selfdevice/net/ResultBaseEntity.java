package net.xnzn.app.selfdevice.net;

public class ResultBaseEntity {

    public static final String SUCCESS = "1";

    protected String success;
    protected String error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
