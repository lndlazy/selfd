package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Google官方代码
 * <p>
 * 此类的作用为，JNI的调用，用来加载.so文件的
 * <p>
 * 获取串口输入输出流
 */

public class SerialPort {

    private static final String TAG = "SerialPort";

    /*
     * Do not remove or rename the field mFd: it is used
     * close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudrate, int flags)
            throws SecurityException, IOException {

        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                //Log.e(TAG, "========开始请求授权========");
                su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                //Log.e(TAG, "========授权成功========");
                su.getOutputStream().write(cmd.getBytes());
                //Log.e(TAG, "========getOutputStream========");

                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
                //Log.e(TAG, "========waitFor========");

            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        //Log.e(TAG, device.getAbsolutePath() + "==============================");
        //开启串口，传入物理地址、波特率、flags值
        mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    //获取串口的输入流
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    //获取串口的输出流
    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI调用，开启串口
    private native static FileDescriptor open(String path, int baudrate, int flags);

    //关闭串口
    public native void close();

    static {
        //System.out.println("==============================");
        //加载库文件.so文件
        System.loadLibrary("serial_port");
        //System.out.println("********************************");
    }
}

