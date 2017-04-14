/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import java.util.Map;

import org.jasig.cas.authentication.Credential;

import com.ez.cas.support.authentication.handler.http.AuthenticationHandlerFromPlatform;
import com.ez.cas.support.authentication.principal.EzCredential;
import com.ez.cas.support.authentication.principal.PlatformPersonidCredential;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version
 */
public class PlatformPersonIdAuthenticationHandlerProxy extends AbstractAuthenticationHandlerProxy {

	private AuthenticationHandlerFromPlatform platform;

	public PlatformPersonIdAuthenticationHandlerProxy(AuthenticationHandlerFromPlatform platform) {
		this.platform = platform;
		this.nextHandler = null;
	}

	public PlatformPersonIdAuthenticationHandlerProxy(AuthenticationHandlerFromPlatform platform,
			AuthenticationHandlerProxy nextHandler) {
		this.platform = platform;
		this.nextHandler = nextHandler;
	}

	@Override
	public boolean supports(Credential credential) {
		return credential instanceof PlatformPersonidCredential;
	}

	@Override
	protected Map<String, Object> authentication(EzCredential credential) {
		PlatformPersonidCredential myCredential = (PlatformPersonidCredential) credential;
		String personId = myCredential.getPersonId();
		String lt = myCredential.getLt();
		String execution = myCredential.getExecution();
		String platformUrl = myCredential.getPlatformUrl();
		Map<String, Object> result = this.platform.doAuthentication(platformUrl, personId);
		if (result != null)
			return result;
		return null;
	}

}
