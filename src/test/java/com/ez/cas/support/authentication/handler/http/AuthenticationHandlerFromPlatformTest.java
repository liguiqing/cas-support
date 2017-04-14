/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.http;

import java.util.Map;

import org.junit.Test;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月12日
 * @Version 
 */
public class AuthenticationHandlerFromPlatformTest {
	
	@Test
	public void test()throws Exception{
		AuthenticationHandlerFromPlatform platform = new AuthenticationHandlerFromPlatform();
		Map<String,Object> result = platform.doAuthentication("http://www.wuhaneduyun.cn:10013/aamif/rest", "F259F9A5229D7692E04010AC73D40970");
	}
}
