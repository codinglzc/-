package com.basic.Activities;

import java.util.ArrayList;
import java.util.List;

import com.basic.adapter.MenuItemAdapter;
import com.basic.connectservice.AttentionService;
import com.basic.connectservice.FriendRequestService;
import com.basic.connectservice.FriendService;
import com.basic.model.menuItemBean;
import com.basic.service.model.FriendRequest;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.basic.ui.OptionsPopupWindow;
import com.basic.ui.OptionsPopupWindow.OnOptionsSelectListener;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AcceptFriendRequestActivity extends ZJBEXBaseActivity implements OnItemClickListener{
	
	private ActionBar actionBar;
	private Intent intent;
	
	private ListView listMenu;
	private MenuItemAdapter itemAdapter;
	private List<menuItemBean> mList = new ArrayList<menuItemBean>();
	private OptionsPopupWindow pwOption;
	private ArrayList<String> Option = new ArrayList<String>();
	
	private User owneruser = new User(); // User
	private FriendRequest friendrequest=new FriendRequest();
	private String GroupName;
	
	private CustomProgressDialog progressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accept_friend_request);
		
		intent = getIntent();
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			friendrequest=(FriendRequest) data.getSerializable("friendrequest");
			Log.d("user", owneruser.toString());
			Log.d("friendrequest", friendrequest.toString());
		}
		
		pwOption = new OptionsPopupWindow(this);
		listMenu = (ListView) super.findViewById(R.id.listmenu);
		
		initActionBar();
		initView();
		initListener();
	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		menuItemBean item1 = new menuItemBean("��ע", friendrequest.getOwneruser().getUser_nickname(), 0, 0);
		menuItemBean item2 = new menuItemBean("����", "����", 0 ,R.drawable.btn_right);
		
		GroupName="����";
		
		mList.add(item1);
		mList.add(item2);
		
		itemAdapter = new MenuItemAdapter(this, mList);
		listMenu.setAdapter(itemAdapter);
		listMenu.setOnItemClickListener(this);
		
		Option=new ArrayList<String>();
		Option.add("����");
		Option.add("ͬѧ");
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

		txt.setText("��Ӻ���");

		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				AcceptFriendRequestActivity.this.finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				AcceptFriendRequestActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.accept_friend_request, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View currentview, int arg2, long arg3) {
		// TODO �Զ����ɵķ������
		switch (arg2) {
		case 0://����˱�ע
			//Toast.makeText(MoreInfoActivity.this, "0", Toast.LENGTH_SHORT).show();
			break;
		case 1://����˷���
			pwOption.setPicker(Option);
			pwOption.setLabels("ѡ�����");
			pwOption.setSelectOptions(0, 0, 0);
			
			pwOption.setOnoptionsSelectListener(new OnOptionsSelectListener() {
				
				@Override
				public void onOptionsSelect(int options1, int option2, int options3) {
					// TODO �Զ����ɵķ������
					GroupName=Option.get(options1);
					 menuItemBean item = new menuItemBean("����", GroupName, 0 ,R.drawable.btn_right);
					  mList.set(1, item);
					  itemAdapter.onDateChange(mList);
				}
			});
			
			pwOption.showAtLocation(currentview, Gravity.BOTTOM, 0, 0);
			break;
		}
	}

	public void addFriend(View source){
		addFriendAsyncTask a=new addFriendAsyncTask();
		a.execute();
	}
	
	public class addFriendAsyncTask extends AsyncTask<Integer, Integer, String>{
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������	
				boolean flag;
				flag=FriendService.addFriendGroup(owneruser.getEmail(), friendrequest.getOwneruser().getEmail(),GroupName);
				flag=FriendService.addFriendGroup(friendrequest.getOwneruser().getEmail(), owneruser.getEmail(),friendrequest.getRequestgroup());
				FriendRequestService.deleteFriendReuqest(owneruser.getEmail(), friendrequest.getOwneruser().getEmail());
				return String.valueOf(flag);
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				String text="";
				if(result.endsWith("true")){
					text="��ϲ�㣡��Ӻ��ѳɹ�";
				}
				else 
					text="��Ӻ���ʧ��";
				
				Toast.makeText(AcceptFriendRequestActivity.this, text, Toast.LENGTH_SHORT).show();
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
					progressDialog = CustomProgressDialog.createDialog(AcceptFriendRequestActivity.this);
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
