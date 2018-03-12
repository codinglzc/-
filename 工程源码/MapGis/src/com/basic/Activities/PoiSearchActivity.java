package com.basic.Activities;

import java.util.ArrayList;
import java.util.List;

import org.other.ui.CommAdapter;
import org.other.ui.CommField;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.basic.service.model.User;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.graphic.GraphicPolylin;
import com.zondy.mapgis.android.graphic.GraphicText;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.android.mapview.MapView.MapViewAnnotationListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewRenderContextListener;
import com.zondy.mapgis.geometry.Dot;
import com.zondy.mapgis.geometry.Rect;
import com.zondy.mapgis.poisearch.PoiItem;
import com.zondy.mapgis.poisearch.PoiPagedResult;
import com.zondy.mapgis.poisearch.PoiSearch;
import com.zondy.mapgis.route.Route;
import com.zondy.mapgis.route.RouteAnalysis;
import com.zondy.mapgis.route.RouteAnalysis.FromAndTo;

import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PoiSearchActivity extends ZJBEXBaseActivity  implements
MapViewAnnotationListener, MapViewRenderContextListener, AMapLocationListener{
	
	private Intent intent;
	private User owneruser=new User();
	
    private String path = Environment.getExternalStorageDirectory().getPath();
	
	private ActionBar actionBar = null;
	private MapView mapView;
    private LocationManagerProxy mLocationManagerProxy;
	private double dot_x,dot_y;
	private int count = 0;
	
	private EditText poiSearchEditText;
	private String  inputString;
	private List<CommField> searchResultList;
	private PoiPagedResult searchPoiResult;
	Dot point = new Dot();
	private RouteAnalysis rtAnalysis;
	Route testRoute;
	Dot mypoint = new Dot();
	Dot mbpoint = new Dot();
	private Bitmap mybmp ;
	private boolean flag=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_poi_search);
		
		intent=getIntent();
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("owneruser", owneruser.toString());
		}
		initLocation();
		initActionBar(); // ���ظı�actionBar
		mapView = (MapView) super.findViewById(R.id.mapview);
		mapView.loadFromFile(path + "/mapgis/map/wuhan/wuhan.xml"); // ���ص�ͼ
		mapView.setRenderContextListener(PoiSearchActivity.this); // ע���ĸ�����
	    mapView.setAnnotationListener(PoiSearchActivity.this);
	    
	    poiSearchEditText = (EditText)findViewById(R.id.EditTextSearch);
		searchResultList=new ArrayList<CommField>();
		mybmp = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.locationofmine)).getBitmap();
	}

	private void initActionBar() {
		// TODO �Զ����ɵķ������
		actionBar=getActionBar();
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

		txt.setText("�������ȵ�");

		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				PoiSearchActivity.this.finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				PoiSearchActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poi_search, menu);
		return true;
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
						LocationProviderProxy.AMapNetwork,-1, 15, this);
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

	@Override
	protected void onPause() {
		// TODO �Զ����ɵķ������
		super.onPause();
		// �Ƴ���λ����
	//	mLocationManagerProxy.removeUpdates(this);
		// ���ٶ�λ
		//mLocationManagerProxy.destroy();
	}
	
	public  void  doPoiSearchQuery(View v)
	{
		//���֮ǰ����ʷ����
		searchResultList.clear();
	
		//��ȡ��ѯ�ؼ���
		inputString = poiSearchEditText.getText().toString();
		//���ò�ѯ����
		PoiSearch.Query mQuery = new PoiSearch.Query(inputString, null, null);
		PoiSearch mPoiSearch = new PoiSearch(mQuery);
		//����Poi��ѯ����ص�ͼ
		mPoiSearch.initWithMap(mapView.getMap());
		//��ȡPoi��ѯ�Ľ��
		searchPoiResult = mPoiSearch.searchPOI();
		if(searchPoiResult!=null)
		{
			// ��Ų�ѯ���
           List<PoiItem> PoiSearchResultList = searchPoiResult.getPage(1);
			// �õ���ϸ��Ϣ
			for (int i = 0; i < PoiSearchResultList.size(); i++) {
				
				PoiItem mItem = PoiSearchResultList.get(i);	
				CommField field=new CommField(mItem.getName(),mItem.getTypeDe());
				searchResultList.add(field);
			}
		}
		else
		{	
			Toast.makeText(this, "û�в�ѯ���", Toast.LENGTH_SHORT).show();
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//�����Զ��岼��
		LinearLayout linLayout = (LinearLayout) getLayoutInflater().inflate(
				                 R.layout.common_list, null);
		ListView listView = (ListView) linLayout.findViewById(R.id.common_listview);
		TextView textTitle = (TextView) linLayout.findViewById(R.id.common_title);
		textTitle.setText("�ȵ���Ϣ");
		
		
		//����������ListAdapter
		CommAdapter adapterResult = new CommAdapter();
		adapterResult.setContext(getApplicationContext());
		adapterResult.setListField(searchResultList);
		
		listView.setAdapter(adapterResult);
		
		builder.setView(linLayout);
		final AlertDialog dlg = builder.create();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO �Զ����ɵķ������
				mbpoint=mapView.locationToMapPoint(searchPoiResult.getPage(1).get(arg2).getPoint());
				mapView.zoomToCenter(mbpoint, 5.0f, false);
				//�����  
				Annotation annotation = new Annotation("", "", mbpoint, mybmp);
				mapView.getAnnotationLayer().addAnnotation(annotation);
				mapView.refresh();
				//·���滮
				doResearch(mbpoint,mypoint);
				
				dlg.dismiss();
			}
			
		});
		
		dlg.show();
	}
	
	
	
	public void doResearch(Dot dot1,Dot dot2) {
		if(count == 1)
		{
		//���·��
		mapView.getGraphicLayer().removeAllGraphics();
		//������
		mapView.getAnnotationLayer().removeAnnotation(1);
		//mapView.getAnnotationLayer().removeAllAnnotations();
		mapView.getGraphicLayer().removeAllGraphics();
		
		}
		
		Dot transPnts[] = new Dot[2];
		//��ͼ����ת��Ϊ��λ����
		transPnts[0] = mapView.mapPointToLocation(mbpoint);
		transPnts[1] = mapView.mapPointToLocation(mypoint);
		
		FromAndTo fromAndTo = new FromAndTo(transPnts[0], transPnts[1]);

		// ��ȡ����֮����ڵ�·��
		List<Route> testRoutes = rtAnalysis.calculateRoute(fromAndTo, rtAnalysis.DrivingLeastDistance, null, null, 0);
		
		if (testRoutes.size() < 1) {
			Toast.makeText(this, "����֮�䲻������Ӧ��·��", Toast.LENGTH_SHORT).show();
			count = 1;
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
		count = 1;

		mapView.refresh();
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
			dot_y=amapLocation.getLatitude();
			dot_x=amapLocation.getLongitude();
			if(flag){
			initView();
			flag=false;
			}
		}
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		point = new Dot(dot_x, dot_y);

		mypoint = mapView.locationToMapPoint(point);
		mapView.zoomToCenter(mypoint, 4.0f, false);
		Annotation annotation = new Annotation("", "", mypoint, mybmp);
		mapView.getAnnotationLayer().addAnnotation(annotation);
		mapView.refresh();
		
	}
}
