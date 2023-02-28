package qr;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.Map;

public class UsbPrinter {

    private final static String LOG_TAG = "UsbPrinter";
    private boolean mNeedEncrypt = true;
    private Context mContext;
    private final MyBroadcastReceiver mBcRcvr = new MyBroadcastReceiver();

    public UsbPrinter(@NonNull Context context) {
        Log.v(LOG_TAG, "encrypted: " + (this.mNeedEncrypt ? "yes" : "no"));
        this.mContext = context;
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.tx.USB_PERMISSION"), 0);
        UsbManager usbMgr = (UsbManager) this.mContext.getSystemService(Context.USB_SERVICE);
        this.mContext.registerReceiver(this.mBcRcvr, new IntentFilter("com.tx.USB_PERMISSION"));
        Map<String, UsbDevice> devMap = usbMgr.getDeviceList();
        Iterator var5 = devMap.keySet().iterator();

        while (var5.hasNext()) {
            String name = (String) var5.next();
            Log.v(LOG_TAG, "found usb device: " + name);
            UsbDevice dev = (UsbDevice) devMap.get(name);
            if (checkPrinter(dev) && !usbMgr.hasPermission(dev)) {
                usbMgr.requestPermission(dev, mPendingIntent);
            }
        }

    }


    public static boolean checkPrinter(@NonNull UsbDevice device) {
        if (device != null) {
            if (device.getInterfaceCount() == 0) {
                Log.w(LOG_TAG, "usb device has not interface");
                return false;
            }

            UsbInterface iface = device.getInterface(0);
            if (iface != null) {
                if (iface.getInterfaceClass() == 7) {
                    Log.v(LOG_TAG, String.format("usb id: %04X", device.getDeviceId()));
                    return true;
                }

                Log.v(LOG_TAG, String.format("the interface is not a printer(%XH)", iface.getInterfaceClass()));
            } else {
                Log.w(LOG_TAG, "failed to get interface");
            }
        }

        return false;
    }
} 