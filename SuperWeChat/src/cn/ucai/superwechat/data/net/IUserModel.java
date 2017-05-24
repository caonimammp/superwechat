package cn.ucai.superwechat.data.net;

import android.content.Context;

import java.io.File;


/**
 * Created by Administrator on 2017/5/19.
 */

public interface IUserModel {
    void Register(Context context, String userName, String nick, String passWord, OnCompleteListener<String> listener);
    void unRegister(Context context,String userName,OnCompleteListener<String> listener);
    void findUserByUserName(Context context,String userName,OnCompleteListener<String> listener);
    void updateUserNick(Context context,String username,String nickname,OnCompleteListener<String> listener);
    void uploadAvatar(Context context, String username, File file, OnCompleteListener<String> listener);
    void addContact(Context context,String username,String cname, OnCompleteListener<String> listener);
    void loadContact(Context context,String username,OnCompleteListener<String> listener);
    void delContact(Context context,String username,String cname, OnCompleteListener<String> listener);
    void updateAvatar(Context context, String username, String avatarType, File file, OnCompleteListener<String> listener);
}
