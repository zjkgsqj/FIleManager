package com.example.vpdemo01;

import java.io.File;
import java.util.List;

import com.example.vpdemo01.picture.ZoomImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

/**
 * 查看大图的Activity界面。
 * 
 */
public class ImageDetailsActivity extends Activity implements
		OnPageChangeListener {

	/**
	 * 用于管理图片的滑动
	 */
	private ViewPager viewPager;

	/**
	 * 显示当前图片的页数
	 */
	private TextView pageText;
	private List<String> list;
	private int position;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_details);
		list = getIntent().getStringArrayListExtra("data");
		position = getIntent().getIntExtra("position", 0);
		
		pageText = (TextView) findViewById(R.id.page_text);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		ViewPagerAdapter adapter = new ViewPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
		viewPager.setOnPageChangeListener(this);
		viewPager.setEnabled(false);
		// 设定当前的页数和总页数
		pageText.setText((position + 1) + "/" + list.size());
	}

	/**
	 * ViewPager的适配器
	 * 
	 */
	class ViewPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//String imagePath = getImagePath(list.get(position));
			BitmapFactory.Options options = new BitmapFactory.Options();
			Bitmap bitmap = null;
			// inJustDecodeBounds为true，那么BitmapFactory.decodeFile并不会真的返回一个Bitmap给你，
			// 它仅仅会把它的宽，高取回来给你
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(list.get(position), options);
			int bitmapSize = options.outHeight * options.outWidth;
			if(bitmapSize > 1000 * 1200){
				options.inJustDecodeBounds = false;
				options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
				//Bitmap bitmap = BitmapFactory.decodeFile(list.get(position));
				bitmap = BitmapFactory.decodeFile(list.get(position),options);
			}else{
				options.inJustDecodeBounds = false;
				options.inSampleSize = 1;
				bitmap = BitmapFactory.decodeFile(list.get(position),options);
			}
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.empty_photo);
			}
			View view = LayoutInflater.from(ImageDetailsActivity.this).inflate(
					R.layout.zoom_image_layout, null);
			ZoomImageView zoomImageView = (ZoomImageView) view
					.findViewById(R.id.zoom_image_view);
			zoomImageView.setImageBitmap(bitmap);
			container.addView(view);
			return view;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

	}

	/**
	 * 获取图片的本地存储路径。
	 * 
	 * @param imageUrl
	 *            图片的URL地址。
	 * @return 图片的本地存储路径。
	 */
	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imageDir = Environment.getExternalStorageDirectory().getPath()
				+ "/PhotoWallFalls/";
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagePath = imageDir + imageName;
		return imagePath;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int currentPage) {
		// 每当页数发生改变时重新设定一遍当前的页数和总页数
		pageText.setText((currentPage + 1) + "/" + list.size());
	}

}