/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.principal;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.Credential;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public interface  EzCredential extends Credential {

	public Credential newInstanceOf(HttpServletRequest request) ;
	
}
