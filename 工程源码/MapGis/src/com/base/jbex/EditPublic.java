package com.base.jbex;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.basic.Activities.MyattentionActivity;
import com.basic.Activities.R;
import com.basic.Activities.PersonInfoActivity.UpLoadAsyncTask;
import com.basic.Activities.R.layout;
import com.basic.Activities.R.menu;
import com.basic.Activities.ZJBEXBaseActivity;
import com.basic.connectservice.AttentionService;
import com.basic.connectservice.HttpUtil;
import com.basic.connectservice.PublicInfoService;
import com.basic.model.myAttentionBean;
import com.basic.service.model.PublicInfo;
import com.basic.service.model.User;
import com.basic.ui.CustomProgressDialog;
import com.basic.util.UpLoadUtil;
import com.base.jbex.AboutSchool;
import com.base.jbex.EditPublic;
import com.base.jbex.SelectDot;
import com.mapgis.model.PublicUserIfo;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.zondy.mapgis.geometry.Dot;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class EditPublic extends ZJBEXBaseActivity {
	
	private Intent intent;
	private User owneruser=new User();
	private String str_title;
	private String str_place;
	private String str_time;
	private String str_details;
	private String label="�˶�";
	
	private ActionBar actionBar = null;
	private TextView txt = null;
	private ImageView publicinfo1;
	private ImageView publicinfo2;
	private TimePicker timepicker;
	private DatePickerDialog picker;
	private TextView timeText;
	private DatePickerDialog.OnDateSetListener DatePickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            // TODO Auto-generated method stub
        }
    };
    
	private double x = 12733297.516522 , y = 3591886.880338;
	
	private CustomProgressDialog progressDialog = null;
	private String imageName="";
	 private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// ����
	 private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	 private static final int PHOTO_REQUEST_CUT = 3;// ���
	 
	 private int iYear,iMonth,iDay,iHour,iMinute;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_public);
		
		publicinfo1=(ImageView) findViewById(R.id.publicinfo1);
		publicinfo2=(ImageView) findViewById(R.id.publicinfo2);
		timepicker=(TimePicker) findViewById(R.id.timepicker);
		timeText=(TextView) findViewById(R.id.timetext);
		
		timepicker.setIs24HourView(true);
		
		intent=getIntent();
		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			owneruser = (User) data.getSerializable("owneruser");
			Log.d("owneruser", owneruser.toString());
		}
		
		initView();
		initListener();
		initActionBar();
		getdata();
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		final Calendar objTime = Calendar.getInstance();
         iYear = objTime.get(Calendar.YEAR);
         iMonth = objTime.get(Calendar.MONTH);
         iDay = objTime.get(Calendar.DAY_OF_MONTH);
         iHour=timepicker.getCurrentHour();
	     iMinute=timepicker.getCurrentMinute();
		 timeText.setText(iYear +"��"+(iMonth+1)+"��"+ iDay+"��");	
	}

	private void setPicker() {
		// TODO �Զ����ɵķ������
		picker = new DatePickerDialog(EditPublic.this, DatePickerListener,
				iYear, iMonth, iDay);
        picker.setCancelable(true);
        picker.setCanceledOnTouchOutside(true);
        picker.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Picker", "Correct behavior!");
                        iYear=picker.getDatePicker().getYear();
                        iMonth=picker.getDatePicker().getMonth();
                        iDay=picker.getDatePicker().getDayOfMonth();
                        timeText.setText(iYear +"��"+(iMonth+1)+"��"+ iDay+"��");
                    }
                });
        picker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Picker", "Cancel!");
                        picker.dismiss();
                    }
                });
        picker.show();
	}

	private void initListener() {
		// TODO �Զ����ɵķ������
		publicinfo1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				imageName=owneruser.getUser_id()+"_1.png";
				setImagePhoto();
			}
		});
		
		publicinfo2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				imageName=owneruser.getUser_id()+"_2.png";
				setImagePhoto();
			}
		});
		
		timeText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				setPicker();
			}
		});
		
		timepicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
				// TODO �Զ����ɵķ������
				iHour=arg0.getCurrentHour();
				iMinute=arg0.getCurrentMinute();
			}
		});
	}

	private void initActionBar() {
		// TODO �Զ����ɵķ������
		actionBar = getActionBar();
		actionBar.setCustomView(R.layout.session_top);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowCustomEnabled(true);
		View view = actionBar.getCustomView();
		ImageButton rightButton = (ImageButton) view.findViewById(R.id.btn_nnavigation);
		rightButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				publish();
			}
			
		});
		ImageButton back = (ImageButton) view.findViewById(R.id.btn_back);
		TextView backtxt=(TextView) view.findViewById(R.id.backtxt);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditPublic.this, SelectDot.class);
				EditPublic.this.startActivity(intent);
				EditPublic.this.finish();
			}
		});
		
		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				EditPublic.this.finish();
			}
		});
		
		txt = (TextView) view.findViewById(R.id.main_Text);
		rightButton.setBackgroundResource(R.drawable.btn_public);
		txt.setText("������Ϣ");
	}
	
	public void  getdata() {
		Intent intent = getIntent();
		double dot1 =intent.getDoubleExtra("dot1", 0);
		double dot2 =intent.getDoubleExtra("dot2", 0);
		x = dot1;
		y = dot2;
		}
	
	
	public void publish()
	{
		EditText ev_title = (EditText) super.findViewById(R.id.edit_tltle);
		//EditText ev_place = (EditText) super.findViewById(R.id.edit_place);
		//EditText ev_time = (EditText) super.findViewById(R.id.edit_time);
		EditText ev_details = (EditText) super.findViewById(R.id.edit_details);
		 str_title = ev_title.getText().toString();
	     //str_place = ev_place.getText().toString();
	     //str_time = ev_time.getText().toString();
		 str_details = ev_details.getText().toString();
		String str = "touxiang";
		String str_name = "name" , type1 = "0" , type2= "0";
		Dot dot = new Dot(x, y);
		
		addpublicinfonAsyncTask a=new addpublicinfonAsyncTask();
		a.execute();
	
//		Toast.makeText(this, str_title, Toast.LENGTH_LONG).show();
	}
    
	public void setImagePhoto(){
		 final AlertDialog dlg = new AlertDialog.Builder(this).create();
	        dlg.show();
	        Window window = dlg.getWindow();
	        // *** ��Ҫ����������ʵ������Ч����.
	        // ���ô��ڵ�����ҳ��,shrew_exit_dialog.xml�ļ��ж���view����
	        window.setContentView(R.layout.alertdialog);
	        // Ϊȷ�ϰ�ť����¼�,ִ���˳�Ӧ�ò���
	        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
	        tv_paizhao.setText("����");
	        tv_paizhao.setOnClickListener(new View.OnClickListener() {
	            @SuppressLint("SdCardPath")
	            public void onClick(View v) {

	            	
	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                // ָ������������պ���Ƭ�Ĵ���·��
	                intent.putExtra(MediaStore.EXTRA_OUTPUT,
	                        Uri.fromFile(new File("/sdcard/JBEX/Cache/publicinfoimages", imageName)));
	                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
	                dlg.cancel();
	            }
	        });
	        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
	        tv_xiangce.setText("���");
	        tv_xiangce.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	            	//imageName = owneruser.getUser_id() + ".png";
	                Intent intent = new Intent(Intent.ACTION_PICK, null);
	                intent.setDataAndType(
	                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
	                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

	                dlg.cancel();
	            }
	        });
	}
	
	
	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO �Զ����ɵķ������
		if (resultCode == RESULT_OK) {
			switch(requestCode){
			 case PHOTO_REQUEST_TAKEPHOTO:

	             startPhotoZoom(
	                     Uri.fromFile(new File("/sdcard/JBEX/Cache/publicinfoimages", imageName)),
	                     480);
	             break;
	             
			 case PHOTO_REQUEST_GALLERY:
	             if (data != null)
	                 startPhotoZoom(data.getData(), 480);
	             break;
	            
			 case PHOTO_REQUEST_CUT:
	             // BitmapFactory.Options options = new BitmapFactory.Options();
	             //
	             // /**
	             // * ��ؼ��ڴˣ���options.inJustDecodeBounds = true;
	             // * ������decodeFile()�����ص�bitmapΪ��
	             // * ������ʱ����options.outHeightʱ���Ѿ�������ͼƬ�ĸ���
	             // */
	             // options.inJustDecodeBounds = true;
	             Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/JBEX/Cache/publicinfoimages/"
	                     + imageName);
	             if(imageName.equals(owneruser.getUser_id()+"_1.png"))
	             publicinfo1.setImageBitmap(bitmap);
	             else 
	              publicinfo2.setImageBitmap(bitmap);
	             //UpLoadAsyncTask upload=new UpLoadAsyncTask();
	             //upload.execute();
	             break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
  
	private void startPhotoZoom(Uri uri1, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // cropΪtrue�������ڿ�����intent��������ʾ��view���Լ���
        intent.putExtra("crop", "true");

        // aspectX aspectY �ǿ�ߵı���
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY �Ǽ���ͼƬ�Ŀ��
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File("/sdcard/JBEX/Cache/publicinfoimages", imageName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
	
	
	@SuppressLint("SimpleDateFormat")
	public class addpublicinfonAsyncTask extends AsyncTask<Integer, Integer, String>{
	     private File file1;
	     private File file2;
		//��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@SuppressWarnings({ "deprecation", "unused" })
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				String flag;
				Date date=new Date(iYear-1900, iMonth, iDay, iHour, iMinute);
				flag=PublicInfoService.addPublicinfo(owneruser,x,y,str_title,str_details,date,label);
				
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String bir=sdf.format(date);
				Timestamp time=Timestamp.valueOf(bir);
				PublicInfo publicinfo=new PublicInfo();
				publicinfo.setId(Long.valueOf(flag));
				publicinfo.setDotX(x);
				publicinfo.setDotY(y);
				publicinfo.setLabel(label);
				publicinfo.setTitle(str_title);
				publicinfo.setDetail(str_details);
				publicinfo.setTime(time);
				publicinfo.setTUser(owneruser);
				
				String actionUrl=HttpUtil.BASE_URL+"servlet/publicinfoUpLoadServlet";
				file1=new File("/sdcard/JBEX/Cache/publicinfoimages/"+owneruser.getUser_id()+"_1.png");
			    file2=new File("/sdcard/JBEX/Cache/publicinfoimages/"+owneruser.getUser_id()+"_2.png");
				if(file1.exists())
				{
					publicinfo.setPicture1(flag+"_1.png");
					//publicinfo.setPicture2("null");
			        UpLoadUtil.uploadFile("/sdcard/JBEX/Cache/publicinfoimages/"
		                 +  owneruser.getUser_id()+"_1.png", flag+"_1.png",actionUrl); 
			    PublicInfoService.setPublicInfo(publicinfo);
				}
				else{
					publicinfo.setPicture1("null");
				}
				 if(file2.exists()){
					 publicinfo.setPicture2(flag+"_2.png");
					// publicinfo.setPicture1("null");
					UpLoadUtil.uploadFile("/sdcard/JBEX/Cache/publicinfoimages/"
			                 + owneruser.getUser_id()+"_2.png", flag+"_2.png",actionUrl);
					PublicInfoService.setPublicInfo(publicinfo);
				 }
				 else{
					 publicinfo.setPicture2("null");
				 }
				 
				 PublicInfoService.setPublicInfo(publicinfo);
				return "true";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				String text="";
				if(result.endsWith("true")){
					text="��ϲ�㣡�����ɹ�";
				}
				else 
					text="ɾ��ʧ��";
				
				if(file1.exists())
				 file1.delete();
				if(file2.exists())
				 file2.delete();
				 
				Toast.makeText(EditPublic.this, text, Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(EditPublic.this, AboutSchool.class);
				Bundle data = new Bundle();
				data.putSerializable("owneruser", owneruser);
				intent.putExtras(data);
				EditPublic.this.startActivityForResult(intent, 222);
				EditPublic.this.finish();
				
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
					progressDialog = CustomProgressDialog.createDialog(EditPublic.this);
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
