package com.message.net;

public class Friend{
	private String friendID;       //����ID
	private String friendName;     //��������
	private String head;           //����ͷ��·��
	private String headModifyTime; //ͷ���ʱ��� 
	private String sex;       
	private int type;		     //���һ����Ϣ����
	private String content;      //���һ����Ϣ������
	private String time;         //���һ����Ϣ��ʱ��
	private int num;
	
	public Friend(){
		
	}
	
	public String getHeadModifyTime() {
        return headModifyTime;
    }

    public void setHeadModifyTime(String headModifyTime) {
        this.headModifyTime = headModifyTime;
    }

    public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getFriendID() {
		return friendID;
	}
	public void setFriendID(String friendID) {
		this.friendID = friendID;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}

