package com.basic.Activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.basic.Activities.AcceptFriendRequestActivity.addFriendAsyncTask;
import com.basic.Activities.FriendRequestActivity.LoadDataAsyncTask;
import com.basic.Activities.FriendRequestActivity.deleteFriendReuqestAsyncTask;
import com.basic.ImageLoad.ImageOptions;
import com.basic.adapter.FriendRequestAdapter;
import com.basic.adapter.JbexRequestAdapter;
import com.basic.connectservice.FriendRequestService;
import com.basic.connectservice.FriendService;
import com.basic.connectservice.JbexFriendService;
import com.basic.connectservice.UserService;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView;
import com.basic.pulltorefresh.RefreshTime;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.IXListViewListener;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.OnMenuItemClickListener;
import com.basic.service.model.MyJbexRequest;
import com.basic.service.model.User;
import com.basic.swipemenulistview.SwipeMenu;
import com.basic.swipemenulistview.SwipeMenuCreator;
import com.basic.swipemenulistview.SwipeMenuItem;
import com.basic.ui.CustomProgressDialog;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyPublishJbexRequestActivity extends ZJBEXBaseActivity implements IXListViewListener{
	private ActionBar actionBar;
	private Intent intent;
	
	private PullToRefreshSwipeMenuListView mListView;
	
	private MyJbexRequest jbexReqeust;
	private List <User> jbexReqeustList=new ArrayList<User>();
	private User owneruser;
	private User frienduser;
	private JbexRequestAdapter jbexrequestadapter;
	
	private CustomProgressDialog progressDialog = null;
	
	private SwipeMenuCreator creator;
	
	//ͼƬ���ؿ�����
    private DisplayImageOptions options;
    private ImageLoader mImageLoader;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_publish_jbex_request);
		
		mListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.listView);
		intent = getIntent();
		
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			jbexReqeust = (MyJbexRequest) data.getSerializable("jbexReqeust");
			owneruser=(User) data.getSerializable("owneruser");
			jbexReqeustList=jbexReqeust.getJbexuserList();
		}
		
		initDisplayOption();
		initActionBar();
		
		initView();
		initListener();
		MyPublishJbexRequestActivity.this.setResult(111);
	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		// step 2. listener item click event
				mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public void onMenuItemClick(int position, SwipeMenu menu, int index) {
						frienduser=jbexReqeustList.get(position);
						switch (index) {
						case 0:
							addFriendGroupAsyncTask a=new addFriendGroupAsyncTask(position);
							a.execute();
							break;
						case 1:
							// delete
							// delete(item);
							deleteJbexFriendReuqestAsyncTask delete=new deleteJbexFriendReuqestAsyncTask(position);
							delete.execute();
							break;
						}
					}
				});
				
				 mListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View currentView, int arg2,
								long arg3) {
							// TODO �Զ����ɵķ������
							 /*frienduser=FriendRequestList.get(arg2-1).getOwneruser();
							 Intent nextIntent=new Intent(FriendRequestActivity.this, AttetnionUserInfoActivity.class);
							 Bundle data=new Bundle();
							 data.putSerializable("frienduser", frienduser);
							 data.putSerializable("owneruser", owneruser);
							 nextIntent.putExtras(data);
							 FriendRequestActivity.this.startActivity(nextIntent);*/
						}
					});
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		jbexrequestadapter=new JbexRequestAdapter(jbexReqeustList, this,mImageLoader,options);
		mListView.setAdapter(jbexrequestadapter);
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
				openItem.setTitle("��������");
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
				MyPublishJbexRequestActivity.this.finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				MyPublishJbexRequestActivity.this.finish();
			}
		});
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_publish_jbex_request, menu);
		return true;
	}

	@Override
	public void onRefresh() {
		// TODO �Զ����ɵķ������
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO �Զ����ɵķ������
		
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
	

	public class deleteJbexFriendReuqestAsyncTask extends AsyncTask<Integer, Integer, String>{
	     private int position;
	     
	     public deleteJbexFriendReuqestAsyncTask(int position) {
			super();
			this.position = position;
		}
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				boolean flag;
				flag=JbexFriendService.deleteJBexFriend(owneruser.getEmail(), frienduser.getEmail(),jbexReqeust.getJbexInfo().getId());
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
					jbexReqeustList.remove(position);
					jbexrequestadapter.onDateChange(jbexReqeustList);
				}
				else 
					text="ɾ��ʧ��";
				
				Toast.makeText(MyPublishJbexRequestActivity.this, text, Toast.LENGTH_SHORT).show();
				
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
					progressDialog = CustomProgressDialog.createDialog(MyPublishJbexRequestActivity.this);
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
				if(!UserService.Isfriend(owneruser.getEmail(), frienduser.getEmail())){
				flag=FriendService.addFriendGroup(owneruser.getEmail(), frienduser.getEmail(),"����");
				flag=FriendService.addFriendGroup(frienduser.getEmail(), owneruser.getEmail(),"����");
				}
				else{
					flag=false;
				}
				JbexFriendService.deleteJBexFriend(owneruser.getEmail(), frienduser.getEmail(),jbexReqeust.getJbexInfo().getId());
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
					text="�����Ѿ���������ֱ�Ӻ��������";
				
				jbexReqeustList.remove(position);
				jbexrequestadapter.onDateChange(jbexReqeustList);
				
				Toast.makeText(MyPublishJbexRequestActivity.this, text, Toast.LENGTH_SHORT).show();
				
				if (progressDialog != null){
					progressDialog.dismiss();
					progressDialog = null;
				}	
				
				Intent intent=new Intent(MyPublishJbexRequestActivity.this, ChatActivity.class);
				Bundle data=new Bundle();
				data.putInt("frienduser", frienduser.getUser_id() );
				data.putInt("owneruser", owneruser.getUser_id());
				intent.putExtras(data);
				
				MyPublishJbexRequestActivity.this.startActivity(intent);
			}
			
	      //�����������û�����Excuteʱ�Ľӿڣ�������ִ��֮ǰ��ʼ���ô˷�����������������ʾ���ȶԻ���
			@Override
			protected void onPreExecute() {
				// TODO �Զ����ɵķ������
				super.onPreExecute();
				
				if (progressDialog == null){
					progressDialog = CustomProgressDialog.createDialog(MyPublishJbexRequestActivity.this);
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
