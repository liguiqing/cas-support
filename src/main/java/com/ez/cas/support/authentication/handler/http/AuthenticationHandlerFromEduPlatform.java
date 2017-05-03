/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
/**
 * 通过慧教云平台进行认证
 * 
 * @author liguiqing
 * @Date 2017年4月11日
 * @Version
 */
public class AuthenticationHandlerFromEduPlatform {
	private static Logger logger = LoggerFactory.getLogger(AuthenticationHandlerFromEduPlatform.class);

	private static long timestamp = 0l;

	private static String accessToken;

	private int retryIntervalByMin = 60;

	private String appId;

	private String appKey;

	private String tokenUrl; //读取token地址

	private String authenticationUrl; //认证用户session地址
	
	private String eduHomeUrl;//教育云平台首页
	
	private String userDetailUrl;
	
	private String userOrgUrl;
	
	private String teachingUrl;
	
	private Gson gson = new Gson();
	
	private InvalidOrgSelector orgSelector;
	
	public AuthenticationHandlerFromEduPlatform(InvalidOrgSelector orgSelector,String eduHomeUrl,String appId, String appKey) {
		this.orgSelector = orgSelector;
		this.eduHomeUrl = eduHomeUrl;
		this.appId = appId;
		this.appKey = appKey;
		
		this.tokenUrl = this.eduHomeUrl + "/apigateway/getAccessToken";
		this.authenticationUrl = this.eduHomeUrl + "/userSession/validaTicket?accessToken=";
		this.userDetailUrl = this.eduHomeUrl + "/baseInfo/user/getUserById?accessToken=";
		this.userOrgUrl = this.eduHomeUrl + "/baseInfo/org/getOrgInfo?accessToken=";
		this.teachingUrl = this.eduHomeUrl + "/baseInfo/user/getTeachingInfo?accessToken=";
	}
	
	public AuthenticationHandlerFromEduPlatform(InvalidOrgSelector orgSelector,String eduHomeUrl,String appId, String appKey,int retryIntervalByMin) {
		this(orgSelector,eduHomeUrl,appId,appKey);
		this.retryIntervalByMin = retryIntervalByMin;
	}
	
	public String  doAuthentication(CloseableHttpClient httpclient,final String ticket) {
			getAccessTokenInterval(httpclient);
			String args = "{\"ticket\":\""+ticket+"\"}";
			return httpPost(httpclient, this.authenticationUrl + accessToken, args,new AfterHttpPost() {
				@Override
				public String execute(String json) {
					if(responseSuccess(json)) {
						return  JsonPath.read(json, "$.userInfo.personId");
					}
					String retCode =   JsonPath.read(json, "$.retCode");
					String retDesc =   JsonPath.read(json, "$.retDesc");
					logger.warn(String.format("Ticket [%s] validate fail, retCode:[%s], retDesc:[%s]", ticket, retCode,retDesc));
					return null;
				}
			});
	}
	
	@Cacheable(value = "UserCache", key = "#personId",unless="#result == null")
	public Map<String, Object> getUserInfo(CloseableHttpClient httpclient,String personId){
		HashMap<String,Object> userInfo = new HashMap<String,Object>();
		
		String user = getUser(httpclient, personId);
		String orgId = JsonPath.parse(user).read("$.orgId");
		//Boolean b = this.orgSelector.isValidOrg(orgId);
		//if(!b) {
		//	return null;
		//}
		
		String org = getOrgInfo(httpclient, orgId);
		String teaching = getTeachingInfo(httpclient, personId);
		
		userInfo.put("user",gson.fromJson(user, Map.class));
		userInfo.put("org", gson.fromJson(org, Map.class));
		userInfo.put("teaching", gson.fromJson(teaching, List.class));
		userInfo.put("from","edu");
		return userInfo;
	}
	
	private String getUser(CloseableHttpClient httpclient,final String personId ){
		String args = "{\"personId\":\"" + personId + "\"}";
		
		return httpPost(httpclient,this.userDetailUrl+ accessToken,args,new  AfterHttpPost() {

			@Override
			public String execute(String json) {
				if(responseSuccess(json)) {
					 Map user = JsonPath.parse(json).read( "$.userInfo");
					 return gson.toJson(user);
				}
				String retCode =   JsonPath.read(json, "$.retCode");
				String retDesc =   JsonPath.read(json, "$.retDesc");
				logger.warn(String.format("Get person [%s]  fail, retCode:[%s], retDesc:[%s]", personId, retCode,retDesc));
				return null;
			}
			
		});
	}
	
	private String getOrgInfo(CloseableHttpClient httpclient,final String orgId ){
		String args = "{\"orgId\":\"" + orgId + "\"}";
		return httpPost(httpclient,this.userOrgUrl+ accessToken,args,new AfterHttpPost() {

			@Override
			public String execute(String json) {
				if(responseSuccess(json)) {
					Map org =   JsonPath.read(json, "$.orgInfo");
					return gson.toJson(org);
				}
				String retCode =   JsonPath.read(json, "$.retCode");
				String retDesc =   JsonPath.read(json, "$.retDesc");
				logger.warn(String.format("Get orgId [%s]  fail, retCode:[%s], retDesc:[%s]", orgId, retCode,retDesc));
				return null;
			}
			
		});
	}
	
	private String getTeachingInfo(CloseableHttpClient httpclient,final String personId){
		String args = "{\"personId\":\"" + personId + "\"}";
		return httpPost(httpclient,this.teachingUrl+ accessToken,args,new AfterHttpPost() {

			@Override
			public String execute(String json) {
				if(responseSuccess(json)) {
					List teaching = JsonPath.parse(json).read( "$.teachList");
					return gson.toJson(teaching);
				}
				String retCode =   JsonPath.read(json, "$.retCode");
				String retDesc =   JsonPath.read(json, "$.retDesc");
				logger.warn(String.format("Get teaching [%s]  fail, retCode:[%s], retDesc:[%s]", personId, retCode,retDesc));
				return null;
			}
			
		});
	}

	
	private String httpPost(CloseableHttpClient httpclient,String url,String args,AfterHttpPost after){
		try {	
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
			StringEntity stringentity = new StringEntity( args,"utf-8");//解决中文乱码问题    
			httpPost.setEntity(stringentity);
			logger.debug("executing request {}", httpPost.getURI());
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() != 200) {
					logger.error(String.format("Request Fail, HttpStatusCode:[%d]",  status.getStatusCode()));
					return null;
				}
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String json = EntityUtils.toString(entity);
					logger.debug("request result {}",json);
					return after.execute(json);
					
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
	
	private Boolean responseSuccess(String json) {
		String retCode = JsonPath.read(json, "$.retCode");
		return "000000".equals(retCode);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("appid", this.appId).append("appkey", this.appKey)
				.append("tokenurl", this.tokenUrl).build();
	}
	
	private  String getRemoteAccessToken(CloseableHttpClient httpclient) throws Exception{
		OpenApi api = OpenApi.getInstance();
		return api.getAccessToken(httpclient,this.appId,this.appKey, this.tokenUrl, "");
	}
	
	private void getAccessTokenInterval(CloseableHttpClient httpclient) {
		try {
			long tempTS = System.currentTimeMillis();
			if(accessToken == null || (tempTS - timestamp)/(1000*60d) >= retryIntervalByMin) {
				accessToken = getRemoteAccessToken(httpclient);
				timestamp = tempTS;
			}
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			accessToken = null;
		}
	}
	
	private  interface AfterHttpPost{
		String  execute(String json);
	} 
	
	public void setOrgSelector(InvalidOrgSelector orgSelector) {
		this.orgSelector = orgSelector;
	}
}
