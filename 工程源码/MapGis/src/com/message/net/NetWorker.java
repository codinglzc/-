package com.message.net;


import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.basic.Activities.ChatActivity;
import com.basic.Activities.MainActivity;
import com.basic.Activities.ZJBEXBaseActivity;

public class NetWorker extends Thread{
	private final String TAG="NetWorker";
	
	final static String IP="192.168.56.1";
	final static int PORT=4040;
	
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	Context mContext;
	Vector<ReceiveInfoListener> listeners=new Vector<ReceiveInfoListener>();
	protected final byte connect = 1;
	protected final byte running = 2;
	protected byte state = connect;      //״̬��Ĭ��Ϊ����״̬��
	
	private boolean onWork=true;

	public NetWorker(Context context) {
		mContext=context;
	}
	
	public NetWorker(){
		
	}
	
	@Override
	public void run() {
		while(onWork){
			switch(state){
			case connect:
				connect();
				break;
			case running:
				receiveMsg();
				break;
			}
		}
		
		try {
			if(socket!=null)
				socket.close();
			if(dis!=null)
				dis.close();
			if(dos!=null)
				dos.close();
			
			onWork=true;
			state=connect;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void connect(){
		//���ӷ�������
		try {
//			Log.i(TAG, "�ͻ��ˣ���ʼ���ӷ�����");
			socket=new Socket(IP, PORT);
			Log.i(TAG, "�ͻ��ˣ����ӷ������ɹ�");
			Log.i(TAG, "client socket="+socket);
			
			state=running;
			dis=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			dos=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
		} catch (UnknownHostException e) {
			Log.i(TAG, "NetWorker connect() �쳣��"+e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i(TAG, "NetWorker connect() �쳣��"+e.toString());
			e.printStackTrace();
		}
	}
	
	// ������Ϣ
	public synchronized void receiveMsg() {
		try {
			int type = dis.readInt();
			switch (type) {
			case Config.RECEIVE_TEXT:
				Log.i(TAG, "���պ��ѵ��ı���Ϣ");
				handReceiveText();
				break;
			case Config.RECEIVE_JBEXFriend:
				Log.i(TAG, "���պ��ѵĽ�������Ϣ");
				handReceiveJBexFriend();
				break;
			case Config.RECEIVE_IMG:
				handReceiveData(Config.MESSAGE_TYPE_IMG);
				break;
			case Config.RECEIVE_AUDIO:
				handReceiveData(Config.MESSAGE_TYPE_AUDIO);
				break;
			case Config.RESULT_GET_OFFLINE_MSG:
				handGetOfflineMsg();
				break;
			}
		} catch (Exception e) {
//			 Log.e(TAG, "receiveMsg() exception:"+e.toString());
		}
	}
	


	public void addReceiveInfoListener(ReceiveInfoListener listener) {
		listeners.add(listener);
	}
	
	public boolean receive(ChatMessage message){
		Log.i(TAG, "NetWorker�е�listener��Ϊ��" + listeners.size());
		
		//�ֽ׶�ֻ��ChatActivityע����ReceiveInfoListener,����listeners��ֻ��һ��ReceiveInfoListener
		ReceiveInfoListener listener = listeners.get(0);
		boolean result = listener.receive(message);

		return result;
	}
	
	
	/*******************���·����ǿͻ������������������ķ���************************/
	
	public boolean sendUid(int userid) {
		// TODO �Զ����ɵķ������
		boolean result=true;
		try {
			dos.writeInt(Config.REQUEST_LOGIN);
			dos.writeInt(userid);
			dos.flush();
		}catch (Exception e){
			e.printStackTrace();
			result=false;
		}
		return result;
	}	
	
	//�����ı���Ϣ
	public boolean sendText(String self, String receiver, String time, String content){
		boolean result=true;
		try {
			dos.writeInt(Config.REQUEST_SEND_TXT);
			dos.writeUTF(self);
			dos.writeUTF(receiver);
			dos.writeUTF(time);
			dos.writeUTF(content);
			
			dos.flush();
			Log.i(TAG, "�û�"+self+"���û�"+receiver+"�����ı���Ϣ��"+content);
		} catch (Exception e) {
			Log.e(TAG, "sendText() exception:"+e.toString());
			e.printStackTrace();
			result=false;
		}
		
		return result;
	}
	
	//����������Ϣ
	public boolean sendAudio(String self, String receiver, String time, String filePath){
		boolean result=true;
		try {
			dos.writeInt(Config.REQUEST_SEND_AUDIO);
			dos.writeUTF(self);
			dos.writeUTF(receiver);
			dos.writeUTF(time);
			
			//����contentָ����λ�ã���ȡ�����ļ����ֽ�����
			Log.i(TAG, "audio path="+filePath);
			readFileSendData(filePath);
			
			Log.i(TAG, "�û�"+self+"�����"+receiver+"����������Ϣ");
		} catch (IOException e) {
			Log.e(TAG, "sendAudio() exception:"+e.toString());
			e.printStackTrace();
			result=false;
		}
		
		return result;
	}
	
	//����ͼƬ��Ϣ
	public boolean sendImg(String self, String receiver, String time, String filePath){
		boolean result=true;
		
		try {
			dos.writeInt(Config.REQUEST_SEND_IMG);
			dos.writeUTF(self);
			dos.writeUTF(receiver);
			dos.writeUTF(time);
			dos.flush();
			
			readFileSendData(filePath);
			
			Log.i(TAG, "�û�"+self+"�����"+receiver+"����ͼƬ��Ϣ��");
			
		} catch (IOException e) {
			Log.e(TAG, "sendImg() exception:"+e.toString());
			e.printStackTrace();
			result=false;
		}
		
		return result;
	}
	
	//���ͽ�������Ϣ
	public boolean sendJbexFriend(String owneruser, String frienduser,
			String jbexinfoId) {
		// TODO �Զ����ɵķ������
		boolean result=true;
		
		try {
			dos.writeInt(Config.REQUEST_SEND_JBEXFriend);
			dos.writeUTF(owneruser);
			dos.writeUTF(frienduser);
			dos.writeUTF(jbexinfoId);
			dos.flush();
			
			Log.i(TAG, "�û�"+owneruser+"�����"+frienduser+"���ͽ�������Ϣ��");
			
		} catch (IOException e) {
			Log.e(TAG, "sendJbexFriend() exception:"+e.toString());
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	/**
	 * ��ȡ�ļ�(ͼƬ������)����������
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    private void readFileSendData(String filePath) throws FileNotFoundException, IOException {
        DataInputStream ddis=new DataInputStream(new FileInputStream(filePath));
        int length=0;
        int totalNum=0;
        byte[] buffer=new byte[1024];
        Log.i(TAG, "img.avaliable="+ddis.available());
        
        while((length=ddis.read(buffer))!=-1){
            totalNum+=length;
            dos.writeInt(length);
            dos.write(buffer, 0, length);
            dos.flush();
        }
        
        dos.writeInt(0);
        dos.flush();
        
        if(ddis!=null){
            ddis.close();
            ddis=null;
        }
        
        Log.i(TAG, "readFileSendData(): send bytes="+totalNum);
    }
	
	//��ȡ�ݴ��ڷ������˵�������Ϣ
	public void getOfflineMessage(){
		try {
			dos.writeInt(Config.REQUEST_GET_OFFLINE_MSG);
			dos.writeUTF(String.valueOf(ZJBEXBaseActivity.getSelf().getUser_id()));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendExitQuest(){
		try{
			dos.writeInt(Config.REQUEST_EXIT);
			//dos.writeInt(ZJBEXBaseActivity.getSelf().getUser_id());
			dos.flush();
		}catch(IOException e){
			Log.e(TAG, "NetWorker sendExitQuest() �˳������쳣��"+e.toString());
		}
	}
	
	
	/*******************���·����ǿͻ��˽��շ�������Ӧ�ķ���************************/
	//
	
	
	//�����ı���Ϣ
	private void handReceiveText() {
		try {
			Log.i(TAG, "---��ʼ�����ı���Ϣ---");
			String friend = dis.readUTF();      //��Ϣ�ķ����ߣ�������ǿ��������Ϣ����һ��
			String self   = dis.readUTF();	  	//���˷����Լ�����Ϣ���Լ��Ǹ���Ϣ�Ľ�����
			String time=dis.readUTF();
			String content=dis.readUTF();
			
			Log.i(TAG, "���յ�"+friend+"����"+self+"���ı���Ϣ��"+content);
			
			ChatMessage chatMessage=new ChatMessage(self, friend, Config.MESSAGE_FROM, Config.MESSAGE_TYPE_TXT, time, content);
			Message msg=new Message();
			Bundle bundle=new Bundle();
			bundle.putSerializable("chatMessage", chatMessage);
			msg.setData(bundle);
			
			//���жϵ�ǰ��Activity�ǲ���MainActivity,�������򷢳�Notification
			ZJBEXBaseActivity activity=ZJBEXBaseActivity.getCurrentActivity();
			
			if(activity instanceof ChatActivity){
				if(receive(chatMessage)){   
					Log.i(TAG, "-- �û������ں���Ϣ�ķ��������죬����ֱ�ӽ���Ϣ���͵����������--");
					msg.what=Config.RECEIVE_MESSAGE;
					ZJBEXBaseActivity.sendMessage(msg);
				}else{                      
					msg.what=Config.SEND_NOTIFICATION;    
					Log.i(TAG, "-- �û��ͱ�ĺ����������죬����״̬����������Ϣ֪ͨ--");
					ZJBEXBaseActivity.sendMessage(msg);
				}
			}else  if(activity instanceof MainActivity){
				msg.what=Config.SEND_MessageFragment; 
				Log.i(TAG, "-- ��ǰActivity��MainAcitivity--msg.what="+msg.what);
				ZJBEXBaseActivity.sendMessage(msg);
			}else{
				msg.what=Config.SEND_NOTIFICATION;    
				Log.i(TAG, "-- ��ǰActivity����MainAcitivity--msg.what="+msg.what);
				ZJBEXBaseActivity.sendMessage(msg);
			}
			
			Log.i(TAG, "----�ı���Ϣ�������----");
			Log.i(TAG, "�û�"+self+"���յ�����"+friend+"�������ı���Ϣ��"+content);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "handleTxt() exception:"+e.toString());
		}
	}
	
	
	 private void handReceiveJBexFriend() {
			// TODO �Զ����ɵķ������
			Message msg=new Message();
			msg.what=Config.SEND_NOTIFICATION_JBEX_FRIEND;    
			ZJBEXBaseActivity.sendMessage(msg);
		}
	 
	//����������ͼƬ��Ϣ
	private void handReceiveData(int type){
	    if(type==Config.MESSAGE_TYPE_IMG){
	        Log.i(TAG, "------��ʼ����ͼƬ��Ϣ------");
	    }else if(type==Config.MESSAGE_TYPE_AUDIO){
	        Log.i(TAG, "------��ʼ����������Ϣ------");
	    }
		
		File file=null;
		try{
			String friend =dis.readUTF();
			String self = dis.readUTF();
			String time=dis.readUTF();
			
			file=FileUtil.createFile(self, type);
			String filePath=file.getAbsolutePath();
			Log.i(TAG, "handReceiveData() filePath="+filePath);
		
			receiveDataWriteFile(filePath);
			
			ChatMessage chatMessage=new ChatMessage(self, friend, Config.MESSAGE_FROM, type, time, filePath);
			Message msg=new Message();
			Bundle bundle=new Bundle();
			bundle.putSerializable("chatMessage", chatMessage);
			msg.setData(bundle);
			/*
			//���жϵ�ǰ��Activity�ǲ���MainActivity,�������򷢳�Notification
			WoliaoBaseActivity activity=WoliaoBaseActivity.getCurrentActivity();
			if(activity instanceof ChatActivity){
				if(receive(chatMessage)){   
					Log.i(TAG, "-- �û������ں���Ϣ�ķ��������죬����ֱ�ӽ���Ϣ���͵����������--");
					msg.what=Config.RECEIVE_MESSAGE;
					WoliaoBaseActivity.sendMessage(msg);
				}else{                      
					msg.what=Config.SEND_NOTIFICATION;    
					Log.i(TAG, "-- �û��ͱ�ĺ����������죬����״̬����������Ϣ֪ͨ--");
					WoliaoBaseActivity.sendMessage(msg);
				}
			}else{
				msg.what=Config.SEND_NOTIFICATION;
				
				//������Ϣ�ķ�����(������)��id��
				WoliaoBaseActivity.friend.setFriendID(friend);
				
				Log.i(TAG, "-- ��ǰ���治��������棬��״̬����������Ϣ֪ͨ--");
				WoliaoBaseActivity.sendMessage(msg);
			}
			
			 if(type==Config.MESSAGE_TYPE_IMG){
		            Log.i(TAG, "------ͼƬ��Ϣ�������------");
		        }else if(type==Config.MESSAGE_TYPE_AUDIO){
		            Log.i(TAG, "------������Ϣ�������------");
		        }*/
		}catch (Exception e) {
			Log.e(TAG, "handReceiveData exception:"+e.toString());
			e.printStackTrace();
		}finally{
			if(file!=null){
				file=null;
			}
		}
	}

    private void receiveDataWriteFile(String filePath) throws FileNotFoundException, IOException {
        DataOutputStream ddos=new DataOutputStream(new FileOutputStream(filePath));   //��������ͼƬд�뱾��SD��
        int length=0;
        int totalNum=0;
        byte[] buffer=new byte[2048];
        while((length=dis.readInt())!=0){
            length=dis.read(buffer, 0, length);
            totalNum+=length;
            ddos.write(buffer, 0, length);
            ddos.flush();
        }
        
        if(ddos!=null){
            try {
                ddos.close();
                ddos=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        Log.i(TAG, "receiveDataWriteFile(): receive bytes="+totalNum);
    }
	
	//
	
	
	
	
	public void handGetOfflineMsg(){
		ArrayList<ChatMessage> ChatMessageList=new ArrayList<ChatMessage>();
		try {
			Log.i(TAG, "---------��ʼ����������Ϣ-----------");
			int listSize=dis.readInt();
			Log.i(TAG, "����"+listSize+"��������Ϣ");
			for(int i=0; i<listSize; i++){
				String friendId=dis.readUTF();
				String selfId=dis.readUTF();
				int type=dis.readInt();
				String time=dis.readUTF();
				
				String content="";
				
				if(type==Config.MESSAGE_TYPE_TXT){
					content=dis.readUTF();
				}else if(type==Config.MESSAGE_TYPE_ADD_FRIEND){
				    //�������������ѯ��ϸ��Ϣ������
				    dos.writeInt(Config.REQUEST_GET_USER);
				    dos.writeUTF(selfId);
				    dos.flush();
				}else{
					int length=dis.readInt();
					byte[] data=new byte[length];
					int size=dis.read(data);
					File file=FileUtil.createFile(selfId, type);
					FileOutputStream out=new FileOutputStream(file);
					out.write(data, 0, size);
					out.flush();
					out.close();
					out=null;
					
					content=file.getAbsolutePath();
				}
				
				//����淢��֪ͨ
				ChatMessage chatMessage=new ChatMessage(selfId, friendId, Config.MESSAGE_FROM, type, time, content);
				ChatMessageList.add(chatMessage);
				Log.i(TAG, "���յ�"+i+"��������Ϣ���ѷ����������");
			}
			
			
			Message msg=new Message();
			Bundle bundle=new Bundle();
			bundle.putParcelableArrayList("chatMessageList", ChatMessageList);
			msg.setData(bundle);
			//���жϵ�ǰ��Activity�ǲ���MainActivity,�������򷢳�Notification
			ZJBEXBaseActivity activity=ZJBEXBaseActivity.getCurrentActivity();
			  if(activity instanceof MainActivity){
				msg.what=Config.SEND_MessageOffline; 
				Log.i(TAG, "-- ��ǰActivity��MainAcitivity--msg.what="+msg.what);
				ZJBEXBaseActivity.sendMessage(msg);
			}
			  
			Log.i(TAG, "");
			} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setOnWork(boolean onWork){
		this.onWork=onWork;
	}

	public boolean writeBuf(byte[] data) {
		int length=data.length;
		try {
			dos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void deleteReceiveInfoListener(ReceiveInfoListener listener) {
		// TODO �Զ����ɵķ������
		listeners.remove(listener);
	}

	
}
