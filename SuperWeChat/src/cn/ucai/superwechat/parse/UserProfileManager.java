package cn.ucai.superwechat.parse;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;

import cn.ucai.easeui.domain.User;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.SuperWeChatHelper.DataSyncListener;
import cn.ucai.superwechat.data.Result;
import cn.ucai.superwechat.data.net.IUserModel;
import cn.ucai.superwechat.data.net.OnCompleteListener;
import cn.ucai.superwechat.data.net.UserModel;
import cn.ucai.superwechat.utils.PreferenceManager;
import cn.ucai.easeui.domain.EaseUser;
import cn.ucai.superwechat.utils.ResultUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserProfileManager {
	IUserModel model;
	/**
	 * application context
	 */
	protected Context appContext = null;

	/**
	 * init flag: test if the sdk has been inited before, we don't need to init
	 * again
	 */
	private boolean sdkInited = false;

	/**
	 * HuanXin sync contact nick and avatar listener
	 */
	private List<DataSyncListener> syncContactInfosListeners;

	private boolean isSyncingContactInfosWithServer = false;

	private EaseUser currentUser;
	private User currentAPPUser;
	private String currentAPPUserNick;

	public UserProfileManager() {
	}

	public synchronized boolean init(Context context) {
		if (sdkInited) {
			return true;
		}
		ParseManager.getInstance().onInit(context);
		syncContactInfosListeners = new ArrayList<DataSyncListener>();
		sdkInited = true;
		model = new UserModel();
		appContext=context;
		return true;
	}

	public void addSyncContactInfoListener(DataSyncListener listener) {
		if (listener == null) {
			return;
		}
		if (!syncContactInfosListeners.contains(listener)) {
			syncContactInfosListeners.add(listener);
		}
	}

	public void removeSyncContactInfoListener(DataSyncListener listener) {
		if (listener == null) {
			return;
		}
		if (syncContactInfosListeners.contains(listener)) {
			syncContactInfosListeners.remove(listener);
		}
	}

	public void asyncFetchContactInfosFromServer(List<String> usernames, final EMValueCallBack<List<EaseUser>> callback) {
		if (isSyncingContactInfosWithServer) {
			return;
		}
		isSyncingContactInfosWithServer = true;
		ParseManager.getInstance().getContactInfos(usernames, new EMValueCallBack<List<EaseUser>>() {

			@Override
			public void onSuccess(List<EaseUser> value) {
				isSyncingContactInfosWithServer = false;
				// in case that logout already before server returns,we should
				// return immediately
				if (!SuperWeChatHelper.getInstance().isLoggedIn()) {
					return;
				}
				if (callback != null) {
					callback.onSuccess(value);
				}
			}

			@Override
			public void onError(int error, String errorMsg) {
				isSyncingContactInfosWithServer = false;
				if (callback != null) {
					callback.onError(error, errorMsg);
				}
			}

		});

	}

	public void notifyContactInfosSyncListener(boolean success) {
		for (DataSyncListener listener : syncContactInfosListeners) {
			listener.onSyncComplete(success);
		}
	}

	public boolean isSyncingContactInfoWithServer() {
		return isSyncingContactInfosWithServer;
	}

	public synchronized void reset() {
		isSyncingContactInfosWithServer = false;
		currentUser = null;
		currentAPPUser=null;
		PreferenceManager.getInstance().removeCurrentUserInfo();
	}

	public synchronized EaseUser getCurrentUserInfo() {
		if (currentUser == null) {
			String username = EMClient.getInstance().getCurrentUser();
			currentUser = new EaseUser(username);
			String nick = getCurrentUserNick();
			currentUser.setNick((nick != null) ? nick : username);
			currentUser.setAvatar(getCurrentUserAvatar());
		}
		return currentUser;
	}
	public synchronized User getCurrentAPPUserInfo() {
		if (currentAPPUser == null) {
			String username = EMClient.getInstance().getCurrentUser();
			currentAPPUser = new User(username);
			String nick = getCurrentUserNick();
			currentAPPUser.setMUserNick((nick != null) ? nick : username);
			currentAPPUser.setAvatar(getCurrentUserAvatar());
		}
		return currentAPPUser;
	}

	public boolean updateCurrentUserNickName(final String nickname) {
		boolean isSuccess = ParseManager.getInstance().updateParseNickName(nickname);
		if (isSuccess) {
			setCurrentAPPUserNick(nickname);
		}
		return isSuccess;
	}

	public String uploadUserAvatar(byte[] data) {
		String avatarUrl = ParseManager.getInstance().uploadParseAvatar(data);
		if (avatarUrl != null) {
			setCurrentUserAvatar(avatarUrl);
		}
		return avatarUrl;
	}

	public void asyncGetCurrentUserInfo() {
		ParseManager.getInstance().asyncGetCurrentUserInfo(new EMValueCallBack<EaseUser>() {

			@Override
			public void onSuccess(EaseUser value) {
			    if(value != null){
    				setCurrentUserNick(value.getNick());
    				setCurrentUserAvatar(value.getAvatar());
			    }
			}

			@Override
			public void onError(int error, String errorMsg) {

			}
		});

	}
	public void asyncGetCurrentAPPUserInfo() {
		model.findUserByUserName(appContext, EMClient.getInstance().getCurrentUser(), new OnCompleteListener<String>() {
			@Override
			public void onSuccess(String s) {
				if (s != null) {
					Result<User> result = ResultUtils.getResultFromJson(s, User.class);
					if (result != null && result.isRetMsg()) {
						User user = result.getRetData();
						if (user != null) {
							setCurrentAPPUserNick(user.getMUserNick());
							setCurrentAPPUserAvatar(user.getAvatar());
						}
					}
				}
			}

			@Override
			public void onError(String error) {

			}
		});

	}
	public void uploadAppUserAvatar(File file){
		model.updateAvatar(appContext, EMClient.getInstance().getCurrentUser(), I.AVATAR_TYPE_USER_PATH,
				file, new OnCompleteListener<String>() {
					@Override
					public void onSuccess(String s) {
						boolean isSuccess = false;
						if (s!=null){
							Result<User> result = ResultUtils.getResultFromJson(s, User.class);
							if (result!=null){
								if (result.isRetMsg()){
									User user = result.getRetData();
									if (user!=null){
										isSuccess = true;
										setCurrentAPPUserAvatar(user.getAvatar());
									}
								}
							}
						}
						appContext.sendBroadcast(new Intent(I.REQUEST_UPDATE_AVATAR)
								.putExtra(I.Avatar.UPDATE_TIME,isSuccess));
					}

					@Override
					public void onError(String error) {
						appContext.sendBroadcast(new Intent(I.REQUEST_UPDATE_AVATAR)
								.putExtra(I.Avatar.UPDATE_TIME,false));
					}
				});
	}


	public void asyncGetUserInfo(final String username,final EMValueCallBack<EaseUser> callback){
		ParseManager.getInstance().asyncGetUserInfo(username, callback);
	}
	private void setCurrentUserNick(String nickname) {
		getCurrentUserInfo().setNick(nickname);
		PreferenceManager.getInstance().setCurrentUserNick(nickname);
	}

	private void setCurrentUserAvatar(String avatar) {
		getCurrentUserInfo().setAvatar(avatar);
		PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
	}
	private void setCurrentAPPUserNick(String nickname) {
		getCurrentAPPUserInfo().setMUserNick(nickname);
		PreferenceManager.getInstance().setCurrentUserNick(nickname);
	}

	private void setCurrentAPPUserAvatar(String avatar) {
		getCurrentAPPUserInfo().setAvatar(avatar);
		PreferenceManager.getInstance().setCurrentUserAvatar(avatar);
	}

	private String getCurrentUserNick() {
		return PreferenceManager.getInstance().getCurrentUserNick();
	}

	private String getCurrentUserAvatar() {
		return PreferenceManager.getInstance().getCurrentUserAvatar();
	}

}
