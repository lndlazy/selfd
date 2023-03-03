package net.xnzn.app.selfdevice.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView {


    private float scaleW = 1f;
    private float scaleH = 1f;

    public void setScaleH(float scaleH) {
        this.scaleH = scaleH;
    }

    public void setScaleW(float scaleW) {
        this.scaleW = scaleW;
    }

    public CameraPreview(Context context) {
        super(context);
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (LeniuUsbManger.getInstance().hasNormalRgbCamera()) {
            scaleW = 1.4f;
            scaleH = 1f;
        } else if (LeniuUsbManger.getInstance().hasHRgbCamera()) {
            scaleW = 1f;
            scaleH = 1.2f;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) (width * scaleW), (int) (height * scaleH));
    }

}