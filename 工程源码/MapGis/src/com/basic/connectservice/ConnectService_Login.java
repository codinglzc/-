package com.basic.connectservice;

import java.util.HashMap;

import com.basic.service.model.User;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * IntentService���������ں�̨�Զ��з�ʽ�����ʱ������
 * 
 * @author lc
 *
 */
public class ConnectService_Login extends IntentService {

	private static final String ACTION_RECV_MSG = "com.basic.connectservice.action.RECEIVE_MESSAGE";

	public ConnectService_Login() {
		super("TestIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		/**
		 * �����ԣ�IntentService�����ǿ��Խ��к�ʱ�Ĳ����� IntentServiceʹ�ö��еķ�ʽ�������Intent������У�
		 * Ȼ����һ��worker thread(�߳�)����������е�Intent
		 * �����첽��startService����IntentService�ᴦ�����һ��֮���ٴ���ڶ���
		 */
		String jsonString = "";
		String flag;
		// ͨ��intent��ȡ���̴߳������û����������ַ���
		String username = intent.getStringExtra("username");
		String password = intent.getStringExtra("password");
		jsonString = doLogin(username, password);
		Log.d("��¼���", jsonString);
		if (jsonString.equals("flase")) {
			flag = "flase";
		} else {
			User user = GetUser.getSimpleUser("user", jsonString);
			flag = user.getFlag();
		}
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(ACTION_RECV_MSG);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra("result", flag);
		broadcastIntent.putExtra("jsonString", jsonString);
		sendBroadcast(broadcastIntent);

	}

	// ���巢������ķ���
	private String doLogin(String username, String password) {
		String jsonString = "";
		// ʹ��Map��װ�������
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		// ���巢�������URL
		// String url = HttpUtil.BASE_URL + "servlet/LoginServlet?username=" +
		// username
		// + "&password=" + password; // GET��ʽ
		String url = HttpUtil.BASE_URL + "servlet/LoginServlet"; // POST��ʽ
		Log.d("url", url);
		Log.d("username", username);
		Log.d("password", password);
		try {
			// ��������
			jsonString = HttpUtil.postRequest(url, map); // POST��ʽ
			// strFlag = HttpUtil.getRequest(url); // GET��ʽ
			Log.d("����������jsonֵ", jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;

	}
}
