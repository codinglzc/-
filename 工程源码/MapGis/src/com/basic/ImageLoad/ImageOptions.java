package com.basic.ImageLoad;

import java.io.File;

import android.util.Log;

import com.basic.Activities.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageOptions {
     public static DisplayImageOptions initDisplayOptions(){
    	 DisplayImageOptions options;
    	 options = new DisplayImageOptions.Builder()
 		//.showStubImage(R.drawable.ic_stub)	//�������ڼ���ͼƬ
 		.showImageOnLoading(R.drawable.ic_stub) //1.8.7����
 		.showImageForEmptyUri(R.drawable.ic_empty)	
 		.showImageOnFail(R.drawable.ic_error)	//���ü���ʧ��ͼƬ
 		.cacheInMemory(true)
 		.cacheOnDisk(true)
 		.displayer(new RoundedBitmapDisplayer(360))	//����ͼƬ�Ƕ�,0Ϊ���Σ�360ΪԲ��
 		.build();
        return options;
     }
     
     public static DisplayImageOptions initDisplayOptionsRange(){
    	 DisplayImageOptions options;
    	 options = new DisplayImageOptions.Builder()
 		//.showStubImage(R.drawable.ic_stub)	//�������ڼ���ͼƬ
 		.showImageOnLoading(R.drawable.ic_stub) //1.8.7����
 		.showImageForEmptyUri(R.drawable.ic_empty)	
 		.showImageOnFail(R.drawable.ic_error)	//���ü���ʧ��ͼƬ
 		.cacheInMemory(true)
 		.cacheOnDisk(true)
 		.displayer(new RoundedBitmapDisplayer(20))	//����ͼƬ�Ƕ�,0Ϊ���Σ�360ΪԲ��
 		.build();
        return options;
     }
}
