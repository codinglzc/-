package com.base.jbex;

import com.basic.Activities.R;
import com.basic.Activities.R.layout;
import com.basic.Activities.R.menu;
import com.basic.Activities.ZJBEXBaseActivity;
import com.basic.service.model.User;
import com.base.jbex.AboutSchool;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.android.mapview.MapView.MapViewAnnotationListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewLongTapListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewRenderContextListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewTapListener;
import com.zondy.mapgis.geometry.Dot;

import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SelectDot extends ZJBEXBaseActivity implements MapViewTapListener ,MapViewLongTapListener,
																	MapViewRenderContextListener,MapViewAnnotationListener 
 {
	private Intent intent;
	private User owneruser=new User();
	
	private Bitmap bmp1 = null;
	public MapView mapview = null;
	ActionBar actionBar =null;
	AnnotationView annotationView = null;
	private String path = Environment.getExternalStorageDirectory().getPath();
	double double_x = 0 , double_y = 0;
	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_dot);
		
		initActionBar();
		
		intent=getIntent();
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("owneruser", owneruser.toString());
		}
		
		Toast.makeText(this, "��ѡ��һ���ص�", Toast.LENGTH_LONG).show();
		
		mapview= (MapView) super.findViewById(R.id.mapview2);
		mapview.loadFromFile(path + "/mapgis/map/wuhan/wuhan.xml"); // ���ص�ͼ
		mapview.setRenderContextListener(this); // ��Ҫע����Ӧ�����ӿ���ļ�����
		mapview.setAnnotationListener(this);
		mapview.setTapListener(this);
		mapview.setLongTapListener(this);
	}

	private void initActionBar() {
		// TODO �Զ����ɵķ������
		actionBar = getActionBar();
		actionBar.setCustomView(R.layout.session_top);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);  
		actionBar.setDisplayShowCustomEnabled(true);
		View view=actionBar.getCustomView();
		ImageButton rightButton=(ImageButton) view.findViewById(R.id.btn_nnavigation);
		ImageButton back=(ImageButton) view.findViewById(R.id.btn_back);
		TextView txt = null;	
		txt=(TextView) view.findViewById(R.id.main_Text);
		rightButton.setBackgroundResource(R.drawable.button_select);	
		txt.setText("ѡ��ص�");
		TextView backtxt=(TextView) view.findViewById(R.id.backtxt);
		
		rightButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub                                          ��������  ��תҳ��
				if(double_x!=0 && double_y!=0){
				Intent intent = new Intent(SelectDot.this,EditPublic.class);
                
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				intent.putExtras(data);
				
				intent.putExtra("dot1", double_x);
				intent.putExtra("dot2", double_y);
				SelectDot.this.startActivity(intent);
				SelectDot.this.finish();}
				else {
					Toast.makeText(SelectDot.this, "��ѡ��һ������", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				SelectDot.this.finish();
			}
		});
		
		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				SelectDot.this.finish();
			}
		});
		
	}


	@Override
	public void mapViewClickAnnotation(MapView arg0, Annotation arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapViewClickAnnotationView(MapView arg0, AnnotationView arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AnnotationView mapViewViewForAnnotation(MapView arg0, Annotation arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mapViewWillHideAnnotationView(MapView arg0,
			AnnotationView arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mapViewWillShowAnnotationView(MapView arg0,
			AnnotationView arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void mapViewRenderContextCreated() {
		// TODO Auto-generated method stub
		mapview.setShowUserLocation(true);
		mapview.zoomTo(4.0f, false);
	}

	@Override
	public void mapViewRenderContextDestroyed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean mapViewLongTap(PointF viewPoint) {
		// TODO Auto-generated method stub
		if (count == 2)
		{
			mapview.getAnnotationLayer().removeAllAnnotations();
			count =0;
		}
		count++;
		if (count ==1)
		{
			// ����ͼ����ת���ɵ�ͼ����
			Dot point = mapview.viewPointToMapPoint(viewPoint);
			String strDeString = String.format("x = %f,%ny = %f", point.getX(),point.getY());
			double_x =  mapview.mapPointToLocation(point).getX();
			double_y =  mapview.mapPointToLocation(point).getY();				
			// ����annotation
			Annotation annotationTap = new Annotation("��ע", strDeString, point,null);
			bmp1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.annotation1)).getBitmap();
			mapview.getAnnotationLayer().addAnnotation(new Annotation("zuobiao", strDeString, point, bmp1));
			mapview.refresh();
			// ��annotationLayer�����annotation
			mapview.getAnnotationLayer().addAnnotation(annotationTap);
			// �����Ƿ���ʾannotationview
			annotationTap.showAnnotationView();
		}
		return false;
	}


	@Override
	public void mapViewTap(PointF arg0) {
		// TODO Auto-generated method stub
		
	}
   
	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������
		if(msg.what==Config.SEND_NOTIFICATION){
			Bundle bundle=msg.getData();
			ChatMessage chatMessage=(ChatMessage)bundle.getSerializable("chatMessage");
			saveToDb(chatMessage,Config.DateBase_GET_MESSAGE);

			sendNotifycation(chatMessage.getSelf(),chatMessage.getFriend());}
		else if (msg.what==Config.SEND_NOTIFICATION_JBEX_FRIEND){
			sendNotifycation_JBEXFriend();
		}
	}
}
