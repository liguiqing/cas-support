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
import com.jayway.jsonpath.JsonPath;

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
	
	private InvalidOrgSelector orgSelector;
	
	private Gson gson = new Gson();
	
	public AuthenticationHandlerFromPlatform() {
		
	}
	
	public AuthenticationHandlerFromPlatform(String userInterFace) {
		this.userInterFace = userInterFace;
	}
	
	public AuthenticationHandlerFromPlatform(InvalidOrgSelector orgSelector,String userInterFace) {
		this.orgSelector = orgSelector;
		this.userInterFace = userInterFace;
	}
	
	public AuthenticationHandlerFromPlatform(InvalidOrgSelector orgSelector) {
		this.orgSelector = orgSelector;
	}
	
	@Cacheable(value = "UserCache",key = "#persionId",unless="#result == null")
	public Map<String,Object> doAuthentication(String platform, String persionId) {
		String userJson = getUserJson(platform +this.userInterFace+ persionId);
		String orgId = JsonPath.read(userJson, "$.userinfo.orgaid");
		Boolean b = orgSelector.isValidOrg(orgId);
		if(b) {
			HashMap<String,Object> user = gson.fromJson(userJson, HashMap.class);
			user.put("from", "bdplatform");
			return user;
		}
		return null;
		
	}
	
	private String getUserJson(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);
			logger.debug("executing request {}", httpget.getURI());
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() != 200) {
					logger.error("Principal ID from {} ",url);
					return null;
				}
					
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String json = EntityUtils.toString(entity);
					
					if (!"000000".equals( JsonPath.read(json, "$.result"))) {
						logger.error("Principal ID from {} ",url);
						return null;
					}
					
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

	public void setUserInterFace(String userInterFace) {
		this.userInterFace = userInterFace;
	}
}
