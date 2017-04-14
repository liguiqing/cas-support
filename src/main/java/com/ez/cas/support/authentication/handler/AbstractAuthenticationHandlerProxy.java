/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import java.util.Map;

import com.ez.cas.support.authentication.principal.EzCredential;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public abstract class AbstractAuthenticationHandlerProxy implements AuthenticationHandlerProxy{

	protected AuthenticationHandlerProxy nextHandler;
	
	@Override
	public Map<String, Object> doAuthentication(EzCredential credential) {
		if(this.supports(credential)) {
			Map<String,Object> result = authentication(credential);
			if(result != null)
				return result; 
		}
		
		if(this.nextHandler !=null)
			return this.nextHandler.doAuthentication(credential);
		
		return null;
	}
	
	protected abstract Map<String, Object> authentication(EzCredential credential) ;
}
