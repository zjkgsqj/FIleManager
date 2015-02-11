package com.example.vpdemo01.folder;

import java.util.List;

import com.example.vpdemo01.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FolderBaseAdapter extends BaseAdapter {
	private LayoutInflater mInflater;  
	private List<String> items; 
	public FolderBaseAdapter(Context context,List<String> items){  
		this.mInflater = LayoutInflater.from(context);
		this.items = items;
	}  
	public int getCount() {  
		return items.size();  
	}  

	public Object getItem(int arg0) {   
		return arg0;  
	}  

	public long getItemId(int arg0) {   
		return arg0;  
	}  

	public View getView(int position, View convertView, ViewGroup parent) {  

		ViewHolder holder = null;  
		if (convertView == null) {  
			holder=new ViewHolder();   
			convertView = mInflater.inflate(R.layout.folders_listview, null);  
			//holder.img = (ImageView)convertView.findViewById(R.id.img);  
			holder.title = (TextView)convertView.findViewById(R.id.folderName);
			convertView.setTag(holder);  

		}else {                       
			holder = (ViewHolder)convertView.getTag();  
		}  
		//holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));  
		holder.title.setText(items.get(position)); 

		return convertView;  
	}  

	public final class ViewHolder{  
		//public ImageView img;  
		public TextView title;
	}  
}
