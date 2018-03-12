package com.basic.Activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.ImageLoad.ImageOptions;
import com.basic.adapter.FriendRequestAdapter;
import com.basic.connectservice.AttentionService;
import com.basic.connectservice.FriendRequestService;
import com.basic.connectservice.FriendService;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView;
import com.basic.pulltorefresh.RefreshTime;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.IXListViewListener;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.OnMenuItemClickListener;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.OnSwipeListener;
import com.basic.service.model.FriendRequest;
import com.basic.service.model.User;
import com.basic.swipemenulistview.SwipeMenu;
import com.basic.swipemenulistview.SwipeMenuCreator;
import com.basic.swipemenulistview.SwipeMenuItem;
import com.basic.ui.CustomProgressDialog;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FriendRequestActivity extends ZJBEXBaseActivity implements IXListViewListener {
	private ActionBar actionBar;
	private Intent intent;
	
	private PullToRefreshSwipeMenuListView mListView;
	private User owneruser = new User(); // User
	private User frienduser=new User(); //frienduser
	
	private ArrayList<FriendRequest> FriendRequestList;
	private FriendRequestAdapter friendRequestAdapter;
	private SwipeMenuCreator creator;
	
	private CustomProgressDialog progressDialog = null;
	
	//ͼƬ���ؿ�����
		private DisplayImageOptions options;
		private ImageLoader mImageLoader;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_request);
		
		mListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.listView);
		intent = getIntent();

		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("owneruser", owneruser.toString());
		}
		
		initDisplayOption();
		initActionBar();
		
		LoadDataAsyncTask a=new LoadDataAsyncTask();
		a.execute();
		
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
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

		txt.setText("�µ�����");

		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				FriendRequestActivity.this.finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				FriendRequestActivity.this.finish();
			}
		});

	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				frienduser=FriendRequestList.get(position).getOwneruser();
				switch (index) {
				case 0:
					// open
					Intent acceptFriendRequest=new Intent(FriendRequestActivity.this, AcceptFriendRequestActivity.class);
					Bundle data = new Bundle();
					data.putSerializable("owneruser", owneruser);
					data.putSerializable("friendrequest", FriendRequestList.get(position));
					acceptFriendRequest.putExtras(data);
					FriendRequestActivity.this.startActivity(acceptFriendRequest);
					break;
				case 1:
					// delete
					// delete(item);
					deleteFriendReuqestAsyncTask delete=new deleteFriendReuqestAsyncTask(position);
					delete.execute();
					break;
				}
			}
		});

		// set SwipeListener
				mListView.setOnSwipeListener(new OnSwipeListener() {

					@Override
					public void onSwipeStart(int position) {
						// swipe start
					}

					@Override
					public void onSwipeEnd(int position) {
						// swipe end
					}
				});
				
	  mListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View currentView, int arg2,
				long arg3) {
			// TODO �Զ����ɵķ������
			 frienduser=FriendRequestList.get(arg2-1).getOwneruser();
			 Intent nextIntent=new Intent(FriendRequestActivity.this, AttetnionUserInfoActivity.class);
			 Bundle data=new Bundle();
			 data.putSerializable("frienduser", frienduser);
			 data.putSerializable("owneruser", owneruser);
			 nextIntent.putExtras(data);
			 FriendRequestActivity.this.startActivity(nextIntent);
		}
	});
	}

	private void initView() {
		friendRequestAdapter=new FriendRequestAdapter(FriendRequestList, this,mImageLoader,options);
		mListView.setAdapter(friendRequestAdapter);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		// step 1. create a MenuCreator
		creator=new SwipeMenuCreator() {
			
			@Override
			public void create(SwipeMenu menu) {
				// TODO �Զ����ɵķ������
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("��Ӻ���");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_request, menu);
		return true;
	}

	@Override
	public void onRefresh() {
		// TODO �Զ����ɵķ������
		LoadDataAsyncTask a=new LoadDataAsyncTask();
		a.execute();
	}

	@Override
	public void onLoadMore() {
		// TODO �Զ����ɵķ������
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
	private void onLoad() {
		SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
		RefreshTime.setRefreshTime(getApplicationContext(),
				df.format(new Date()));
		mListView.setRefreshTime(RefreshTime
				.getRefreshTime(getApplicationContext()));
		mListView.stopRefresh();

		mListView.stopLoadMore();

	}
	
	public class LoadDataAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				FriendRequestList=(ArrayList<FriendRequest>) FriendRequestService.getFriendRequestList(owneruser.getEmail());
				return "success";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
				initView();
				initListener();
				onLoad();
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
					progressDialog = CustomProgressDialog.createDialog(FriendRequestActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
	
	public class deleteFriendReuqestAsyncTask extends AsyncTask<Integer, Integer, String>{
	     private int position;
	     
	     public deleteFriendReuqestAsyncTask(int position) {
			super();
			this.position = position;
		}
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				boolean flag;
				flag=FriendRequestService.deleteFriendReuqest(owneruser.getEmail(), frienduser.getEmail());
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
					text="��ϲ�㣡ɾ���ɹ�";
					FriendRequestList.remove(position);
					friendRequestAdapter.onDateChange(FriendRequestList);
				}
				else 
					text="ɾ��ʧ��";
				
				Toast.makeText(FriendRequestActivity.this, text, Toast.LENGTH_SHORT).show();
				
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
					progressDialog = CustomProgressDialog.createDialog(FriendRequestActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
	
	public class addFriendGroupAsyncTask extends AsyncTask<Integer, Integer, String>{
		
	     private int position;
	     public addFriendGroupAsyncTask(int position) {
			super();
			this.position = position;
		}
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				boolean flag;
				flag=FriendService.addFriendGroup(owneruser.getEmail(), frienduser.getEmail(),"����");
				flag=FriendService.addFriendGroup(frienduser.getEmail(), owneruser.getEmail(),FriendRequestList.get(position).getRequestgroup());
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
					FriendRequestList.remove(position);
					friendRequestAdapter.onDateChange(FriendRequestList);
				}
				else 
					text="��Ӻ���ʧ��";
				
				Toast.makeText(FriendRequestActivity.this, text, Toast.LENGTH_SHORT).show();
				
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
					progressDialog = CustomProgressDialog.createDialog(FriendRequestActivity.this);
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
