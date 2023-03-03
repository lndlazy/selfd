package net.xnzn.app.selfdevice.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import androidx.annotation.NonNull;

import net.xnzn.lib_log.L;
import net.xnzn.lib_utils.AppUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Camera2Manager {

    private static final String TAG = "Camera2Proxy";

    private Context mContext;

    private int mPreviewRotation = 0;//预览角度
    private int mCameraId = 0; // 要打开的摄像头ID
    private CameraCharacteristics mCameraCharacteristics; // 相机属性
    private CameraManager mCameraManager; // 相机管理者
    private CameraDevice mCameraDevice; // 相机对象
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder; // 相机预览请求的构造器
    private CaptureRequest mPreviewRequest;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private ImageReader mPictureImageReader;
    private ImageReader mPreviewImageReader;
    private Surface mPreviewSurface;

    private Size mPreviewSize; // 预览大小
    private Size mPictureSize; // 拍照大小
    private int mDisplayRotation = 0; // 原始Sensor画面顺时针旋转该角度后，画面朝上
    private int mDeviceOrientation = 0; // 设备方向，由相机传感器获取

    /* 缩放相关 */
    private final int MAX_ZOOM = 200; // 放大的最大值，用于计算每次放大/缩小操作改变的大小
    private int mZoom = 0; // 0~mMaxZoom之间变化
    private float mStepWidth; // 每次改变的宽度大小
    private float mStepHeight; // 每次改变的高度大小

    /**
     * 打开摄像头的回调
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.d(TAG, "onOpened");
            mCameraDevice = camera;
            initPreviewRequest();
            createCommonSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.d(TAG, "onDisconnected");
            L.i( "onDisconnected");
            releaseCamera();
            openCamera();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.e(TAG, "Camera Open failed, error: " + error);
            L.i( "Camera Open failed, error: " + error);
            releaseCamera();
            openCamera();
        }
    };

    @TargetApi(Build.VERSION_CODES.M)
    public Camera2Manager() {
        mContext = AppUtil.getApplication();
    }

    private final SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Rect rect = holder.getSurfaceFrame();
//            setUpCameraOutputs(rect.width(), rect.height());
            mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            try {
                String[] ids = mCameraManager.getCameraIdList();
                mCameraCharacteristics = mCameraManager.getCameraCharacteristics(ids[mCameraId]);
                mPreviewSize = getPreviewSize();
                mPictureSize = getPictureSize();
                holder.setFixedSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "surfaceChanged: width: " + width + ", height: " + height);
            float ratio;
            if (width > height) {
                ratio = height * 1.0f / width;
            } else {
                ratio = width * 1.0f / height;
            }
            if (ratio == mPreviewSize.getHeight() * 1f / mPreviewSize.getWidth()) {
                setPreviewSurface(holder); // 等view的大小固定后再设置surface
                openCamera();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            releaseCamera();
        }
    };


    public void setUpCameraOutputs(int width, int height) {
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] ids = mCameraManager.getCameraIdList();

            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(ids[mCameraId]);
            StreamConfigurationMap map = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] supportPictureSizes = map.getOutputSizes(ImageFormat.JPEG);
            Size pictureSize = Collections.max(Arrays.asList(supportPictureSizes), new CompareSizesByArea());
            float aspectRatio = pictureSize.getHeight() * 1.0f / pictureSize.getWidth();
            Size[] supportPreviewSizes = map.getOutputSizes(SurfaceTexture.class);
            // 一般相机页面都是固定竖屏，宽是短边，所以根据view的宽度来计算需要的预览大小
            Size previewSize = chooseOptimalSize(supportPreviewSizes, width, aspectRatio);
            Log.d(TAG, "pictureSize: " + pictureSize);
            Log.d(TAG, "previewSize: " + previewSize);
            mPictureSize = pictureSize;
            mPreviewSize = previewSize;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private static byte[] convertPlanes2NV21(int width, int height, ByteBuffer yPlane, ByteBuffer uPlane, ByteBuffer vPlane) {
        int totalSize = width * height * 3 / 2;
        byte[] nv21Buffer = new byte[totalSize];
        int len = yPlane.capacity();
        yPlane.get(nv21Buffer, 0, len);
        vPlane.get(nv21Buffer, len, vPlane.capacity());
        byte lastValue = uPlane.get(uPlane.capacity() - 1);
        nv21Buffer[totalSize - 1] = lastValue;
        return nv21Buffer;
    }


    byte[] data;


    @SuppressLint("MissingPermission")

    public void openCamera() {
        Log.v(TAG, "openCamera");
        startBackgroundThread(); // 对应 releaseCamera() 方法中的 stopBackgroundThread()


        try {
            if (mPreviewSize != null) {
                mPreviewImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.YUV_420_888, 2);

                mPreviewImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader imageReader) {

                        Image image = imageReader.acquireLatestImage();
                        if (image != null) {
                            Image.Plane[] planes = image.getPlanes();
                            ByteBuffer buffer;
                            if (image.getFormat() == ImageFormat.YUV_420_888) {
                                buffer = planes[0].getBuffer();
                                if (data == null) {
                                    data = new byte[buffer.capacity() * 3 / 2];
                                }
                                int len = buffer.capacity();
                                buffer.get(data, 0, len);
                                buffer = planes[2].getBuffer();//plane[0] + plane[2] =NV21;; plane[0] + plane[1] =NV12
                                buffer.get(data, len, buffer.capacity());
                            }
                            image.close();
                            if (callback != null) {
                                callback.onFramCallback(data, imageReader.getWidth(), imageReader.getHeight(), mPreviewRotation, false);
                            }
                        }
                    }
                }, mBackgroundHandler);

            }

            if (mCameraId > mCameraManager.getCameraIdList().length - 1) {
                return;
            }

            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraManager.getCameraIdList()[mCameraId]);
            // 每次切换摄像头计算一次就行，结果缓存到成员变量中
            initZoomParameter();

            // 打开摄像头
            mCameraManager.openCamera(mCameraManager.getCameraIdList()[mCameraId], mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void initZoomParameter() {
        Rect rect = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        Log.d(TAG, "sensor_info_active_array_size: " + rect);
        // max_digital_zoom 表示 active_rect 除以 crop_rect 的最大值
        float max_digital_zoom = mCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
        Log.d(TAG, "max_digital_zoom: " + max_digital_zoom);
        // crop_rect的最小宽高
        float minWidth = rect.width() / max_digital_zoom;
        float minHeight = rect.height() / max_digital_zoom;
        // 因为缩放时两边都要变化，所以要除以2
        mStepWidth = (rect.width() - minWidth) / MAX_ZOOM / 2;
        mStepHeight = (rect.height() - minHeight) / MAX_ZOOM / 2;
    }

    public void releaseCamera() {
        Log.v(TAG, "releaseCamera");
        if (null != mCaptureSession) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (mPictureImageReader != null) {
            mPictureImageReader.close();
            mPictureImageReader = null;
        }
        if (mPreviewImageReader != null) {
            mPreviewImageReader.close();
            mPreviewImageReader = null;
        }
        stopBackgroundThread(); // 对应 openCamera() 方法中的 startBackgroundThread()
    }

    public Camera2Manager setPreviewSurface(SurfaceHolder holder) {
        mPreviewSurface = holder.getSurface();
        holder.addCallback(mSurfaceHolderCallback);
        return this;
    }

    private TextureView mTextureView;

    public Camera2Manager setPreviewSurface(TextureView textureView) {
        mTextureView = textureView;
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        return this;
    }


    private Callback callback;

    public Camera2Manager setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public interface Callback {
        void onFramCallback(byte[] nv21, int width, int height, int rotation, boolean mirror);
    }


    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.v(TAG, "onSurfaceTextureAvailable. width: " + width + ", height: " + height);
//            setUpCameraOutputs(width, height);
            mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            try {
                String[] ids = mCameraManager.getCameraIdList();
                mCameraCharacteristics = mCameraManager.getCameraCharacteristics(ids[mCameraId]);
                mPreviewSize = getPreviewSize();
                mPictureSize = getPictureSize();
            } catch (Exception e) {
                e.printStackTrace();
            }
            surface.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewSurface = new Surface(surface);

            configureTransform(width, height);


            openCamera();
            // resize TextureView
            int previewWidth = getPreviewSize().getWidth();
            int previewHeight = getPreviewSize().getHeight();

        }


        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.v(TAG, "onSurfaceTextureSizeChanged. width: " + width + ", height: " + height);
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.v(TAG, "onSurfaceTextureDestroyed");
            releaseCamera();
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };


    //设置预览角度
    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mTextureView || null == mPreviewSize) {
            return;
        }

        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getWidth(), mPreviewSize.getHeight());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();

        bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
        float scaleX = (float) viewWidth / mPreviewSize.getWidth();
        float scaleY = (float) viewHeight / mPreviewSize.getHeight();

        float scaleX2 = 1;
        float scaleY2 = 1;
        if (scaleX > scaleY) {
            //x拉伸多，补充y
            scaleY2 = (mPreviewSize.getHeight() * scaleX) / viewHeight;
        }
        if (scaleX < scaleY) {
            //y拉伸多，补充x
            scaleX2 = (mPreviewSize.getWidth() * scaleY) / viewWidth;
        }


        matrix.postScale(scaleX2, scaleY2, centerX, centerY);
        matrix.postRotate(mPreviewRotation, centerX, centerY);
        mTextureView.setTransform(matrix);


    }


    private void createCommonSession() {
        List<Surface> outputs = new ArrayList<>();
        // preview output
        if (mPreviewSurface != null) {
            Log.d(TAG, "createCommonSession add target mPreviewSurface");
            outputs.add(mPreviewSurface);
        }

        outputs.add(mPreviewImageReader.getSurface());

        // picture output
        Size pictureSize = mPictureSize;
        if (pictureSize != null) {
            Log.d(TAG, "createCommonSession add target mPictureImageReader");
            mPictureImageReader = ImageReader.newInstance(pictureSize.getWidth(), pictureSize.getHeight(), ImageFormat.JPEG, 1);
            outputs.add(mPictureImageReader.getSurface());
        }
        try {
            // 一个session中，所有CaptureRequest能够添加的target，必须是outputs的子集，所以在创建session的时候需要都添加进来
            mCameraDevice.createCaptureSession(outputs, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mCaptureSession = session;
                    startPreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.e(TAG, "ConfigureFailed. session: " + session);
                }
            }, mBackgroundHandler); // handle 传入 null 表示使用当前线程的 Looper
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void initPreviewRequest() {
        if (mPreviewSurface == null) {
            Log.e(TAG, "initPreviewRequest failed, mPreviewSurface is null");
            return;
        }
        try {
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            // 设置预览输出的 Surface
            mPreviewRequestBuilder.addTarget(mPreviewSurface);
            //设置预览回调的 surface
            mPreviewRequestBuilder.addTarget(mPreviewImageReader.getSurface());
//

            // 设置连续自动对焦
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 设置自动曝光
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            // 设置自动白平衡
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_AUTO);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void startPreview() {
        Log.v(TAG, "startPreview");
        if (mCaptureSession == null || mPreviewRequestBuilder == null) {
            Log.w(TAG, "startPreview: mCaptureSession or mPreviewRequestBuilder is null");
            return;
        }
        try {
            // 开始预览，即一直发送预览的请求
            CaptureRequest captureRequest = mPreviewRequestBuilder.build();
            mCaptureSession.setRepeatingRequest(captureRequest, null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void stopPreview() {
        Log.v(TAG, "stopPreview");
        if (mCaptureSession == null) {
            Log.w(TAG, "stopPreview: mCaptureSession is null");
            return;
        }
        try {
            mCaptureSession.stopRepeating();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    public void captureStillPicture(ImageReader.OnImageAvailableListener onImageAvailableListener) {
        if (mPictureImageReader == null) {
            Log.w(TAG, "captureStillPicture failed! mPictureImageReader is null");
            return;
        }
        mPictureImageReader.setOnImageAvailableListener(onImageAvailableListener, mBackgroundHandler);
        try {
            // 创建一个用于拍照的Request
            CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mPictureImageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation(mDeviceOrientation));
            // 预览如果有放大，拍照的时候也应该保存相同的缩放
            Rect zoomRect = mPreviewRequestBuilder.get(CaptureRequest.SCALER_CROP_REGION);
            if (zoomRect != null) {
                captureBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoomRect);
            }
            stopPreview();
            mCaptureSession.abortCaptures();
            final long time = System.currentTimeMillis();
            mCaptureSession.capture(captureBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    Log.w(TAG, "onCaptureCompleted, time: " + (System.currentTimeMillis() - time));
                    try {
                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
                        mCaptureSession.capture(mPreviewRequestBuilder.build(), null, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    startPreview();
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private int getJpegOrientation(int deviceOrientation) {
        if (deviceOrientation == OrientationEventListener.ORIENTATION_UNKNOWN) return 0;
        int sensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        // Round device orientation to a multiple of 90
        deviceOrientation = (deviceOrientation + 45) / 90 * 90;
        // Reverse device orientation for front-facing cameras
        boolean facingFront = mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics
                .LENS_FACING_FRONT;
        if (facingFront) deviceOrientation = -deviceOrientation;
        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360;
        Log.d(TAG, "jpegOrientation: " + jpegOrientation);
        return jpegOrientation;
    }

    public boolean isFrontCamera() {
        return mCameraId == CameraCharacteristics.LENS_FACING_BACK;
    }

    public Size getPreviewSize() {
        return mPreviewSize;
    }

    public Camera2Manager setPreviewSize(Size previewSize) {
        mPreviewSize = previewSize;
        return this;
    }

    public Size getPictureSize() {
        return mPictureSize;
    }

    public void setPictureSize(Size pictureSize) {
        mPictureSize = pictureSize;
    }

    public void switchCamera() {
        mCameraId ^= 1;
        Log.d(TAG, "switchCamera: mCameraId: " + mCameraId);
        releaseCamera();
        openCamera();
    }

    public void handleZoom(boolean isZoomIn) {
        if (mCameraDevice == null || mCameraCharacteristics == null || mPreviewRequestBuilder == null) {
            return;
        }
        if (isZoomIn && mZoom < MAX_ZOOM) { // 放大
            mZoom++;
        } else if (mZoom > 0) { // 缩小
            mZoom--;
        }
        Log.v(TAG, "handleZoom: mZoom: " + mZoom);
        Rect rect = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        int cropW = (int) (mStepWidth * mZoom);
        int cropH = (int) (mStepHeight * mZoom);
        Rect zoomRect = new Rect(rect.left + cropW, rect.top + cropH, rect.right - cropW, rect.bottom - cropH);
        Log.d(TAG, "zoomRect: " + zoomRect);
        mPreviewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoomRect);
        startPreview(); // 需要重新 start preview 才能生效
    }

    public void triggerFocusAtPoint(float x, float y, int width, int height) {
        Log.d(TAG, "triggerFocusAtPoint (" + x + ", " + y + ")");
        Rect cropRegion = mPreviewRequestBuilder.get(CaptureRequest.SCALER_CROP_REGION);
        MeteringRectangle afRegion = getAFAERegion(x, y, width, height, 1f, cropRegion);
        // ae的区域比af的稍大一点，聚焦的效果比较好
        MeteringRectangle aeRegion = getAFAERegion(x, y, width, height, 1.5f, cropRegion);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, new MeteringRectangle[]{afRegion});
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, new MeteringRectangle[]{aeRegion});
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_START);
        try {
            mCaptureSession.capture(mPreviewRequestBuilder.build(), mAfCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private MeteringRectangle getAFAERegion(float x, float y, int viewWidth, int viewHeight, float multiple, Rect cropRegion) {
        Log.v(TAG, "getAFAERegion enter");
        Log.d(TAG, "point: [" + x + ", " + y + "], viewWidth: " + viewWidth + ", viewHeight: " + viewHeight);
        Log.d(TAG, "multiple: " + multiple);
        // do rotate and mirror
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        Matrix matrix1 = new Matrix();
        matrix1.setRotate(mDisplayRotation);
        matrix1.postScale(isFrontCamera() ? -1 : 1, 1);
        matrix1.invert(matrix1);
        matrix1.mapRect(viewRect);
        // get scale and translate matrix
        Matrix matrix2 = new Matrix();
        RectF cropRect = new RectF(cropRegion);
        matrix2.setRectToRect(viewRect, cropRect, Matrix.ScaleToFit.CENTER);
        Log.d(TAG, "viewRect: " + viewRect);
        Log.d(TAG, "cropRect: " + cropRect);
        // get out region
        int side = (int) (Math.max(viewWidth, viewHeight) / 8 * multiple);
        RectF outRect = new RectF(x - side / 2, y - side / 2, x + side / 2, y + side / 2);
        Log.d(TAG, "outRect before: " + outRect);
        matrix1.mapRect(outRect);
        matrix2.mapRect(outRect);
        Log.d(TAG, "outRect after: " + outRect);
        // 做一个clamp，测光区域不能超出cropRegion的区域
        Rect meteringRect = new Rect((int) outRect.left, (int) outRect.top, (int) outRect.right, (int) outRect.bottom);
        meteringRect.left = clamp(meteringRect.left, cropRegion.left, cropRegion.right);
        meteringRect.top = clamp(meteringRect.top, cropRegion.top, cropRegion.bottom);
        meteringRect.right = clamp(meteringRect.right, cropRegion.left, cropRegion.right);
        meteringRect.bottom = clamp(meteringRect.bottom, cropRegion.top, cropRegion.bottom);
        Log.d(TAG, "meteringRegion: " + meteringRect);
        return new MeteringRectangle(meteringRect, 1000);
    }

    private final CameraCaptureSession.CaptureCallback mAfCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            Integer state = result.get(CaptureResult.CONTROL_AF_STATE);
            Log.d(TAG, "CONTROL_AF_STATE: " + state);
            if (state == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED || state == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                Log.d(TAG, "process: start normal preview");
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.FLASH_MODE_OFF);
                startPreview();
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }
    };


    private void startBackgroundThread() {
        if (mBackgroundThread == null || mBackgroundHandler == null) {
            Log.v(TAG, "startBackgroundThread");
            mBackgroundThread = new HandlerThread("CameraBackground");
            mBackgroundThread.start();
            mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        }
    }

    private void stopBackgroundThread() {
        Log.v(TAG, "stopBackgroundThread");
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Size chooseOptimalSize(Size[] sizes, int dstSize, float aspectRatio) {
        if (sizes == null || sizes.length <= 0) {
            Log.e(TAG, "chooseOptimalSize failed, input sizes is empty");
            return null;
        }
        int minDelta = Integer.MAX_VALUE; // 最小的差值，初始值应该设置大点保证之后的计算中会被重置
        int index = 0; // 最小的差值对应的索引坐标
        for (int i = 0; i < sizes.length; i++) {
            Size size = sizes[i];
            // 先判断比例是否相等
            if (size.getWidth() * aspectRatio == size.getHeight()) {
                int delta = Math.abs(dstSize - size.getHeight());
                if (delta == 0) {
                    return size;
                }
                if (minDelta > delta) {
                    minDelta = delta;
                    index = i;
                }
            }
        }
        return sizes[index];
    }

    private int clamp(int x, int min, int max) {
        if (x > max) return max;
        if (x < min) return min;
        return x;
    }

    public Camera2Manager setCameraId(int cameraId) {
        mCameraId = cameraId;
        if (LeniuUsbManger.getInstance().hasHRgbCamera()) {
            mPreviewRotation = 90;
        }
        return this;
    }


    public Camera2Manager setPreviewRotaion(int rotation) {
        mPreviewRotation = rotation;
        return this;
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // 我们在这里投放，以确保乘法不会溢出
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

}
