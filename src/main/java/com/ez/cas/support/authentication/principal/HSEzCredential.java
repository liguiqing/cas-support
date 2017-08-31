/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.principal;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.Credential;

import com.passportsoft.sso.PassportClientUtil;

/**
 * 山西寰烁单点登录凭证
 * 
 * @author liguiqing
 * @Date 2017年8月21日
 * @Version 
 */
public class HSEzCredential extends AbstractEzCredenttial {

	private String platformPath = "hslogin" ;
	
	private String fromUrl;
	
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
		return this.fromUrl;
	}

	@Override
	public String getId() {
		return this.fromUrl;
	}

	@Override
	public Credential newInstanceOf(HttpServletRequest request) {
		if(request.getRequestURL().indexOf(this.platformPath) > 0){
			HSEzCredential credential = new HSEzCredential();
			credential.fromUrl = this.fromUrl;
			return credential;	
		}
		return null;
	}

	@Override
	public String fromUrl() {
		return this.fromUrl;
	}
}
