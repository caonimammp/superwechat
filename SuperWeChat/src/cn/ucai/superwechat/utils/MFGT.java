package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ucai.superwechat.R;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.SplashActivity;
import cn.ucai.superwechat.ui.WelcomeActivity;

/**
 * Created by Administrator on 2017/5/19.
 */

public class MFGT {

    public static void gotoLogin(Activity activity) {
        mfgt(activity,LoginActivity.class);
    }
    public static void gotoMain(Activity activity) {
        mfgt(activity,MainActivity.class);
    }

    public static void mfgt(Context context,Class clazz) {
        context.startActivity(new Intent(context,clazz));
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoWelcome(Activity activity) {
        mfgt(activity,WelcomeActivity.class);
    }
}
