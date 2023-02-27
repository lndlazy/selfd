package net.xnzn.app.selfdevice.login;

import android.graphics.RectF;
import android.hardware.camera2.params.Face;
import android.view.TextureView;

import androidx.annotation.NonNull;

import com.cs.camera.view.FaceView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.lib_commin_ui.base.constant.Constant;

import java.util.ArrayList;

public class FaceLoginActivity extends SelfCommonActivity implements Camera2HelperFace.FaceDetectListener {

    private static final String TAG = "FaceLoginActivity";
    protected TextureView mTextureView;
    protected FaceView faceView;
    private Camera2HelperFace camera2HelperFace;

    @Override
    protected void initView() {
        super.initView();

        mTextureView = findViewById(R.id.textureView);
        faceView = findViewById(R.id.faceView);
//        FaceDetector
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
        return R.layout.activity_face_login;
    }

    @Override
    protected void initData() {

        requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);

        camera2HelperFace = new Camera2HelperFace(this, mTextureView);
        camera2HelperFace.setFaceDetectListener(this);
    }

    @Override
    protected void initLisitener() {

    }


    @Override
    public void onFaceDetect(@NonNull Face[] faces, @NonNull ArrayList<RectF> facesRect) {
        faceView.setFaces(facesRect);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (camera2HelperFace != null) {
                camera2HelperFace.releaseCamera();
                camera2HelperFace.releaseThread();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}