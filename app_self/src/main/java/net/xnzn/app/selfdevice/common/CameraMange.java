package net.xnzn.app.selfdevice.common;

import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.view.SurfaceHolder;


import com.herohan.uvcapp.CameraHelper;
import com.herohan.uvcapp.ICameraHelper;
import com.serenegiant.opengl.renderer.MirrorMode;
import com.serenegiant.usb.IFrameCallback;
import com.serenegiant.usb.Size;
import com.serenegiant.usb.UVCCamera;

import java.nio.ByteBuffer;
import java.util.List;

public class CameraMange {

    private static final String TAG = CameraMange.class.getSimpleName();
    private int previewRotation = 0;//预览角度
    private int algorithmRotation = 0;//算法角度
    private int mirror = MirrorMode.MIRROR_NORMAL;//镜像方式

    private ICameraHelper mCameraHelper;
    private SurfaceHolder mHolder;
    private Listener listener;
    byte[] nv21 = new byte[460800];
    private Size previewSize;
    private UsbDevice mCameraDevice;


//    private static final Object mSync = new Object();


    public CameraMange setSurfaceHolder(SurfaceHolder holder) {
        this.mHolder = holder;
        return this;
    }

    public CameraMange setFrameCallback(Listener listener) {
        this.listener = listener;
        init();
        return this;
    }


    public void release() {
        mCameraHelper.release();
    }

    public void stopPreview() {
        mCameraHelper.stopPreview();
    }

    private SurfaceTexture surfaceTexture;

    public void init() {
        //设置SurfaceView的Surface监听回调
        if (mHolder != null) {
            mHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    Log.e(TAG, "surfaceCreated");
                    if (mCameraHelper != null) {
                        //添加预览Surface
                        mCameraHelper.addSurface(mHolder.getSurface(), false);
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                    Log.e(TAG, "surfaceChanged");
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    Log.e(TAG, "surfaceDestroyed");
                    if (mCameraHelper != null) {
                        //移除预览Surface
                        mCameraHelper.removeSurface(mHolder.getSurface());
                    }
                }
            });

        }

//        Object mSync = new Object();
        //预览
        mCameraHelper = new CameraHelper();
        mCameraHelper.setStateCallback(new ICameraHelper.StateCallback() {
            @Override
            public void onAttach(UsbDevice device) {

                Log.e(TAG, "onAttach VendorId:" + device.getVendorId() + " ProductId:" + device.getProductId() + " ProductName:");

                if (mCameraDevice != null) {
                    return;
                }

                if (mHolder != null) {

                    //                    设置为当前设备（如果没有权限，会显示授权对话框）
                    Log.e(TAG, "selectDevice === VendorId:" + device.getVendorId() + " ProductId:" + device.getProductId() + " ProductName:");
                    if (LeniuUsbManger.getInstance().isNormalRgbCamera(device)) {
                        //正常款rgb
                        mCameraDevice = device;
                        mCameraHelper.selectDevice(device);
                        return;
                    }

                    if (LeniuUsbManger.getInstance().isHRgbCamera(device)) {
                        mCameraDevice = device;
                        mCameraHelper.selectDevice(device);
                        //扁款rgb
                        previewRotation = 90;
                        algorithmRotation = 270;

                        return;
                    }

                } else {

                    if (LeniuUsbManger.getInstance().isNormalIRCamera(device)) {
                        // IR
                        mCameraDevice = device;
                        mCameraHelper.selectDevice(device);
                        return;
                    }

                    if (LeniuUsbManger.getInstance().isHIRCamera(device)) {
                        mCameraDevice = device;
                        mCameraHelper.selectDevice(device);
                        //扁款 IR
                        previewRotation = 90;
                        algorithmRotation = 270;

                        return;
                    }

                }

//                synchronized (mSync) {}

            }

            @Override
            public void onDeviceOpen(UsbDevice device, boolean isFirstOpen) {
                Log.e(TAG, "onDeviceOpen VendorId:" + device.getVendorId() + " ProductId:" + device.getProductId() + " ProductName:");

                //打开UVC设备成功（也就是已经获取到UVC设备的权限）
                mCameraHelper.openCamera();

                //打开UVC摄像头
//                UVCParam param = new UVCParam();
//                param.setQuirks(UVCCamera.UVC_QUIRK_FIX_BANDWIDTH);
//                mCameraHelper.openCamera(param);
            }

            @Override
            public void onCameraOpen(UsbDevice device) {

                //开始预览
                List<Size> sizes = mCameraHelper.getSupportedSizeList();

                for (int i = 0; i < sizes.size(); i++) {
                    Size size = sizes.get(i);
                    Log.i(TAG, "支持的预览尺寸 VendorId:" + device.getVendorId() + " ProductId:" + device.getProductId() + " " + size.width + "x" + size.height);
                }
                for (int i = 0; i < sizes.size(); i++) {
                    Size size = sizes.get(i);
                    if (size.width == 640 && size.height == 480) {
                        previewSize = size;
                        Log.e(TAG, "最終预览尺寸 VendorId:" + device.getVendorId() + " ProductId:" + device.getProductId() + " " + previewSize.width + "x" + previewSize.height);
                        mCameraHelper.setPreviewSize(size);
                        break;
                    }
                }

                //摄像头已打开，打开预览
                mCameraHelper.setPreviewConfig(
                        mCameraHelper
                                .getPreviewConfig()
                                .setRotation(previewRotation)
                                .setMirror(mirror)
                );
                mCameraHelper.startPreview();

                //获取预览使用的Size（包括帧格式、宽度、高度、FPS）
//                Size size = mCameraHelper.getPreviewSize();
//                if (size != null) {
//                    int width = size.width;
//                    int height = size.height;
//                需要自适应摄像头分辨率的话，设置新的宽高比
//                mCameraViewMain.setAspectRatio(width, height);
//                }

                Log.e(TAG, "预览分比率");

                //添加预览Surface

                if (mHolder != null) {
                    mCameraHelper.addSurface(mHolder.getSurface(), false);
                }

                mCameraHelper.setFrameCallback(mIFrameCallback, UVCCamera.PIXEL_FORMAT_NV21);


            }

            @Override
            public void onCameraClose(UsbDevice device) {
                Log.e(TAG, "onCameraClose VendorId:" + device.getVendorId() + " ProductId:" + device.getProductId() + " ProductName:");

                if (mCameraHelper != null) {
                    //移除预览Surface
                    if (mHolder != null) {
                        mCameraHelper.removeSurface(mHolder.getSurface());
                    }
                }
            }

            @Override
            public void onDeviceClose(UsbDevice device) {
                Log.e(TAG, "onDeviceClose VendorId:" + device.getVendorId() + " ProductId:" + device.getProductId() + " ProductName:");

                Log.e(TAG, device.getDeviceName() + "掉线");
            }

            @Override
            public void onDetach(UsbDevice device) {
                Log.e(TAG, "onDetach VendorId:" + device.getVendorId() + " ProductId:" + device.getProductId() + " ProductName:");

            }

            @Override
            public void onCancel(UsbDevice device) {
                Log.e(TAG, "onCancel VendorId:" + device.getVendorId() + " ProductId:" + device.getProductId() + " ProductName:");

            }
        });


    }


    private final IFrameCallback mIFrameCallback = new IFrameCallback() {
        @Override
        public void onFrame(final ByteBuffer frame) {
//            Log.e(TAG, "onFrame  ");
//                        Log.i("数据", "is rgb:" + isRgb + " " + frame.remaining() + System.currentTimeMillis());
//                        byte[] nv21 = new byte[frame.remaining()];
//                        frame.get(nv21, 0, nv21.length);

            frame.get(nv21, 0, frame.remaining());
            if (listener != null) {
                listener.onFramCallback(nv21.clone(), previewSize.width, previewSize.height, algorithmRotation, mirror);
            }

//            if (frame.remaining() == nv21.length) {
//                frame.get(nv21, 0, frame.remaining());
////                        nv21ToBitmap.nv21ToBitmap(nv21, size.width, size.height);
//                if (listener != null) {
//                    listener.onFramCallback(nv21.clone(), previewSize.width, previewSize.height, algorithmRotation, mirror);
//                }
//            } else {
//                frame.get(nv21, 0, frame.remaining());
//            }
        }
    };


    public interface Listener {
        void onFramCallback(byte[] nv21, int width, int height, int rotation, int mirror);
    }

}
