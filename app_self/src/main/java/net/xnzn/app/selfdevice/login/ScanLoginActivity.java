package net.xnzn.app.selfdevice.login;


import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

import qr.BarCodeManage;

public class ScanLoginActivity extends SelfCommonActivity {

    private static final String TAG = "ScanLoginActivity";

    @Override
    protected void initView() {
        super.initView();

        EditText etInput = findViewById(R.id.etInput);
        etInput.setInputType(InputType.TYPE_NULL);
        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                Log.d(TAG, "keyCode:" + keyCode);

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

//            BarCodeManage.getInstance().setListener(new BarCodeManage.Listener() {
//                @Override
//                public void onScan(String barCode) {
//
//                    Log.d(TAG, "二维码内容::" + barCode);
////                if (rlBg.getVisibility() == View.VISIBLE) {
////                    return;
////                }
////
////                request.setNuClearMode(3);
////                request.setAuthCode(barCode);
////                stopVerify();
////                uploadData();
//                }
//            });
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