package qr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = MyBroadcastReceiver.class.getSimpleName();
    static final String FILTER_NAME = "com.tx.USB_PERMISSION";

    public MyBroadcastReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        String str = intent.getAction();
        if (str.equals("com.tx.USB_PERMISSION")) {
            Log.v(LOG_TAG, "receive broadcast: com.tx.USB_PERMISSION");
            synchronized (this) {
                if (!intent.getBooleanExtra("permission", false)) {
                    Log.e("MyBroadcastReceiver", "permission was rejected!");
                }
            }
        }

    }
}
