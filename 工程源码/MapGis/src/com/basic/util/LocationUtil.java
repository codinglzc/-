package com.basic.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class LocationUtil implements
AMapLocationListener{
	private Activity mActivity;
	private LocationManagerProxy mLocationManagerProxy;
	public double mLocationLatlngLatitude;// ������Ϣ
	public double mLocationLatlngLongitude; //������Ϣ
	public float mLocationAccurancy;// ��λ��ȷ��Ϣ
	public String mLocationMethod;// ��λ������Ϣ
	public String mLocationTime;// ��λʱ����Ϣ
	public String mLocationDes;// ��λ������Ϣ
	public String mLocationCountry;// ���ڹ���
	public String mLocationProvince;// ����ʡ
	public String mLocationCity;// ������
	public String mLocationCounty;// ��������
	public String mLocationRoad;// ���ڽֵ�
	public String mLocationPOI;// POI����
	public String mLocationCityCode;// ���б���
	public String mLocationAreaCode;// �������
	
	public Activity getmActivity() {
		return mActivity;
	}

	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	/**
	 * ��ʼ����λ
	 */
	public void init(Activity activity) {
		// ��ʼ����λ��ֻ�������綨λ
		this.mActivity=activity;
		mLocationManagerProxy = LocationManagerProxy.getInstance(mActivity);
		mLocationManagerProxy.setGpsEnable(true);
		// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
		// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
		// �ڶ�λ�������ں��ʵ��������ڵ���destroy()����
		// ����������ʱ��Ϊ-1����λֻ��һ��,
		// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
	}
	
	public void Onpause(){
		mLocationManagerProxy.removeUpdates(this);
		// ���ٶ�λ
		mLocationManagerProxy.destroy();
	}
	
	@Override
	public void onLocationChanged(Location amapLocation) {
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
			// ��λ�ɹ��ص���Ϣ�����������Ϣ
			mLocationLatlngLatitude=amapLocation.getLatitude();
			mLocationLatlngLongitude=amapLocation.getLongitude();
			mLocationAccurancy=amapLocation.getAccuracy();
			mLocationMethod=amapLocation.getProvider();
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(amapLocation.getTime());
			mLocationTime=df.format(date);
			mLocationDes=amapLocation.getAddress();
			mLocationCountry=amapLocation.getCountry();
			if (amapLocation.getProvince() == null) {
				mLocationProvince="null";
			} else {
				mLocationProvince=amapLocation.getProvince();
			}
			mLocationCity=amapLocation.getCity();
			mLocationCounty=amapLocation.getDistrict();
			mLocationRoad=amapLocation.getRoad();
			mLocationPOI=amapLocation.getPoiName();
			mLocationCityCode=amapLocation.getCityCode();
			mLocationAreaCode=amapLocation.getAdCode();
		} else {
			Log.e("AmapErr","Location ERR:" + amapLocation.getAMapException().getErrorCode());
		}
	}
}
