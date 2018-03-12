package com.basic.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewHeightBaseOnChildren {
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// TODO �Զ����ɵķ������
		// ��ȡListView��Ӧ��Adapter   
		ListAdapter listAdapter = listView.getAdapter();   
		if (listAdapter == null) {   
			return;   
		}   

		int totalHeight = 0;   
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
			// listAdapter.getCount()�������������Ŀ   
			View listItem = listAdapter.getView(i, null, listView);   
			// ��������View �Ŀ��   
			listItem.measure(0, 0);    
			// ͳ������������ܸ߶�   
			totalHeight += listItem.getMeasuredHeight();    
		}   

		ViewGroup.LayoutParams params = listView.getLayoutParams();   
		params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
		// listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�   
		// params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�   
		listView.setLayoutParams(params);   
	}   
	
	public static void setListViewHeight(ExpandableListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		int totalHeight = 0;
		int count = listAdapter.getCount();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
}
