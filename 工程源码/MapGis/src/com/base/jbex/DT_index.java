package com.base.jbex;

import java.util.ArrayList;
import java.util.List;

import com.base.jbex.JbexActivity.setAttentionDataAsyncTask;
import com.base.jbex.JbexActivity.setDataAsyncTask;
import com.base.jbex.JbexActivity.setFriendDataAsyncTask;
import com.basic.Activities.R;
import com.basic.Activities.ZJBEXBaseActivity;
import com.basic.ImageLoad.AnimateFirstDisplayListener;
import com.basic.ImageLoad.ImageOptions;
import com.basic.ImageLoad.ImageStringUtil;
import com.basic.connectservice.JbexInfoService;
import com.basic.connectservice.PublicInfoService;
import com.basic.connectservice.dynamicInfoService;
import com.basic.service.model.DynamicInfo;
import com.basic.service.model.JbexInfo;
import com.basic.service.model.PublicInfo;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.mapgis.model.DynamciInfoAnnotation;
import com.mapgis.model.JbexInfoAnnotation;
import com.mapgis.model.PublicInfoAnnotation;
import com.mapgis.model.PublicUserIfo;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zondy.mapgis.android.annotation.Annotation;
import com.zondy.mapgis.android.annotation.AnnotationLayer;
import com.zondy.mapgis.android.annotation.AnnotationView;
import com.zondy.mapgis.android.mapview.MapView;
import com.zondy.mapgis.android.mapview.MapView.MapViewAnnotationListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewLongTapListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewRenderContextListener;
import com.zondy.mapgis.android.mapview.MapView.MapViewTapListener;
import com.zondy.mapgis.geometry.Dot;

import android.os.AsyncTask;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DT_index extends ZJBEXBaseActivity implements MapViewAnnotationListener,MapViewLongTapListener, MapViewTapListener,MapViewRenderContextListener{
    
	private static int mdynamicuserifoID=0;
	private static int mdynamicuserifo_friend=1;
	private static int mdynamicuserifo_attention=2;
	
	private Intent intent;
	private User owneruser=new User();
	
	private ActionBar actionBar = null;
	private MapView mapView;
	private View mContents;

	AnnotationView annotationView = null;
	Annotation annotationRecord = null;
	
	  private List<DynamicInfo> mdynamicinfoList = new ArrayList<DynamicInfo>();
	   private List<DynamicInfo> mdynamicinfoList_friend = new ArrayList<DynamicInfo>();
	   private List<DynamicInfo> mdynamicinfoList_attention = new ArrayList<DynamicInfo>();
	private CustomProgressDialog progressDialog = null;
	
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	private String path = Environment.getExternalStorageDirectory().getPath();

	TextView txt = null; // actionbar���м����
	
	// ------------����---------------
	private PopupWindow popupWindow;
	private ListView lv_group;
	private View view;
	private List<String> groups;
	// ------------����---------------
	
	private Bitmap DynamicBitmap;             //��̬��ǩ
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dt_index);
		
		intent=getIntent();
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("owneruser", owneruser.toString());
		}
		
		initDisplayOption();
		initActionBar(); // ���ظı�actionBar
		initAnnotation();
		
		mapView = (MapView) super.findViewById(R.id.mapview5);
		mapView.loadFromFile(path + "/mapgis/map/wuhan/wuhan.xml"); // ���ص�ͼ
		mapView.setRenderContextListener(DT_index.this); // ע���ĸ�����
		mapView.setAnnotationListener(DT_index.this);
		mapView.setLongTapListener(DT_index.this);
		mapView.setTapListener(DT_index.this);
		
		setDataAsyncTask a=new setDataAsyncTask();
		a.execute();
		
	}

	private void initAnnotation() {
		// TODO Auto-generated method stub
		DynamicBitmap=((BitmapDrawable) getResources().getDrawable(R.drawable.dt_annotation)).getBitmap();
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
	}
	
	/**
	 * ���������˵����˵�item����Ӧ�¼�
	 */
	private void showWindow(View parent) {

		if (popupWindow == null) {
			/**
			 * ȡ��Xml�е�view
			 */
//			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.group_list, null);        //�����Զ�����ʽ
			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			groups = new ArrayList<String>();
			groups.add("������̬");
			groups.add("�ҵĺ���");
			groups.add("�ҵĹ�ע");

			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, 300, 350); // ����һ��PopuWidow����
		}
		popupWindow.setFocusable(true); // ʹ��ۼ�
				popupWindow.setOutsideTouchable(true); // ����������������ʧ
				popupWindow.setBackgroundDrawable(new BitmapDrawable()); // �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���

		WindowManager windowManager1 = getWindowManager();
		int xPos = windowManager1.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);
		popupWindow.showAsDropDown(parent, xPos, 5);

		mapView.getAnnotationLayer().removeAllAnnotations();
		mapView.refresh();

		// �����˵�item����¼�����======================================
		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				//				String str = String.valueOf(id);
				int id_1 = (int) id;
				switch (id_1) {
				case 0:
					txt.setText("������̬");
					if(mdynamicinfoList.size()==0)
					{
						setDataAsyncTask a=new setDataAsyncTask();
						a.execute();
					}
					else
					drawAnnotations(0);	
					break;

				case 1:
					txt.setText("�ҵĺ���");
					if(mdynamicinfoList_friend.size()==0)
					{
						setFriendDataAsyncTask a=new setFriendDataAsyncTask();
						a.execute();
					}
					else
						drawAnnotations(1);	
					break;

				case 2:
					txt.setText("�ҵĹ�ע");
					if(mdynamicinfoList_attention.size()==0)
					{
						setAttentionDataAsyncTask a=new setAttentionDataAsyncTask();
						a.execute();
					}
					else
						drawAnnotations(2);	
					break;

				default:
					Toast.makeText(DT_index.this, "δ֪����", Toast.LENGTH_LONG).show();
					break;
				}
				mapView.refresh();
				if (popupWindow != null)
					popupWindow.dismiss();
			}
		});
	}

	/**
	 *  ���ظı�actionBar
	 */
	private void initActionBar() {
		// TODO �Զ����ɵķ������
		actionBar = getActionBar();
		actionBar.setCustomView(R.layout.session_top);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		View view = actionBar.getCustomView();
		//ImageButton rightButton = (ImageButton) view.findViewById(R.id.btn_topright);
		ImageButton rightButton = (ImageButton) view.findViewById(R.id.btn_nnavigation);
		ImageButton back = (ImageButton) view.findViewById(R.id.btn_back);
		txt = (TextView) view.findViewById(R.id.main_Text);
		TextView backtxt=(TextView) view.findViewById(R.id.backtxt);
		
		rightButton.setBackgroundResource(R.drawable.publish);
		
		ImageButton ibtn = (ImageButton) view.findViewById(R.id.btn_nnavigation);
		ibtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//------------------------------
				Intent intent = new Intent(DT_index.this, DT_seletedot.class);
				
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				intent.putExtras(data);
				
				DT_index.this.startActivity(intent);
				DT_index.this.finish();
				Toast.makeText(DT_index.this, "���������һ��ҳ��" , Toast.LENGTH_LONG).show();
			}
		});
		txt.setText("������̬");
		txt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View view1 = findViewById(R.id.title_tt);
				showWindow(view1);                                    // popuWindow �����˵���ʾ
			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				DT_index.this.finish();
			}
		});
      
		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				DT_index.this.finish();
			}
		});
	}

	
	
	/**
	 * ���ƺ���
	 * @param type
	 */
	public void drawAnnotations(int  type) {
      mapView.getAnnotationLayer().removeAllAnnotations();
		
		String JBtitle, JBusername, JBtime;
		Dot JBposition = null ;
		String JBuserPicture;

		//List<JbexInfoAnnotation> annotationList=new ArrayList<JbexInfoAnnotation>();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
			switch (type) {
			case 0:
				for(int i=0;i<mdynamicinfoList.size();i++){
					JBtitle=mdynamicinfoList.get(i).getDetail();
					JBusername=mdynamicinfoList.get(i).getTUser().getUser_nickname();
					JBtime=sdf.format(mdynamicinfoList.get(i).getTime());
					JBuserPicture=mdynamicinfoList.get(i).getTUser().getPicture();
					JBposition=mapView.locationToMapPoint(new Dot(mdynamicinfoList.get(i).getDotX(), mdynamicinfoList.get(i).getDotY()));
					
					DynamciInfoAnnotation annotation=new DynamciInfoAnnotation(JBusername, JBtitle, JBtime,JBposition, DynamicBitmap);
					annotation.setJBuserPicture(JBuserPicture);
					annotation.setMdynamciuserifoID(mdynamicuserifoID);
					annotation.setWhich(i);
					
					mapView.getAnnotationLayer().addAnnotation(annotation);
				}
				break;
				
            case 1:
            	for(int i=0;i<mdynamicinfoList_friend.size();i++){
					JBtitle=mdynamicinfoList_friend.get(i).getDetail();
					JBusername=mdynamicinfoList_friend.get(i).getTUser().getUser_nickname();
					JBtime=sdf.format(mdynamicinfoList_friend.get(i).getTime());
					JBuserPicture=mdynamicinfoList_friend.get(i).getTUser().getPicture();
					JBposition=mapView.locationToMapPoint(new Dot(mdynamicinfoList_friend.get(i).getDotX(), mdynamicinfoList_friend.get(i).getDotY()));
					
					DynamciInfoAnnotation annotation=new DynamciInfoAnnotation(JBusername, JBtitle, JBtime,JBposition, DynamicBitmap);
					annotation.setJBuserPicture(JBuserPicture);
					annotation.setMdynamciuserifoID(mdynamicuserifo_friend);
					annotation.setWhich(i);
					
					mapView.getAnnotationLayer().addAnnotation(annotation);
				}
				break;
           case 2:
        	   for(int i=0;i<mdynamicinfoList_attention.size();i++){
					JBtitle=mdynamicinfoList_attention.get(i).getDetail();
					JBusername=mdynamicinfoList_attention.get(i).getTUser().getUser_nickname();
					JBtime=sdf.format(mdynamicinfoList_attention.get(i).getTime());
					JBuserPicture=mdynamicinfoList_attention.get(i).getTUser().getPicture();
					JBposition=mapView.locationToMapPoint(new Dot(mdynamicinfoList_attention.get(i).getDotX(), mdynamicinfoList_attention.get(i).getDotY()));
					
					DynamciInfoAnnotation annotation=new DynamciInfoAnnotation(JBusername, JBtitle, JBtime,JBposition, DynamicBitmap);
					annotation.setJBuserPicture(JBuserPicture);
					annotation.setMdynamciuserifoID(mdynamicuserifo_attention);
					annotation.setWhich(i);
					
					mapView.getAnnotationLayer().addAnnotation(annotation);
				}
				break;
			default:
				break;
			} 
			
			mapView.refresh();
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
	public void mapViewTap(PointF arg0) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public boolean mapViewLongTap(PointF arg0) {
		// TODO �Զ����ɵķ������
		return false;
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
	public AnnotationView mapViewViewForAnnotation(MapView mapView,
			Annotation annotation) {
		
		DynamciInfoAnnotation dt_annotations=(DynamciInfoAnnotation)annotation;
		
		annotationView = new AnnotationView(annotation, this);
		mContents = getLayoutInflater().inflate(R.layout.dt_annotationview, null);
		annotationView.setCalloutContentView(mContents);
		
		final int Id=dt_annotations.getMdynamciuserifoID();
		final int which=dt_annotations.getWhich();
		
		Button detail_button=(Button) mContents.findViewById(R.id.btn_dt_detail);
        ImageView userimage=(ImageView) mContents.findViewById(R.id.dt_userimage);
        TextView  username=(TextView) mContents.findViewById(R.id.dt_ann_username);
        TextView  details=(TextView) mContents.findViewById(R.id.dt_details);
        
        mImageLoader.displayImage(ImageStringUtil.getImageURL(dt_annotations.getJBuserPicture()), userimage, options, animateFirstListener);
        username.setText(annotation.getUid());
        details.setText(annotation.getTitle());
        
		detail_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(DT_index.this, DT_details.class);
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				switch (Id) {
				case 0:
					data.putSerializable("dynamicinfo", mdynamicinfoList.get(which));
					break;

				case 1:
					data.putSerializable("dynamicinfo", mdynamicinfoList_friend.get(which));
					break;
				case 2:
					data.putSerializable("dynamicinfo", mdynamicinfoList_attention.get(which));
					break;
				default:
					break;
				}
				intent.putExtras(data);
				DT_index.this.startActivity(intent);
			}
		});

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

	public class setDataAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				
				mdynamicinfoList=dynamicInfoService.getdynamicInfoList();
				return "success";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
				drawAnnotations(0);
				
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
					progressDialog = CustomProgressDialog.createDialog(DT_index.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
	
	public class setFriendDataAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				mdynamicinfoList_friend=dynamicInfoService.getFriendynamicInfoList(String.valueOf(owneruser.getUser_id()));
				return "success";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
				drawAnnotations(1);
				
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
					progressDialog = CustomProgressDialog.createDialog(DT_index.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
	
	public class setAttentionDataAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				mdynamicinfoList_attention=dynamicInfoService.getAttentionynamicInfoList(String.valueOf(owneruser.getUser_id()));
				return "success";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
				drawAnnotations(2);
				
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
					progressDialog = CustomProgressDialog.createDialog(DT_index.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
	
	@Override
	public void processMessage(Message msg) {
		// TODO Auto-generated method stub
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
