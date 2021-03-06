/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superwechat.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupManager.EMGroupOptions;
import com.hyphenate.chat.EMGroupManager.EMGroupStyle;
import com.hyphenate.exceptions.HyphenateException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.easeui.domain.Group;
import cn.ucai.easeui.domain.Member;
import cn.ucai.easeui.widget.EaseAlertDialog;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.data.Result;
import cn.ucai.superwechat.data.net.IUserModel;
import cn.ucai.superwechat.data.net.OnCompleteListener;
import cn.ucai.superwechat.data.net.UserModel;
import cn.ucai.superwechat.utils.CommonUtils;
import cn.ucai.superwechat.utils.L;
import cn.ucai.superwechat.utils.ResultUtils;

import static cn.ucai.superwechat.I.REQUEST_CODE_PICK_PIC;
import static cn.ucai.superwechat.ui.UserProfileActivity.getAvatarPath;

public class NewGroupActivity extends BaseActivity {
    private static final String TAG = "NewGroupActivity";
    private EditText groupNameEditText;
    private ProgressDialog progressDialog;
    private EditText introductionEditText;
    private CheckBox publibCheckBox;
    private CheckBox memberCheckbox;
    private TextView secondTextView;

    IUserModel model;
    LinearLayout groupIconLayout;
    ImageView ivIcon;
    String avatarName;
    File avatarFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.em_activity_new_group);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        showLeftBack();
        groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
        introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
        publibCheckBox = (CheckBox) findViewById(R.id.cb_public);
        memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
        secondTextView = (TextView) findViewById(R.id.second_desc);
        groupIconLayout = (LinearLayout) findViewById(R.id.layout_group_icon);
        ivIcon = (ImageView) findViewById(R.id.iv_avatar);
        publibCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondTextView.setText(R.string.join_need_owner_approval);
                } else {
                    secondTextView.setText(R.string.Open_group_members_invited);
                }
            }
        });
        setListener();
        model = new UserModel();
    }

    private void setListener() {
        titleBar.getRightLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        groupIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadHeadPhoto();
            }
        });
    }

    /**
     * @param
     */
    public void save() {
        String name = groupNameEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            new EaseAlertDialog(this, R.string.Group_name_cannot_be_empty).show();
        } else {
            // select from contact list
            startActivityForResult(new Intent(this, GroupPickContactsActivity.class).putExtra("groupName", name), I.REQUEST_CODE_PICK_CONTACT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_PIC:
                if (data == null || data.getData() == null) {
                    return;
                }
                startPhotoZoom(data.getData());
                break;
            case I.REQUEST_CODE_CUTTING:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            case I.REQUEST_CODE_PICK_CONTACT:
                if (resultCode == RESULT_OK) {
                    createEMGroup(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void createEMGroup(final Intent data) {
        final String st2 = getResources().getString(R.string.Failed_to_create_groups);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String groupName = groupNameEditText.getText().toString().trim();
                String desc = introductionEditText.getText().toString();
                String[] members = data.getStringArrayExtra("newmembers");
                try {
                    EMGroupOptions option = new EMGroupOptions();
                    option.maxUsers = 200;
                    option.inviteNeedConfirm = true;

                    String reason = NewGroupActivity.this.getString(R.string.invite_join_group);
                    reason = EMClient.getInstance().getCurrentUser() + reason + groupName;

                    if (publibCheckBox.isChecked()) {
                        option.style = memberCheckbox.isChecked() ? EMGroupStyle.EMGroupStylePublicJoinNeedApproval : EMGroupStyle.EMGroupStylePublicOpenJoin;
                    } else {
                        option.style = memberCheckbox.isChecked() ? EMGroupStyle.EMGroupStylePrivateMemberCanInvite : EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                    EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(groupName, desc, members, reason, option);

                    createAppGroup(emGroup,members);

                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(NewGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    public void uploadHeadPhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dl_title_upload_photo);
        builder.setItems(new String[]{getString(R.string.dl_msg_take_photo), getString(R.string.dl_msg_local_upload)},
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                Toast.makeText(NewGroupActivity.this, getString(R.string.toast_no_support),
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(pickIntent, REQUEST_CODE_PICK_PIC);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, I.REQUEST_CODE_CUTTING);
    }

    /**
     * save the picture data
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            ivIcon.setImageDrawable(drawable);
            saveBitmapFile(photo);
        }
    }

    private void saveBitmapFile(Bitmap bitmap) {
        if (bitmap != null) {
            String imagePath = getAvatarPath(NewGroupActivity.this, I.AVATAR_TYPE) + "/" + getAvatarName() + ".jpg";
            avatarFile = new File(imagePath);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(avatarFile));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getAvatarName() {
        avatarName = I.AVATAR_TYPE_GROUP_PATH + System.currentTimeMillis();
        return avatarName;
    }

    private void createFaile(final HyphenateException e) {
        final String st2 = getResources().getString(R.string.Is_to_create_a_group_chat);
        runOnUiThread(new Runnable() {
            public void run() {
                if (e != null) {
                    Toast.makeText(NewGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                } else {
                    CommonUtils.showLongToast(st2);
                }
            }
        });
    }
    Group group1;
    private void createAppGroup(final EMGroup group, final String[] members) {
        model.createGroup(NewGroupActivity.this, group.getGroupId(), group.getGroupName(), group.getDescription(),
                group.getOwner(), group.isPublic(), group.isMemberAllowToInvite(), avatarFile,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        boolean isSuccess = false;
                        if (s != null) {
                            Result<Group> result = ResultUtils.getResultFromJson(s, Group.class);
                            if (result != null && result.isRetMsg()) {

                                group1 = result.getRetData();
                                createSuccess();
                                if(members!=null&&members.length>0){
                                    addMembers(group1.getMGroupHxid(),getMembers(members));
                                }else {
                                    isSuccess = true;
                                }
                                if(group1!=null){
                                    setCodeorView(group1);
                                }
                            }
                        }
                        if (!isSuccess) {
                            createFaile(null);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        createFaile(null);
                    }
                });
    }

    private void addMembers(String hxid, String members) {
        model.addMembers(NewGroupActivity.this, members, hxid,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (s!=null){
                            Result result = ResultUtils.getResultFromJson(s, Group.class);
                            if (result!=null && result.isRetMsg()){
                                createSuccess();
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }

    private String getMembers(String[] members) {
        String m = Arrays.toString(members).toString();
        StringBuffer str = new StringBuffer();
        for (String member : members) {
            str.append(member).append(",");
        }
        return str.toString();
    }

    private void createSuccess() {
        runOnUiThread(new Runnable() {
            public void run() {
//                progressDialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void back(View view) {
        finish();
    }

    public void setCodeorView(Group codeorView) {
        String avatar = group1.getAvatar();
    }
}
