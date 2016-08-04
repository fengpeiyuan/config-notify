package com.config_notify.rpc.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpClientTools {

	private int getResultCode;

	private String strGetResponseBody;

	private String errorInfo;

	private byte[] content = null;

	private InputStream inStream = null;

	private long responseLength = 0L;

	private final CloseableHttpClient httpclient;

	private final RequestConfig requestConfig;
	
	public HttpClientTools() {
		this(60000, 60000, 60000);
	}

	public HttpClientTools(int connectionRequestTimeoutMills, int connectionTimeoutMills, int soTimeoutMills) {
		httpclient = HttpClientBuilder.create()
				    .disableAutomaticRetries()
				    .setMaxConnPerRoute(20)
				    .setMaxConnTotal(200)
				    .build();
		requestConfig = RequestConfig.custom()  
				.setConnectionRequestTimeout(connectionRequestTimeoutMills)
				.setConnectTimeout(connectionTimeoutMills)
				.setSocketTimeout(soTimeoutMills).build();
	}
	/**
	 * 根据给定的URL地址和参数字符串，以Get方法调用，如果成功返回true，如果失败返回false
	 * @param url String url地址，可以带参数
	 * @param param String 参数字符串，例如：a=1&b=2&c=3
	 * @return boolean true－成功，false失败，如果返回成功可以getStrGetResponseBody()
	 * 获取返回内容字符串，如果失败，则可访问getErrorInfo()获取错误提示。
	 */
	public boolean executeGetMethod(String url, String param, String charset) {
		if (url == null || url.length() <= 0) {
			errorInfo = "invalid url";
			return false;
		}
		StringBuffer serverURL = new StringBuffer(url);
		if (param != null && param.length() > 0) {
			if(serverURL.indexOf("?") > -1){
				serverURL.append("&");
			}else{
				serverURL.append("?");
			}
			serverURL.append(param);
		}
		HttpGet httpget = new HttpGet(serverURL.toString());
		httpget.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = null;
			try{
				response = httpclient.execute(httpget);
				getResultCode = response.getStatusLine().getStatusCode();
				HttpEntity httpEntity = response.getEntity();
				responseLength = httpEntity.getContentLength();
				strGetResponseBody = EntityUtils.toString(httpEntity, charset);
				EntityUtils.consume(httpEntity);
			}finally{
				if(response != null){
					response.close();
				}
			}
			if (getResultCode >= 200 && getResultCode < 303) {
				return true;
			} else if (getResultCode >= 400 && getResultCode < 500) {
				httpget.abort();
				errorInfo = "error：" + getResultCode;
			} else {
				httpget.abort();
				errorInfo = "error：" + getResultCode;
			}
		} catch (Exception ex) {
			errorInfo = ex.getMessage();
		} finally {
			if(httpget != null){
				httpget.releaseConnection();
			}
		}
		return false;
	}


	public void closeHttpClient(){
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}
	/**
	 * @return the inStream
	 */
	public InputStream getInStream() {
		return inStream;
	}
	/**
	 * @return the responseLength
	 */
	public long getResponseLength() {
		return responseLength;
	}

	/**
	 * 表单数据提交
	 * @param strURL
	 * @param params
	 * @param charset
	 * @return
	 */
	public boolean executePostFormDataMethod(String strURL, Map<String,String> params, String charset) {
		if (strURL == null || strURL.length() <= 0) {
			errorInfo = "invalid url";
			return false;
		}
		UrlEncodedFormEntity entity = null;
		if(params != null && !params.isEmpty()){
			List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
			for(String key : params.keySet()){
				formParams.add(new BasicNameValuePair(key, params.get(key)));
			}
			entity = new UrlEncodedFormEntity(formParams, Charset.forName(charset));
		}
		HttpPost httpPost = new HttpPost(strURL);
		httpPost.setConfig(requestConfig);
		try {
			if(entity != null){
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse response = null;
			try{
				response = httpclient.execute(httpPost);
				getResultCode = response.getStatusLine().getStatusCode();
				HttpEntity httpEntity = response.getEntity();
				strGetResponseBody = EntityUtils.toString(httpEntity, charset);
				EntityUtils.consume(httpEntity);
			}finally{
				if(httpPost != null){
					httpPost.releaseConnection();
				}
			}
			
			if (getResultCode >= 200 && getResultCode < 303) {
				return true;
			} else if (getResultCode >= 400 && getResultCode < 500) {
				httpPost.abort();
				errorInfo = "error:" + getResultCode;
			} else {
				httpPost.abort();
				errorInfo = "error:" + getResultCode;
			}
		} catch (Exception ex) {
			errorInfo = ex.getMessage();
		} finally {
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(httpPost != null){
				httpPost.releaseConnection();
			}
		}
		return false;
	}
	
	public int getiGetResultCode() {
		return getResultCode;
	}

	public String getStrGetResponseBody() {
		return strGetResponseBody;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

}
