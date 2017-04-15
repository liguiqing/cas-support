/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.http;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月12日
 * @Version 
 */

@ContextHierarchy({
	@ContextConfiguration(locations = {"classpath:META-INF/spring/ezConfiguration.xml"})
})
public class AuthenticationHandlerFromPlatformTest extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	AuthenticationHandlerFromPlatform platform ;
	
	@Test
	public void test()throws Exception{
        assertNotNull(platform);
		Map<String,Object> result = platform.doAuthentication("http://www.wuhaneduyun.cn:10013/aamif/rest", "11ad039a5bdc4497aa1b01f61246283c");
	}
}
