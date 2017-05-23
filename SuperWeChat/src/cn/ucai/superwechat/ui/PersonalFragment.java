package cn.ucai.superwechat.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.easeui.domain.User;
import cn.ucai.easeui.ui.EaseBaseFragment;
import cn.ucai.superwechat.R;
import cn.ucai.superwechat.SuperWeChatHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends EaseBaseFragment {


    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvNick)
    TextView tvNick;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.ivSetting)
    ImageView ivSetting;
    Unbinder unbinder;

    public PersonalFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setTitle(getString(R.string.me));
        User user = SuperWeChatHelper.getInstance().getUserProfileManager().getCurrentAPPUserInfo();
        if(user!=null){
            tvNick.setText(user.getMUserNick());
            tvUserName.setText("微信号： "+user.getMUserName());
            if(!TextUtils.isEmpty(user.getAvatar())){
                Glide.with(getContext()).load(user.getAvatar()).placeholder(cn.ucai.superwechat.R.drawable.em_default_avatar).into(ivAvatar);
            }else{
                Glide.with(getContext()).load(cn.ucai.superwechat.R.drawable.em_default_avatar).into(ivAvatar);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_Perfile, R.id.layout_Money, R.id.layout_Setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_Perfile:
                break;
            case R.id.layout_Money:
                break;
            case R.id.layout_Setting:
                break;
        }
    }
}
