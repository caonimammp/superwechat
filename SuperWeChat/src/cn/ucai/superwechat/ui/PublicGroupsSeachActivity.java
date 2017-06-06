package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import cn.ucai.easeui.utils.EaseUserUtils;
import cn.ucai.superwechat.R;

public class PublicGroupsSeachActivity extends BaseActivity{
    private RelativeLayout containerLayout;
    private EditText idET;
    private TextView nameText;
    private ImageView avatar;
    public static EMGroup searchedGroup;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(cn.ucai.superwechat.R.layout.em_activity_public_groups_search);
        super.onCreate(arg0);
        showLeftBack();
        setListener();
        containerLayout = (RelativeLayout) findViewById(cn.ucai.superwechat.R.id.rl_searched_group);
        idET = (EditText) findViewById(cn.ucai.superwechat.R.id.et_search_id);
        nameText = (TextView) findViewById(cn.ucai.superwechat.R.id.name);
        avatar = (ImageView)findViewById(R.id.avatar);
        searchedGroup = null;
    }

    private void setListener() {
        titleBar.getRightLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        searchGroup();
                    }
            });
    }


    public void searchGroup(){
        if(TextUtils.isEmpty(idET.getText())){
            return;
        }
        
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(cn.ucai.superwechat.R.string.searching));
        pd.setCancelable(false);
        pd.show();
        
        new Thread(new Runnable() {

            public void run() {
                try {
                    searchedGroup = EMClient.getInstance().groupManager().getGroupFromServer(idET.getText().toString());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            containerLayout.setVisibility(View.VISIBLE);
                            nameText.setText(searchedGroup.getGroupName());
                            EaseUserUtils.setGroupAvatarByhxid(PublicGroupsSeachActivity.this, searchedGroup.getGroupId(),avatar);
                        }
                    });
                    
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            searchedGroup = null;
                            containerLayout.setVisibility(View.GONE);
                            if(e.getErrorCode() == EMError.GROUP_INVALID_ID){
                                Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superwechat.R.string.group_not_existed), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superwechat.R.string.group_search_failed) + " : " + getString(cn.ucai.superwechat.R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
        
    }
    
    
    /**
     * enter the detail screen of group
     * @param view
     */
    public void enterToDetails(View view){
        startActivity(new Intent(this, GroupSimpleDetailActivity.class));
    }
}
