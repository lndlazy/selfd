package net.xnzn.app.selfdevice.login;

import android.app.Activity;
import android.text.TextUtils;

import net.xnzn.app.selfdevice.charge.ChargeActivity;
import net.xnzn.app.selfdevice.my.PersonalActivity;
import net.xnzn.app.selfdevice.query.QueryActivity;
import net.xnzn.app.selfdevice.sign.SignActivity;
import net.xnzn.app.selfdevice.utils.NextPageConstant;

public class NextPageAction {

    //进入下一个页面
    public static Class<? extends Activity> go2nextPage(String nextPage) {

        if (TextUtils.isEmpty(nextPage))
            return null;

        switch (nextPage) {

            case NextPageConstant.CHARGE_PAGE:
                return ChargeActivity.class;

            case NextPageConstant.QUERY_PAGE:
                return QueryActivity.class;


            case NextPageConstant.PERSON_PAGE:
                return PersonalActivity.class;


            case NextPageConstant.SIGN_PAGE:
                return SignActivity.class;

        }

        return null;
    }


} 