package net.xnzn.app.selfdevice.my.fragment;

import android.view.View;
import android.widget.TextView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.BaseFragment;
import net.xnzn.app.selfdevice.widget.CustomItemMiddleEditView;

public class MyModifyPwdFragment extends BaseFragment {


    private CustomItemMiddleEditView cmdPwdBefore;
    private CustomItemMiddleEditView cmdNewPwd;
    private CustomItemMiddleEditView cmdNewPwdRepet;
    private TextView tvModify;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_modify_pwd;
    }

    @Override
    public void initView(View view) {

        cmdPwdBefore = view.findViewById(R.id.cmdPwdBefore);
        cmdNewPwd = view.findViewById(R.id.cmdNewPwd);
        cmdNewPwdRepet = view.findViewById(R.id.cmdNewPwdRepet);
        tvModify = view.findViewById(R.id.tvModify);

        tvModify.setOnClickListener(v -> {
            modify();
        });
    }

    private void modify() {

        cmdPwdBefore.getInput();
        cmdNewPwd.getInput();
        cmdNewPwdRepet.getInput();

    }

    @Override
    public void initData() {

    }
}