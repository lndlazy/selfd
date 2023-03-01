package net.xnzn.app.selfdevice.my.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.BaseFragment;
import net.xnzn.app.selfdevice.widget.CustomMyItemEditView;
import net.xnzn.app.selfdevice.widget.CustomMyItemView;
import net.xnzn.app.selfdevice.widget.MyTouchDialog;

import java.util.Calendar;

public class MyBaseFragment extends BaseFragment implements View.OnClickListener {

    protected CustomMyItemEditView cmiName;
    protected CustomMyItemEditView cmiTel;
    protected CustomMyItemView cmiGender;
    protected CustomMyItemView cmiBirthday;
    protected CustomMyItemEditView cmiIdCard;
    protected CustomMyItemEditView cmiEmail;
    protected CustomMyItemEditView cmiAddress;
    protected CustomMyItemView cmiDoctorSay;
    protected CustomMyItemView cmiHeight;
    protected TextView tvSaveNow;
    protected DatePickerDialog datePickerDialog;
    protected AlertDialog commonDialog;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_base;
    }

    @Override
    public void initView(View view) {

        cmiName = view.findViewById(R.id.cmiName);
        cmiTel = view.findViewById(R.id.cmiTel);
        cmiGender = view.findViewById(R.id.cmiGender);
        cmiBirthday = view.findViewById(R.id.cmiBirthday);
        cmiIdCard = view.findViewById(R.id.cmiIdCard);
        cmiAddress = view.findViewById(R.id.cmiAddress);
        cmiEmail = view.findViewById(R.id.cmiEmail);
        cmiDoctorSay = view.findViewById(R.id.cmiDoctorSay);
        cmiHeight = view.findViewById(R.id.cmiHeight);
        tvSaveNow = view.findViewById(R.id.tvSaveNow);
        tvSaveNow.setOnClickListener(this);
        cmiGender.setOnClickListener(this);
        cmiBirthday.setOnClickListener(this);
        cmiDoctorSay.setOnClickListener(this);
        cmiHeight.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    String[] genders = new String[]{"男", "女"};

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvSaveNow:
                break;

            case R.id.cmiGender:
                showCommonDialog("性别", genders, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cmiGender.setRightText(genders[i]);
                    }
                });
                break;
            case R.id.cmiBirthday:
                showDatePickDlg();
                break;
            case R.id.cmiDoctorSay:
                break;
            case R.id.cmiHeight:
                break;

        }
    }

    private void showDatePickDlg() {

        if (getActivity() == null)
            return;

        Calendar calendar = Calendar.getInstance();
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int month = (monthOfYear + 1);
                    String temp = month < 10 ? "0" + month : month + "";

                    cmiBirthday.setRightText(year + "-" + temp);
//                queryDate = year + "-" + temp + "-" + "01";
//                current = 1;
//                adapter.getData().clear();
//                adapter.notifyDataSetChanged();
//                initData1();
//                loadData();
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            DatePicker picker = datePickerDialog.getDatePicker();
            picker.setCalendarViewShown(false);
            ((ViewGroup) ((ViewGroup) picker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);//不显示日期

        }

        if (datePickerDialog != null && !datePickerDialog.isShowing())
            datePickerDialog.show();
    }

//    private  MyTouchDialog genderDialog;

//    private void showGender() {
//
//
//        if (getActivity() == null)
//            return;
//
////        if (genderDialog == null) {
////            genderDialog =   new MyTouchDialog(getActivity());
////            genderDialog.setContentView(R.layout.dialog_gender_choose);
////            genderDialog.setCanceledOnTouchOutside(true);
////            genderDialog.findViewById(R.id.tvCancel).setOnClickListener(view1 -> genderDialog.dismiss());
////            genderDialog.findViewById(R.id.tvSure).setOnClickListener(v -> loginOut());
////        }
////        if (!genderDialog.isShowing())
////            genderDialog.show();
//
//
//        final String hobby[] = {"吃饭", "睡觉", "敲代码", "看电视", "打游戏"};
//        AlertDialog dialog1 = new MyTouchDialog.Builder(getActivity())
////                .setIcon(R.drawable.hmbb)
//                .setTitle("性别")
//                .setItems(hobby, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // Toast.makeText(MainActivity.this, hobby[i], Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create();
//        dialog1.show();
//    }

//    private int currentSelectPosition;


    private void showCommonDialog(String title, String[] content, DialogInterface.OnClickListener listener) {

//        if (commonDialog == null)
        if (getActivity() == null)
            return;

//        MyTouchDialog myTouchDialog = new MyTouchDialog(getActivity()) {
//
//            @Override
//            protected void resetTime() {
//
//
//                super.resetTime();
//            }
//        };

//        new myTouchDialog.Builder();
        commonDialog = new MyTouchDialog.Builder(getActivity())
//                .setIcon(R.drawable.hmbb)
                .setTitle(title)
                .setItems(content, listener)
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
                .create();

//        commonDialog.setTitle(title);
        commonDialog.setCancelable(true);
        if (commonDialog != null && !commonDialog.isShowing())
            commonDialog.show();

    }

}