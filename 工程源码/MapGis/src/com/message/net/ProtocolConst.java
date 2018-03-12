package com.message.net;

public class ProtocolConst {

	// protected static final String ip = "192.168.1.102";
	protected static final String ip = "10.66.41.7";
	// protected static final String ip = "192.168.0.117";
	protected static final int port = 8888;
	public static final int CMD_REGISTER = 1;              //��ע�ᡮ����
	public static final int CMD_LOGIN = 2;				   //����¼������
	public static final int CMD_CHECK_IN = 3;			   //������������롮����
	public static final int CMD_GET_ALL_LIST = 4;          //����ȡ�û���ص������б�����
	public static final int CMD_SEND_INFO_TO_USER = 5;     //��������Ϣ�����ѡ����� 
	public static final int CMD_SEND_INFO_TO_GROUP = 6;    //��������Ϣ���顮����

	public static final int CMD_LOGIN_SUCESS = 100;        //�����������ġ���¼�ɹ�������
	public static final int CMD_LOGIN_FAILD = 101;         //�����������ġ���¼ʧ�ܡ�����
	public static final int CMD_REGISTER_SUCESS = 102;     //�����������ġ�ע��ɹ�������
	public static final int CMD_REGISTER_FAILD = 103;      //�����������ġ�ע��ʧ�ܡ�����
	public static final int CMD_CHECK_IN_SUCESS = 104;     //�����������ġ���¼�ɹ�������
	public static final int CMD_CHECK_IN_FAILD = 105;		//�����������ġ������������ʧ�ܡ�����
	public static final int CMD_GET_ALL_LIST_SUCESS = 106;  //�����������ġ���ȡ�����б�ɹ�������
	public static final int CMD_GET_ALL_LIST_FAILD = 107;   //�����������ġ���ȡ�����б�ʧ�ܡ�����
	public static final int CMD_HAS_USER_ONLINE = 108;      //�����������ġ��û����ߡ�����
	public static final int CMD_HAS_USER_OFFLINE = 109;     //�����������ġ��û����ߡ�����
	public static final int CMD_SEND_INFO_SUCESS = 110;     //������������'������Ϣ�����ѳɹ�������
	public static final int CMD_SEND_INFO_FAILD = 111;      //������������'������Ϣ������ʧ�ܡ�����
	
	public static final int CMD_RECEIVE_INFO = 112;             //��������������
	public static final int CMD_RECEIVE_INFO_ON_MAIN = 113;     //��Main������յ���Ϣ����ǰ��ʾ����Main���棩
	public static final int CMD_RECEIVE_INFO_FROM_GROUP = 114;  //���յ����������Ϣ

	public static final int CMD_PASSWORD_NOT_SAME = 200;                //�������벻һ��
	public static final int CMD_UPDATE_RECEIVE_INFO = 201;              //���½��յ�����Ϣ
	public static final int CMD_UPDATE_RECEIVE_INFO_FROM_GROUP = 202;   //���½��յ����������Ϣ

	public static final int CMD_PLAY_MSG = 500;             //������ʾ��
	public static final int CMD_SYSTEM_INFO = 900;          //ϵͳ��Ϣ
	public static final int CMD_SYSTEM_ERROR = 901;         //ϵͳ����

}
