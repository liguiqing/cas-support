/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;

public class OpenApi {
	private static Logger logger = LoggerFactory.getLogger(OpenApi.class);
	
	private static class Holder{
		private static OpenApi instance = new OpenApi();
	}
	
	private OpenApi() {
		
	}

	public static OpenApi getInstance() {
		return Holder.instance;
	}

	public String getAccessToken(CloseableHttpClient httpclient, String appId, String appKey, String apiServerUrl,
			String platformCode) throws Exception {

		long ts = System.currentTimeMillis();
		String keyinfo = appId + appKey + String.valueOf(ts);
		byte[] hmacSHA = EncryptionUtils.getHmacSHA1(keyinfo, appKey);
		String digest = EncryptionUtils.bytesToHexString(hmacSHA);
		digest = digest.toUpperCase();
		
		HttpPost httppost = new HttpPost(apiServerUrl);
		httppost.setHeader("Content-Type", "application/json; charset=utf-8");
		String args = "{\"appId\":\""+appId+"\",\"timeStamp\":\""+ts+"\",\"keyInfo\":\""+digest+"\"}";
		StringEntity stringentity = new StringEntity(args, "utf-8");// 解决中文乱码问题
		httppost.setEntity(stringentity);
		CloseableHttpResponse response = httpclient.execute(httppost);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String json  = EntityUtils.toString(response.getEntity());
				return JsonPath.read(json,"$.tokenInfo.accessToken");
			}
		}catch(Exception ex){
			logger.warn(ex.getMessage());
		}finally {
			response.close();
		}
		return null;
	}
}
