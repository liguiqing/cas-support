/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import java.util.Map;

import org.jasig.cas.authentication.Credential;

import com.ez.cas.support.authentication.handler.db.AuthenticationHandlerFromDB;
import com.ez.cas.support.authentication.principal.EzCredential;
import com.ez.cas.support.authentication.principal.EzUserPasswordCredential;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version
 */
public class UserPasswordAuthenticationHandlerProxy extends AbstractAuthenticationHandlerProxy {

	private AuthenticationHandlerFromDB dbHandler ;

	public UserPasswordAuthenticationHandlerProxy(AuthenticationHandlerFromDB dbHandler) {
		this.dbHandler = dbHandler;
		this.nextHandler = null;
	}

	public UserPasswordAuthenticationHandlerProxy(AuthenticationHandlerFromDB dbHandler,
			AuthenticationHandlerProxy next) {
		this.dbHandler = dbHandler;
		this.nextHandler = next;
	}

	@Override
	protected Map<String, Object> authentication(EzCredential credential) {

		EzUserPasswordCredential myCredential = (EzUserPasswordCredential) credential;
		String username = myCredential.getUsername();
		String password = myCredential.getPassword();
		Boolean success = this.dbHandler.doAuthentication(username, password);
		if(success) {
			Map<String, Object> result = this.dbHandler.getUserDetail(username);
			if (result != null)
				return result;
		}

		return null;
	}

	@Override
	public boolean supports(Credential credential) {
		return credential instanceof EzUserPasswordCredential;
	}

}
