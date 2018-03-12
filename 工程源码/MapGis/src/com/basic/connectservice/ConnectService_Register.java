package com.basic.connectservice;

import java.util.HashMap;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ConnectService_Register extends IntentService {

	private static final String ACTION_RECV_MSG = "com.basic.connectservice.action.RECEIVE_MESSAGE";

	public ConnectService_Register() {
		super("TestIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		/**
		 * �����ԣ�IntentService�����ǿ��Խ��к�ʱ�Ĳ����� IntentServiceʹ�ö��еķ�ʽ�������Intent������У�
		 * Ȼ����һ��worker thread(�߳�)����������е�Intent
		 * �����첽��startService����IntentService�ᴦ�����һ��֮���ٴ���ڶ���
		 */
		// ͨ��intent��ȡ���̴߳��������䡢�ǳƺ������ַ���
		String email = intent.getStringExtra("email");
		String password = intent.getStringExtra("password");
		String user_nickname = intent.getStringExtra("user_nickname");
		Boolean flag = doRegister(email, password, user_nickname);
		if (!flag) {
			Log.d("ע����", flag.toString());
		} else if (flag) {
			Log.d("ע����", flag.toString());
		}
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ACTION_RECV_MSG);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra("result", flag.toString());
		sendBroadcast(broadcastIntent);
	}

	private Boolean doRegister(String email, String password,
			String user_nickname) {
		String strFlag = "";
		// ʹ��Map��װ�������
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("password", password);
		map.put("user_nickname", user_nickname);
		// ���巢�������URL
		// String url = HttpUtil.BASE_URL + "servlet/RegisterServlet?username="
		// + username + "&password=" + password + "&email=" + email; // GET��ʽ
		String url = HttpUtil.BASE_URL + "servlet/RegisterServlet"; // POST��ʽ
		Log.d("url", url);
		Log.d("user_nickname", user_nickname);
		Log.d("email", email);
		Log.d("password", password);
		try {
			// ��������
			strFlag = HttpUtil.postRequest(url, map); // POST��ʽ
			// strFlag = HttpUtil.getRequest(url); // GET��ʽ
			// if (strFlag == null) {
			// Log.d("http�������", "false");
			// strFlag = "false";
			// } else {
			// Log.d("ע����", strFlag);
			// }
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("http������", "ʧ�ܣ�");
		}

		if (strFlag.trim().equals("true")) {
			return true;
		} else {
			return false;
		}
	}
}
