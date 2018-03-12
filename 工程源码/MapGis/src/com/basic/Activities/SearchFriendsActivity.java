package com.basic.Activities;

import com.basic.connectservice.UserService;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.basic.util.StringUtils;
import com.message.net.ChatMessage;
import com.message.net.Config;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFriendsActivity extends ZJBEXBaseActivity {
	
	private ActionBar actionBar;
	private Intent intent;
	private User owner;
	private User frienduser;
	private EditText edittext;
	private Button btn_OK;
	private HandlerSearchFriends handler;   //����һ��Handlerʵ��
	private CustomProgressDialog progressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friends);
		
		 intent=getIntent();
		 if ((intent.getExtras()) != null) {
				Bundle data = intent.getExtras();
				owner = (User) data.getSerializable("user");
				Log.d("user", owner.toString());
			}
		 
		 edittext=(EditText) super.findViewById(R.id.emailText);
		 btn_OK=(Button) super.findViewById(R.id.btn_OK);
		 handler=new HandlerSearchFriends();  
		 initActionBar();
	     initView();
		 initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stu
	}
	
	public void searchfriend(View soucre){
		 String email=edittext.getText().toString();
		if(email.equals(owner.getEmail())){
			Toast.makeText(SearchFriendsActivity.this, "��ѯ�ĺ������Լ�=.=", Toast.LENGTH_SHORT).show();
		}
		else if(StringUtils.isEmail(email)){
			/*new Thread (new Runnable() {
				@Override
				public void run() {
					// TODO �Զ����ɵķ������
					frienduser=getFriendsUser.getUser(email);
					 Message msg = new Message(); 
					if(frienduser==null){
						msg.what=1;
						SearchFriendsActivity.this.handler.sendMessage(msg);
					}
					else {
						//Toast.makeText(SearchFriendsActivity.this, frienduser.toString(), Toast.LENGTH_SHORT).show();
					}
					
				}
			}).start();	*/
			SearchFriendsAsyncTask a=new SearchFriendsAsyncTask(email);
			a.execute();
		}
			else{
				Toast.makeText(SearchFriendsActivity.this, "�����ʽ����ȷ", Toast.LENGTH_SHORT).show();
			}
	}
	private void initView() {
		// TODO Auto-generated method stub
		
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		actionBar = getActionBar();
		actionBar.setCustomView(R.layout.session_top);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);  
		actionBar.setDisplayShowCustomEnabled(true);
		View view=actionBar.getCustomView();
		
		ImageButton rightButton=(ImageButton) view.findViewById(R.id.btn_nnavigation);
		ImageButton back=(ImageButton) view.findViewById(R.id.btn_back);
		TextView txt=(TextView) view.findViewById(R.id.main_Text);
		rightButton.setBackgroundResource(0);
		TextView backtxt=(TextView) view.findViewById(R.id.backtxt);
		
		txt.setText("Ѱ�Һ���");
		
		backtxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				SearchFriendsActivity.this.finish();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				SearchFriendsActivity.this.finish();
			}
		});
	}
  
	public class HandlerSearchFriends extends  Handler{

		public HandlerSearchFriends() { 
		} 

		public HandlerSearchFriends(Looper L) { 
			super(L); 
		} 
		
		// ���������д�˷������������� 
		@Override
		public void handleMessage(Message msg) {
			// TODO �Զ����ɵķ������
			super.handleMessage(msg);
			//�˴�����UI
			switch (msg.what) {
			case 1:
				Toast.makeText(SearchFriendsActivity.this, "�����������û���", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}	
		}
		
	}
	
	public class SearchFriendsAsyncTask extends AsyncTask<Integer, Integer, String>{
       private String email;
       
		public SearchFriendsAsyncTask(String email) {
		super();
		this.email = email;
	}
     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
	//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
		@Override
		protected String doInBackground(Integer... arg0) {
			// TODO �Զ����ɵķ������
			frienduser=UserService.getUser(email,"email");
			if(frienduser==null){
				return "null";
			}
			else if(UserService.Isfriend(owner.getEmail(), frienduser.getEmail())){
				return "yes";
			}
			else return "no";
			
		}
     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
	//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
		@Override
		protected void onPostExecute(String result) {
			// TODO �Զ����ɵķ������
			super.onPostExecute(result);
			
			if (progressDialog != null){
				progressDialog.dismiss();
				progressDialog = null;
			}
			
			if(result.equals("null")){
				Toast.makeText(SearchFriendsActivity.this, "�����������û���", Toast.LENGTH_SHORT).show();
			}
			else if(result.equals("yes")){
				Intent friendInfo=new Intent(SearchFriendsActivity.this, FriendsInfoActivity.class);
				Bundle data=new Bundle();
				data.putSerializable("owneruser", owner);
				data.putSerializable("frienduser", frienduser);
				data.putBoolean("flag", false);
				friendInfo.putExtras(data);
				SearchFriendsActivity.this.startActivity(friendInfo);
			}
			else if(result.equals("no")){
				Intent addfriend=new Intent(SearchFriendsActivity.this, AddFriendActivity.class);
				Bundle data=new Bundle();
				data.putSerializable("owneruser", owner);
				data.putSerializable("frienduser", frienduser);
				addfriend.putExtras(data);
				SearchFriendsActivity.this.startActivity(addfriend);
			}
			
		}
		
      //�����������û�����Excuteʱ�Ľӿڣ�������ִ��֮ǰ��ʼ���ô˷�����������������ʾ���ȶԻ���
		@Override
		protected void onPreExecute() {
			// TODO �Զ����ɵķ������
			super.onPreExecute();
			
			if (progressDialog == null){
				progressDialog = CustomProgressDialog.createDialog(SearchFriendsActivity.this);
		    	progressDialog.setMessage("���ڼ�����...");
			}
			
	    	progressDialog.show();
		}
		
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
