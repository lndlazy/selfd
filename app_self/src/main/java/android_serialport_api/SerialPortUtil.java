package android_serialport_api;

import android.util.Log;

import net.xnzn.lib_utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SerialPortUtil {

    private static final String TAG = "SerialPortUtil";
    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private ReceiveThread mReceiveThread = null;
    private boolean isStart = false;
    //private UsbManager mUsbManager;
//  private UsbPrinter mUsbPrinter;
//  private Context context;

    /**
     * 打开串口，接收数据
     * 通过串口，接收单片机发送来的数据
     */
    public void openSerialPort() {

        try {
//      this.context = context;
            //mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
//      mUsbPrinter = new UsbPrinter(context);

            SerialPortFinder serialPortFinder = new SerialPortFinder();

            String[] allDevicesPath = serialPortFinder.getAllDevicesPath();
//            System.out.println("allDevicesPath:::" + allDevicesPath.toString());

            serialPort = new SerialPort(new File("/dev/ttyS9"), 9600, 0);
//            serialPort = new SerialPort(new File("/dev/ttyS9"), 9600, 0);

//            serialPort = new SerialPort(new File("/dev/ttyS9"), 9600, 0);
            //serialPort = new SerialPort(new File("/dev/ttyACM0"), 115200, flags);//9600
            //调用对象SerialPort方法，获取串口中"读和写"的数据流
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            isStart = true;
            Log.d(TAG, "=====串口打开成功=====");

            final List<String> data = new ArrayList<>();

            if (allDevicesPath.length > 0) {
                for (String device : allDevicesPath) {

                    File file = new File(device);
                    String path = file.getAbsolutePath();

                    StringBuilder permission = new StringBuilder();
                    permission.append("[");
                    permission.append(file.canRead() ? " 可读" : "不可读");
                    permission.append(file.canWrite() ? "  可写 " : " 不可写 ");
                    permission.append(file.canExecute() ? "可执行" : "不可执行");
                    permission.append("]");

                    String msg = "(" + path + ")" + " 权限:" + permission;
                    data.add(msg);
                }
            }

            for (String sd : data) {
                Log.d(TAG, "权限::" + sd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        getSerialPort();
    }

    /**
     * 关闭串口
     * 关闭串口中的输入输出流
     */
    public void closeSerialPort() {
        Log.d(TAG, "关闭串口");
        try {
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }

            if (serialPort != null) {
                serialPort.close();
                serialPort = null;
            }

//      if (mUsbPrinter != null) {
//        mUsbPrinter.close();
//        mUsbPrinter = null;
//      }

            isStart = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    /**
//     * 发送数据
//     * 通过串口，发送数据到单片机
//     *
//     * @param data 要发送的数据
//     */
//    public void sendSerialPort(String data) {
//        try {
//            byte[] sendData = DataUtils.HexToByteArr(data);
//            outputStream.write(sendData);
//            outputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void getSerialPort() {
        if (mReceiveThread == null) {
            mReceiveThread = new ReceiveThread();
        }
        mReceiveThread.start();
    }

    String conent;

    /**
     * 接收串口数据的线程
     */

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isStart) {
                if (inputStream == null) {
                    return;
                }
                try {
                    int available = 0;
                    //TODO
                    while (available == 0) {
                        sleep(500);
                        available = inputStream.available();
                        Log.d(TAG, "available::" + available);
                    }
                    Log.d(TAG, "available------->::" + available);
                    byte[] readData = new byte[1024];

                    int readCount = 0;
                    conent = "";
                    while (readCount < available) {
                        readCount += inputStream.read(readData);
                        conent += new String(readData, 0, readCount, StandardCharsets.UTF_8);
                    }

                    if (readCount > 0) {
                        conent = new String(readData, 0, readCount, StandardCharsets.UTF_8);
                        ToastUtil.showShort("内容:" + conent);
                        Log.e(TAG, "内容:" + conent);
                        if (scanResult != null)
                            scanResult.ScanInfo(conent);
                    }

                    //sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


//  private UsbDevice getCorrectDevice() {
//
//    UsbManager usbMgr = (UsbManager) context.getSystemService(Context.USB_SERVICE);
//    Map<String, UsbDevice> devMap = usbMgr.getDeviceList();
//    for (String name : devMap.keySet()) {
//      Log.e(TAG, "check device: " + name);
//      if (UsbPrinter.checkPrinter(devMap.get(name)))
//        return devMap.get(name);
//    }
//    return null;
//  }


    public interface ScanResult {

        void ScanInfo(String msg);

    }


    ScanResult scanResult;

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }
}
