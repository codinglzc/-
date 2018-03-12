package com.base.jbex;
import java.util.ArrayList;
import java.util.List;

import com.basic.Activities.R;
import com.basic.Activities.ZJBEXBaseActivity;
import com.basic.Activities.R.drawable;
import com.basic.Activities.R.id;
import com.basic.Activities.R.layout;
import com.basic.Activities.R.menu;
import com.basic.ImageLoad.AnimateFirstDisplayListener;
import com.basic.ImageLoad.ImageOptions;
import com.basic.ImageLoad.ImageStringUtil;
import com.basic.connectservice.MyJbexRequestService;
import com.basic.service.model.JbexInfo;
import com.basic.service.model.MyJbexRequest;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.mapgis.model.JbexInfoAnnotation;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationLayer;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.graphic.GraphicCircle;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.android.mapview.MapView.MapViewAnnotationListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewRenderContextListener;
import com.zondy.mapgis.geometry.Dot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FootRoutActivity extends ZJBEXBaseActivity implements
MapViewAnnotationListener, MapViewRenderContextListener{
	private static int mjbexuserifoID=0;
	private User owneruser=new User();
	private Intent intent;
	private ActionBar actionBar ;
	
	private MapView mapView;
	private String path = Environment.getExternalStorageDirectory().getPath();
	private List<MyJbexRequest> mjbexinfoList = new ArrayList<MyJbexRequest>();
	
	private CustomProgressDialog progressDialog = null;
	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
	
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	private Bitmap studyBmp;             //ѧϰ��ǩ
	private Bitmap eatBmp;                //�Է���ǩ
	private Bitmap KTVBmp;                //KTV��ǩ
	private Bitmap walkBmp;                //ɢ����ǩ
	private Bitmap basketballBmp;           // �����ǩ
	private Bitmap RunBmp;                  //�ܲ���ǩ
	private Bitmap footballBmp;              //�����ǩ
	private Bitmap sportsBmp;               //�˶���ǩ
	private Bitmap bodyBmp;                 //�����ǩ
	private Bitmap milkBmp;                 //�̲��ǩ
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foot_rout);
	
		intent=getIntent();
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("owneruser", owneruser.toString());
		}
		
	   initDisplayOption();
       initactionBar();
       initView();
       initBitMap();
       
       mapView = (MapView) super.findViewById(R.id.mapview);
	   mapView.loadFromFile(path + "/mapgis/map/wuhan/wuhan.xml"); // ���ص�ͼ
	   mapView.setRenderContextListener(FootRoutActivity.this); // ע���ĸ�����
	   mapView.setAnnotationListener(FootRoutActivity.this);
 
       setDataAsyncTask a=new setDataAsyncTask();
	   a.execute();
	   
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
	}

	private void initBitMap() {
		// TODO �Զ����ɵķ������
		studyBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.annotation_study)).getBitmap();
		 eatBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.eat_annotation)).getBitmap();
		 KTVBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.ktv_annotation)).getBitmap();
		 walkBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.run_annation)).getBitmap();
		 basketballBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.basketball_annotation)).getBitmap();
		 RunBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.run_annation)).getBitmap();
		 footballBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.football_annotation)).getBitmap();
		 sportsBmp= ((BitmapDrawable) getResources().getDrawable(R.drawable.run_annation)).getBitmap();
		 bodyBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.body_annotation)).getBitmap();
		 milkBmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.milk_annotation)).getBitmap();
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		
	}

	private void initactionBar() {
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

		txt.setText("�ҵ��㼣");

		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				FootRoutActivity.this.finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				FootRoutActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.foot_rout, menu);
		return true;
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
	public void mapViewRenderContextCreated() {
		// TODO �Զ����ɵķ������
		mapView.setShowUserLocation(true);
		mapView.zoomTo(4.0f, false);
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
	public AnnotationView mapViewViewForAnnotation(MapView arg0, Annotation annotation) {
		// TODO �Զ����ɵķ������
	     View mContents;
		AnnotationView annotationView = null;
		JbexInfoAnnotation jbexannotation=(JbexInfoAnnotation) annotation;
		final int Id=jbexannotation.getMpoublicuserifoID();
		final int which=jbexannotation.getWhich();
		
		annotationView = new AnnotationView(annotation, this);
		// �����Զ���callout����ʽ
		mContents = getLayoutInflater().inflate(
				R.layout.jbexcontentview, null);
		annotationView.setCalloutContentView(mContents);

		String strTitle = " ���⣺ " + annotation.getTitle(); // ��ȡ��ǩtitle
		TextView title = ((TextView) mContents.findViewById(R.id.title1));
		title.setText(strTitle);

		String strDesciption = " ʱ�䣺 " + annotation.getDescription(); // ��ȡ��ǩ�ĵ������������ｫ����Ϊ���ʱ��
		TextView snippet = ((TextView) mContents.findViewById(R.id.snippet1));
		snippet.setText(strDesciption);

		String struid = annotation.getUid();
		TextView JBusername = (TextView) mContents.findViewById(R.id.public_username); // ��ȡ��ǩ�ĵ�UID�������ｫ����Ϊ���username
		JBusername.setText(struid);

		ImageView userimage=(ImageView) mContents.findViewById(R.id.icn);
		mImageLoader.displayImage(ImageStringUtil.getImageURL(jbexannotation.getJBuserPicture()), userimage, options, animateFirstListener);
		
		Button detail_button=(Button) mContents.findViewById(R.id.btn_AS1_detail);
		
		detail_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(FootRoutActivity.this, JbexDetailsActivity.class);
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				switch (Id) {
				case 0:
					data.putSerializable("jbexuserifo", mjbexinfoList.get(which).getJbexInfo());
					break;
				}
				intent.putExtras(data);
				FootRoutActivity.this.startActivity(intent);
			}
		});
		
		AnnotationLayer annotationLayer = mapView.getAnnotationLayer();
		int index = annotationLayer.indexOf(annotation);
		annotationLayer.moveAnnotation(index, -1);
		mapView.refresh();
		// ��annotationviewƽ�Ƶ���ͼ����
		annotationView.setPanToMapViewCenter(true);
		return annotationView;
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
	
	public void draw_cir(Dot dot,int size)
	{
		int level[]=new int[10];
		level[0]=Color.parseColor("#fcf161");
		level[1]=Color.parseColor("#f0dc70");
		level[2]=Color.parseColor("#cbc547");
		level[3]=Color.parseColor("#ffe600");
		level[4]=Color.parseColor("#ffd400");
		level[5]=Color.parseColor("#f8aba6");
		level[6]=Color.parseColor("#f69c9f");
		level[7]=Color.parseColor("#f58f98");
		level[8]=Color.parseColor("#f391a9");
		level[9]=Color.parseColor("#d71345");
		
		 int num=size/10;
	      double radius = (num+1) * 50;
	      GraphicCircle graphicCircle = new GraphicCircle(dot,radius);
	      if(num>=10)
	    	  graphicCircle.setColor(level[9]);
	      else
	    	  graphicCircle.setColor(level[num]);
	      graphicCircle.setBorderlineWidth(5);		
	      mapView.getGraphicLayer().addGraphic(graphicCircle);
		}
	
	
	 private void drawAnnotations() {
			// TODO �Զ����ɵķ������
		 String JBtitle, JBusername, JBtime;
		 Dot JBposition = null ;
		 String JBuserPicture;
		 Bitmap bmp;
        for(int i=0;i<mjbexinfoList.size();i++){
        	JbexInfo jbexinfo=mjbexinfoList.get(i).getJbexInfo();
        	JBtitle=jbexinfo.getTitle();
			JBusername=jbexinfo.getTUser().getUser_nickname();
			JBtime=sdf.format(jbexinfo.getTime());
			JBuserPicture=jbexinfo.getTUser().getPicture();
			bmp=selectAnnotationByLabel(jbexinfo.getLabel());
			JBposition=mapView.locationToMapPoint(new Dot(jbexinfo.getDotX(), jbexinfo.getDotY()));
			
			JbexInfoAnnotation annotation=new JbexInfoAnnotation(JBusername, JBtitle, JBtime,JBposition, bmp);
			annotation.setJBuserPicture(JBuserPicture);
			annotation.setMpoublicuserifoID(mjbexuserifoID);
			annotation.setWhich(i);
			
			draw_cir(JBposition,jbexinfo.getSize());
			mapView.getAnnotationLayer().addAnnotation(annotation);
			
			//Toast.makeText(FootRoutActivity.this, mjbexinfoList.get(i).getJbexInfo().getSize(), Toast.LENGTH_SHORT).show();
        }
		}
	
	 private Bitmap selectAnnotationByLabel(String label) {
			// TODO �Զ����ɵķ������
			if(label.equals("ѧϰ"))
			return studyBmp;
			else if(label.equals("�Է�"))
			return eatBmp;
			else if(label.equals("KTV"))
			return KTVBmp;
			else if(label.equals("ɢ��"))
				return walkBmp;
			else if(label.equals("����"))
				return basketballBmp;
			else if(label.equals("�ܲ�"))
				return RunBmp;
			else if(label.equals("����"))
				return footballBmp;
			else if(label.equals("�˶�"))
				return sportsBmp;
			else if(label.equals("����"))
				return bodyBmp;
			else if(label.equals("�̲�"))
				return milkBmp;
			else return null;
		}


	public class setDataAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				mjbexinfoList=MyJbexRequestService.getMyJbexRequestList(owneruser.getEmail());
				return "success";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
				drawAnnotations();
				
				if (progressDialog != null){
					progressDialog.dismiss();
					progressDialog = null;
				}	
			
			}
			
		//�����������û�����Excuteʱ�Ľӿڣ�������ִ��֮ǰ��ʼ���ô˷�����������������ʾ���ȶԻ���
			@Override
			protected void onPreExecute() {
				// TODO �Զ����ɵķ������
				super.onPreExecute();
				
				if (progressDialog == null){
					progressDialog = CustomProgressDialog.createDialog(FootRoutActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}

}
