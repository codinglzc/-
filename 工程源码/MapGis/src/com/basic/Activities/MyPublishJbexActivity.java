package com.basic.Activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.basic.Activities.MyattentionActivity.deleteMyAttentionAsyncTask;
import com.basic.Activities.MyattentionActivity.setDataAsyncTask;
import com.basic.ImageLoad.ImageOptions;
import com.basic.ImageLoad.ImageStringUtil;
import com.basic.adapter.myPublishJbexAdapter;
import com.basic.connectservice.JbexInfoService;
import com.basic.connectservice.MyJbexRequestService;
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

public class MyPublishJbexActivity extends ZJBEXBaseActivity implements IXListViewListener{
	
	private ActionBar actionBar;
	private Intent intent;
	
	private PullToRefreshSwipeMenuListView listmenu;
	private SwipeMenuCreator creator;
	
	private User owneruser=new User();
	private CustomProgressDialog progressDialog = null;
	
	private List <MyJbexRequest> MyJbexRequestList;
	private myPublishJbexAdapter mypublishjbexadapter;
	
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_publish_jbex);
		
		listmenu=(PullToRefreshSwipeMenuListView) findViewById(R.id.listmypublishjbex);
		
		intent=getIntent();
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("user", owneruser.toString());
		}
		
		initDisplayOption();
		initActionBar();
		setDataAsyncTask load=new setDataAsyncTask();
		load.execute();
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
		rightButton.setBackgroundResource(R.drawable.loginname);
		TextView backtxt=(TextView) view.findViewById(R.id.backtxt);
		
		txt.setText("�ҵĽ�����");
		
		backtxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				MyPublishJbexActivity.this.finish();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				MyPublishJbexActivity.this.finish();
			}
		});
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_publish_jbex, menu);
		return true;
	}
   
	 private void onLoad() {
			// TODO �Զ����ɵķ������
		 SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
		 RefreshTime.setRefreshTime(getApplicationContext(),
				 df.format(new Date()));
		 listmenu.setRefreshTime(RefreshTime
				 .getRefreshTime(getApplicationContext()));
		 listmenu.stopRefresh();

		 listmenu.stopLoadMore();
		}
	 
	private void initView() {
			// TODO �Զ����ɵķ������
		mypublishjbexadapter=new myPublishJbexAdapter(MyJbexRequestList, this, mImageLoader, options);
		listmenu.setAdapter(mypublishjbexadapter);
		listmenu.setPullRefreshEnable(true);
		listmenu.setPullLoadEnable(true);
		listmenu.setXListViewListener(this);
		// step 1. create a MenuCreator
		creator=new SwipeMenuCreator() {
			
			@Override
			public void create(SwipeMenu menu) {
				
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
		listmenu.setMenuCreator(creator);
		}
	private void initListener() {
			// TODO �Զ����ɵķ������
		listmenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				//frienduser=attentionUser.get(position);
				switch (index) {
				case 0:
					deleteMyPublishJbexAsyncTask delete=new deleteMyPublishJbexAsyncTask(position);
					delete.execute();
					break;
				}
			}
		});
		
		listmenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO �Զ����ɵķ������

				MyJbexRequest jbexRequest=MyJbexRequestList.get(arg2-1);
				if(jbexRequest.getJbexuserList().size()!=0){
				 Intent nextIntent=new Intent(MyPublishJbexActivity.this, MyPublishJbexRequestActivity.class);
				 Bundle data=new Bundle();
				 data.putSerializable("jbexReqeust", jbexRequest);
				 data.putSerializable("owneruser", owneruser);
				 nextIntent.putExtras(data);
				 MyPublishJbexActivity.this.startActivityForResult(nextIntent, 1);
				}
			}
		});
		}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
	public class setDataAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				
				MyJbexRequestList=MyJbexRequestService.getMyJbexRequestList(owneruser.getEmail());
				//setData();
				
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
					progressDialog = CustomProgressDialog.createDialog(MyPublishJbexActivity.this);
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

	@Override
	public void onRefresh() {
		// TODO �Զ����ɵķ������
		setDataAsyncTask load=new setDataAsyncTask();
		load.execute();
	}

	@Override
	public void onLoadMore() {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO �Զ����ɵķ������
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 111:
			onRefresh();
			break;
		default:
			break;
			}
	}
	
	public class deleteMyPublishJbexAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
		int position;
		
	     public deleteMyPublishJbexAsyncTask(int position) {
			super();
			this.position = position;
		}
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				String flag;
				flag=JbexInfoService.deleteJbexinfo(String.valueOf(MyJbexRequestList.get(position).getJbexInfo().getId()));
				return flag;
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
					MyJbexRequestList.remove(position);
					mypublishjbexadapter.onDateChange(MyJbexRequestList);
				}
				else 
					text="ɾ��ʧ��";
				
				Toast.makeText(MyPublishJbexActivity.this, text, Toast.LENGTH_SHORT).show();
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
					progressDialog = CustomProgressDialog.createDialog(MyPublishJbexActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
}
