package com.basic.Activities;

import com.basic.service.model.User;
import com.message.net.ChatMessage;
import com.message.net.Config;

import android.os.Bundle;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyPublishActivity extends ZJBEXBaseActivity {
	
	private ActionBar actionBar;
	private Intent intent;
	private User owneruser=new User();
	
	private RelativeLayout  MyPublishPublic;
	private RelativeLayout  MyPublishJbex;
	private RelativeLayout  MyPublishdynamic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_publish);
		
		intent=getIntent();
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("owneruser", owneruser.toString());
		}
		
		MyPublishPublic=(RelativeLayout) findViewById(R.id.new_public);
		MyPublishJbex=(RelativeLayout) findViewById(R.id.new_Jbex);
		MyPublishdynamic=(RelativeLayout) findViewById(R.id.new_dynamic);
		
		initActionBar(); // ���ظı�actionBar
		initListener();
	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		MyPublishPublic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				if(owneruser.getSecurityControl()!=0)
				{
				Intent MyPublishpublic = new Intent(MyPublishActivity.this,
						MyPublishPublicActivity.class);
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				MyPublishpublic.putExtras(data);
				startActivity(MyPublishpublic);
				}else{
					Toast.makeText(MyPublishActivity.this, "��Ǹ����û�з���������Ϣ��Ȩ�ޡ���Ҫ������qq798750509��ϵ", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		MyPublishJbex.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				Intent MyPublishJbex = new Intent(MyPublishActivity.this,
						MyPublishJbexActivity.class);
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				MyPublishJbex.putExtras(data);
				startActivity(MyPublishJbex);
			}
		});
		
		MyPublishdynamic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				Intent MyPublishdynamic = new Intent(MyPublishActivity.this,
						MyPublishDynamciAcvtivity.class);
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				MyPublishdynamic.putExtras(data);
				startActivity(MyPublishdynamic);
			}
		});
	}

	private void initActionBar() {
		// TODO �Զ����ɵķ������
		actionBar=getActionBar();
		actionBar.setCustomView(R.layout.session_top);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		
		View view = actionBar.getCustomView();
		ImageButton nnavigation = (ImageButton) view
				.findViewById(R.id.btn_nnavigation);
		ImageButton back = (ImageButton) view.findViewById(R.id.btn_back);
		TextView txt = (TextView) view.findViewById(R.id.main_Text);
		TextView backtxt = (TextView) view.findViewById(R.id.backtxt);

		nnavigation.setBackgroundResource(0);

		txt.setText("�ҵķ���");

		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				MyPublishActivity.this.finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				MyPublishActivity.this.finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_publish, menu);
		return true;
	}

	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������
		if(msg.what==Config.SEND_NOTIFICATION){
			Bundle bundle=msg.getData();
			ChatMessage chatMessage=(ChatMessage)bundle.getSerializable("chatMessage");
			saveToDb(chatMessage,Config.DateBase_GET_MESSAGE);

			sendNotifycation(chatMessage.getSelf(),chatMessage.getFriend());}
		else if (msg.what==Config.SEND_NOTIFICATION_JBEX_FRIEND){
			sendNotifycation_JBEXFriend();
	}
	}

}
