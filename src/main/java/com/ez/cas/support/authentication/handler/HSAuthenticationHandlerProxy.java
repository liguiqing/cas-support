/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import com.ez.cas.support.HttpClientBuilder;
import com.ez.cas.support.authentication.handler.http.InvalidOrgSelector;
import com.ez.cas.support.authentication.principal.EzCredential;
import com.ez.cas.support.authentication.principal.HSEzCredential;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jasig.cas.authentication.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 寰烁认证处理器
 * 
 * @author liguiqing
 * @Date 2017年8月21日
 * @Version 
 */
public class HSAuthenticationHandlerProxy extends AbstractAuthenticationHandlerProxy {
	private static Logger logger = LoggerFactory.getLogger(HSAuthenticationHandlerProxy.class);
	
	private String userInfoUrl = "https://demo.userapi.hseduyun.com/usersApi/getTecherLoginBaseInfo?userId=";
	
	private InvalidOrgSelector orgSelector;
	
	private AuthenticationHandlerProxy cmsUserProxy;
	
	public HSAuthenticationHandlerProxy(InvalidOrgSelector orgSelector) {
		this.orgSelector = orgSelector;
	}
	
	public HSAuthenticationHandlerProxy(InvalidOrgSelector orgSelector,AuthenticationHandlerProxy cmsUserProxy) {
		this.orgSelector = orgSelector;
		this.cmsUserProxy = cmsUserProxy;
	}

	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}

	@Override
	public boolean supports(Credential credential) {
		return credential instanceof HSEzCredential;
	}

	@Override
	protected Map<String, Object> authentication(EzCredential credential) {
		CloseableHttpClient httpclient = HttpClientBuilder.getInstance().createHttpClient();
		try {
			HSEzCredential hsCredential = (HSEzCredential)credential;
			String personId =  hsCredential.getId();
			
			String userType =  JsonPath.parse(hsCredential.getUserJson()).read("$.usertype")+"";
			if("1".equals(userType)) {//管理员调用CMS 认证
				return this.cmsUserProxy.doAuthentication(hsCredential);
			}
			
			String userInfoJson = getUser(httpclient, personId);
			DocumentContext dc = JsonPath.parse(userInfoJson);

			String schoolId = dc.read("$.data.schoolId")+"";
			if(!this.orgSelector.isValidOrg(schoolId)) {
				return null;
			}
			HashMap<String,Object> user = new HashMap<>();
			user.put("user",dc.read("$.data"));
			user.put("from","hs");
			return user;
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private String getUser(CloseableHttpClient httpclient, String personId) {
		try {
			String url = this.userInfoUrl+personId;
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Content-Type", "application/json; charset=utf-8");
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
					
					if (!"200".equals( JsonPath.read(json, "$.code"))) {
						logger.error("Principal ID from {} ",url);
						return null;
					}
					logger.debug("User from {}  is {}",this.userInfoUrl+personId,json);
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
		}
		return null;
	}
}
