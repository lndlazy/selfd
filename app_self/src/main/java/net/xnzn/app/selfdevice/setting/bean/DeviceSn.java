package net.xnzn.app.selfdevice.setting.bean;

public class DeviceSn {

    private String deviceSn;

    public DeviceSn() {
    }

    public DeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    @Override
    public String toString() {
        return "DeviceSn{" +
                "deviceSn='" + deviceSn + '\'' +
                '}';
    }
}

