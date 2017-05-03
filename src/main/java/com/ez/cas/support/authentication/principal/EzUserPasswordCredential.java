/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.principal;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jasig.cas.authentication.Credential;

/**
 * 用户名密码凭证
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public class EzUserPasswordCredential extends AbstractEzCredenttial implements Serializable{
	private String usernameParam = "username";
	
	private String passwordParam = "password";
	
	private String username;
	
	private String password;
	
	private String fromUrl;
	
	public EzUserPasswordCredential() {
		
	}
	
	public EzUserPasswordCredential(String usernameParam,String passwordParam) {
		this.usernameParam = usernameParam;
		this.passwordParam = passwordParam;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getId() {
		return this.username;
	}

	@Override
	public Credential newInstanceOf(HttpServletRequest request) {
		if(this.supports(request, this.usernameParam,this.passwordParam)) {
			EzUserPasswordCredential instance = new EzUserPasswordCredential();
			instance.username = request.getParameter(this.usernameParam) ;
			instance.password = request.getParameter(this.passwordParam) ;
			instance.fromUrl = request.getContextPath()+"?"+request.getParameter("service");
			return instance;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(this.username).build();
	}

	@Override
	public String fromUrl() {
		return this.fromUrl;
	}
}
