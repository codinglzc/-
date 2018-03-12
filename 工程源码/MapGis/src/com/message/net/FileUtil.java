package com.message.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
    public final static String HEAD_PATH=Environment.getExternalStorageDirectory()+"/woliao/head";
    public final static String WOLIAO_PATH=Environment.getExternalStorageDirectory()+"/woliao";
	
    /**
	 * ����һ����userIdΪ�ļ������ļ��������û�ͷ���ļ�·��Ϊ"/sdcard/woliao/selfId/friendId.jpg"
	 * @param selfId
	 * @param friendId
	 * @return
	 */
	public static File createFile(String selfId, String friendId){
		String filePath=Environment.getExternalStorageDirectory()+"/woliao/"+selfId;
		File fileParent=new File(filePath);
		if(fileParent.exists()==false){
			fileParent.mkdirs();
		}
		
		File file=new File(filePath+"/"+friendId+".jpg");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return file;
	}

	/**
	 * ����fileType,������ͨ��jpg��3gp�ļ�������ͼƬ������
	 * @param selfId
	 * @param fileType
	 * @return
	 */
	public static File createFile(String selfId, int fileType) {
		String nowTime = TimeUtil.getAbsoluteTime();
		String filePath = Environment.getExternalStorageDirectory()
				+ "/woliao/" + selfId;
		File fileParent = new File(filePath);
		if (fileParent.exists() == false) {
			fileParent.mkdirs();
		}
		File file = null;
		if (fileType == Config.MESSAGE_TYPE_IMG) {
			file = new File(filePath + "/" + nowTime + ".jpg");
		} else if (fileType == Config.MESSAGE_TYPE_AUDIO) {
			file = new File(filePath + "/" + nowTime + ".3gp");
		}

		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("FileUtil", "createFile file" + file);
		return file;
	}
	
	public static boolean writeFile(ContentResolver cr, File file, Uri uri) {
	    Log.i("FileUtil", "cr="+cr+", file="+file+", uri="+uri);
		boolean result=true;
		try {
			FileOutputStream fout = new FileOutputStream(file);
			Log.i("FileUtil", "fout="+fout);
			Bitmap bitmap=BitmapFactory.decodeStream(cr.openInputStream(uri));
			Log.i("FileUtil", "bitmap="+bitmap);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
			
			try {
				fout.flush();
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
				result=false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result=false;
		}catch(Exception e){
		    Log.i("FileUtil", "exception="+e.toString());
		}
		
		return result;
	}
	
	//�����û�ID,����һ���Ը�IDΪ�ļ�����jpgͼƬ
    public static File createHeadFile(String userId){
        File fileParent = new File(HEAD_PATH);
        if (fileParent.exists() == false) {
            fileParent.mkdirs();
        }
        
        File file = null;
        file = new File(HEAD_PATH + "/" + userId+ ".jpg");
        if (file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return file;
    }
    
    public static Bitmap getHeadFile(int userId){
        File file = new File(HEAD_PATH+"/"+userId+".jpg");
        if (file.exists() == false) {
            return null;
        }
        
        Bitmap bitmap=BitmapFactory.decodeFile(HEAD_PATH+"/"+userId+".jpg");
        return bitmap;
    }
}
