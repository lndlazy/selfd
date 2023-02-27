package qr;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.serialport.SerialPort;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BarCodeManage {


  private static final String TAG = BarCodeManage.class.getSimpleName();
  private static BarCodeManage manage;
  private Handler handler = new Handler(Looper.getMainLooper());
  private Listener listener;

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public static BarCodeManage getInstance() {
    if (manage == null) {
      synchronized (BarCodeManage.class) {
        if (manage == null) {
          manage = new BarCodeManage();
        }
      }
    }
    return manage;
  }

  private BarCodeManage() {
    //打开串口
    initSerial();
  }


  private boolean isYs() {
    if (TextUtils.equals("YS", Build.MANUFACTURER)) {
      return true;
    }
    return false;
  }


  private SerialPort mSerialPort;
  private InputStream mFileInputStream;
  private ReadThread readThread;

  private void initSerial() {
    try {
      if (isYs()) {
        if (Build.MODEL.contains("3568")) {
          mSerialPort = new SerialPort(new File("/dev/ttyS9"), 9600);
        } else {
          mSerialPort = new SerialPort(new File("/dev/ttyS3"), 9600);
        }

      } else {
//                mSerialPort = new SerialPort(new File("/dev/ttyS3"), 9600, 0);
        return;
      }
      this.mFileInputStream = mSerialPort.getInputStream();
      Log.e(TAG, "串口扫码器-打开成功");
      readThread = new ReadThread();
      readThread.start();


    } catch (IOException e) {
      e.printStackTrace();
      Log.e(TAG, "串口扫码器打开异常" + e.getMessage());
    }

  }

  class ReadThread extends Thread {

    private boolean isWorking = true;
    private byte[] buffer = new byte[1024];
    private byte[] buffer2 = new byte[1024];
    private int index;

    public void setWorking(boolean working) {
      isWorking = working;
    }

    @Override
    public void run() {
      super.run();
      while (isWorking) {

        try {
          int recv = mFileInputStream.read(buffer, index, buffer.length - index);
//                    Log.e("串口扫码结果", "read size " + recv + "index 起始位：" + index + "  buffer " + new String(buffer).trim());

          if (buffer[index + recv - 1] == 0x0D) {
            Log.e("串口扫码结果", "==========");
            //3133303138 3430363538 3331303633 3739360D
            //回车符
            byte[] temp = new byte[index + recv];
            System.arraycopy(buffer, 0, temp, 0, temp.length - 1);
            String barCode = new String(temp);
            //清空缓冲区
            index = 0;
            System.arraycopy(buffer2, 0, buffer, 0, buffer.length);

            handler.post(new Runnable() {
              @Override
              public void run() {
                if (listener != null) {
                  listener.onScan(barCode.trim());
                }
              }
            });

          } else {
            index = index + recv;
            //继续读取内容
            Log.e("串口扫码结果", "继续读");
          }

          sleep(50);

        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      }
    }


  }


  public void release() {
    if (readThread != null) {
      readThread.setWorking(false);
      readThread = null;
    }
    manage = null;
  }


  public interface Listener {
    void onScan(String barCode);

  }
} 