package net.xnzn.app.selfdevice.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import net.xnzn.app.selfdevice.R;

public class CustomMyItemView extends ConstraintLayout {

    protected TextView tvItemLeft;
    protected TextView tvItemRight;

    public CustomMyItemView(Context context) {
        this(context, null);
    }

    public CustomMyItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMyItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomMyItemView);

        String leftText = mTypedArray.getString(R.styleable.CustomMyItemView_myLeftText);
        if (!TextUtils.isEmpty(leftText)) {
            tvItemLeft.setText(leftText);
        }

        String rightText = mTypedArray.getString(R.styleable.CustomMyItemView_myRightText);
        if (!TextUtils.isEmpty(rightText)) {
            tvItemRight.setText(rightText);
        }

        int leftColor = mTypedArray.getColor(R.styleable.CustomMyItemView_myLeftTextColor, context.getResources().getColor(R.color.text_color_33));
        tvItemLeft.setTextColor(leftColor);

        int rightColor = mTypedArray.getColor(R.styleable.CustomMyItemView_myRightTextColor, context.getResources().getColor(R.color.text_color_ccc));
        tvItemRight.setTextColor(rightColor);

        //获取show_views属性，如果没有设置时默认为0x26
//    showView = mTypedArray.getInt(R.styleable.HeaderBar_show_views, 0x26);
//    text_center.setTextColor(mTypedArray.getColor(R.styleable.HeaderBar_title_text_clolor, Color.WHITE));
        mTypedArray.recycle();
//    showView(showView);

    }

    //初始化UI，可根据业务需求设置默认值。
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_my_item_view, this, true);
        tvItemLeft = findViewById(R.id.tvItemLeft);
        tvItemRight = findViewById(R.id.tvItemRight);

//    tvItemLeft.setBackgroundColor(Color.BLACK);
//    tvItemLeft.setTextColor(Color.WHITE);

    }

    public void setRightText(String text) {
        if (text != null) {
            tvItemRight.setText(text);
            tvItemRight.setTextColor(getResources().getColor(R.color.text_color_33));
        }
    }

    public void setRightEmpty() {
        tvItemRight.setText("请选择");
        tvItemRight.setTextColor(getResources().getColor(R.color.text_color_ccc));
    }

    public void setLeftText(String text) {
        if (text != null) {
            tvItemLeft.setText(text);
            tvItemLeft.setTextColor(getResources().getColor(R.color.text_color_33));
        }
    }

}