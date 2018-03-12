package com.basic.Activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.basic.connectservice.UserService;
import com.basic.service.model.User;
import com.message.net.ChatMessage;
import com.message.net.Communication;
import com.message.net.Config;
import com.message.net.DatabaseUtil;
import com.message.net.Friend;
import com.message.net.ProtocolConst;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public abstract class ZJBEXBaseActivity extends Activity{
	//Activity�ļ��ϣ���������Activity��¼�ڴˣ��˳�����ʱ������ر�Activity
	protected static LinkedList<ZJBEXBaseActivity> queue = new LinkedList<ZJBEXBaseActivity>();
	public static final String communication = "���Ժ�����ͨ�š���";
	public static final String communication_faild = "�Բ���ͨ��ʧ�ܣ�";
	protected static Communication con;
	private static MediaPlayer player;
	protected static DatabaseUtil dbUtil;
	private static final String TAG="ZJBEXBaseActivity";
	public static User self=new User();
	public static User friend=new User();
	
	final int EXIT_DIALOG=0x12;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (!queue.contains(this))
			queue.add(this);
		if (player == null) {
			player = MediaPlayer.create(this, R.raw.msg);
			Log.i(TAG, TAG+" player="+player);
			try {
				player.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(dbUtil==null){
		    dbUtil=new DatabaseUtil(this);
		}
		Log.i(TAG, "ĿǰActivity number="+queue.size());
	}
	
	public static ZJBEXBaseActivity getActivity(int index) {
		if (index < 0 || index >= queue.size())
			throw new IllegalArgumentException("out of queue");
		return queue.get(index);
	}
	
	public static User getSelf() {
		return self;
	}

	public static void setSelf(User self) {
		ZJBEXBaseActivity.self = self;
	}
   
	public static DatabaseUtil getDbUtil() {
		return dbUtil;
	}

	public static void setDbUtil(DatabaseUtil dbUtil) {
		ZJBEXBaseActivity.dbUtil = dbUtil;
	}
	
	public static User getFriend() {
		return friend;
	}

	public static void setFriend(User friend) {
		ZJBEXBaseActivity.friend = friend;
	}

	public static ZJBEXBaseActivity getCurrentActivity() {
		return queue.getLast();
	}
	
	public void makeTextShort(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void makeTextLong(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	public abstract void processMessage(Message msg);

	public static void sendMessage(int cmd, String text) {
		Message msg = new Message();
		msg.what = cmd;
		msg.obj = text;
		sendMessage(msg);
	}
    
	public static void sendMessage(Message msg) {
		handler.sendMessage(msg);
	}

	public static void sendEmptyMessage(int what) {
		handler.sendEmptyMessage(what);
	}
   
	
	public static void saveMessageToDb(String owneruser,String frienduser,int direction, int type, String time, String content,int Data_Base_type) {
		ContentValues values=new ContentValues();
		values.put("self_Id", "'"+owneruser+"'");
		values.put("friend_Id", "'"+frienduser+"'");
		values.put("direction", direction);
		values.put("type", type);
		values.put("time", time);
		values.put("content", content);
		dbUtil.insertMessage(values,Data_Base_type);
	}
	
	public static List<Friend> getFriendMessageList(String userid){
		List<Friend> list=new ArrayList<Friend>();
		list=dbUtil.queryFriends(userid);
		return list;
	}
	
	public static void setsetFriendNumToDb(String self_Id, String friend_Id,int num){
		self_Id="'"+self_Id+"'";
		friend_Id="'"+friend_Id+"'";
		dbUtil.setFriendNum(self_Id, friend_Id, num);
	}
	
	public static void saveToDb(ChatMessage msg,int Data_Base_type) {
		ContentValues values=new ContentValues();
		values.put("self_Id", "'"+msg.getSelf()+"'");
		values.put("friend_Id", "'"+msg.getFriend()+"'");
		values.put("direction", msg.getDirection());
		values.put("type", msg.getType());
		values.put("time", msg.getTime());
		values.put("content", msg.getContent());
		dbUtil.insertMessage(values,Data_Base_type);
	}
	
	public static void MessageOfflineSaveToDb(ArrayList<ChatMessage> chatMessageList) {
		// TODO �Զ����ɵķ������
		for(int i=0;i<chatMessageList.size();i++){
			ChatMessage chatmes=chatMessageList.get(i);
			saveToDb(chatmes,Config.DateBase_GET_MESSAGE);
		}
	}

	//Handler�����Ǿ�̬�ģ�������TuliaoBaseActivity�����඼�ǹ���ͬһ����Ϣ����
		private static Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ProtocolConst.CMD_SYSTEM_INFO: {
					queue.getLast().makeTextShort(msg.obj.toString());
				}
					break;
				case ProtocolConst.CMD_SYSTEM_ERROR: {
					queue.getLast().makeTextShort(msg.obj.toString());
				}
					break;
				case ProtocolConst.CMD_PLAY_MSG: {
					playMsg();
				}
					break;
				case Config.finFrienduser:{
					
					final String friendId=msg.getData().getString("friendID");
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO �Զ����ɵķ������
						User friend=UserService.getUser(friendId,"id");
						ZJBEXBaseActivity.setFriend(friend);
						}
					}).start();	
				}
				break;
					
				default:
					if(!queue.isEmpty()){
						queue.getLast().processMessage(msg);
					}
					break;
				}
			}
		};
   
		public static void playMsg() {
			try {
				player.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		
		public static String getTime() {
			return DateFormat.format("hh:mm:ss", Calendar.getInstance()).toString();
		}
		
		public    void sendNotifycation_JBEXFriend(){
			//����Notification
			playMsg();
			NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			Notification notification=new Notification(R.drawable.notify_icon, "����������", System.currentTimeMillis());
			Intent intent=new Intent(this, MyPublishJbexActivity.class);
			
			Bundle data=new Bundle();
			data.putSerializable("owneruser", self);
			intent.putExtras(data);
			
			PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(this, "����������", "�鿴�������", pendingIntent);
			notification.flags|=Notification.FLAG_AUTO_CANCEL;
			manager.notify(0, notification);
		}
		
		public void sendNotifycation(String selfid,String friendid){
			//����Notification
			
			playMsg();
			// ��Android����֪ͨ����������Ҫ��ϵͳ������֪ͨ������NotificationManager������һ��ϵͳService��  
			NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			// ����һ��PendingIntent����Intent���ƣ���ͬ�������ڲ������ϵ��ã���Ҫ������״̬��������activity��
			//���Բ��õ���PendingIntent,�����Notification��ת�������ĸ�Activity
			Intent intent=new Intent(this, ChatActivity.class);
			Bundle data=new Bundle();
			data.putInt("frienduser", Integer.valueOf(friendid));
			data.putInt("owneruser", Integer.valueOf(selfid));
			intent.putExtras(data);
			
			PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			Notification notification=new Notification(R.drawable.notify_icon, "��������Ϣ", System.currentTimeMillis());
			notification.setLatestEventInfo(this, "����Ϣ", "�鿴����Ϣ", pendingIntent);
			// FLAG_AUTO_CANCEL������֪ͨ���û����ʱ��֪ͨ���������  
            // ͨ��֪ͨ������������֪ͨ�����id��ͬ����ÿclick����statu��������һ����ʾ  
			notification.flags|=Notification.FLAG_AUTO_CANCEL;
			
			manager.notify(1, notification);
			
			/*
			 * Pendingintent��ֵ����
               pendingintent��ֵ������ȡ����ֵ�ǵ�һ�ε�ֵ����null��������ڶ������������һ������ѡ���й�ϵ��
               FLAG_ONE_SHOT
               FLAG_NO_CREATE
               FLAG_CANCEL_CURRENT
               FLAG_UPDATE_CURRENT
                              ����4��flag�����ʹ�õ���FLAG_UPDATE_CURRENT��
                              ��Ϊ������Intent�и��µ�ʱ����Ҫ�õ����flagȥ�������������
                              ����������´��¼�������ʱ�䵽���ʱ��extras��Զ�ǵ�һ��Intent��extras��
                              ʹ��FLAG_CANCEL_CURRENTҲ����������extras��ֻ�������Ȱ�ǰ���extras�����
                              ����FLAG_CANCEL_CURRENT��FLAG_UPDATE_CURRENT�����������ܷ���newһ��Intent��
               FLAG_UPDATE_CURRENT�ܹ���newһ��Intent����FLAG_CANCEL_CURRENT���ܣ�ֻ��ʹ�õ�һ�ε�Intent��

������flag�ͱȽ����ã�����FLAG_ONE_SHOT��ȡ��PendingIntentֻ��ʹ��һ�Σ���ʹ��PendingIntentҲ��ʧ�ܣ�����FLAG_NO_CREAT��ȡ��PendingIntent��������Intent�������򷵻�NULLֵ.
			 */
		}
		
		@Override
		public void onBackPressed() {
			Log.i(TAG, "Activity number="+queue.size());
			if(queue.size()==1){	//��ǰActivity�����һ��Activity��
				showDialog(EXIT_DIALOG);
			}else{
				queue.getLast().finish();
			}
		}
		
		@Override
		public void finish() {
			super.finish();
			if(!queue.isEmpty()){
				queue.removeLast();
			}
		}

		public void exit() {
			//�ر�Socket���ӡ����������
			con.stopWork();
			con.setInstanceNull();
			
			//�ر����ݿ⡢MediaPlayer
							
			if(player!=null)
				player=null;
			//�ر����ݿ⡢MediaPlayer
			if(dbUtil!=null){
				dbUtil.close();
			}	
			//����Activity
			while (queue.size() > 0)
				queue.getLast().finish();
		}
		
		@Override
		protected Dialog onCreateDialog(int id) {
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			Log.i(TAG, "dialog id="+id);
			switch(id){
			case EXIT_DIALOG:{
				Log.i(TAG, "Ҫ���������˳����ѶԻ���");
				builder.setMessage("�˳�΢�У�")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//����������͡��˳�������
						con.sendExitQuest();
						
						//�رյ���������Socket���ӣ��������������
						exit();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
			}
				break;
			}
			AlertDialog dialog=builder.create();
			Log.i(TAG, "dialog="+dialog);
			return dialog;
		}
		
		private void showExitDialog(){
			AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
			builder.setMessage("�˳����ģ�")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//����������͡��˳�������
					con.sendExitQuest();
					
					//�رյ���������Socket���ӣ��������������
					exit();
				}
			})
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.create().show();
		}
	
}
