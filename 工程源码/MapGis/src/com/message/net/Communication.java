package com.message.net;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Communication�����û���ʵ�ʷ��͡�������Ϣ�߳�֮����������û���Communication�������Communication�����߳�ִ�з�������
 * @author hu
 *
 */
public class Communication {

	private NetWorker netWorker;              //����һ���߳���
	public static Communication instance;    //һ����̬��Communicationʵ��
	public static MessageDigest md;          //��ϢժҪ

	private Communication() {
		netWorker = new NetWorker();
		
		//�߳̿�ʼִ��
		netWorker.start();
		try {
			if (md == null)
				md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String MD5(String strSrc) {
		byte[] bt = strSrc.getBytes();
		md.update(bt);
		String strDes = bytes2Hex(md.digest());   // �ֽ�������16�����ַ���
		return strDes;
	}

	private static String bytes2Hex(byte[] bts) {
		StringBuffer des = new StringBuffer();
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}

	public static Communication newInstance() {
		if (instance == null)
			instance = new Communication();
		return instance;
	}
	
	public void setInstanceNull(){
		instance=null;
	}

	public NetWorker getTransportWorker() {
		return netWorker;
	}

	//�����˳�ʱ��������
	public void stopWork(){
		netWorker.setOnWork(false);
	}

	
	public boolean  sendJbexFriend(String owneruser,String frienduser,String jbexinfoId){
		return netWorker.sendJbexFriend(owneruser,frienduser,jbexinfoId);
	}
	
	public boolean sendUid(int userid){
		return netWorker.sendUid(userid);
	}
	public boolean sendImg(String self, String friend, String time, String content) {
		return netWorker.sendImg(self, friend, time, content);
	}

	public boolean sendText(String self, String friend, String time, String content) {
		return netWorker.sendText(self, friend, time, content);
	}

	public boolean sendAudio(String self, String friend, String time,
			String content) {
		return netWorker.sendAudio(self, friend, time, content);
	}
	
	//�����ݴ��ڷ������˵�������Ϣ
	public void getOfflineMessage(){
		netWorker.getOfflineMessage();
	}
	
	public void sendExitQuest(){
		netWorker.sendExitQuest();
	}

	public void addReceiveInfoListener(ReceiveInfoListener listener) {
		netWorker.addReceiveInfoListener(listener);
	}
	public void deleteReceiveInfoListener(ReceiveInfoListener listener) {
		netWorker.deleteReceiveInfoListener(listener);
	}
	public String newSessionID() {
		return String.valueOf(System.currentTimeMillis());
	}
	
	public void reconnect() {
		netWorker.notify();
	}
}
