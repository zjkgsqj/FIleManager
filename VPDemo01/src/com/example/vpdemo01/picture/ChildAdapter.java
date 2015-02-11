package com.example.vpdemo01.picture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpdemo01.R;
import com.example.vpdemo01.picture.MyImageView.OnMeasureListener;
import com.example.vpdemo01.picture.NativeImageLoader.NativeImageCallBack;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class ChildAdapter extends BaseAdapter implements OnClickListener {
	private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
	/**
	 * 用来存储图片的选中情况
	 */
	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	private GridView mGridView;
	private List<String> list;
	protected LayoutInflater mInflater;
	private Context context;
	private PopupWindow popupWindow;  //弹出菜单

	public ChildAdapter(Context context, List<String> list, GridView mGridView) {
		this.list = list;
		this.mGridView = mGridView;
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path = list.get(position);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.grid_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
			
			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
				
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		viewHolder.mImageView.setTag(path);
		viewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//如果是未选中的CheckBox,则添加动画
				if(!mSelectMap.containsKey(position) || !mSelectMap.get(position)){
					addAnimation(viewHolder.mCheckBox);
					if(popupWindow ==null || !popupWindow.isShowing()) { 
						//PopupWindow的界面
						View popView = mInflater.inflate(R.layout.popup_tools, null);
						//初始化PopupWindow
						initPopupWindow(buttonView,popView);
					}
				}
				mSelectMap.put(position, isChecked);
				// 批量遍历map,计算是否有选中的项，没有时隐藏菜单栏
				int checkedNum = 0;
				Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry<Integer, Boolean> entry = it.next();
					if(entry.getValue()){
						checkedNum ++;
					}
				}
				if(checkedNum==0 && popupWindow !=null && popupWindow.isShowing()){
					popupWindow.dismiss();
				}
			}
		});
		
		viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(position) ? mSelectMap.get(position) : false);
		
		//利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack() {
			
			@Override
			public void onImageLoader(Bitmap bitmap, String path) {
				ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
				if(bitmap != null && mImageView != null){
					mImageView.setImageBitmap(bitmap);
				}
			}
		});
		
		if(bitmap != null){
			viewHolder.mImageView.setImageBitmap(bitmap);
		}else{
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		
		return convertView;
	}
	
	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画 
	 * @param view
	 */
	private void addAnimation(View view){
		float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), 
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
				set.setDuration(150);
		set.start();
	}	
	
	/**
	 * 获取选中的Item的position
	 * @return
	 */
	public List<Integer> getSelectItems(){
		List<Integer> list = new ArrayList<Integer>();
		for(Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();){
			Map.Entry<Integer, Boolean> entry = it.next();
			if(entry.getValue()){
				list.add(entry.getKey());
			}
		}
		
		return list;
	}
		
	public static class ViewHolder{
		public MyImageView mImageView;
		public CheckBox mCheckBox;
	}

	/**
	 * 初始化弹出菜单
	 */
	private void initPopupWindow(View parent,View popView){
		TextView text_copy = (TextView) popView.findViewById(R.id.tools_copy);		
		text_copy.setOnClickListener(this);
		TextView text_cut = (TextView) popView.findViewById(R.id.tools_cut);		
		text_cut.setOnClickListener(this);
		TextView text_delete = (TextView) popView.findViewById(R.id.tools_delete);		
		text_delete.setOnClickListener(this);
		TextView text_more = (TextView) popView.findViewById(R.id.tools_more);		
		text_more.setOnClickListener(this);
		
		popupWindow = new PopupWindow(popView,  
				ViewGroup.LayoutParams.MATCH_PARENT,  
				ViewGroup.LayoutParams.WRAP_CONTENT);
		//popupWindow.setBackgroundDrawable(new BitmapDrawable()); 
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		/**设置PopupWindow弹出和退出时候的动画效果*/  
		popupWindow.setAnimationStyle(R.style.AnimationPreview);			        
		popupWindow.setFocusable(false); //设置PopupWindow可获得焦点
		//popupWindow.setTouchable(true); //设置PopupWindow可触摸
		//popupWindow.setFocusableInTouchMode(true);
		//popupWindow.setOutsideTouchable(true); //设置非PopupWindow区域可触摸
		popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}
	
	public void onClick(View v) {		
		switch(v.getId()){
		case R.id.tools_copy:
			showToast("复制");
			popupWindow.dismiss();
			break;
		case R.id.tools_cut:
			showToast("剪切");
			popupWindow.dismiss();
			break;
		case R.id.tools_delete:
			showToast("删除");
			popupWindow.dismiss();
			break;
		case R.id.tools_more:
			showToast("更多");
			popupWindow.dismiss();
			break;
		}
	}
	
	/**显示toast*/
	public void showToast(String str){
		Toast.makeText(context,str, Toast.LENGTH_LONG).show();
	}
	
}
