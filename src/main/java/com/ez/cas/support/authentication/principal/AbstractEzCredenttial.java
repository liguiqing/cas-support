/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.principal;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月11日
 * @Version 
 */
public abstract class AbstractEzCredenttial implements EzCredential {

	protected Boolean supports(HttpServletRequest request,String... params) {
		for(String param:params) {
			if(request.getParameter(param) == null)
				return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
