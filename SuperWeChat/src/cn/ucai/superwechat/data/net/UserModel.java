package cn.ucai.superwechat.data.net;

import android.content.Context;

import cn.ucai.easeui.domain.User;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/5/19.
 */

public class UserModel implements IUserModel{
    @Override
    public void Register(Context context, String userName, String nick, String passWord, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,nick)
                .addParam(I.User.PASSWORD,passWord)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    @Override
    public void unRegister(Context context, String userName, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void findUserByUserName(Context context, String userName, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(listener);
    }
}
