package net.xnzn.app.selfdevice.login;


import android.Manifest;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.lib_commin_ui.base.constant.Constant;

import android_serialport_api.SerialPortUtil;
import qr.UsbPrinter;


public class ScanLoginActivity extends SelfCommonActivity {

    private static final String TAG = "ScanLoginActivity";

    @Override
    protected boolean showTitleBar() {
        return false;
    }


    private final int REQUEST_CODE = 0x32;

    private String[] pers = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void initView() {
        super.initView();

        requestPermission(REQUEST_CODE, pers);

//        EditText etInput = findViewById(R.id.etInput);
//        etInput.setInputType(InputType.TYPE_NULL);
//        etInput.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    String result = etInput.getText().toString();
//                    Log.d(TAG, "扫描结果:" + result);
//
//                    if (TextUtils.isEmpty(result))
//                        return false;
//
//                }
//
//                return false;
//            }
//
//        });
    }

    @Override
    protected boolean showTitle() {
        return false;
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
//        UsbPrinter usbPrinter = new UsbPrinter(this);
        SerialPortUtil serialPortUtil = new SerialPortUtil();
        serialPortUtil.openSerialPort();
//        BarCodeManage.getInstance().setListener(new BarCodeManage.Listener() {
//            @Override
//            public void onScan(String barCode) {
//
//                Log.d(TAG, "二维码内容::" + barCode);
////                if (rlBg.getVisibility() == View.VISIBLE) {
////                    return;
////                }
////
////                request.setNuClearMode(3);
////                request.setAuthCode(barCode);
////                stopVerify();
////                uploadData();
//            }
//        });
    }

    @Override
    protected void initLisitener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//            BarCodeManage.getInstance().release();
    }


}