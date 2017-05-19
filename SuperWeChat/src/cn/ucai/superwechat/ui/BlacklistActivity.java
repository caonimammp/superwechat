package cn.ucai.superwechat.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;

import cn.ucai.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Collections;
import java.util.List;

/**
 * Blacklist screen
 * 
 */
public class BlacklistActivity extends BaseActivity {
	private BlacklistAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(cn.ucai.superwechat.R.layout.em_activity_black_list);

		ListView listView = (ListView) findViewById(cn.ucai.superwechat.R.id.list);

		// get blacklist from local databases
		 List<String> blacklist = EMClient.getInstance().contactManager().getBlackListUsernames();

		// show the blacklist
		if (blacklist != null) {
			Collections.sort(blacklist);
			adapter = new BlacklistAdapter(this, blacklist);
			listView.setAdapter(adapter);
		}

		registerForContextMenu(listView);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(cn.ucai.superwechat.R.menu.em_remove_from_blacklist, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == cn.ucai.superwechat.R.id.remove) {
			final String tobeRemoveUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// remove user out from blacklist
			removeOutBlacklist(tobeRemoveUser);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * remove user out from blacklist
	 * 
	 * @param tobeRemoveUser
	 */
	void removeOutBlacklist(final String tobeRemoveUser) {
	    final ProgressDialog pd = new ProgressDialog(this);
	    pd.setMessage(getString(cn.ucai.superwechat.R.string.be_removing));
	    pd.setCanceledOnTouchOutside(false);
	    pd.show();
	    new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager().removeUserFromBlackList(tobeRemoveUser);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            adapter.remove(tobeRemoveUser);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), cn.ucai.superwechat.R.string.Removed_from_the_failure, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
	}

	public void toGroupDetails(View view) {
	}

	/**
	 * adapter
	 * 
	 */
	private class BlacklistAdapter extends ArrayAdapter<String> {

		public BlacklistAdapter(Context context, List<String> objects) {
			super(context, 0, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getContext(), cn.ucai.superwechat.R.layout.ease_row_contact, null);
			}
			String username = getItem(position);
			TextView name = (TextView) convertView.findViewById(cn.ucai.superwechat.R.id.name);
			ImageView avatar = (ImageView) convertView.findViewById(cn.ucai.superwechat.R.id.avatar);
			
			EaseUserUtils.setUserAvatar(getContext(), username, avatar);
			EaseUserUtils.setUserNick(username, name);
			
			return convertView;
		}

	}
}
