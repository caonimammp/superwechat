package cn.ucai.superwechat.data.net;

import android.content.Context;


/**
 * Created by Administrator on 2017/5/19.
 */

public interface IUserModel {
    void Register(Context context, String userName, String nick, String passWord, OnCompleteListener<String> listener);

    void unRegister(Context context,String userName,OnCompleteListener<String> listener);
}
