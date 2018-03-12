package com.basic.connectservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.basic.service.model.User;

public class AttentionService {
	
	public static final String PATH_URL_GetAttentionUser = HttpUtil.BASE_URL+"servlet/JsonAttentionServlet?email=";
	public static final String PATH_URL_AddAttentionUser = HttpUtil.BASE_URL+"servlet/AttetnionServlet";
	
	public static List<User> GetAttentionList(String email) {
		String url=PATH_URL_GetAttentionUser+email;
		Log.d("PATH_URL_GetAttentionUser", url);
		String jsonString = "";
		
		try {
			jsonString = HttpUtil.getRequest(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("��ȡjsonStringʧ��", "fail");
		}
		
		List<User> attentionList = new ArrayList<User>();
		if (jsonString != null) {
			attentionList= GetUser.getMultiUser("attentionUser",jsonString);
			return attentionList;
		}
		else return attentionList;
	}
	
	public static boolean addAttentionUser(String owneruser,String frienduser){
		String url=PATH_URL_AddAttentionUser;
		
		HashMap<String, String> map = new HashMap<String, String>();
		String strFlag="";
		map.put("owneruser", owneruser);
		map.put("frienduser", frienduser);
		map.put("style", "add");
		
		try {
			strFlag =HttpUtil.postRequest(url, map);
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			Log.d("http������", "ʧ�ܣ�");
		}
		
		if (strFlag.trim().equals("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean deleteAttentionUser(String owneruser,String frienduser){
		String url=PATH_URL_AddAttentionUser;
		
		HashMap<String, String> map = new HashMap<String, String>();
		String strFlag="";
		map.put("owneruser", owneruser);
		map.put("frienduser", frienduser);
		map.put("style", "delete");
		
		try {
			strFlag =HttpUtil.postRequest(url, map);
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
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
