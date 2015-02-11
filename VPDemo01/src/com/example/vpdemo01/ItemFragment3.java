package com.example.vpdemo01;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemFragment3 extends Fragment {	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState){
		View contextView = inflater.inflate(R.layout.fragment_3, container, false);  

		//获取Activity传递过来的参数  
		Bundle mBundle = getArguments();  
		String title = mBundle.getString("arg");  
		//mTextView.setText(title); 
		int tabNum = mBundle.getInt("TabNum");

		return contextView;  
	}
	
	@Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
    }  
}
