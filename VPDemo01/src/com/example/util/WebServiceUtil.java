package com.example.util;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

/*
 * WebService工具类(SoapObject or JSON)
 */
public class WebServiceUtil {
	
	private Boolean _isdotnet = true;
	/*
	 * 设置当前WebServices是否支持 .net 的WebServices；
	 * @param dotNetWebService: 默认true;
	 */
	public WebServiceUtil setIsDotNet(Boolean dotNetWebService)
	{
		_isdotnet = dotNetWebService;
		return this;
	}
	
	private int _setHttpTimeOut = 10* 1000;
	/*
	 * 设置HTTP请求的时间，单位：秒；
	 * @param secondTime: 默认 10 s
	 */
	public WebServiceUtil setHttpTimeOut(int secondTime)
	{
		_setHttpTimeOut = secondTime;
		return this;
	}
	
	private Boolean _isdebug = false;
	/*
	 * 设置启用HTTP的Debug模式
	 * @param isdebug: 默认 false
	 */
	public WebServiceUtil setIsDebug(Boolean isdebug)
	{
		_isdebug = isdebug;
		return this;
	}
	
	private Boolean _iswritelog = false;
	/*
	 * 是否输出日志
	 * @param iswritelog: 默认 false
	 */
	public WebServiceUtil setOutLog(Boolean iswritelog)
	{
		_iswritelog = iswritelog;
		return this;
	}
	
	/*
	 * 获取WebService数据，并以字符形式返回。
	 * @param Url: WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
	 * @param NameSpace: WebService的服务的命名空间，可以WSDL数据中找到 (http://***.com.cn/)
	 * @param MethodName: WebService的调用函数方法名称(getDataMethod)
	 * @param Maps: 请求服务需要提交的数据集
	 * @Return: 服务以字符类型返回请求数据
	 * @Exception: 写入控制台日志
	 */
	public String GetString(String Url, String NameSpace, String MethodName, Map<String, String>  RequestDatas){
		return GetString(Url, NameSpace, MethodName, RequestDatas, null, null);
	}
	
	/*
	 * 获取WebService数据，并以字符形式返回。
	 * @param Url: WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
	 * @param NameSpace: WebService的服务的命名空间，可以WSDL数据中找到 (http://***.com.cn/)
	 * @param MethodName: WebService的调用函数方法名称(getDataMethod)
	 * @param Maps: 请求服务需要提交的数据集
	 * @param SoapHeadeName: 设置WebService的HTTP头名称
	 * @param SoapHeadeValues: 设置 SoapHeade 的数据集
	 * @Return: 服务以字符类型返回请求数据
	 * @Exception: 写入控制台日志
	 */
	public String GetString(String Url, String NameSpace, String MethodName, Map<String, String>  RequestDatas, String SoapHeadeName, Map<String, String> SoapHeadeValues)  {
		SoapObject soap = GetObject(Url, NameSpace, MethodName, RequestDatas, SoapHeadeName, SoapHeadeValues);
		if(soap != null && soap.getPropertyCount() > 0)
		{
			String getResultString = soap.getProperty(0).toString();
			if(_iswritelog)	System.out.println("[Return Data] : "+ getResultString);
			return getResultString;
		}
		return null;
	}

	/*
	 * 获取WebService数据，返回SoapObject对象。
	 * @param Url: WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
	 * @param NameSpace: WebService的服务的命名空间，可以WSDL数据中找到 (http://***.com.cn/)
	 * @param MethodName: WebService的调用函数方法名称(getDataMethod)
	 * @param Maps: 请求服务需要提交的数据集
	 * @Return: 服务返回SoapObject对象
	 * @Exception: 写入控制台日志
	 */
	public SoapObject GetObject(String Url, String NameSpace, String MethodName,  Map<String, String>  RequestDatas) {
		return GetObject(Url, NameSpace, MethodName, RequestDatas, null, null);
	}
	
	/*
	 * 获取WebService数据，返回SoapObject对象。
	 * @param Url: WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
	 * @param NameSpace: WebService的服务的命名空间，可以WSDL数据中找到 (http://***.com.cn/)
	 * @param MethodName: WebService的调用函数方法名称(getDataMethod)
	 * @param Maps: 请求服务需要提交的数据集
	 * @param SoapHeadeName: 设置WebService的HTTP头名称
	 * @param SoapHeadeValues: 设置 SoapHeade 的数据集
	 * @Return: 服务返回SoapObject对象
	 * @Exception: 写入控制台日志
	 */
	public SoapObject GetObject(String Url, final String NameSpace, final String MethodName, Map<String, String>  RequestDatas, String SoapHeadeName, Map<String, String> SoapHeadeValues) {
		try {
			
			final SoapObject soap = new SoapObject(NameSpace, MethodName);
			
			// 系统日志输出
			if(_iswritelog)	System.out.println("[URL] : "	+ Url);
			if(_iswritelog)	System.out.println("[NameSpace] : "	+ NameSpace);
			if(_iswritelog)	System.out.println("[MethodName] : "	+ MethodName);
			if(_iswritelog)	System.out.println("[SOAP Action] : "+ NameSpace+MethodName);
	
			// 设置WebService提交的数据集
			if (RequestDatas != null && !RequestDatas.isEmpty()) {
				for (Map.Entry<String, String> entry : RequestDatas.entrySet()) {
					soap.addProperty(entry.getKey(),  entry.getValue());
				}
			}
	
			// 设置HTTP头信息
			Element[] header = null;
			if(SoapHeadeName != null && SoapHeadeValues != null && !SoapHeadeValues.isEmpty())
			{
			        header = new Element[1];
			        header[0] = new Element().createElement(NameSpace, SoapHeadeName);
		        
			        for (Map.Entry<String, String> entry : SoapHeadeValues.entrySet()) {
			        	Element element = new Element().createElement(NameSpace, entry.getKey());
			        	element.addChild(Node.TEXT, entry.getValue());
			        	header[0].addChild(Node.ELEMENT, element);
				}
			}	
	
			// 初始化数据请求
			final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = _isdotnet;
			if(header  != null)envelope.headerOut = header;
			envelope.bodyOut = soap;
			envelope.setOutputSoapObject(soap);	
			// 发起Web请求
			final HttpTransportSE http = new HttpTransportSE(Url, _setHttpTimeOut);
			http.debug = _isdebug;
			// 异步的计算任务，（Android在4.0后，在主线程访问网络调用webservice时会出现异常，所以这么写）
			FutureTask<SoapObject> future = new FutureTask<SoapObject>(  
	                new Callable<SoapObject>() {  
	                    @Override  
	                    public SoapObject call() throws Exception {    
	                        // 调用webservice  
	                        http.call(NameSpace+MethodName , envelope);
	                        // 获取返回信息  
	                        if (envelope.getResponse() != null) {  
	                            SoapObject result = (SoapObject) envelope.bodyIn;  
	                            if(_iswritelog)	System.out.println("[SOAP.getPropertyCount] : "	+ result.getPropertyCount());
	                            return result;
	                        }else{
	                        	return null;
	                        }	                          
	                    }  
	                });  
			// 新建线程进行计算
	        new Thread(future).start(); 
	        //阻塞一直等待执行完成拿到结果
	        return future.get(); 
	        
			//http.call(NameSpace+MethodName , envelope);			
			// 获取Web请求结果， 数据需要从 result.getProperty(0) 获取
			//SoapObject result = (SoapObject) envelope.bodyIn;			
			//if(_iswritelog)	System.out.println("[SOAP.getPropertyCount] : "	+ result.getPropertyCount());			
			//return result;

		} catch (Exception e) {
			if(_iswritelog)	System.err.println("[Http Exception] : "	+ e.getMessage());
		}
		return null;
	}
}