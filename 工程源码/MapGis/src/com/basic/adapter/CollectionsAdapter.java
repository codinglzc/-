package com.basic.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.basic.Activities.R;
import com.basic.ImageLoad.AnimateFirstDisplayListener;
import com.basic.ImageLoad.ImageStringUtil;
import com.basic.model.CollectionsBean;
import com.basic.service.model.JbexInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


public class CollectionsAdapter extends BaseAdapter{
	private List<JbexInfo> jbexinfoList=new ArrayList<JbexInfo>();
	private LayoutInflater mInflater;
	
	private ImageLoader imageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	
	public CollectionsAdapter( List<JbexInfo> jbexinfoList,
			Context mContext,ImageLoader imageLoader,DisplayImageOptions option) {
		super();
		
		this.imageLoader=imageLoader;
		this.options=option;
		this.jbexinfoList = jbexinfoList;
		this.mInflater= LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO �Զ����ɵķ������
		return jbexinfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO �Զ����ɵķ������
		return jbexinfoList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO �Զ����ɵķ������
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO �Զ����ɵķ������
		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView= mInflater.inflate(R.layout.collection_item_layout, null);
			viewHolder.collectionsPhoto=(ImageView) convertView.findViewById(R.id.collectionsPhoto);
			viewHolder.collectionSex=(ImageView)convertView.findViewById(R.id.collectionSex);
			viewHolder.collectionsUsername=(TextView) convertView.findViewById(R.id.collectionsUsername);
			//viewHolder.CollectionsRestTime=(TextView)convertView.findViewById(R.id.CollectionsRestTime);
			viewHolder.CollectionsTime=(TextView)convertView.findViewById(R.id.CollectionsTime);
			viewHolder.collectionsPlace=(TextView)convertView.findViewById(R.id.collectionsPlace);
			viewHolder.collectionsType=(TextView)convertView.findViewById(R.id.collectionsType);
			viewHolder.collectionsTitle=(TextView)convertView.findViewById(R.id.collectionsTitle);
			viewHolder.collectionsDetails=(TextView)convertView.findViewById(R.id.collectionsDetails);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}

		imageLoader.displayImage(ImageStringUtil.getImageURL(jbexinfoList.get(position).getTUser().getPicture()), viewHolder.collectionsPhoto, options, animateFirstListener);
		
		if(jbexinfoList.get(position).getTUser().getSex()==1)
		viewHolder.collectionSex.setImageResource(R.drawable.man);
		else
			if(jbexinfoList.get(position).getTUser().getSex()==2)
			viewHolder.collectionSex.setImageResource(R.drawable.woman);
		
		viewHolder.collectionsUsername.setText(jbexinfoList.get(position).getTUser().getUser_nickname());
		//viewHolder.CollectionsRestTime.setText(mListCollectionsBean.get(position).getCollectionsRestTime());
		viewHolder.CollectionsTime.setText(sdf.format(jbexinfoList.get(position).getTime()));
		viewHolder.collectionsPlace.setText("�й����ʴ�ѧ(�人)");
		viewHolder.collectionsType.setText(jbexinfoList.get(position).getLabel());
		viewHolder.collectionsTitle.setText(jbexinfoList.get(position).getTitle());
		viewHolder.collectionsDetails.setText(jbexinfoList.get(position).getDetail());

		return convertView;
	}
	
	public void onDateChange(List<JbexInfo> apk_list) {
		// TODO �Զ����ɵķ������
		this.jbexinfoList = apk_list;
		this.notifyDataSetChanged();
	}
	
	 class ViewHolder{
		   public ImageView collectionsPhoto;    //�ղص��û�����ͼƬID��
		   public ImageView collectionSex;        //�ղغ��ѵ��Ա�
		   public TextView collectionsUsername;    //�ղغ��ѵ�����
		   //public TextView CollectionsRestTime;     //�ղص�ʣ��ʱ��
		   public TextView CollectionsTime;         //�ղصĽ��ʱ��
		   public TextView collectionsPlace;          //�ղصĽ��ص�
		   public TextView collectionsType;          //�ղؽ�������
		   public TextView collectionsTitle;          //�ղؽ��ı���
		   public TextView collectionsDetails;         //�ղؽ���ϸ��
	   }
	 
}
