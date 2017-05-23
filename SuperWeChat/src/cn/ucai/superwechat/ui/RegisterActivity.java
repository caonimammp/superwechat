/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.data.Result;
import cn.ucai.superwechat.data.net.IUserModel;
import cn.ucai.superwechat.data.net.OnCompleteListener;
import cn.ucai.superwechat.data.net.UserModel;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.MD5;
import cn.ucai.superwechat.utils.ResultUtils;

import com.hyphenate.exceptions.HyphenateException;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * register screen
 * 
 */
public class RegisterActivity extends BaseActivity {
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;
	private EditText userNickEditText;
	boolean isSuccess=false;
	ProgressDialog pd;
	IUserModel model;
	String username;
	String pwd;
	String confirm_pwd;
	String nick;
	Button btnRegister;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(cn.ucai.superwechat.R.layout.em_activity_register);
		initView();
	}

	private void initView() {
		btnRegister = (Button) findViewById(R.id.btnRegister);
		userNameEditText = (EditText) findViewById(cn.ucai.superwechat.R.id.username);
		userNickEditText = (EditText) findViewById(R.id.nickname);
		passwordEditText = (EditText) findViewById(cn.ucai.superwechat.R.id.password);
		confirmPwdEditText = (EditText) findViewById(cn.ucai.superwechat.R.id.confirm_password);
		pd = new ProgressDialog(RegisterActivity.this);
	}
	private void initDialog(){
		pd.setMessage("注册中");
		pd.show();
	}
	private void dismissDialog(){
		if(pd!=null&&pd.isShowing()){
			pd.dismiss();
		}
	}
	private void initData() {
		username = userNameEditText.getText().toString().trim();
		L.e("main","username"+username);
		nick = userNickEditText.getText().toString().trim();
		pwd = MD5.getMessageDigest(passwordEditText.getText().toString().trim());
		confirm_pwd = MD5.getMessageDigest(confirmPwdEditText.getText().toString().trim());
	}

	public void register(View view){
		initData();
		model = new UserModel();
		model.Register(RegisterActivity.this, username, nick, pwd, new OnCompleteListener<String>() {
			@Override
			public void onSuccess(String s) {
				if(s!=null){
					Result result = ResultUtils.getResultFromJson(s,null);
					if(result.getRetCode()== I.MSG_REGISTER_USERNAME_EXISTS){
						CommonUtils.showLongToast(R.string.User_already_exists);
					}else if(result.getRetCode()==I.MSG_REGISTER_FAIL){
						CommonUtils.showLongToast(R.string.Registration_failed);
					}else {
						isSuccess = false;
						HXRegister();
					}
				}
				if(isSuccess){
					dismissDialog();
				}
			}

			@Override
			public void onError(String error) {
					CommonUtils.showLongToast("注册失败");
			}
		});

	}
	public void HXRegister() {
		initData();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, getResources().getString(cn.ucai.superwechat.R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, getResources().getString(cn.ucai.superwechat.R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(cn.ucai.superwechat.R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
			confirmPwdEditText.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(this, getResources().getString(cn.ucai.superwechat.R.string.Two_input_password), Toast.LENGTH_SHORT).show();
			return;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setMessage(getResources().getString(cn.ucai.superwechat.R.string.Is_the_registered));
			pd.show();

			new Thread(new Runnable() {
				public void run() {
					try {
						// call method in SDK
						EMClient.getInstance().createAccount(username, pwd);
						runOnUiThread(new Runnable() {
							public void run() {
								if (!RegisterActivity.this.isFinishing())
									pd.dismiss();
								// save current user
								SuperWeChatHelper.getInstance().setCurrentUserName(username);
								Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superwechat.R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
								finish();
							}
						});
					} catch (final HyphenateException e) {
						runOnUiThread(new Runnable() {
							public void run() {
								if (!RegisterActivity.this.isFinishing())
									pd.dismiss();
								int errorCode=e.getErrorCode();
								if(errorCode==EMError.NETWORK_ERROR){
									Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superwechat.R.string.network_anomalies), Toast.LENGTH_SHORT).show();
								}else if(errorCode == EMError.USER_ALREADY_EXIST){
									Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superwechat.R.string.User_already_exists), Toast.LENGTH_SHORT).show();
								}else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
									Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superwechat.R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
								}else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
								    Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superwechat.R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superwechat.R.string.Registration_failed), Toast.LENGTH_SHORT).show();
								}
							}
						});
						model.unRegister(RegisterActivity.this, username, new OnCompleteListener<String>() {
							@Override
							public void onSuccess(String result) {
								CommonUtils.showLongToast("注册失败");
							}

							@Override
							public void onError(String error) {
								CommonUtils.showLongToast("服务器异常");
							}
						});
					}
				}
			}).start();

		}
	}

	public void back(View view) {
		finish();
	}

}
