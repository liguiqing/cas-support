/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import java.util.Map;

import org.jasig.cas.authentication.Credential;

import com.ez.cas.support.authentication.principal.EzCredential;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public interface AuthenticationHandlerProxy {
	
	public Map<String,Object> doAuthentication(EzCredential credential);
 
	public boolean supports(Credential credential) ;
	
}
