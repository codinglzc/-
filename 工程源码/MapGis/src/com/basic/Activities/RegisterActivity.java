package com.basic.Activities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.basic.connectservice.AttentionService;
import com.basic.connectservice.ConnectService_Register;
import com.basic.connectservice.UserService;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.message.net.ChatMessage;
import com.message.net.Config;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegisterActivity extends ZJBEXBaseActivity {

	private EditText editText_user_nickname = null; // �û��ǳ�
	private EditText editText_password = null; // ����
	private EditText editText_repassword = null; // �ظ���������
	private EditText editText_email = null; // ����
	private Button button_register = null; // ע�ᰴť
	private ImageView imageView_goback = null; // ����
	private boolean emailCursor = true;// �ж������������ʧȥ��껹�ǻ�ù��
	private boolean repasswordCursor = true;// �ж��ظ������������ʧȥ��껹�ǻ�ù��

	private String nickname;
	private String email;
	private String password;
	private String repassword;

	private CustomProgressDialog progressDialog = null;
	
	User user = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_layout);

		init();
		initListener();

		initView();
	}

	private void initView() {
		button_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (matchRegisterMsg()) {
					// ���У��ɹ�
					RegisterAsyncTask a=new RegisterAsyncTask();
					a.execute();
				}
			}
		});
	}

	protected boolean matchRegisterMsg() {
		nickname = editText_user_nickname.getText().toString().trim();
		email = editText_email.getText().toString().trim();
		password = editText_password.getText().toString().trim();
		repassword = editText_repassword.getText().toString().trim();
		if (nickname.equals("")) {
			Toast.makeText(RegisterActivity.this, "�û�������Ϊ��", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (email.equals("")) {
			Toast.makeText(RegisterActivity.this, "���䲻��Ϊ��", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (password.equals("")) {
			Toast.makeText(RegisterActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (repassword.equals("")) {
			Toast.makeText(RegisterActivity.this, "�ظ����벻��Ϊ��",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!checkPassword(password, repassword)) {
			Toast.makeText(RegisterActivity.this, "�������벻���", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	/**
	 * ҳ��Ԫ�س�ʼ��
	 */
	private void init() {
		this.editText_user_nickname = (EditText) findViewById(R.id.editText_register_user_nickname);
		this.editText_email = (EditText) findViewById(R.id.editText_register_email);
		this.editText_password = (EditText) findViewById(R.id.editText_register_password);
		this.editText_repassword = (EditText) findViewById(R.id.editText_register_repassword);
		this.button_register = (Button) findViewById(R.id.button_register);
		this.imageView_goback = (ImageView) findViewById(R.id.imageView_register_goback);
	}

	/**
	 * �����¼��ĳ�ʼ�� �����������ʧȥ�������������������Ĺ��ʧȥ������ע�ᰴť������������ذ�ť�������
	 */
	private void initListener() {
		this.editText_email.setOnFocusChangeListener(new CheckEmailListener());
		this.editText_repassword
				.setOnFocusChangeListener(new RePasswordListener());
		// this.button_register.setOnClickListener(new RegisterListener());
		this.imageView_goback.setOnClickListener(new ExitListener());
	}

	/**
	 * CheckUsernameListener ������������������ʧȥ����,��ʾ������ĸ�ʽ�Ƿ���ȷ
	 */
	private class CheckEmailListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			String myEmail = editText_email.getText().toString();
			if (emailCursor = !emailCursor) {
				if (!isEmail(myEmail)) {
					Toast.makeText(RegisterActivity.this, "�����ʽ����ȷ",
							Toast.LENGTH_SHORT).show();
					// editText_email.requestFocus();
				}
			}
		}
	}

	public boolean isEmail(String email) {
		// String str =
		// "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		// System.out.println(m.matches());
		return m.matches();
	}

	/**
	 * RePasswordListener �ظ���������ʧȥ���ļ�����
	 */
	private class RePasswordListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (repasswordCursor = !repasswordCursor) {
				if (!checkPassword(editText_password.getText().toString(),
						editText_repassword.getText().toString())) {
					// editText_password.setText("");
					editText_repassword.setText("");
					// editText_repassword.requestFocus();
					Toast.makeText(RegisterActivity.this, "�������벻һ��������������",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/**
	 * �Ƚ���������������Ƿ�һ�� editText_repassword������ɺ󣬹��ı��������editText_password���бȽϣ�
	 * �����һ�£�������ʾ���������������������
	 * 
	 * @param psw1
	 *            ����������������
	 * @param psw2
	 *            �ظ�����������������
	 * @return ��������һ�·���true�����򷵻�false
	 */
	private boolean checkPassword(String psw1, String psw2) {
		if (psw1.equals(psw2))
			return true;
		else
			return false;
	}

	/**
	 * ExitListener ���á����ذ�ť�ĵ�������������ص���½����
	 */
	private class ExitListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			RegisterActivity.this.finish();
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
   
	public class RegisterAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				boolean flag;
				flag=UserService.registerUser(editText_user_nickname.getText().toString(), editText_email.getText().toString(), editText_password.getText().toString());
				
				return String.valueOf(flag);
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				if(result.equals("true")){
					
					Toast.makeText(RegisterActivity.this, "ע��ɹ�����",
							Toast.LENGTH_SHORT).show();
					// ������Activity
					RegisterActivity.this.finish();
				}else if(result.equals("false")){
					Toast.makeText(RegisterActivity.this, "������û����ѱ�ע�ᣬ���޸�����!",
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
					progressDialog = CustomProgressDialog.createDialog(RegisterActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
}
