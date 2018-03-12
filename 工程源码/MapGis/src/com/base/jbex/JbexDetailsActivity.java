package com.base.jbex;

import com.basic.Activities.AddFriendActivity;
import com.basic.Activities.FriendsInfoActivity;
import com.basic.Activities.MainActivity;
import com.basic.Activities.MoreInfoActivity;
import com.basic.Activities.PersonInfoActivity;
import com.basic.Activities.R;
import com.basic.Activities.SearchFriendsActivity;
import com.basic.Activities.R.layout;
import com.basic.Activities.R.menu;
import com.basic.Activities.ZJBEXBaseActivity;
import com.basic.ImageLoad.AnimateFirstDisplayListener;
import com.basic.ImageLoad.ImageOptions;
import com.basic.ImageLoad.ImageStringUtil;
import com.basic.connectservice.AttentionService;
import com.basic.connectservice.CollectionService;
import com.basic.connectservice.JbexFriendService;
import com.basic.connectservice.UserService;
import com.basic.service.model.JbexInfo;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.message.net.TimeUtil;
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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class JbexDetailsActivity extends ZJBEXBaseActivity {
	private ActionBar actionBar;
	private Intent intent;
	
	private JbexInfo jbexuserifo;
	private User owneruser = new User(); // User
	private User frienduser=new User();
	
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	private CustomProgressDialog progressDialog = null;
	
	private ImageView userImage;
	private TextView jbexUsername;
	private ImageView jbexSex;
	private TextView jbexRestTime;
	private TextView jbexTime;
	private TextView jbexPlace;
	private TextView jbexType;
	private TextView jbexTitle;
	private TextView jbexDetails;
	private ImageView jbexinfo1;
	private ImageView jbexinfo2;
	private Button btn_OK;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jbex_details);
		
		intent = getIntent();

		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("user", owneruser.toString());
			jbexuserifo=(JbexInfo) data.getSerializable("jbexuserifo");
		}
		
		frienduser=jbexuserifo.getTUser();
		
		userImage=(ImageView) findViewById(R.id.userPhoto);
		jbexUsername=(TextView) findViewById(R.id.jbexUsername);
		jbexSex=(ImageView) findViewById(R.id.jbexSex);
		jbexRestTime=(TextView) findViewById(R.id.jbexRestTime);
		jbexTime=(TextView) findViewById(R.id.jbexTime);
		jbexPlace=(TextView) findViewById(R.id.jbexPlace);
		jbexType=(TextView) findViewById(R.id.jbexType);
		jbexTitle=(TextView) findViewById(R.id.jbexTitle);
		jbexDetails=(TextView) findViewById(R.id.jbexDetails);
		jbexinfo1=(ImageView) findViewById(R.id.jbexinfo1);
		jbexinfo2=(ImageView) findViewById(R.id.jbexinfo2);
		btn_OK=(Button) findViewById(R.id.btn_OK);
		
		initDisplayOption();
		initView();
		initListener();
		initActionBar();

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
		rightButton.setBackgroundResource(R.drawable.button_collection);
		TextView backtxt=(TextView) view.findViewById(R.id.backtxt);
		
		txt.setText("�����ϸ");
		
		backtxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				JbexDetailsActivity.this.finish();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				JbexDetailsActivity.this.finish();
			}
		});
		
		rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				AddCollectionAsyncTask collection=new AddCollectionAsyncTask();
				collection.execute();
			}
		});
	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		userImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				if(owneruser.getUser_id()!=frienduser.getUser_id()){
					SearchFriendsAsyncTask a=new SearchFriendsAsyncTask();
					a.execute();
				}
				else{
					Toast.makeText(JbexDetailsActivity.this, "��������û����Լ�", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_OK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				startJbexFriendAsyncTask a=new startJbexFriendAsyncTask();
				a.execute();
			}
		}); 
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		
		if(owneruser.getUser_id()==frienduser.getUser_id()){
			btn_OK.setVisibility(View.GONE);
		}
		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		mImageLoader.displayImage(ImageStringUtil.getImageURL(frienduser.getPicture()), userImage, options, animateFirstListener);
		if(frienduser.getSex()==1)
			jbexSex.setImageResource(R.drawable.man);
		else if(frienduser.getSex()==2)
			jbexSex.setImageResource(R.drawable.woman);
		jbexUsername.setText(frienduser.getUser_nickname());
		jbexTime.setText(sdf.format(jbexuserifo.getTime()));
		jbexPlace.setText("�й����ʴ�ѧ�人");
		jbexType.setText(jbexuserifo.getLabel());
		jbexTitle.setText(jbexuserifo.getTitle());
		jbexDetails.setText(jbexuserifo.getDetail());
		
		if(!jbexuserifo.getPicture1().equals("null")){
			mImageLoader.displayImage(ImageStringUtil.getImageURLByjbex(jbexuserifo.getPicture1()), jbexinfo1, options, animateFirstListener);
		}
		
		if(!jbexuserifo.getPicture2().equals("null")){
			mImageLoader.displayImage(ImageStringUtil.getImageURLByjbex(jbexuserifo.getPicture2()), jbexinfo2, options, animateFirstListener);
		}
		jbexRestTime.setText(TimeUtil.getRestTime(sdf.format(jbexuserifo.getTime())));
		
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptionsRange();
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jbex_details, menu);
		return true;
	}
	
   public void startJbex(View view){
	   
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
   
	public class startJbexFriendAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				
				boolean flag;
				flag=JbexFriendService.addJBexFriend(owneruser.getEmail(), frienduser.getEmail(), jbexuserifo.getId());
				
				return String.valueOf(flag);
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
				if(result.equals("true"))
				{
				con.sendJbexFriend(String.valueOf(owneruser.getUser_id()),String.valueOf(frienduser.getUser_id()), String.valueOf(jbexuserifo.getId()));
				Toast.makeText(JbexDetailsActivity.this, "��������ɹ�", Toast.LENGTH_SHORT).show();
				}else
					Toast.makeText(JbexDetailsActivity.this, "���Ѿ���������", Toast.LENGTH_SHORT).show();
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
					progressDialog = CustomProgressDialog.createDialog(JbexDetailsActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
	
	public class SearchFriendsAsyncTask extends AsyncTask<Integer, Integer, String>{
	       
		public SearchFriendsAsyncTask() {
		super();
	}
     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
	//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
		@Override
		protected String doInBackground(Integer... arg0) {
			// TODO �Զ����ɵķ������   
			if(UserService.Isfriend(owneruser.getEmail(), frienduser.getEmail())){
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
			

			 if(result.equals("yes")){
				Intent friendInfo=new Intent(JbexDetailsActivity.this, FriendsInfoActivity.class);
				Bundle data=new Bundle();
				data.putSerializable("owneruser", owneruser);
				data.putSerializable("frienduser", frienduser);
				data.putBoolean("flag", false);
				friendInfo.putExtras(data);
				JbexDetailsActivity.this.startActivity(friendInfo);
			}
			else if(result.equals("no")){
				Intent addfriend=new Intent(JbexDetailsActivity.this, AddFriendActivity.class);
				Bundle data=new Bundle();
				data.putSerializable("owneruser", owneruser);
				data.putSerializable("frienduser", frienduser);
				addfriend.putExtras(data);
				JbexDetailsActivity.this.startActivity(addfriend);
			}
		}}
	
	public class AddCollectionAsyncTask extends AsyncTask<Integer, Integer, String>{
	       
		public AddCollectionAsyncTask() {
		super();
	}
     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
	//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
		@Override
		protected String doInBackground(Integer... arg0) {
			// TODO �Զ����ɵķ������  
			boolean flag;
			flag=CollectionService.addCollections(String.valueOf(owneruser.getUser_id()),String.valueOf(jbexuserifo.getId()));
			return String.valueOf(flag);
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
		
			 if(result.equals("true")){
				Toast.makeText(JbexDetailsActivity.this, "��ϲ�㣡�ղسɹ�", Toast.LENGTH_SHORT).show();
			}
			else if(result.equals("false")){
				Toast.makeText(JbexDetailsActivity.this, "���Ѿ��ղع���", Toast.LENGTH_SHORT).show();
			}
		}}
}
