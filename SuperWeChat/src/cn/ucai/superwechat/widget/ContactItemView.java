package cn.ucai.superwechat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactItemView extends LinearLayout{

    private TextView unreadMsgView;

    public ContactItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ContactItemView(Context context) {
        super(context);
        init(context, null);
    }
    
    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, cn.ucai.superwechat.R.styleable.ContactItemView);
        String name = ta.getString(cn.ucai.superwechat.R.styleable.ContactItemView_contactItemName);
        Drawable image = ta.getDrawable(cn.ucai.superwechat.R.styleable.ContactItemView_contactItemImage);
        ta.recycle();
        
        LayoutInflater.from(context).inflate(cn.ucai.superwechat.R.layout.em_widget_contact_item, this);
        ImageView avatar = (ImageView) findViewById(cn.ucai.superwechat.R.id.avatar);
        unreadMsgView = (TextView) findViewById(cn.ucai.superwechat.R.id.unread_msg_number);
        TextView nameView = (TextView) findViewById(cn.ucai.superwechat.R.id.name);
        if(image != null){
            avatar.setImageDrawable(image);
        }
        nameView.setText(name);
    }
    
    public void setUnreadCount(int unreadCount){
        unreadMsgView.setText(String.valueOf(unreadCount));
    }
    
    public void showUnreadMsgView(){
        unreadMsgView.setVisibility(View.VISIBLE);
    }
    public void hideUnreadMsgView(){
        unreadMsgView.setVisibility(View.INVISIBLE);
    }
    
}
