package com.basic.model;

public class CollectionsBean {
	private int CollectionsPhoto;    //�ղص��û�����ͼƬID��
	private String CollectionsName;      //�ղغ��ѵ�����
	private int    CollectionsSex;       //�ղغ��ѵ��Ա�      0 δ֪ 1��  2Ů
	private String CollectionsRestTime;      //�ղص�ʣ��ʱ��
	private String CollectionsTime;       //�ղصĽ��ʱ��
	private String Collectionsplace;      //�ղصĽ��ص�
	private String CollectionsType;       //�ղؽ�������
	private String CollectionsTitle;      //�ղؽ��ı���
	private String CollectionsDetails;      //�ղؽ���ϸ��
	
	public CollectionsBean(int collectionsPhoto, String collectionsName,
            int collectionsSex,
			String collectionsRestTime, String collectionsTime,
			String collectionsplace, String collectionsType,
			String collectionsTitle, String collectionsDetails) {
		super();
		CollectionsPhoto = collectionsPhoto;
		CollectionsName = collectionsName;
		CollectionsSex = collectionsSex;
		CollectionsRestTime = collectionsRestTime;
		CollectionsTime = collectionsTime;
		Collectionsplace = collectionsplace;
		CollectionsType = collectionsType;
		CollectionsTitle = collectionsTitle;
		CollectionsDetails = collectionsDetails;
	}

	public int getCollectionsSex() {
		return CollectionsSex;
	}

	public void setCollectionsSex(int collectionsSex) {
		CollectionsSex = collectionsSex;
	}

	public int getCollectionsPhoto() {
		return CollectionsPhoto;
	}

	public void setCollectionsPhoto(int collectionsPhoto) {
		CollectionsPhoto = collectionsPhoto;
	}

	@Override
	public String toString() {
		// TODO �Զ����ɵķ������
		return super.toString();
	}
	
	public String getCollectionsName() {
		return CollectionsName;
	}
	public void setCollectionsName(String collectionsName) {
		CollectionsName = collectionsName;
	}

	public String getCollectionsRestTime() {
		return CollectionsRestTime;
	}
	public void setCollectionsRestTime(String collectionsRestTime) {
		CollectionsRestTime = collectionsRestTime;
	}
	public String getCollectionsTime() {
		return CollectionsTime;
	}
	public void setCollectionsTime(String collectionsTime) {
		CollectionsTime = collectionsTime;
	}
	public String getCollectionsplace() {
		return Collectionsplace;
	}
	public void setCollectionsplace(String collectionsplace) {
		Collectionsplace = collectionsplace;
	}
	public String getCollectionsType() {
		return CollectionsType;
	}
	public void setCollectionsType(String collectionsType) {
		CollectionsType = collectionsType;
	}
	public String getCollectionsTitle() {
		return CollectionsTitle;
	}
	public void setCollectionsTitle(String collectionsTitle) {
		CollectionsTitle = collectionsTitle;
	}
	public String getCollectionsDetails() {
		return CollectionsDetails;
	}
	public void setCollectionsDetails(String collectionsDetails) {
		CollectionsDetails = collectionsDetails;
	}
	
	
}

