/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.web;

import org.jasig.cas.web.flow.AuthenticationExceptionHandler;
import org.springframework.binding.message.MessageContext;

import org.jasig.cas.authentication.AuthenticationException;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月15日
 * @Version 
 */
public class EzAuthenticationExceptionHandler extends AuthenticationExceptionHandler {


	 public String handle(AuthenticationException e, MessageContext messageContext) {
		 String result = super.handle(e, messageContext);
		 return result;
	 }
	
}
