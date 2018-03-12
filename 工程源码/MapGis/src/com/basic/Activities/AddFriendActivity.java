package com.basic.Activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.basic.Activities.MoreInfoActivity.setAttentionAsyncTask;
import com.basic.ImageLoad.AnimateFirstDisplayListener;
import com.basic.ImageLoad.ImageOptions;
import com.basic.ImageLoad.ImageStringUtil;
import com.basic.adapter.MenuItemAdapter;
import com.basic.connectservice.AttentionService;
import com.basic.model.menuItemBean;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.basic.ui.OptionsPopupWindow;
import com.basic.ui.SlipButton;
import com.basic.ui.OptionsPopupWindow.OnOptionsSelectListener;
import com.basic.ui.SlipButton.OnChangedListener;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddFriendActivity extends ZJBEXBaseActivity {
	
	private ActionBar actionBar;
	private Intent intent;
	private ListView listMenu;
	private MenuItemAdapter itemAdapter;
	private List<menuItemBean>  mList=new ArrayList<menuItemBean>();
	private ImageView image;
	private TextView friendsName;
	private ImageView sex;
	private Button addFriends;
	private SlipButton attentionSlipButton=null;
	
	
	private CustomProgressDialog progressDialog = null;
	
	private User frienduser;
	private User owneruser;
	private List<User> attentionUser=new ArrayList<User>();
	
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		
		intent=getIntent();
		if((intent.getExtras())!=null){
			Bundle data = intent.getExtras();
			owneruser=(User) data.getSerializable("owneruser");
			frienduser = (User) data.getSerializable("frienduser");
			Log.d("frienduser", frienduser.toString());
			Log.d("owneruser", owneruser.toString());
		}
		
		listMenu=(ListView) findViewById(R.id.listMenu);
		image=(ImageView) findViewById(R.id.image);
		friendsName=(TextView) findViewById(R.id.friendsName);
		sex=(ImageView) findViewById(R.id.sex);
		addFriends=(Button) findViewById(R.id.addFriend);
		attentionSlipButton=(SlipButton) super.findViewById(R.id.attentionSlipButton);
		
		LoadAttentionAsyncTask load=new LoadAttentionAsyncTask();
		load.execute();
		
		initDisplayOption();
		initActionBar();
		initView();
		initListener();
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		attentionSlipButton.SetOnChangedListener(new OnChangedListener() {

			@Override
			public void OnChanged(boolean CheckState) {
				// TODO �Զ����ɵķ������
				setAttentionAsyncTask set=new setAttentionAsyncTask(CheckState);
				set.execute();
			}
		});
	}
	
	private void initView() {
		// TODO �Զ����ɵķ������
		
		mImageLoader.displayImage(ImageStringUtil.getImageURL(frienduser.getPicture()), image, options, animateFirstListener);
	
		friendsName.setText(frienduser.getUser_nickname());
		
		if(frienduser.getSex()==0)
		sex.setImageResource(0);
		else if(frienduser.getSex()==1)
		sex.setImageResource(R.drawable.man);
		else if(frienduser.getSex()==2)
		sex.setImageResource(R.drawable.woman);
		
		menuItemBean item1=new menuItemBean("����", frienduser.getEmail(), 0, 0);
		menuItemBean item2=new menuItemBean("����ǩ��", frienduser.getUser_nickname(), 0, 0);
		menuItemBean item3=new menuItemBean("�ֻ�����", frienduser.getTelephone(), 0, 0);
		menuItemBean item4=new menuItemBean("ѧУ", frienduser.getSchool(), 0, 0);
		menuItemBean item5=new menuItemBean("ѧԺ", frienduser.getAcademy(), 0, 0);
		SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd");     
		menuItemBean item6=new menuItemBean("����", myFmt.format(frienduser.getBirthday()), 0, 0);
		
		mList.add(item1);
		mList.add(item2);mList.add(item3);mList.add(item4); mList.add(item5);
		mList.add(item6);
		
		itemAdapter=new MenuItemAdapter(this, mList);
		listMenu.setAdapter(itemAdapter);
		
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
		
		txt.setText("������Ϣ");
		
		backtxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				AddFriendActivity.this.finish();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				AddFriendActivity.this.finish();
			}
		});
	}
   
	public void addfriend(View source){
         Intent nextintent=new Intent(AddFriendActivity.this, ValidationMessageActivity.class);
            Bundle data=new Bundle();
			data.putSerializable("owneruser", owneruser);
			data.putSerializable("frienduser", frienduser);
			nextintent.putExtras(data);
			this.startActivity(nextintent);
	}

   
	public class LoadAttentionAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				
				attentionUser=AttentionService.GetAttentionList(owneruser.getEmail());
				Log.d("attentionUser", attentionUser.toString());
				Log.d("frienduser", frienduser.toString());
				boolean flag=false;
				for(User user :attentionUser){
					if(user.getEmail().endsWith(frienduser.getEmail())){
						flag=true;
					}
				}
				return String.valueOf(flag);
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
				if(result.equals("true")){
					attentionSlipButton.setCheck(true);
				}
				else {
					attentionSlipButton.setCheck(false);
				}
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
					progressDialog = CustomProgressDialog.createDialog(AddFriendActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
	
	public class setAttentionAsyncTask extends AsyncTask<Integer, Integer, String>{
	      private boolean CheckState;
	      
    	public setAttentionAsyncTask(boolean checkState) {
			super();
			CheckState = checkState;
		}
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������	
				boolean flag;
				if(CheckState){
					flag=AttentionService.addAttentionUser(owneruser.getEmail(), frienduser.getEmail());
					Log.d("attentionflag", String.valueOf(flag));
					return "true";
				}else{
					flag=AttentionService.deleteAttentionUser(owneruser.getEmail(), frienduser.getEmail());
					Log.d("attentionflag", String.valueOf(flag));
					return "false";
				}
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				if(result.equals("true"))
				Toast.makeText(AddFriendActivity.this, "�ر��ע���", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(AddFriendActivity.this, "ȡ���ر��ע���", Toast.LENGTH_SHORT).show();
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
					progressDialog = CustomProgressDialog.createDialog(AddFriendActivity.this);
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
