package com.basic.connectservice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * ֪ʶ�㣺 1��HttpЭ���ֶ� Range"bytes="+start+"-"+end 2��RandomAccessFile ���������λ��
 * 3�������̷߳�����������
 * 
 * @author lc
 *
 */

public class DownLoad {

	// �����̳߳�
	private Executor threadPool = Executors.newFixedThreadPool(3);

	static class DownLoadRunnable implements Runnable {

		private String url;
		private String fileName;
		private long start;
		private long end;
		private Handler handler;

		public DownLoadRunnable(String url, String fileName, long start,
				long end) {
			this.url = url;
			this.fileName = fileName;
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				URL httpUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) httpUrl
						.openConnection();
				conn.setReadTimeout(5000);
				conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
				conn.setRequestMethod("GET");
				RandomAccessFile access = new RandomAccessFile(new File(
						fileName), "rwd");
				access.seek(start);

				InputStream in = conn.getInputStream();
				byte[] b = new byte[1024 * 4];
				int len = 0;
				while ((len = in.read(b)) != -1) {
					access.write(b, 0, len);
				}
				if (access != null) {
					access.close();
				}
				if (in != null) {
					in.close();
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void downLoadFile(String url) {
		try {
			URL httpUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) httpUrl
					.openConnection();
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			int count = conn.getContentLength();
			int block = count / 3;

			String fileName = getFileName(url);
			File parent = Environment.getExternalStorageDirectory();
			File fileDownLoad = new File(parent, fileName);

			/**
			 * 11/3=3��2����һ���߳�0-2 ���ڶ����߳�3-5�� �������߳�6-10
			 */
			for (int i = 0; i < 3; i++) {
				long start = i * block;
				long end = (i + 1) * block - 1;
				if (i == 2) {
					end = count;
				}
				DownLoadRunnable runnable = new DownLoadRunnable(url,
						fileDownLoad.getAbsolutePath(), start, end);
				threadPool.execute(runnable);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// �õ��ļ�������
	public String getFileName(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

}
