package com.example.vpdemo01;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vpdemo01.folder.FolderBaseAdapter;

public class ItemFragment extends Fragment {
	
	private TextView folderPath;  
	private ListView folderList;  
	private List<String> items = null;//存放名称  
    private List<String> paths = null;//存放路径  
    private String rootPath = "/";  

	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState){
		View contextView = inflater.inflate(R.layout.fragment_main, container, false);  
		folderPath = (TextView) contextView.findViewById(R.id.folderPath);
		folderList = (ListView) contextView.findViewById(R.id.folderList); 
		
		/*//获取Activity传递过来的参数  
		Bundle mBundle = getArguments();  
		String title = mBundle.getString("arg");  
		int tabNum = mBundle.getInt("TabNum");*/
        this.getFileDir(rootPath);
		return contextView;  
	}
	
	@Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
    }  
	
	public void getFileDir(String filePath) {  
        try{  
            folderPath.setText("当前路径:"+filePath);// 设置当前所在路径  
            items = new ArrayList<String>();  
            paths = new ArrayList<String>();  
            File f = new File(filePath);  
            File[] files = f.listFiles();// 列出所有文件  
            // 如果不是根目录,则列出返回根目录和上一目录选项  
            if (!filePath.equals(rootPath)) {  
                items.add("返回根目录");  
                paths.add(rootPath);  
                items.add("返回上一层目录");  
                paths.add(f.getParent());  
            }
            // 将所有文件存入list中  
            if(files != null){  
                int count = files.length;// 文件个数  
                for (int i = 0; i < count; i++) {  
                    File file = files[i];  
                    items.add(file.getName());  
                    paths.add(file.getPath());  
                }  
            }  
            FolderBaseAdapter folderBaseAdapter = new FolderBaseAdapter(getActivity(),items);
            folderList.setAdapter(folderBaseAdapter);
            folderList.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String path = paths.get(arg2); 
					File file = new File(path);  
					//如果是文件夹就继续分解  
					if(file.isDirectory()){  
						getFileDir(path);  
					}else{  
						new AlertDialog.Builder(getActivity())
						    .setTitle("提示")
						    .setMessage(file.getName()+" 是一个文件！")
						    .setPositiveButton("OK", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
								}  

						}).show();  
					}  

				}
            	
            });           
            
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
	}

}
