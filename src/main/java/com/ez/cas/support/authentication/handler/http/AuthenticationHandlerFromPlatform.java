/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import com.google.gson.Gson;

/**
 * 
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public class AuthenticationHandlerFromPlatform {
	private  static Logger logger = LoggerFactory.getLogger(AuthenticationHandlerFromPlatform.class);
	
	private  String userInterFace = "/user/";
	
	public AuthenticationHandlerFromPlatform() {
		
	}
	
	public AuthenticationHandlerFromPlatform(String userInterFace) {
		this.userInterFace = userInterFace;
	}
	
	@Cacheable(value = "UserCache",key = "#persionId")
	public Map<String,Object> doAuthentication(String platform, String persionId) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			HttpGet httpget = new HttpGet(platform +this.userInterFace+ persionId);
			logger.debug("executing request {}", httpget.getURI());
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() != 200) {
					logger.error("Principal ID from {} {} mismatch",platform,persionId);
					return null;
				}
					
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					Gson gson = new Gson();
					String userDataJson = EntityUtils.toString(entity);
					HashMap<String,Object> json = gson.fromJson(userDataJson, HashMap.class);
					logger.info("{}", json);
					if (!"success".equalsIgnoreCase(json.get("desc") + "")) {
						logger.warn("Principal ID from {} {} mismatch",platform,persionId);
						return null;
					}
					json.put("from", "bd");
					return json;
				}

			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage());
		} catch (ParseException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}
}
