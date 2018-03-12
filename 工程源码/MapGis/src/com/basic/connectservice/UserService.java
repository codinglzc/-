package com.basic.connectservice;

import java.util.HashMap;

import android.util.Log;

import com.basic.service.model.User;

public class UserService {
	
	public static final String PATH_URL = HttpUtil.BASE_URL+"servlet/FindUserServlet?email=";
	public static final String PATH_URLIsfriend=HttpUtil.BASE_URL+"servlet/IsFriendServlet";
    public static User getUser(String email,String type){
    	 String url=PATH_URL+email+"&type="+type;
    	 Log.d("url", url);
    	 String jsonString = "";
    	 User user=new User();
    	 try {
 			jsonString = HttpUtil.getRequest(url);
 			 Log.d("jsonFriendUser", jsonString);
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			Log.d("��ȡjsonStringʧ��", "fail");
 		}
    	 if(jsonString.equals("")!=true){
    	  user= GetUser.getSimpleUser("user", jsonString);
    	  return user;
    	 }
    	 else 
    		 return null;
    }
    
    public static boolean registerUser(String user_nickname,String email ,String password){
    	 String strFlag = "";
 		// ʹ��Map��װ�������
 		HashMap<String, String> map = new HashMap<String, String>();
 		map.put("email", email);
 		map.put("password", password);
 		map.put("user_nickname", user_nickname);
 		// ���巢�������URL
 		String url = HttpUtil.BASE_URL + "servlet/RegisterServlet"; // POST��ʽ
 		try {
 			// ��������
 			strFlag = HttpUtil.postRequest(url, map); // POST��ʽ
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
    public static boolean LoginUser(String username,String password){

    	String jsonString = "";
		String flag;
		jsonString = doLogin(username, password);
		Log.d("��¼���", jsonString);
		if (jsonString.equals("flase")) {
			flag = "flase";
		} else {
			User user = GetUser.getSimpleUser("user", jsonString);
			flag = user.getFlag();
		}
		
		if (flag.trim().equals("true")) {
			return true;
		} else {
			return false;
		}
    }
    
    public static boolean Isfriend(String owneruser,String frienduser){
    	String url=PATH_URLIsfriend;
    	String strFlag="";
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put("owneruser", owneruser);
    	map.put("frienduser",frienduser);
    	
    	try {
			strFlag = HttpUtil.postRequest(url, map);
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			Log.d("http������", "ʧ�ܣ�");
		} // POST��ʽ
    	
    	if (strFlag.trim().equals("true")) {
			return true;
		} else {
			return false;
		}
    }
    
 // ���巢������ķ���
 	private static String doLogin(String username, String password) {
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
