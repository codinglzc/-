package com.basic.Activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.other.actionsheet.Method.Action1;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.ImageLoad.AnimateFirstDisplayListener;
import com.basic.ImageLoad.ImageOptions;
import com.basic.ImageLoad.ImageStringUtil;
import com.basic.adapter.MenuItemAdapter;
import com.basic.connectservice.HttpUtil;
import com.basic.connectservice.SettingUserInfo;
import com.basic.model.menuItemBean;
import com.basic.service.model.User;
import com.basic.ui.ActionSheet;
import com.basic.ui.CustomProgressDialog;
import com.basic.ui.OptionsPopupWindow;
import com.basic.ui.OptionsPopupWindow.OnOptionsSelectListener;
import com.basic.ui.TimePopupWindow;
import com.basic.ui.TimePopupWindow.OnTimeSelectListener;
import com.basic.ui.TimePopupWindow.Type;
import com.basic.util.ListViewHeightBaseOnChildren;
import com.basic.util.StringUtils;
import com.basic.util.UpLoadUtil;
import com.message.net.ChatMessage;
import com.message.net.Config;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

@SuppressLint({ "ResourceAsColor", "SimpleDateFormat", "SdCardPath" })
public class PersonInfoActivity extends ZJBEXBaseActivity {
	private ActionBar actionBar;
	private Intent intent;
	private ListView listMenu;
	private MenuItemAdapter itemAdapter;
	private ActionSheet actionSheet;
	private List<menuItemBean> mList = new ArrayList<menuItemBean>();
	private TimePopupWindow pwTime;
	private ImageView image;
	private ImageView sex;
	private TextView friendsName;
	private String ReusltSchool = null; // ���ص�ѧԺ����
	private String ResultSex = null; // ���ص��Ա�
	private String ResultAge = null; // ���ص�����
	private String ResultDate = null; // ���ص�����
	private String ReusltAcademy = null; // ���ص�Ժϵ
	private String telephone=null;     //���صĵ绰����
	private String person_signature=null; //���صĸ���ǩ��
	private Date birthday=null;     //���ص�����
	private OptionsPopupWindow pwOption;
	private ArrayList<String> Sex = new ArrayList<String>();// Sex������Դ
	private ArrayList<String> Age = new ArrayList<String>();// Age������Դ

	private User user = new User(); // User

	private CustomProgressDialog progressDialog = null;
	
	 private String imageName;
	 private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// ����
	 private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
	 private static final int PHOTO_REQUEST_CUT = 3;// ���
	    
	//ͼƬ���ؿ�����
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_info);

		intent = getIntent();

		if ((intent.getExtras()) != null) {
			Bundle data = intent.getExtras();
			user = (User) data.getSerializable("user");
			Log.d("user", user.toString());
		}
        
		image=(ImageView) super.findViewById(R.id.image);
		friendsName=(TextView) findViewById(R.id.friendsName);
		sex=(ImageView) findViewById(R.id.sex);
		listMenu = (ListView) super.findViewById(R.id.listMenu);
		pwTime = new TimePopupWindow(this, Type.YEAR_MONTH_DAY);
		pwOption = new OptionsPopupWindow(this);
        
		initDisplayOption();
		initView();
		initListener();
		initOptions();
		initActionBar();
	}

	private void initDisplayOption() {
		// TODO �Զ����ɵķ������
		options=ImageOptions.initDisplayOptions();
		mImageLoader = ImageLoader.getInstance();
	}

	private void initOptions() {
		// TODO �Զ����ɵķ������
		Sex.add("��");
		Sex.add("Ů");
	}

	private void initListener() {
		// TODO �Զ����ɵķ������

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

		nnavigation.setBackgroundResource(R.drawable.personinfo);

		txt.setText("������Ϣ");

		backtxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				PersonInfoActivity.this.finish();
			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				PersonInfoActivity.this.finish();
			}
		});

		nnavigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SetFriendsAsyncTask a=new SetFriendsAsyncTask();
				a.execute();
				Intent intent=new Intent();
				intent.putExtra("user", user);
				PersonInfoActivity.this.setResult(111, intent);
			}
		});
	}

	private void initView() {
		// TODO �Զ����ɵķ������
		ReusltAcademy = user.getAcademy();
		ReusltSchool = user.getSchool();
		person_signature=user.getPerson_signature();
		telephone=user.getTelephone();
		birthday=user.getBirthday();
		
		if (user.getSex() == 0){
			ResultSex = "δ��";
			sex.setImageResource(0);
		}
		else if (user.getSex() == 1){
			ResultSex = "��";
			sex.setImageResource(R.drawable.man);
			}
		else{
			sex.setImageResource(R.drawable.woman);
			ResultSex = "Ů";
		}
		
		mImageLoader.displayImage(ImageStringUtil.getImageURL(user.getPicture()), image, options, animateFirstListener);
		friendsName.setText(user.getUser_nickname());
		
		ResultAge = String.valueOf(StringUtils.getAgeByBirthday(user
				.getBirthday()));

		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
		ResultDate = myFmt.format(user.getBirthday());

		menuItemBean item2 = new menuItemBean("�ǳ�", user.getUser_nickname(), 0,
				0);
		menuItemBean item3 = new menuItemBean("����", user.getEmail(), 0, 0);
		menuItemBean item4 = new menuItemBean("�Ա�", ResultSex, 0,
				R.drawable.btn_right);
		menuItemBean item5 = new menuItemBean("����", ResultAge, 0,
				R.drawable.btn_right);
		menuItemBean item6 = new menuItemBean("����ǩ��",
				person_signature, 0, R.drawable.btn_right);
		menuItemBean item7 = new menuItemBean("�ֻ�����", telephone, 0,
				R.drawable.btn_right);
		menuItemBean item8 = new menuItemBean("ѧУ", ReusltSchool, 0, 0);
		menuItemBean item9 = new menuItemBean("ѧԺ", ReusltAcademy, 0,
				R.drawable.btn_right);
		menuItemBean item10 = new menuItemBean("����", ResultDate, 0,
				R.drawable.btn_right);

		mList.add(item2);
		mList.add(item3);
		mList.add(item4);
		mList.add(item5);
		mList.add(item6);
		mList.add(item7);
		mList.add(item8);
		mList.add(item9);
		mList.add(item10);

		itemAdapter = new MenuItemAdapter(this, mList);
		listMenu.setAdapter(itemAdapter);
		listMenu.setOnItemClickListener(new SchooltemListener());
		ListViewHeightBaseOnChildren.setListViewHeightBasedOnChildren(listMenu);
	}

	public void setImagePhoto(View source){
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

	                imageName = user.getUser_id() + ".png";
	                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                // ָ������������պ���Ƭ�Ĵ���·��
	                intent.putExtra(MediaStore.EXTRA_OUTPUT,
	                        Uri.fromFile(new File("/sdcard/JBEX/Cache/images", imageName)));
	                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
	                dlg.cancel();
	            }
	        });
	        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
	        tv_xiangce.setText("���");
	        tv_xiangce.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	                imageName = user.getUser_id() + ".png";
	                Intent intent = new Intent(Intent.ACTION_PICK, null);
	                intent.setDataAndType(
	                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
	                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

	                dlg.cancel();
	            }
	        });
	}
	
	class SchooltemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View currentview,
				int arg2, long arg3) {

			switch (arg2) {

			case 7: // ѧԺ
				Intent intent2 = new Intent(PersonInfoActivity.this,
						ChooseSchoolActivity.class);
				intent2.putExtra("school", ReusltAcademy);
				PersonInfoActivity.this.startActivityForResult(intent2, 1);
				break;

			case 8: // ����
				pwTime.showAtLocation(currentview, Gravity.BOTTOM, 0, 0,
						new Date());
				pwTime.setOnTimeSelectListener(new OnTimeSelectListener() {
					@Override
					public void onTimeSelect(Date date) {
						// TODO �Զ����ɵķ������
						birthday=date;
						ResultDate = StringUtils.getTime(date);
						ResultAge = String.valueOf(StringUtils
								.getAgeByBirthday(date));
						menuItemBean item2 = new menuItemBean("����", ResultAge,
								0, R.drawable.btn_right);
						menuItemBean item = new menuItemBean("����", ResultDate,
								0, R.drawable.btn_right);
						mList.set(3, item2);
						mList.set(8, item);
						itemAdapter.onDateChange(mList);
					}
				});
				Toast.makeText(PersonInfoActivity.this, "�޸ĺ� Ҫ����Ŷ��", Toast.LENGTH_SHORT).show();
				break;

			case 2: // �Ա�
				pwOption.setPicker(Sex);
				pwOption.setLabels("�Ա�");
				// ����Ĭ��ѡ�е�������Ŀ
				pwOption.setSelectOptions(0, 0, 0);
				pwOption.setOnoptionsSelectListener(new OnOptionsSelectListener() {
					@Override
					public void onOptionsSelect(int options1, int option2,
							int options3) {
						// TODO �Զ����ɵķ������
						ResultSex = Sex.get(options1);
						menuItemBean item = new menuItemBean("�Ա�", ResultSex,
								0, R.drawable.btn_right);
						mList.set(2, item);
						itemAdapter.onDateChange(mList);
					}
				});
				pwOption.showAtLocation(currentview, Gravity.BOTTOM, 0, 0);
				Toast.makeText(PersonInfoActivity.this, "�޸ĺ� Ҫ����Ŷ��", Toast.LENGTH_SHORT).show();
				break;

			case 3: // ����
				Toast.makeText(PersonInfoActivity.this, "��ѡ�����������Զ���������",
						Toast.LENGTH_SHORT).show();
				pwTime.showAtLocation(currentview, Gravity.BOTTOM, 0, 0,
						new Date());
				pwTime.setOnTimeSelectListener(new OnTimeSelectListener() {
					@Override
					public void onTimeSelect(Date date) {
						// TODO �Զ����ɵķ������
						birthday=date;
						ResultDate = StringUtils.getTime(date);
						ResultAge = String.valueOf(StringUtils
								.getAgeByBirthday(date));
						menuItemBean item2 = new menuItemBean("����", ResultAge,
								0, R.drawable.btn_right);
						menuItemBean item = new menuItemBean("����", ResultDate,
								0, R.drawable.btn_right);
						mList.set(3, item2);
						mList.set(8, item);
						itemAdapter.onDateChange(mList);
					}
				});
				Toast.makeText(PersonInfoActivity.this, "�޸ĺ� Ҫ����Ŷ��", Toast.LENGTH_SHORT).show();
				break;

			case 1: // �ǳ�
                  
				break;

			case 4:// ����ǩ��
                    Intent nextIntent=new Intent(PersonInfoActivity.this, InputInfoActivity.class);
                    nextIntent.putExtra("text", person_signature);
                    nextIntent.putExtra("type", "text");
                    PersonInfoActivity.this.startActivityForResult(nextIntent,1);
				break;
				
			case 5: //�绰����
				 Intent nextNumber=new Intent(PersonInfoActivity.this, InputInfoActivity.class);
				 nextNumber.putExtra("text", telephone);
				 nextNumber.putExtra("type", "num");
                 PersonInfoActivity.this.startActivityForResult(nextNumber,1);
			   break;
			   
			default:
				break;
			}

		}
	}
 
    
	
	/**
	 * Ϊ�˵õ����ص����ݣ�������ǰ���Activity�У�ָMainActivity�ࣩ��дonActivityResult����
	 * 
	 * requestCode �����룬������startActivityForResult()���ݹ�ȥ��ֵ resultCode
	 * ����룬��������ڱ�ʶ�������������ĸ���Activity
	 */
	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case 333:
			ReusltAcademy = data.getStringExtra("school");
			menuItemBean item = new menuItemBean("ѧԺ", ReusltAcademy, 0,R.drawable.btn_right);
			mList.set(7, item);
			itemAdapter.onDateChange(mList);
			Toast.makeText(PersonInfoActivity.this, "�޸ĺ� Ҫ����Ŷ��", Toast.LENGTH_SHORT).show();
			break;
		case 111:
			person_signature=data.getStringExtra("info");
			menuItemBean itemperson_signature = new menuItemBean("����ǩ��", person_signature, 0,R.drawable.btn_right);
			mList.set(4, itemperson_signature);
			itemAdapter.onDateChange(mList);
			Toast.makeText(PersonInfoActivity.this, "�޸ĺ� Ҫ����Ŷ��", Toast.LENGTH_SHORT).show();
			break;
		case 222:
			telephone=data.getStringExtra("info");
			menuItemBean itemtelephone = new menuItemBean("�ֻ�����", telephone, 0,R.drawable.btn_right);
			mList.set(5, itemtelephone);
			itemAdapter.onDateChange(mList);
			Toast.makeText(PersonInfoActivity.this, "�޸ĺ� Ҫ����Ŷ��", Toast.LENGTH_SHORT).show();
			break;
			
		default:
			break;
		}
        
		if (resultCode == RESULT_OK) {
			switch(requestCode){
			 case PHOTO_REQUEST_TAKEPHOTO:

	             startPhotoZoom(
	                     Uri.fromFile(new File("/sdcard/JBEX/Cache/images", imageName)),
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
	             Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/JBEX/Cache/images/"
	                     + imageName);
	            // image.setImageBitmap(bitmap);
	             UpLoadAsyncTask upload=new UpLoadAsyncTask();
	             upload.execute();
	             break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	 private void updateAvatarInServer(String imageName2) {
		// TODO �Զ����ɵķ������
		 user.setPicture(imageName2);
		 SettingUserInfo.settingUserInfo(user);
		 String actionUrl=HttpUtil.BASE_URL+"servlet/UpLoadServlet";
		 UpLoadUtil.uploadFile("/sdcard/JBEX/Cache/images/"
                 + imageName, imageName2,actionUrl);
		 
		 
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
	                Uri.fromFile(new File("/sdcard/JBEX/Cache/images", imageName)));
	        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
	        intent.putExtra("noFaceDetection", true); // no face detection
	        startActivityForResult(intent, PHOTO_REQUEST_CUT);
	    }
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person_info, menu);
		return true;
	}

	public void Exitbtn(View source) {
		actionSheet = new ActionSheet(PersonInfoActivity.this);
		actionSheet.show("ȷ��Ҫ�˳�ô��", new String[] { "�˳�" },
				new Action1<Integer>() {
					@Override
					public void invoke(Integer index) {
						actionSheet.hide();
						if (index == 0) {
							PersonInfoActivity.this.setResult(222);
							PersonInfoActivity.this.finish();
						}
					}
				});
	}
	
	public class SetFriendsAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				user.setAcademy(ReusltAcademy);
				user.setSchool(ReusltSchool);
				user.setBirthday(birthday);
				user.setPerson_signature(person_signature);
				user.setTelephone(telephone);
				int sex = 0;
				if(ResultSex.endsWith("δ��"))
					sex=0;
				else if(ResultSex.endsWith("��"))
					sex=1;
				else if(ResultSex.endsWith("Ů"))
					sex=2;
				user.setSex(sex);
				boolean flag = SettingUserInfo.settingUserInfo(user);
				Log.d("flag------->", String.valueOf(flag));
				
				return "success";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				
				if (progressDialog != null){
					progressDialog.dismiss();
					progressDialog = null;
				}	
				Toast.makeText(PersonInfoActivity.this, "��ϲ�� ����ɹ�Ŷ��", Toast.LENGTH_SHORT).show();
			}
			
	      //�����������û�����Excuteʱ�Ľӿڣ�������ִ��֮ǰ��ʼ���ô˷�����������������ʾ���ȶԻ���
			@Override
			protected void onPreExecute() {
				// TODO �Զ����ɵķ������
				super.onPreExecute();
				
				if (progressDialog == null){
					progressDialog = CustomProgressDialog.createDialog(PersonInfoActivity.this);
			    	progressDialog.setMessage("���ڼ�����...");
				}
				
		    	progressDialog.show();
			}
			
		}
	
	public class UpLoadAsyncTask extends AsyncTask<Integer, Integer, String>{
	     
	     //��ִ̨�У��ȽϺ�ʱ�Ĳ��������Է������ע�����ﲻ��ֱ�Ӳ���UI��
		//�˷����ں�̨�߳�ִ�У�����������Ҫ������ͨ����Ҫ�ϳ���ʱ�䡣��ִ�й����п��Ե���publicProgress(Progress��)����������Ľ��ȡ�
			@Override
			protected String doInBackground(Integer... arg0) {
				// TODO �Զ����ɵķ������
				updateAvatarInServer(imageName);
				return "success";
			}
	     //�൱��Handler ����UI�ķ�ʽ�������������ʹ����doInBackground
		//�õ��Ľ���������UI�� �˷��������߳�ִ�У�����ִ�еĽ����Ϊ�˷����Ĳ�������
			@Override
			protected void onPostExecute(String result) {
				// TODO �Զ����ɵķ������
				super.onPostExecute(result);
				user.setPicture(imageName);
				mImageLoader.clearDiskCache();
				mImageLoader.clearMemoryCache();
				mImageLoader.displayImage(ImageStringUtil.getImageURL(user.getPicture()), image, options, animateFirstListener);
				if (progressDialog != null){
					progressDialog.dismiss();
					progressDialog = null;
				}	
				Intent intent=new Intent();
				intent.putExtra("user", user);
				PersonInfoActivity.this.setResult(111, intent);
				Toast.makeText(PersonInfoActivity.this, "��ϲ�� ����ɹ�Ŷ��", Toast.LENGTH_SHORT).show();
			}
			
	      //�����������û�����Excuteʱ�Ľӿڣ�������ִ��֮ǰ��ʼ���ô˷�����������������ʾ���ȶԻ���
			@Override
			protected void onPreExecute() {
				// TODO �Զ����ɵķ������
				super.onPreExecute();
				
				if (progressDialog == null){
					progressDialog = CustomProgressDialog.createDialog(PersonInfoActivity.this);
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
