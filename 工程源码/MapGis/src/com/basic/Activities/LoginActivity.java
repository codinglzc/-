package com.basic.Activities;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.connectservice.ConnectService_Login;
import com.basic.connectservice.GetUser;
import com.basic.connectservice.UserService;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.message.net.ChatMessage;
import com.message.net.Communication;
import com.message.net.Config;

public class LoginActivity extends ZJBEXBaseActivity {

	private Button loginButton;
	private EditText editText_username;
	private EditText editText_password;
	private String userName;
	private String passWord;

	public User user = new User();

	private Intent intent;
	private TextView registerTxt = null;
	
	private CustomProgressDialog progressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);

		/*
		 * ��½-----------------------------------------------------------------
		 */
		con = Communication.newInstance();
		
		initView();
	
		/*
		 * ת��ע��ҳ��----------------------------------------------------------------
		 * --
		 */
		registerTxt = (TextView) super.findViewById(R.id.registerTxt);
		registerTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intent = new Intent(LoginActivity.this, RegisterActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});

	}

	private void initView() {
		// TODO Auto-generated method stub
		editText_username = (EditText) findViewById(R.id.editText_username);
		editText_password = (EditText) findViewById(R.id.editText_password);
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (matchLoginMsg()) {
					LoginAsyncTask a=new LoginAsyncTask();
					a.execute();
				}

			}
		});
	}

	protected boolean matchLoginMsg() {
		// TODO Auto-generated method stub
		userName = editText_username.getText().toString().trim();
		passWord = editText_password.getText().toString().trim();
		if (userName.equals("")) {
			Toast.makeText(LoginActivity.this, "�˺Ų���Ϊ��", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (passWord.equals("")) {
			Toast.makeText(LoginActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
   
	public class LoginAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				boolean flag;
				flag=UserService.LoginUser(editText_username.getText().toString(),editText_password.getText().toString());
				user=UserService.getUser(editText_username.getText().toString(), "email");
				
				return String.valueOf(flag);
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				if(result.equals("true")){
					
					     setSelf(user);
					    con.sendUid(user.getUser_id());
					     
					    //����������ͻ�ȡΪ������Ϣ���� 
			            con.getOfflineMessage();
			           
						// ����Main Activity
						Intent nextIntent = new Intent(LoginActivity.this,
								MainActivity.class);
						Bundle data = new Bundle();
						data.putSerializable("user", user);
						nextIntent.putExtras(data);
						LoginActivity.this.startActivity(nextIntent);
						
					   LoginActivity.this.finish();
					   
				}else if(result.equals("false")){
					Toast.makeText(LoginActivity.this, "�û����������������������!",
							Toast.LENGTH_SHORT).show();
				}
				
				super.onPostExecute(result);
				if (progressDialog != null){
					progressDialog.dismiss();
					progressDialog = null;
				}	
			
			}
			
	      //�����������û�����Excuteʱ�Ľӿڣ�������ִ��֮ǰ��ʼ���ô˷�����������������ʾ���ȶԻ���
			@Override
			protected void onPreExecute() {
				// TODO �Զ����ɵķ������
				super.onPreExecute();
				
				if (progressDialog == null){
					progressDialog = CustomProgressDialog.createDialog(LoginActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
}
