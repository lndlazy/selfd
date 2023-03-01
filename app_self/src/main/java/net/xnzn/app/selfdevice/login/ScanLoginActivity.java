package net.xnzn.app.selfdevice.login;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.qr.BarCodeManage;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

public class ScanLoginActivity extends SelfCommonActivity {

    private static final String TAG = "ScanLoginActivity";

    @Override
    protected boolean showTitleBar() {
        return false;
    }

//    private final int REQUEST_CODE = 0x32;
//
//    private String[] pers = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void initView() {
        super.initView();

//        requestPermission(REQUEST_CODE, pers);
//        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
//        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//        List<UsbDevice> usbDevices = new ArrayList<>();
//        while (deviceIterator.hasNext()) {
//            UsbDevice device = deviceIterator.next();
//            usbDevices.add(device);
//
//            int productId = device.getProductId();
//            int vendorId = device.getVendorId();
//            Log.e("getDeviceList", "getDeviceList: " + device.getProductName() + ",productId:" + productId
//                    + ",vendorId:" + vendorId);
//
//        }

        EditText etInput = findViewById(R.id.etInput);
        etInput.setInputType(InputType.TYPE_NULL);
        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String result = etInput.getText().toString();
                    Log.d(TAG, "扫描结果:" + result);

                    if (TextUtils.isEmpty(result))
                        return false;

                }

                return false;
            }

        });
    }

    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected boolean showBar() {
        return false;
    }

    @Override
    protected int showView() {
        return R.layout.activity_scan_login;
    }

    @Override
    protected void initData() {

//        BarCodeManage.getInstance().setListener(new BarCodeManage.Listener() {
//            @Override
//            public void onScan(String barCode) {
//
//                Log.d(TAG, "二维码内容::" + barCode);
//            }
//        });
    }

    @Override
    protected void initLisitener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BarCodeManage.getInstance().release();
    }


}