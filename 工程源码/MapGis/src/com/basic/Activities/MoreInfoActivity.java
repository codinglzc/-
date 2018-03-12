package com.basic.Activities;

import java.util.ArrayList;
import java.util.List;

import com.basic.Activities.PersonInfoActivity.SchooltemListener;
import com.basic.Activities.PersonInfoActivity.SetFriendsAsyncTask;
import com.basic.adapter.MenuItemAdapter;
import com.basic.connectservice.AttentionService;
import com.basic.connectservice.FriendService;
import com.basic.connectservice.SettingUserInfo;
import com.basic.model.menuItemBean;
import com.basic.service.model.User;
import com.basic.ui.ActionSheet;
import com.basic.ui.CustomProgressDialog;
import com.basic.ui.OptionsPopupWindow;
import com.basic.ui.OptionsPopupWindow.OnOptionsSelectListener;
import com.basic.ui.SlipButton;
import com.basic.ui.SlipButton.OnChangedListener;
import com.message.net.ChatMessage;
import com.message.net.Config;

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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MoreInfoActivity extends ZJBEXBaseActivity {
    
	private ActionBar actionBar;
	private Intent intent;
	private ListView listMenu;
	private MenuItemAdapter itemAdapter;
	private List<menuItemBean> mList = new ArrayList<menuItemBean>();
	private ActionSheet actionSheet;
	private SlipButton attentionSlipButton=null;
	private OptionsPopupWindow pwOption;
	private ArrayList<String> Option = new ArrayList<String>();  //Option������Դ
	
	private CustomProgressDialog progressDialog = null;
	
	private User owneruser = new User(); // User
	private User frienduser=new User();
	private String GroupName="";
	private List<User> attentionUser=new ArrayList<User>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_info);
		
		intent = getIntent();

		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			frienduser=(User) data.getSerializable("frienduser");
		    GroupName=data.getString("GroupName");
			Log.d("user", owneruser.toString());
		}
		
		attentionSlipButton=(SlipButton) super.findViewById(R.id.attentionSlipButton);
		listMenu = (ListView) super.findViewById(R.id.listmenu);
		pwOption = new OptionsPopupWindow(this);
		LoadAttentionAsyncTask load=new LoadAttentionAsyncTask();
		load.execute();
		
		initListener();
		initActionBar();
	}

	private void initActionBar() {
		// TODO �Զ����ɵķ������

		actionBar = getActionBar();
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

		txt.setText("����");

		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				MoreInfoActivity.this.finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				MoreInfoActivity.this.finish();
			}
		});

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
		});;
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		menuItemBean item1 = new menuItemBean("��ע", frienduser.getUser_nickname(), 0, 0);
		menuItemBean item2 = new menuItemBean("����", GroupName, 0 ,R.drawable.btn_right);
		
		mList.add(item1);
		mList.add(item2);
		
		itemAdapter = new MenuItemAdapter(this, mList);
		listMenu.setAdapter(itemAdapter);
		listMenu.setOnItemClickListener(new ItemListener());
	}
   
	public void deleteFriend(View source){
		deleteFriendAsyncTask delete=new deleteFriendAsyncTask();
		delete.execute();
	}
	
	class ItemListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View currentview, int arg2,
				long arg3) {
			// TODO �Զ����ɵķ������
			switch (arg2) {
			case 0://����˱�ע
				//Toast.makeText(MoreInfoActivity.this, "0", Toast.LENGTH_SHORT).show();
				break;
			case 1://����˷���
				Option=new ArrayList<String>();
				if(GroupName.endsWith("���ĺ���")){
					Option.add("ͬѧ");
					Option.add("����");
				}else if(GroupName.endsWith("ͬѧ"))
				{
					Option.add("���ĺ���");
					Option.add("����");
				}else if(GroupName.endsWith("����")){
					Option.add("���ĺ���");
					Option.add("ͬѧ");
				}
				
				pwOption.setPicker(Option);
				pwOption.setLabels("ѡ�����");
				pwOption.setSelectOptions(0, 0, 0);
				
				pwOption.setOnoptionsSelectListener(new OnOptionsSelectListener() {
					
					@Override
					public void onOptionsSelect(int options1, int option2, int options3) {
						// TODO �Զ����ɵķ������
						setFriendsGroupAsyncTask a=new setFriendsGroupAsyncTask(Option.get(options1));
						a.execute();
						GroupName=Option.get(options1);
						Intent intent2=new Intent();
						intent2.putExtra("GroupName", GroupName);
						MoreInfoActivity.this.setResult(444,intent2);
					}
				});
				pwOption.showAtLocation(currentview, Gravity.BOTTOM, 0, 0);
				break;
			case 2://������ر����
				Toast.makeText(MoreInfoActivity.this, "2", Toast.LENGTH_SHORT).show();
				break;
			}
		}	
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
				initView();
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
					progressDialog = CustomProgressDialog.createDialog(MoreInfoActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
	
	public class setFriendsGroupAsyncTask extends AsyncTask<Integer, Integer, String>{
	      private String GroupName;
	      
	     public setFriendsGroupAsyncTask(String groupName) {
			super();
			GroupName = groupName;
		}
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������	
				FriendService.setFriendsGroup(owneruser.getEmail(),frienduser.getEmail(), GroupName);
				return "success";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
			      menuItemBean item = new menuItemBean("����", GroupName, 0 ,R.drawable.btn_right);
				  mList.set(1, item);
				  itemAdapter.onDateChange(mList);
				  
				  Toast.makeText(MoreInfoActivity.this, "�޸ķ������", Toast.LENGTH_SHORT).show();
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
					progressDialog = CustomProgressDialog.createDialog(MoreInfoActivity.this);
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
					Toast.makeText(MoreInfoActivity.this, "�ر��ע���", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(MoreInfoActivity.this, "ȡ���ر��ע���", Toast.LENGTH_SHORT).show();
				
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
					progressDialog = CustomProgressDialog.createDialog(MoreInfoActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}

	public class deleteFriendAsyncTask extends AsyncTask<Integer, Integer, String>{

		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������	
				return String.valueOf(FriendService.deleteFriend(owneruser.getEmail(), frienduser.getEmail()));
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
				if(result.equals("true"))
					Toast.makeText(MoreInfoActivity.this, "ɾ�����ѳɹ�", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(MoreInfoActivity.this, "ɾ������ʧ��", Toast.LENGTH_SHORT).show();
				
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
					progressDialog = CustomProgressDialog.createDialog(MoreInfoActivity.this);
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
