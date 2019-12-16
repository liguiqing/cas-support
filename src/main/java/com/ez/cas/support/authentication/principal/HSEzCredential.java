/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.principal;

import com.jayway.jsonpath.JsonPath;
import com.jiasheng.api.JiashengApiException;
import com.jiasheng.api.JiashengOAuthException;
import com.jiasheng.api.utils.SsoUtil;
import org.jasig.cas.authentication.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * 山西寰烁单点登录凭证
 * 
 * @author liguiqing
 * @Date 2017年8月21日
 * @Version 
 */
public class HSEzCredential extends AbstractEzCredenttial {
	private static Logger logger = LoggerFactory.getLogger(HSEzCredential.class);

	private String platformPath = "hslogin" ;
	
	private String fromUrl;
	
	private String userJson;
	
	private String id;
	
	public void setPlatformPath(String platformPath) {
		this.platformPath = platformPath;
	}

	public HSEzCredential() {
		
	}
	
	public HSEzCredential(String fromUrl) {
		this.fromUrl = fromUrl;
	}
	
	@Override
	public String toString() {
		return this.fromUrl+this.userJson;
	}

	@Override
	public String getId() {
		return this.id;
	}
	
	public String getUserJson() {
		return userJson;
	}

	@Override
	public Credential newInstanceOf(HttpServletRequest request) {
		if(request.getRequestURL().indexOf(this.platformPath) > 0){
			String userJson = getUserJson(request);
			if(userJson !=  null) {
				String id =  JsonPath.parse(userJson).read("$.userid")+"";
				HSEzCredential credential = new HSEzCredential();
				credential.fromUrl = this.fromUrl;
				credential.id = id;
				credential.userJson = userJson;
				return credential;	
			}
		}
		return null;
	}
	
	protected String getUserJson(HttpServletRequest request) {
		try {
			return SsoUtil.getUser(request);
		} catch (JiashengApiException e) {
			logger.error("SSO Failure  JiashengApiException {}",e.getMessage());
		} catch (JiashengOAuthException e) {
			logger.error("SSO Failure JiashengOAuthException {}",e.getMessage());
		}
		return null;
	}

	@Override
	public String fromUrl() {
		return this.fromUrl;
	}
}
