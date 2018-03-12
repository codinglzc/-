package com.basic.Activities;

import java.util.ArrayList;
import java.util.Date;

import com.basic.ImageLoad.AnimateFirstDisplayListener;
import com.basic.ImageLoad.ImageOptions;
import com.basic.ImageLoad.ImageStringUtil;
import com.basic.connectservice.FriendRequestService;
import com.basic.connectservice.SettingUserInfo;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.basic.ui.OptionsPopupWindow;
import com.basic.ui.OptionsPopupWindow.OnOptionsSelectListener;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ValidationMessageActivity extends ZJBEXBaseActivity {
    
	private ActionBar actionBar;
	private Intent intent;
	private ImageView image;
	private TextView friendsName;
	private ImageView sex;
	private TextView GroupName;
	private EditText signature_et;
	
    private CustomProgressDialog progressDialog = null;
    
    private OptionsPopupWindow pwOption;
	private ArrayList<String> group = new ArrayList<String>();// Group������Դ
	
	private User frienduser;
	private User owneruser;
	
	private String requestGroup;
	private String validationmessage="";
	
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validation_message);
		
		intent=getIntent();
		if((intent.getExtras())!=null){
			Bundle data = intent.getExtras();
			owneruser=(User) data.getSerializable("owneruser");
			frienduser = (User) data.getSerializable("frienduser");
			Log.d("frienduser", frienduser.toString());
			Log.d("owneruser", owneruser.toString());
		}
		
		pwOption = new OptionsPopupWindow(this);
		image=(ImageView) findViewById(R.id.image);
		friendsName=(TextView) findViewById(R.id.friendsName);
		sex=(ImageView) findViewById(R.id.sex);
		GroupName=(TextView) findViewById(R.id.GroupName);
		signature_et=(EditText) findViewById(R.id.signature_et);
		
		initDisplayOption();
		initActionBar();
		initView();
		initListener();
		initOptions();
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
	}

	private void initOptions() {
		// TODO �Զ����ɵķ������
		group.add("����");
		group.add("ͬѧ");
	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		
		requestGroup="����";
		GroupName.setText(requestGroup);
		mImageLoader.displayImage(ImageStringUtil.getImageURL(frienduser.getPicture()), image, options, animateFirstListener);
		friendsName.setText(frienduser.getUser_nickname());
		
		if(frienduser.getSex()==0)
		sex.setImageResource(0);
		else if(frienduser.getSex()==1)
		sex.setImageResource(R.drawable.man);
		else if(frienduser.getSex()==2)
		sex.setImageResource(R.drawable.woman);
	}

	public void addfriend(View source){
		
		pwOption.setPicker(group);
		pwOption.setLabels("�����Ⱥ��");
		// ����Ĭ��ѡ�е�������Ŀ
		pwOption.setSelectOptions(0, 0, 0);
		pwOption.setOnoptionsSelectListener(new OnOptionsSelectListener() {
			
			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				// TODO �Զ����ɵķ������
				GroupName.setText(String.valueOf(group.get(options1)));
				requestGroup=group.get(options1);
			}
		});
		pwOption.showAtLocation(source, Gravity.BOTTOM, 0, 0);
	}
	
	public void sendRequest(View source){
		validationmessage=signature_et.getText().toString();
		addFriendsRequestAsyncTask a=new addFriendsRequestAsyncTask();
		a.execute();
	}
	
	private void initActionBar() {
		// TODO �Զ����ɵķ������
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
		
		txt.setText("��֤��Ϣ");
		
		backtxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				ValidationMessageActivity.this.finish();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				ValidationMessageActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.validation_message, menu);
		return true;
	}
	
	public class addFriendsRequestAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				boolean flag;
				flag=FriendRequestService.addFriendRequest(owneruser.getEmail(), frienduser.getEmail(), requestGroup, new Date(), validationmessage);
				if(flag)
					return "true";
				else return "false";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				if(result.endsWith("true"))
					Toast.makeText(ValidationMessageActivity.this, "����ɹ���", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(ValidationMessageActivity.this, "���Ѿ�������ˣ��벻Ҫ�ظ�����", Toast.LENGTH_SHORT).show();
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
					progressDialog = CustomProgressDialog.createDialog(ValidationMessageActivity.this);
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
