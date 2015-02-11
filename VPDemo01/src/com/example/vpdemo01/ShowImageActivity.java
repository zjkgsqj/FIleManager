package com.example.vpdemo01;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.vpdemo01.picture.ChildAdapter;

public class ShowImageActivity extends Activity {
	private GridView mGridView;
	private List<String> list;
	private ChildAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_activity);
		
		mGridView = (GridView) findViewById(R.id.child_grid);
		list = getIntent().getStringArrayListExtra("data");
       
		adapter = new ChildAdapter(this, list, mGridView);
		mGridView.setAdapter(adapter);
		// 图片点击事件
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent mIntent = new Intent(ShowImageActivity.this, ImageDetailsActivity.class);
				mIntent.putExtra("position", arg2);
				mIntent.putStringArrayListExtra("data", (ArrayList<String>)list);
				startActivity(mIntent);
				
			}}); 
		
	}

	@Override
	public void onBackPressed() {
		//Toast.makeText(this, "选中 " + adapter.getSelectItems().size() + " item", Toast.LENGTH_LONG).show();
		super.onBackPressed();
	}			
}