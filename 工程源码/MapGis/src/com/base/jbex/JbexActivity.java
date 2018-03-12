package com.base.jbex;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.base.jbex.AboutSchool.setDataAsyncTask;
import com.basic.Activities.R;
import com.basic.Activities.R.drawable;
import com.basic.Activities.R.id;
import com.basic.Activities.R.layout;
import com.basic.Activities.ZJBEXBaseActivity;
import com.basic.ImageLoad.AnimateFirstDisplayListener;
import com.basic.ImageLoad.ImageOptions;
import com.basic.ImageLoad.ImageStringUtil;
import com.basic.connectservice.JbexInfoService;
import com.basic.connectservice.PublicInfoService;
import com.basic.service.model.JbexInfo;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
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
import com.zondy.mapgis.android.graphic.GraphicCircle;
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
import android.graphics.Color;
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

public class JbexActivity extends ZJBEXBaseActivity implements MapViewAnnotationListener,MapViewLongTapListener, MapViewTapListener,MapViewRenderContextListener{
    
	private static int mjbexuserifoID=0;
	private static int mjbexuserifoID_friend=1;
	private static int mjbexuserifoID_attention=2;
	
	private Intent intent;
	private User owneruser=new User();
	
    private CustomProgressDialog progressDialog = null;
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	private String path = Environment.getExternalStorageDirectory().getPath();
	
	private ActionBar actionBar = null;
	private MapView mapView;
	private View mContents;
	
	TextView main_txt = null; // actionbar���м����
	
	// ------------����---------------
		private PopupWindow popupWindow;
		private ListView lv_group;
		private View view;
		private List<String> groups;
		// ------------����---------------
		
    private ArrayList<JbexInfo> mjbexinfoList = new ArrayList<JbexInfo>();
    private ArrayList<JbexInfo> mjbexinfoList_friend = new ArrayList<JbexInfo>();
    private ArrayList<JbexInfo> mjbexinfoList_attention = new ArrayList<JbexInfo>();
    
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
    
    private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jbex);
		
		intent=getIntent();
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("owneruser", owneruser.toString());
		}
		
		
		initDisplayOption();
		initActionBar(); // ���ظı�actionBar
		initAnnotation();
		
		mapView = (MapView) super.findViewById(R.id.mapview);
		mapView.loadFromFile(path + "/mapgis/map/wuhan/wuhan.xml"); // ���ص�ͼ
		mapView.setRenderContextListener(JbexActivity.this); // ע���ĸ�����
		mapView.setAnnotationListener(JbexActivity.this);
		mapView.setLongTapListener(JbexActivity.this);
		mapView.setTapListener(JbexActivity.this);
		
		setDataAsyncTask a=new setDataAsyncTask();
		a.execute();
		
		mapView.refresh();
	}
   
	 private void initAnnotation() {
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
		
	 
	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
	}

	@SuppressWarnings("null")
	private void drawAnnotations(int ii) {
			// TODO �Զ����ɵķ������
		
		mapView.getAnnotationLayer().removeAllAnnotations();
		mapView.getGraphicLayer().removeAllGraphics();
		
		String JBtitle, JBusername, JBtime;
		Dot JBposition = null ;
		String JBuserPicture;
		Bitmap bmp;
		//List<JbexInfoAnnotation> annotationList=new ArrayList<JbexInfoAnnotation>();
			switch (ii) {
			case 0:
				for(int i=0;i<mjbexinfoList.size();i++){
					JBtitle=mjbexinfoList.get(i).getTitle();
					JBusername=mjbexinfoList.get(i).getTUser().getUser_nickname();
					JBtime=sdf.format(mjbexinfoList.get(i).getTime());
					JBuserPicture=mjbexinfoList.get(i).getTUser().getPicture();
					bmp=selectAnnotationByLabel(mjbexinfoList.get(i).getLabel());
					JBposition=mapView.locationToMapPoint(new Dot(mjbexinfoList.get(i).getDotX(), mjbexinfoList.get(i).getDotY()));
					
					JbexInfoAnnotation annotation=new JbexInfoAnnotation(JBusername, JBtitle, JBtime,JBposition, bmp);
					annotation.setJBuserPicture(JBuserPicture);
					annotation.setMpoublicuserifoID(mjbexuserifoID);
					annotation.setWhich(i);
					
					draw_cir(JBposition,mjbexinfoList.get(i).getSize());
					mapView.getAnnotationLayer().addAnnotation(annotation);
				}
				break;
				
            case 1:
            	for(int i=0;i<mjbexinfoList_friend.size();i++){
					JBtitle=mjbexinfoList_friend.get(i).getTitle();
					JBusername=mjbexinfoList_friend.get(i).getTUser().getUser_nickname();
					JBtime=sdf.format(mjbexinfoList_friend.get(i).getTime());
					JBuserPicture=mjbexinfoList_friend.get(i).getTUser().getPicture();
					bmp=selectAnnotationByLabel(mjbexinfoList_friend.get(i).getLabel());
					JBposition=mapView.locationToMapPoint(new Dot(mjbexinfoList_friend.get(i).getDotX(), mjbexinfoList_friend.get(i).getDotY()));
					
					JbexInfoAnnotation annotation=new JbexInfoAnnotation(JBusername, JBtitle, JBtime,JBposition, bmp);
					annotation.setJBuserPicture(JBuserPicture);
					annotation.setMpoublicuserifoID(mjbexuserifoID_friend);
					annotation.setWhich(i);
					
					draw_cir(JBposition,mjbexinfoList_friend.get(i).getSize());
					mapView.getAnnotationLayer().addAnnotation(annotation);
				}
				break;
				
           case 2:
        	   for(int i=0;i<mjbexinfoList_attention.size();i++){
					JBtitle=mjbexinfoList_attention.get(i).getTitle();
					JBusername=mjbexinfoList_attention.get(i).getTUser().getUser_nickname();
					JBtime=sdf.format(mjbexinfoList_attention.get(i).getTime());
					JBuserPicture=mjbexinfoList_attention.get(i).getTUser().getPicture();
					bmp=selectAnnotationByLabel(mjbexinfoList_attention.get(i).getLabel());
					JBposition=mapView.locationToMapPoint(new Dot(mjbexinfoList_attention.get(i).getDotX(), mjbexinfoList_attention.get(i).getDotY()));
					
					JbexInfoAnnotation annotation=new JbexInfoAnnotation(JBusername, JBtitle, JBtime,JBposition, bmp);
					annotation.setJBuserPicture(JBuserPicture);
					annotation.setMpoublicuserifoID(mjbexuserifoID_attention);
					annotation.setWhich(i);
					
					draw_cir(JBposition,mjbexinfoList_attention.get(i).getSize());
					mapView.getAnnotationLayer().addAnnotation(annotation);
				}
  				break;
  				
			default:
				break;
			} 
			
			mapView.refresh();
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

	private void initData() {
			// TODO �Զ����ɵķ������
			
		}
	
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
		main_txt = (TextView) view.findViewById(R.id.main_Text);
		TextView backtxt=(TextView) view.findViewById(R.id.backtxt);
		
		rightButton.setBackgroundResource(R.drawable.publish);
		
		ImageButton ibtn = (ImageButton) view.findViewById(R.id.btn_nnavigation);
		ibtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JbexActivity.this, JbexSelectDot.class);
				
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				intent.putExtras(data);
				
				JbexActivity.this.startActivity(intent);
				JbexActivity.this.finish();
				//Toast.makeText(AboutSchool.this, "���������һ��ҳ��" , Toast.LENGTH_LONG).show();
			}
		});
		main_txt.setText("������");
		main_txt.setOnClickListener(new OnClickListener() {
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
				JbexActivity.this.finish();
			}
		});
      
		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				JbexActivity.this.finish();
			}
		});
		}
	
	protected void showWindow(View parent) {
		// TODO �Զ����ɵķ������
		if (popupWindow == null) {
			/**
			 * ȡ��Xml�е�view
			 */
//			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			view = getLayoutInflater().inflate(R.layout.group_list, null);        //�����Զ�����ʽ
			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			groups = new ArrayList<String>();
			groups.add("�������"); 
			groups.add("�ҵĺ���");
			groups.add("�ҵĹ�ע");

			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, 300, 350); // ����һ��PopuWidow����
			
			// �����˵�item����¼�����======================================
			lv_group.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int position, long id) {
					//				String str = String.valueOf(id);
					int id_1 = (int) id;
					switch (id_1) {
					case 0:
						main_txt.setText("�������");
						if(mjbexinfoList.size()==0)
						{
							setDataAsyncTask a=new setDataAsyncTask();
							a.execute();
						}
						else
						drawAnnotations(0);	
						break;

					case 1:
						main_txt.setText("�ҵĺ���");
						if(mjbexinfoList_friend.size()==0)
						{
							setFriendDataAsyncTask a=new setFriendDataAsyncTask();
							a.execute();
						}
						else
						drawAnnotations(1);
						break;

					case 2:
						main_txt.setText("�ҵĹ�ע");
						if(mjbexinfoList_attention.size()==0)
						{
							setAttentionDataAsyncTask a=new setAttentionDataAsyncTask();
							a.execute();
						}
						else
						drawAnnotations(2);	
						break;

					default:
						Toast.makeText(JbexActivity.this, "δ֪����", Toast.LENGTH_LONG).show();
						break;
					}
					mapView.refresh();
					if (popupWindow != null)
						popupWindow.dismiss();
				}
			});
			
		}
		popupWindow.setFocusable(true); // ʹ��ۼ�
				popupWindow.setOutsideTouchable(true); // ����������������ʧ
				popupWindow.setBackgroundDrawable(new BitmapDrawable()); // �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���

		WindowManager windowManager1 = getWindowManager();
		int xPos = windowManager1.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);
		popupWindow.showAsDropDown(parent, xPos, 5);

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
		AnnotationView annotationView = null;
		// TODO Auto-generated method stub
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
				Intent intent = new Intent(JbexActivity.this, JbexDetailsActivity.class);
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				switch (Id) {
				case 0:
					data.putSerializable("jbexuserifo", mjbexinfoList.get(which));
					break;

				case 1:
					data.putSerializable("jbexuserifo", mjbexinfoList_friend.get(which));
					break;
				case 2:
					data.putSerializable("jbexuserifo", mjbexinfoList_attention.get(which));
					break;
				default:
					break;
				}
				intent.putExtras(data);
				JbexActivity.this.startActivity(intent);
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
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO �Զ����ɵķ������
		switch (requestCode) {
		case 222:
			setDataAsyncTask a=new setDataAsyncTask();
			a.execute();
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public class setDataAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				mjbexinfoList=(ArrayList<JbexInfo>) JbexInfoService.getJbexInfoList(sdf.format(new Date()));
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
					progressDialog = CustomProgressDialog.createDialog(JbexActivity.this);
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
				mjbexinfoList_friend=(ArrayList<JbexInfo>) JbexInfoService.getFrienJbexInfoListByUserid(String.valueOf(owneruser.getUser_id()),sdf.format(new Date()));
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
					progressDialog = CustomProgressDialog.createDialog(JbexActivity.this);
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
				mjbexinfoList_attention=(ArrayList<JbexInfo>) JbexInfoService.getAttentionjbexInfoListByUserid(String.valueOf(owneruser.getUser_id()),sdf.format(new Date()));
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
					progressDialog = CustomProgressDialog.createDialog(JbexActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
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
