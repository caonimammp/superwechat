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

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExitGroupDialog extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.ucai.superwechat.R.layout.em_logout_actionsheet);

        TextView text = (TextView) findViewById(cn.ucai.superwechat.R.id.tv_text);
        Button exitBtn = (Button) findViewById(cn.ucai.superwechat.R.id.btn_exit);
        
        text.setText(cn.ucai.superwechat.R.string.exit_group_hint);
        String toast = getIntent().getStringExtra("deleteToast");
        if(toast != null)
        	text.setText(toast);
        exitBtn.setText(cn.ucai.superwechat.R.string.exit_group);
    }
    
    public void logout(View view){
    	setResult(Activity.RESULT_OK);
        finish();
        
    }
    
    public void cancel(View view) {
        finish();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
