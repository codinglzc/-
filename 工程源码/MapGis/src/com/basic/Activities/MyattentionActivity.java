package com.basic.Activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.basic.ImageLoad.ImageOptions;
import com.basic.adapter.myAttentionAdapter;
import com.basic.connectservice.AttentionService;
import com.basic.connectservice.FriendRequestService;
import com.basic.model.myAttentionBean;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView;
import com.basic.pulltorefresh.RefreshTime;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.IXListViewListener;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.OnMenuItemClickListener;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.OnSwipeListener;
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
import android.annotation.SuppressLint;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class MyattentionActivity extends ZJBEXBaseActivity implements IXListViewListener{
	
	private List<myAttentionBean> mListmyAttentionBean = new ArrayList<myAttentionBean>();
	private myAttentionAdapter myAttentionAdapter;
	private ActionBar actionBar;
	private Intent intent;
	
	private PullToRefreshSwipeMenuListView listmenu;
	private SwipeMenuCreator creator;
	
	private User owneruser=new User();
	private User frienduser=new User();
	private List<User> attentionUser=new ArrayList<User>();
	private CustomProgressDialog progressDialog = null;
	
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myattention);
		
		listmenu=(PullToRefreshSwipeMenuListView) super.findViewById(R.id.listmyAttention);
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
		View view=actionBar.getCustomView();
		
		ImageButton rightButton=(ImageButton) view.findViewById(R.id.btn_nnavigation);
		ImageButton back=(ImageButton) view.findViewById(R.id.btn_back);
		TextView txt=(TextView) view.findViewById(R.id.main_Text);
		rightButton.setBackgroundResource(R.drawable.loginname);
		TextView backtxt=(TextView) view.findViewById(R.id.backtxt);
		
		txt.setText("�ҵĹ�ע");
		
		backtxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				MyattentionActivity.this.finish();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				MyattentionActivity.this.finish();
			}
		});
	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		// step 2. listener item click event
		listmenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				frienduser=attentionUser.get(position);
				switch (index) {
				case 0:
					// open
					 Intent nextIntent=new Intent(MyattentionActivity.this, AttetnionUserInfoActivity.class);
					 Bundle data=new Bundle();
					 data.putSerializable("frienduser", frienduser);
					 data.putSerializable("owneruser", owneruser);
					 nextIntent.putExtras(data);
					 MyattentionActivity.this.startActivity(nextIntent);
					break;
				case 1:
					// delete
					// delete(item)
					deleteMyAttentionAsyncTask delete=new deleteMyAttentionAsyncTask(position);
					delete.execute();
					break;
				}
			}
		});
       
		// set SwipeListener
		listmenu.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});
		
		listmenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO �Զ����ɵķ������
				 frienduser=attentionUser.get(arg2-1);
				 Intent nextIntent=new Intent(MyattentionActivity.this, AttetnionUserInfoActivity.class);
				 Bundle data=new Bundle();
				 data.putSerializable("frienduser", frienduser);
				 data.putSerializable("owneruser", owneruser);
				 nextIntent.putExtras(data);
				 MyattentionActivity.this.startActivity(nextIntent);
			}
		});
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		
		myAttentionAdapter=new myAttentionAdapter(mListmyAttentionBean, this,mImageLoader,options);
		listmenu.setAdapter(myAttentionAdapter);
		listmenu.setPullRefreshEnable(true);
		listmenu.setPullLoadEnable(true);
		listmenu.setXListViewListener(this);
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
				openItem.setTitle("�鿴����");
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
		listmenu.setMenuCreator(creator);
	}

	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
	private void setData() {
		// TODO �Զ����ɵķ�����
		Log.d("attentionUser", attentionUser.toString());
		
		mListmyAttentionBean=new  ArrayList<myAttentionBean>();
		myAttentionBean myAttention[]=new myAttentionBean[attentionUser.size()];
		
		for(int i=0;i<attentionUser.size();i++){
			
		myAttention[i]=new myAttentionBean(attentionUser.get(i).getPicture(), attentionUser.get(i).getUser_nickname(), attentionUser.get(i).getSchool(), attentionUser.get(i).getAcademy(), attentionUser.get(i).getSex());
		mListmyAttentionBean.add(myAttention[i]);
		
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.myattention, menu);
		return true;
	}
	
	
	public class setDataAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				
				attentionUser=AttentionService.GetAttentionList(owneruser.getEmail());
				setData();
				
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
					progressDialog = CustomProgressDialog.createDialog(MyattentionActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}

	private void onLoad() {
		SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
		RefreshTime.setRefreshTime(getApplicationContext(),
				df.format(new Date()));
		listmenu.setRefreshTime(RefreshTime
				.getRefreshTime(getApplicationContext()));
		listmenu.stopRefresh();

		listmenu.stopLoadMore();

	}
	
	public class deleteMyAttentionAsyncTask extends AsyncTask<Integer, Integer, String>{
	     private int position;
	     
	     public deleteMyAttentionAsyncTask(int position) {
			super();
			this.position = position;
		}
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				boolean flag;
				flag=AttentionService.deleteAttentionUser(owneruser.getEmail(), frienduser.getEmail());
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
					attentionUser.remove(position);
					setData();
					myAttentionAdapter.onDateChange((ArrayList<myAttentionBean>) mListmyAttentionBean);
				}
				else 
					text="ɾ��ʧ��";
				
				Toast.makeText(MyattentionActivity.this, text, Toast.LENGTH_SHORT).show();
				
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
					progressDialog = CustomProgressDialog.createDialog(MyattentionActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
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
