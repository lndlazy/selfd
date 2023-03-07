package net.xnzn.app.selfdevice.common;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import net.xnzn.lib_utils.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class LeniuUsbManger {

    //普通摄像头
    public static final int NORMAL_RGB_VID = 5546;
    public static final int NORMAL_RGB_PID = 5461;

    public static final int NORMAL_IR_VID = 5512;
    public static final int NORMAL_IR_PID = 5529;


    //扁平款摄像头
    public static final int H_RGB_VID = 6997;
    public static final int H_RGB_PID = 1412;

    public static final int H_IR_VID = 6997;
    public static final int H_IR_PID = 1413;


    //809菜品拍照
    public static final int COLLECT_VID = 8964;
    public static final int COLLECT_PID = 18819;

    //USB管理器:负责管理USB设备的类
    private UsbManager mUsbManager;
    private Context mContext;

    public boolean hasCollectCamera() {
        return findDevice(COLLECT_VID, COLLECT_PID) != null;
    }

    public boolean isCollectCamera(UsbDevice device) {
        if (COLLECT_VID == device.getVendorId() && COLLECT_PID == device.getProductId()) {
            return true;
        }
        return false;
    }


    private static LeniuUsbManger instance = new LeniuUsbManger();


    public static LeniuUsbManger getInstance() {
        return instance;
    }

    private LeniuUsbManger() {
        mContext = AppUtil.getApplication();
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
    }



    public boolean hasFaceCamera() {
        if (hasNormalRgbCamera() || hasHRgbCamera()) {
            return true;
        }
        return false;
    }

    public boolean hasNormalRgbCamera() {
        return findDevice(NORMAL_RGB_VID, NORMAL_RGB_PID) != null;
    }

    public boolean isNormalRgbCamera(UsbDevice device) {
        if (NORMAL_RGB_VID == device.getVendorId() && NORMAL_RGB_PID == device.getProductId()) {
            return true;
        }
        return false;
    }


    public boolean hasNormalIRCamera() {
        return findDevice(NORMAL_IR_VID, NORMAL_IR_PID) != null;
    }

    public boolean isNormalIRCamera(UsbDevice device) {
        if (NORMAL_IR_VID == device.getVendorId() && NORMAL_IR_PID == device.getProductId()) {
            return true;
        }
        return false;
    }


    public boolean hasHRgbCamera() {
//        return findDevice(H_RGB_VID, H_RGB_PID) != null;
        return findDevice(NORMAL_IR_VID, NORMAL_IR_PID) != null;
    }

    public boolean isHRgbCamera(UsbDevice device) {
        if (H_RGB_VID == device.getVendorId() && H_RGB_PID == device.getProductId()) {
            return true;
        }
        return false;
    }


    public boolean hasHIRCamera() {
        return findDevice(H_IR_VID, H_IR_PID) != null;
    }


    public boolean isHIRCamera(UsbDevice device) {
        if (H_IR_VID == device.getVendorId() && H_IR_PID == device.getProductId()) {
            return true;
        }
        return false;
    }

    //查詢指定设备
    public UsbDevice findDevice(int venderId, int productId) {

        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice usb = deviceIterator.next();
            if (venderId == usb.getVendorId() && productId == usb.getProductId()) {//找到指定设备
                //找到USB设备
                return usb;
            }
        }
        return null;
    }


    //采集摄像头
    public List<UsbDevice> getCollectCameras() {
        List<UsbDevice> list = new ArrayList<>();
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice usb = deviceIterator.next();
            if (isCollectCamera(usb)) {
                list.add(usb);
            }
        }
        return list;

    }
}