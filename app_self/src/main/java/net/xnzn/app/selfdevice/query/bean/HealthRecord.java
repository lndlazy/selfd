package net.xnzn.app.selfdevice.query.bean;

public class HealthRecord {
    private Double bloodPressureHigh;
    private Double bloodPressureLow;
    private Double bmi;
    private String custName;
    private String custNum;
    private Double heartRate;
    private Double height;
    private String mobile;
    private String orgFullName;
    private String registerDate;
    private Integer sex;//性别 1-男 2-女
    private String shape;
    private Double weight;

    public Double getBloodPressureHigh() {
        return bloodPressureHigh;
    }

    public Double getBloodPressureLow() {
        return bloodPressureLow;
    }

    public String getBloodStr() {
        return bloodPressureHigh + "/" + bloodPressureLow;
    }

    public Double getBmi() {
        return bmi;
    }

    public String getCustName() {
        return custName;
    }

    public String getCustNum() {
        return custNum;
    }

    public Double getHeartRate() {
        return heartRate;
    }

    public Double getHeight() {
        return height;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOrgFullName() {
        return orgFullName;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public Integer getSex() {
        return sex;
    }

    public String getSexStr() {
        if (getSex() == 1) {
            return "男";
        }
        if (getSex() == 2) {
            return "女";
        }
        return "";
    }

    public String getShape() {
        return shape;
    }

    public Double getWeight() {
        return weight;
    }
}
