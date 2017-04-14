/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;

import com.ez.cas.support.authentication.principal.EduPlatformCredential;
import com.ez.cas.support.authentication.principal.EzCredential;
import com.ez.cas.support.authentication.principal.EzUserPasswordCredential;
import com.ez.cas.support.authentication.principal.PlatformPersonidCredential;
import com.google.gson.Gson;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public class EasytntFaceAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler{
	
	private AuthenticationHandlerProxy proxy;
	
	public EasytntFaceAuthenticationHandler(AuthenticationHandlerProxy proxy) {
		this.proxy = proxy;
	}
	
	@Override
	public boolean supports(Credential credential) {
		return credential instanceof EzCredential;
	}

	@Override
	protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {
		Map<String,Object> result = proxy.doAuthentication((EzCredential)credential);
		logger.debug("Authentication for {}",result);
		if(result == null)
			throw new FailedLoginException(getErrorMessage(credential)); 
		Gson gson = new Gson();
		String userJson = gson.toJson(result);
		HashMap<String,Object> attrs = new HashMap<>();
		attrs.put("data",userJson);
		HandlerResult handlerResult =  createHandlerResult(credential, this.principalFactory.createPrincipal(credential.getId(),attrs), null);
		logger.info(handlerResult.getPrincipal().getAttributes().get("data")+"");
		return handlerResult;
	}

	private String getErrorMessage(Credential credential) {
		if(credential instanceof EduPlatformCredential) {
			return "您暂时还无法使用智能阅卷应用，可能的原因是：\r\n1、您的学校没有购买我们的应用服务;\r\n2、您的学校没有及时更新您的信息;";
		}
		
		if(credential instanceof PlatformPersonidCredential) {
			return "您暂时还无法使用智能阅卷系统，可能的原因是：\r\n1、您的学校没有购买我们阅卷的服务;\r\n2、您的学校没有及时更新您的信息;";
		}
		
		if(credential instanceof EzUserPasswordCredential) {
			return "用户名或者密码错误！";
		}
		return "登录失败！";
	}
}
