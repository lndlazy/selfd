package net.xnzn.app.selfdevice.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import net.xnzn.app.selfdevice.R;

public class CustomItemMiddleEditView extends ConstraintLayout {

    protected TextView tvItemLeft;
    protected EditText etMiddle;

    public CustomItemMiddleEditView(Context context) {
        this(context, null);
    }

    public CustomItemMiddleEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomItemMiddleEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomItemMiddleEditView);

        String leftText = mTypedArray.getString(R.styleable.CustomItemMiddleEditView_mMiddleEditLeftText);
        if (!TextUtils.isEmpty(leftText)) {
            tvItemLeft.setText(leftText);
        }

        String rightText = mTypedArray.getString(R.styleable.CustomItemMiddleEditView_mMiddleEditRightText);
        if (!TextUtils.isEmpty(rightText)) {
            etMiddle.setText(rightText);
        }

        String middleHintText = mTypedArray.getString(R.styleable.CustomItemMiddleEditView_mMiddleEditRightHint);
        if (!TextUtils.isEmpty(middleHintText)) {
            etMiddle.setHint(middleHintText);
        }

        int leftColor = mTypedArray.getColor(R.styleable.CustomItemMiddleEditView_mMiddleEditLeftTextColor, context.getResources().getColor(R.color.text_color_33));
        tvItemLeft.setTextColor(leftColor);

        int rightColor = mTypedArray.getColor(R.styleable.CustomItemMiddleEditView_mMiddleEditRightTextColor, context.getResources().getColor(R.color.text_color_ccc));
        etMiddle.setTextColor(rightColor);

        //获取show_views属性，如果没有设置时默认为0x26
//    showView = mTypedArray.getInt(R.styleable.HeaderBar_show_views, 0x26);
//    text_center.setTextColor(mTypedArray.getColor(R.styleable.HeaderBar_title_text_clolor, Color.WHITE));
        mTypedArray.recycle();
//    showView(showView);

    }

    //初始化UI，可根据业务需求设置默认值。
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_my_item_middle_edit_view, this, true);
        tvItemLeft = findViewById(R.id.tvItemLeft);
        etMiddle = findViewById(R.id.etMiddle);

//    tvItemLeft.setBackgroundColor(Color.BLACK);
//    tvItemLeft.setTextColor(Color.WHITE);

    }

    public void setRightText(String text) {
        if (text != null)
            etMiddle.setText(text);
//            etMiddle.setTextColor(getResources().getColor(R.color.text_color_33));
//        }
    }

    public void setRightEmpty() {
        etMiddle.setText("");
    }

    public void setLeftText(String text) {
        if (text != null) {
            tvItemLeft.setText(text);
            tvItemLeft.setTextColor(getResources().getColor(R.color.text_color_33));
        }
    }

    public String getInput() {
        return  etMiddle.getText().toString();
    }

}