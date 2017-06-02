package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;

import cn.ucai.easeui.domain.User;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.MainActivity;
import cn.ucai.superwechat.ui.ProfileActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
import cn.ucai.superwechat.ui.SendMessageActivity;
import cn.ucai.superwechat.ui.SettingsActivity;
import cn.ucai.superwechat.ui.UserProfileActivity;
import cn.ucai.superwechat.ui.VideoCallActivity;
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
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    private static void mfgt(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    private static void mfgt(Context context,Class clazz) {
        context.startActivity(new Intent(context,clazz));
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoWelcome(Activity activity) {
        mfgt(activity,WelcomeActivity.class);
    }

    public static void gotoRegister(Activity activity) {
        mfgt(activity, RegisterActivity.class);
    }
    public static void gotoSettings(Activity activity){
        mfgt(activity, SettingsActivity.class);
    }

    public static void logout(Activity activity) {
        mfgt(activity,new Intent(activity,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    public static void gotoProfile(Activity activity, User user) {
        mfgt(activity,new Intent(activity,ProfileActivity.class)
                .putExtra(I.User.TABLE_NAME,user));
    }
    public static void gotoProfile(Activity activity, String username) {
        mfgt(activity,new Intent(activity,ProfileActivity.class)
                .putExtra(I.User.USER_NAME,username));
    }
    public static void gotoVideo(Activity activity, String username) {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(activity, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            mfgt(activity,new Intent(activity, VideoCallActivity.class).putExtra("username", username)
                    .putExtra("isComingCall", false));
        }
    }

}
