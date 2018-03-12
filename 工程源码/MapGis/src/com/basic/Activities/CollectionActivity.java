package com.basic.Activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.base.jbex.JbexDetailsActivity;
import com.basic.Activities.MyPublishJbexActivity.deleteMyPublishJbexAsyncTask;
import com.basic.Activities.MyPublishJbexActivity.setDataAsyncTask;
import com.basic.ImageLoad.ImageOptions;
import com.basic.adapter.CollectionsAdapter;
import com.basic.adapter.myPublishJbexAdapter;
import com.basic.connectservice.CollectionService;
import com.basic.connectservice.JbexInfoService;
import com.basic.connectservice.MyJbexRequestService;
import com.basic.model.CollectionsBean;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView;
import com.basic.pulltorefresh.RefreshTime;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.IXListViewListener;
import com.basic.pulltorefresh.PullToRefreshSwipeMenuListView.OnMenuItemClickListener;
import com.basic.service.model.JbexInfo;
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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CollectionActivity extends ZJBEXBaseActivity implements IXListViewListener{
	
	private List<JbexInfo> jbexinfoList=new ArrayList<JbexInfo>();
	private CollectionsAdapter collectionAdapter;
	
	private ActionBar actionBar;
	private Intent intent;
	
	private User owneruser=new User();
	private CustomProgressDialog progressDialog = null;
	
	private PullToRefreshSwipeMenuListView listmenu;
	private SwipeMenuCreator creator;
	
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		
		 intent=getIntent();
		 listmenu=(PullToRefreshSwipeMenuListView) super.findViewById(R.id.listCollection);
		 
		 intent=getIntent();
			if ((intent.getExtras()) != null) {
				Bundle data = intent.getExtras();
				owneruser = (User) data.getSerializable("owneruser");
				Log.d("user", owneruser.toString());
			}
			
		 initActionBar();
		 initDisplayOption();
		 
		 setDataAsyncTask load=new setDataAsyncTask();
		 load.execute();
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		collectionAdapter=new CollectionsAdapter(jbexinfoList, this, mImageLoader, options);
		listmenu.setAdapter(collectionAdapter);
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
					deleteMyCollectionJbexAsyncTask delete=new deleteMyCollectionJbexAsyncTask(position);
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

				JbexInfo jbexinfo=jbexinfoList.get(arg2-1);
				 Intent nextIntent=new Intent(CollectionActivity.this, JbexDetailsActivity.class);
				 Bundle data=new Bundle();
				 data.putSerializable("jbexuserifo", jbexinfo);
				 data.putSerializable("owneruser", owneruser);
				 nextIntent.putExtras(data);
				 CollectionActivity.this.startActivity(nextIntent);
				}
		});
	}
	
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
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
	 
	 @Override
	 public void onRefresh() {
		 // TODO �Զ����ɵķ������
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
		
		txt.setText("�ҵ��ղ�");
		
		backtxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				CollectionActivity.this.finish();
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				CollectionActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection, menu);
		return true;
	}

	
	public class setDataAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				
				jbexinfoList=CollectionService.GetCollectionsList(String.valueOf(owneruser.getUser_id()));
				
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
					progressDialog = CustomProgressDialog.createDialog(CollectionActivity.this);
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
	public void onLoadMore() {
		// TODO �Զ����ɵķ������
		
	}

	public class deleteMyCollectionJbexAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
		int position;
		
	     public deleteMyCollectionJbexAsyncTask(int position) {
			super();
			this.position = position;
		}
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				boolean flag;
				flag=CollectionService.deleteCollections(String.valueOf(owneruser.getUser_id()), String.valueOf(jbexinfoList.get(position).getId()));
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
					jbexinfoList.remove(position);
					collectionAdapter.onDateChange(jbexinfoList);
				}
				else 
					text="ɾ��ʧ��";
				
				Toast.makeText(CollectionActivity.this, text, Toast.LENGTH_SHORT).show();
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
					progressDialog = CustomProgressDialog.createDialog(CollectionActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
}
