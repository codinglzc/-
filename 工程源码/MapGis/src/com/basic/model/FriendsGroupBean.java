package com.basic.model;

import java.util.List;

public class FriendsGroupBean {

    private String GroupName;      //������������
    private String online_txt;     // ���������ߵ���Ŀ
    private List<FriendsBean> Friendslist;    //�����к��ѵ��б�
    
    
    public FriendsGroupBean(String groupName, String online_txt,
			List<FriendsBean> friendslist) {
		super();
		GroupName = groupName;
		this.online_txt = online_txt;
		Friendslist = friendslist;
	}

	public String getOnline_txt() {
		return online_txt;
	}

	public void setOnline_txt(String online_txt) {
		this.online_txt = online_txt;
	}

	public List<FriendsBean> getFriendslist() {
		return Friendslist;
	}
	
	public void setFriendslist(List<FriendsBean> friendslist) {
		Friendslist = friendslist;
	}

	public FriendsGroupBean() {
		super();
		// TODO �Զ����ɵĹ��캯�����
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	@Override
	public String toString() {
		return "FriendsGroupBean [GroupName=" + GroupName + ", online_txt="
				+ online_txt + ", Friendslist=" + Friendslist + "]";
	}

}
