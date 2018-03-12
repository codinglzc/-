package com.basic.Activities;

import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.basic.Activities.R;
import com.basic.Activities.R.layout;
import com.basic.Activities.R.menu;
import com.basic.service.model.User;
import com.basic.util.LocationUtil;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.graphic.GraphicPolylin;
import com.zondy.mapgis.android.graphic.GraphicText;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.android.mapview.MapView.MapViewAnnotationListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewLongTapListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewRenderContextListener;
import com.zondy.mapgis.geometry.Dot;
import com.zondy.mapgis.geometry.Rect;
import com.zondy.mapgis.route.Route;
import com.zondy.mapgis.route.RouteAnalysis;
import com.zondy.mapgis.route.RouteAnalysis.FromAndTo;

import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RouteActivity extends ZJBEXBaseActivity implements
		MapViewAnnotationListener, MapViewRenderContextListener,
		AMapLocationListener, MapViewLongTapListener {

	private User owneruser = new User();
	private Intent intent;
	private ActionBar actionBar;
	private MapView mapView;
	private String path = Environment.getExternalStorageDirectory().getPath();

	private LocationManagerProxy mLocationManagerProxy;
	private double dot_x, dot_y;

	private RouteAnalysis rtAnalysis;
	Route testRoute;
	private int count = 0;
	Dot point[] = new Dot[2];
	Dot pointofmine = new Dot();
	private Bitmap bmp1 = null;
	private Bitmap mybmp = null;
    private boolean flag=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);

		initactionBar();
		initLocation();
		mapView = (MapView) super.findViewById(R.id.mapview);
		mapView.loadFromFile(path + "/mapgis/map/wuhan/wuhan.xml"); // ���ص�ͼ
		mapView.setRenderContextListener(RouteActivity.this); // ע���ĸ�����
		mapView.setAnnotationListener(RouteActivity.this);
		mapView.setLongTapListener(this);

		bmp1 = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.locationofend)).getBitmap();
		mybmp = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.locationofmine)).getBitmap();
	}

	private void initLocation() {
		// TODO �Զ����ɵķ������
		// ��ʼ����λ��ֻ�������綨λ
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(true);
		// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
		// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
		// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
		// ����������ʱ��Ϊ-1����λֻ��һ��,
		// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, -1, 15, this);
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		point[0] = new Dot(dot_x, dot_y);

		Dot mpoint = mapView.locationToMapPoint(point[0]);
		mapView.zoomToCenter(mpoint, 4.0f, false);
		Annotation annotation = new Annotation("", "", mpoint, mybmp);
		mapView.getAnnotationLayer().addAnnotation(annotation);
		mapView.refresh();
	}

	private void initactionBar() {
		// TODO �Զ����ɵķ������x
		actionBar = getActionBar();
		actionBar.setCustomView(R.layout.session_top);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);

		View view = actionBar.getCustomView();
		ImageButton nnavigation = (ImageButton) view
				.findViewById(R.id.btn_nnavigation);
		ImageButton back = (ImageButton) view.findViewById(R.id.btn_back);
		TextView txt = (TextView) view.findViewById(R.id.main_Text);
		TextView backtxt = (TextView) view.findViewById(R.id.backtxt);

		nnavigation.setBackgroundResource(0);

		txt.setText("����");

		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				RouteActivity.this.finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				RouteActivity.this.finish();
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO �Զ����ɵķ������
		super.onPause();
		// �Ƴ���λ����
		// mLocationManagerProxy.removeUpdates(this);
		// ���ٶ�λ
		// mLocationManagerProxy.destroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route, menu);
		return true;
	}

	@Override
	public void mapViewRenderContextCreated() {
		// TODO �Զ����ɵķ������
		rtAnalysis=new RouteAnalysis();	
		rtAnalysis.setMap(mapView.getMap());
	}

	@Override
	public void mapViewRenderContextDestroyed() {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mapViewClickAnnotation(MapView arg0, Annotation arg1) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void mapViewClickAnnotationView(MapView arg0, AnnotationView arg1) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public AnnotationView mapViewViewForAnnotation(MapView arg0, Annotation arg1) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public boolean mapViewWillHideAnnotationView(MapView arg0,
			AnnotationView arg1) {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public boolean mapViewWillShowAnnotationView(MapView arg0,
			AnnotationView arg1) {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public void processMessage(Message msg) {
		// TODO �Զ����ɵķ������
		if (msg.what == Config.SEND_NOTIFICATION) {
			Bundle bundle = msg.getData();
			ChatMessage chatMessage = (ChatMessage) bundle
					.getSerializable("chatMessage");
			saveToDb(chatMessage, Config.DateBase_GET_MESSAGE);

			sendNotifycation(chatMessage.getSelf(), chatMessage.getFriend());
		} else if (msg.what == Config.SEND_NOTIFICATION_JBEX_FRIEND) {
			sendNotifycation_JBEXFriend();
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO �Զ����ɵķ������
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			dot_y = amapLocation.getLatitude();
			dot_x = amapLocation.getLongitude();
			if(flag){
			initView();
			flag=false;
			}
		}
	}

	@Override
	public boolean mapViewLongTap(PointF viewPoint) {
		// TODO Auto-generated method stub
		if (testRoute != null) {
			// ���֮ǰ��·��
			mapView.getGraphicLayer().removeAllGraphics();
		}

		if (count == 2) {
			// �Ƴ���ע
			mapView.getAnnotationLayer().removeAnnotation(1);
			//mapView.getAnnotationLayer().removeAllAnnotations();
			mapView.getGraphicLayer().removeAllGraphics();
			count = 0;
			
			
		}

		count++;

		if (count == 1) {
			// ����ͼ����ת���ɵ�ͼ����
			point[1] = mapView.viewPointToMapPoint(viewPoint);
			Annotation annotation0 = new Annotation("Annotation1", "��ע1",point[1], bmp1);
			// ��������ע��ͼ
			annotation0.setCanShowAnnotationView(false);
			mapView.getAnnotationLayer().addAnnotation(annotation0);
			mapView.refresh();
			doResearch();
			
		}

		return false;
	}

 public void doResearch() {
		Dot transPnts[] = new Dot[2];
		//��ͼ����ת��Ϊ��λ����
		//transPnts[0] = mapView.mapPointToLocation(point[0]);
		transPnts[1] = mapView.mapPointToLocation(point[1]);
		
		FromAndTo fromAndTo = new FromAndTo(point[0], transPnts[1]);

		// ��ȡ����֮����ڵ�·��
		List<Route> testRoutes = rtAnalysis.calculateRoute(fromAndTo, rtAnalysis.DrivingLeastDistance, null, null, 0);
		
		if (testRoutes.size() < 1) {
			Toast.makeText(this, "����֮�䲻������Ӧ��·��", Toast.LENGTH_SHORT).show();
			return;
		}

	
		// ȡ����һ��·��
		testRoute = testRoutes.get(0);
		// ��ȡ���
		Rect bRect = testRoute.getBoundingRect();
		// ��λ����ת���ɵ�ͼ����
		Dot blDot = mapView.locationToMapPoint(new Dot(bRect.getXMin(), bRect
				.getYMin()));
		Dot brDot = mapView.locationToMapPoint(new Dot(bRect.getXMax(), bRect
				.getYMax()));
		Rect disRect = new Rect(blDot.getX(), blDot.getY(), brDot.getX(),
				brDot.getY());
		mapView.zoomToRange(disRect, false);

		// ��ȡ·����Ŀ
		int segCount = testRoute.getStepCount();
		// ��ȡ·������
		String overViewString = testRoute.getOverview();
		// ��ȡ·������
		Dot[] coorsDots = testRoute.getCoors();
		Dot[] routeDots = new Dot[coorsDots.length];
		for (int i = 0; i < coorsDots.length; i++) {
			routeDots[i] = mapView.locationToMapPoint(coorsDots[i]);
		}

		// ������ʾ���ı�
		GraphicText routeText = new GraphicText(routeDots[1], overViewString);
		routeText.setFontSize(25);
		

		// ���û��Ƶ�·��
		GraphicPolylin routeLine = new GraphicPolylin(routeDots);
		routeLine.setColor(Color.argb(255, 0, 255,0 ));
		routeLine.setLineWidth(10);

		// ���Ƹ�·��
		mapView.getGraphicLayer().addGraphic(routeLine);
		// ���·������
		mapView.getGraphicLayer().addGraphic(routeText);

		mapView.refresh();
	}

}
