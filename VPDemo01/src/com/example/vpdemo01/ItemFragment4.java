package com.example.vpdemo01;

import java.util.HashMap;
import java.util.Map;

import com.example.util.WebServiceUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ItemFragment4 extends Fragment {
	
	//private TextView mTextView;  
	private EditText editText;  
    private TextView result;  
    private Button button;  
    // 命名空间  
    private String nameSpace = "http://WebXml.com.cn/";
    // 调用的方法名称  
    private String methodName = "getMobileCodeInfo"; 
    // EndPoint  
    private String endPoint = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx"; 
    // SOAP Action = NameSpace+MethodName
    //private String soapAction = "http://WebXml.com.cn/getMobileCodeInfo"; 

	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState){
		View contextView = inflater.inflate(R.layout.fragment_4, container, false);  
		//mTextView = (TextView) contextView.findViewById(R.id.textview);
		editText = (EditText) contextView.findViewById(R.id.editText1); 
		button = (Button)contextView.findViewById(R.id.button1);
		result = (TextView) contextView.findViewById(R.id.result);

//		//获取Activity传递过来的参数  
//		Bundle mBundle = getArguments();  
//		String title = mBundle.getString("arg");  
//		int tabNum = mBundle.getInt("TabNum");							
//      mTextView.setText(R.string.label_phone); 
		
		button.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // 手机号码（段）  
                String phoneSec = editText.getText().toString().trim();  
                // 简单判断用户输入的手机号码（段）是否合法  
                if ("".equals(phoneSec) || phoneSec.length() < 7) {  
                    // 给出错误提示  
                	CharSequence html1 = Html.fromHtml("<font color='blue'>" + 
                		getString(R.string.warning_phone_tooshort) +"</font>"); 
                	editText.setError(html1);  
                	editText.requestFocus();  
                    // 将显示查询结果的TextView清空  
                	result.setText("");  
                    return;  
                }  
                Map<String, String> RequestDatas = new HashMap<String,String>();
                RequestDatas.put("mobileCode", phoneSec);
                RequestDatas.put("userId", "");
                // 查询手机号码（段）信息  
                WebServiceUtil webser = new WebServiceUtil();
                // 调用webservice查询手机归属地
                String text = webser.GetString(endPoint, nameSpace, methodName, RequestDatas);
                // 将WebService返回的结果显示在TextView中  
                result.setText(text); 
            }  
        }); 

		return contextView;  
	}
	
	@Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
    }  
}
