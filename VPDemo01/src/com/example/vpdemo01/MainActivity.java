package com.example.vpdemo01;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity {

	private static String[] TITLE = new String[]{"首页","图片","音乐","其他"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ViewPager pager = (ViewPager)findViewById(R.id.pager);  
		//构造适配器  
        List<Fragment> fragments=new ArrayList<Fragment>();  
        fragments.add(new ItemFragment());  
        fragments.add(new ItemFragment2());  
        fragments.add(new ItemFragment3());
        fragments.add(new ItemFragment4());    
        
		FragmentPagerAdapter pagerAdapter = new TabPageIndicatorAdapter(getSupportFragmentManager(),fragments);
		pager.setAdapter(pagerAdapter);
		//设置缓存页面，当前页面的相邻N各页面都会被缓存
		pager.setOffscreenPageLimit(TITLE.length-1);
		//实例化TabPageIndicator然后设置ViewPager与之关联  
        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);  
        indicator.setViewPager(pager);  
		
        //对ViewPager设置监听，用indicator设置就行了  
        indicator.setOnPageChangeListener(new OnPageChangeListener() {  
              
            @Override  
            public void onPageSelected(int arg0) {  
                //Toast.makeText(getApplicationContext(), TITLE[arg0], Toast.LENGTH_SHORT).show();  
            }  
              
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2) {  
                  
            }  
              
            @Override  
            public void onPageScrollStateChanged(int arg0) {  
                  
            }  
        }); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** 
     * ViewPager适配器 
     * 
     */  
    public class TabPageIndicatorAdapter extends FragmentPagerAdapter {  
    	private List<Fragment> mFragments;
    	
        public TabPageIndicatorAdapter(FragmentManager fm,List<Fragment> fragments) {  
            super(fm);  
            mFragments=fragments;  
        }  
  
        @Override  
        public Fragment getItem(int position) {  
            //新建一个Fragment来展示ViewPager item的内容，并传递参数  
        	Fragment fragment = mFragments.get(position); 
        	Bundle args = new Bundle();
            args.putInt("TabNum", position);
            args.putString("arg", TITLE[position]);    
            fragment.setArguments(args);
            return fragment;  
        }  
  
        @Override  
        public CharSequence getPageTitle(int position) {  
            return TITLE[position % TITLE.length];  
        }  
  
        @Override  
        public int getCount() {  
            return TITLE.length;  
        }  
    }  
	
}
