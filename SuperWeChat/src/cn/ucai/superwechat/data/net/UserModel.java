package cn.ucai.superwechat.data.net;

import android.content.Context;
import android.util.Log;

import java.io.File;

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

    @Override
    public void updateUserNick(Context context, String username, String nickname, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.NICK,nickname)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void uploadAvatar(Context context, String username, File file,
                             OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,username)
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    @Override
    public void addContact(Context context, String username,String cname, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CONTACT)
                .addParam(I.Contact.USER_NAME,username)
                .addParam(I.Contact.CU_NAME,cname)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void loadContact(Context context, String username, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,username)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void delContact(Context context, String username, String cname, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CONTACT)
                .addParam(I.Contact.USER_NAME,username)
                .addParam(I.Contact.CU_NAME,cname)
                .targetClass(String.class)
                .execute(listener);
    }
    @Override
    public void updateAvatar(Context context, String username, String avatarType, File file, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,username)
                .addParam(I.AVATAR_TYPE,avatarType)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }
    @Override
    public void createGroup(Context context, String hxid, String name, String des, String owner,
                            boolean isPublic, boolean isInviets, File file,
                            OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_CREATE_GROUP)
                .addParam(I.Group.HX_ID,hxid)
                .addParam(I.Group.NAME,name)
                .addParam(I.Group.DESCRIPTION,des)
                .addParam(I.Group.OWNER,owner)
                .addParam(I.Group.IS_PUBLIC,String.valueOf(isPublic))
                .addParam(I.Group.ALLOW_INVITES,String.valueOf(isInviets))
                .targetClass(String.class)
                .addFile2(file)
                .post()
                .execute(listener);
    }
    @Override
    public void addMembers(Context context, String members, String hxid, OnCompleteListener<String> listener) {
       OkHttpUtils<String> utils = new OkHttpUtils<>(context);
       utils.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBERS)
                       .addParam(I.Member.USER_NAME,members)
                       .addParam(I.Member.GROUP_HX_ID,hxid)
                       .targetClass(String.class)
                       .execute(listener);
    }

    @Override
    public void updateGroupNameByHXID(Context context, String groupId, String newGroupName, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_GROUP_NAME_BYHXID)
                .addParam(I.Group.HX_ID,groupId)
                .addParam(I.Group.NAME,newGroupName)
                .targetClass(String.class)
                .execute(listener);
    }

    @Override
    public void addGroupMember(Context context, String username, String groupId, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBER)
                .addParam(I.Member.USER_NAME,username)
                .addParam(I.Group.HX_ID,groupId)
                .targetClass(String.class)
                .execute(listener);
    }
    @Override
    public void addGroupMembers(Context context, String username, String groupId, OnCompleteListener<String> listener) {
        OkHttpUtils<String> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_GROUP_MEMBERS)
                .addParam(I.Member.USER_NAME,username)
                .addParam(I.Group.HX_ID,groupId)
                .targetClass(String.class)
                .execute(listener);
    }
}
