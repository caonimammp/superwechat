package cn.ucai.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.easeui.domain.User;
import cn.ucai.easeui.utils.EaseUserUtils;
import cn.ucai.superwechat.I;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;
import cn.ucai.superwechat.utils.MFGT;

/**
 * Created by clawpo on 2017/5/25.
 */

public class ProfileActivity extends BaseActivity {
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.tv_userinfo_nick)
    TextView mTvUserinfoNick;
    @BindView(R.id.tv_userinfo_name)
    TextView mTvUserinfoName;
    @BindView(R.id.btn_add_contact)
    Button mBtnAddContact;
    @BindView(R.id.btn_send_msg)
    Button mBtnSendMsg;
    @BindView(R.id.btn_send_video)
    Button mBtnSendVideo;
    User user = null;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        super.onCreate(arg0);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        String username = getIntent().getStringExtra(I.User.USER_NAME);
        if(username!=null){
            user = SuperWeChatHelper.getInstance().getAPPContactList().get(username);
        }
        if(user==null){
            user = (User)getIntent().getSerializableExtra(I.User.TABLE_NAME);
        }
        if(user!=null){
            showInfo();
        }else {
            finish();
        }
    }

    private void showInfo() {
        Log.i("main", "ProfileActivity.showInfo:"+user.toString());
        mTvUserinfoName.setText(user.getMUserName());
        EaseUserUtils.setAPPUserNick(user.getMUserName(), mTvUserinfoNick);
        EaseUserUtils.setAPPUserAvatar(ProfileActivity.this, user, mProfileImage);
        showButton(SuperWeChatHelper.getInstance().getContactList().containsKey(user.getMUserName()));
    }

    private void showButton(boolean isContact) {
        mBtnAddContact.setVisibility(isContact ? View.GONE : View.VISIBLE);
        mBtnSendMsg.setVisibility(isContact ? View.VISIBLE : View.GONE);
        mBtnSendVideo.setVisibility(isContact ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.btn_add_contact)
    public void onClick() {
        startActivity(new Intent(this,SendMessageActivity.class).putExtra(I.User.USER_NAME,user.getMUserName()));
    }
}