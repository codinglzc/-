package com.basic.Activities;

import java.util.ArrayList;
import java.util.List;

import com.basic.adapter.ChooseSchoolAdapter;
import com.basic.adapter.MenuItemAdapter;
import com.basic.model.menuItemBean;
import com.message.net.ChatMessage;
import com.message.net.Config;

import android.os.Bundle;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class ChooseSchoolActivity extends ZJBEXBaseActivity {
    
	private ActionBar actionBar;
	private Intent intent;
	private ListView listMenu;
	private ChooseSchoolAdapter itemAdapter;
	private List<menuItemBean>  mList=new ArrayList<menuItemBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_school);
		
        intent=getIntent();
		
		listMenu=(ListView) super.findViewById(R.id.listMenu);
		
		initActionBar();
		initView();
		initListener();
		
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
		
		txt.setText("ѡ��ѧԺ");
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				ChooseSchoolActivity.this.finish();
			}
		});
	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		
		//����Listmenu�ļ������ϴ����ݸ���һ��Activity
		listMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO �Զ����ɵķ������
				String school=mList.get(arg2).getLeftmenu_item_txt();
				//������ʹ��Intent����
				Intent intent2=new Intent();
				//�ѷ������ݴ���Intent
				intent2.putExtra("school", school);
				
				//���÷�������
				ChooseSchoolActivity.this.setResult(333,intent2);
				ChooseSchoolActivity.this.finish();
				}
			
		});
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		menuItemBean item1=new menuItemBean("�������ѧѧԺ", "  ", 0,0);
		menuItemBean item2=new menuItemBean("��ԴѧԺ", "  ", 0,0);
		menuItemBean item3=new menuItemBean("�Ļ��뻯ѧѧԺ", "  ", 0,0);
		menuItemBean item4=new menuItemBean("����ѧԺ", "  ", 0,0);
		menuItemBean item5=new menuItemBean("����ѧԺ", "  ", 0,0);
		menuItemBean item6=new menuItemBean("����������ռ���ϢѧԺ", "  ", 0,0);
		menuItemBean item7=new menuItemBean("��е����ӹ���ѧԺ", "  ", 0,0);
		menuItemBean item8=new menuItemBean("���ù���ѧԺ", "  ", 0,0);
		menuItemBean item9=new menuItemBean("�����ѧԺ", "  ", 0,0);
		menuItemBean item10=new menuItemBean("��Ϣ����ѧԺ", "  ", 0,0);
		menuItemBean item11=new menuItemBean("��ѧ������ѧԺ", "  ", 0,0);
		menuItemBean item12=new menuItemBean("�鱦ѧԺ", "  ", 0,0);
		menuItemBean item13=new menuItemBean("�����봫ýѧԺ", "  ", 0,0);
		menuItemBean item14=new menuItemBean("��������ѧԺ", "  ", 0,0);
		menuItemBean item15=new menuItemBean("���˼����ѧԺ", "  ", 0,0);
		menuItemBean item16=new menuItemBean("�����ѧԺ", "  ", 0,0);
		menuItemBean item17=new menuItemBean("���ʽ���ѧԺ", "  ", 0,0);
		menuItemBean item18=new menuItemBean("���Ĺ�ѧԺѧԺ", "  ", 0,0);
		menuItemBean item19=new menuItemBean("�Զ���ѧԺѧԺ", "  ", 0,0);
		
		mList.add(item1);
		mList.add(item2);
		mList.add(item3);
		mList.add(item4); 
		mList.add(item5);
		mList.add(item6);
		mList.add(item7); 
		mList.add(item8); 
		mList.add(item9);
		mList.add(item10);
		mList.add(item11);
		mList.add(item12);
		mList.add(item13);
		mList.add(item14);
		mList.add(item15);
		mList.add(item16);
		mList.add(item17);
		mList.add(item18);
		mList.add(item19);
		
		itemAdapter=new ChooseSchoolAdapter(this, mList,intent.getStringExtra("school"));
		listMenu.setAdapter(itemAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_school, menu);
		return true;
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
