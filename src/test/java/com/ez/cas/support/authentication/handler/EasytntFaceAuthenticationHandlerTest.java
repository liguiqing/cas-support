/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import static org.junit.Assert.assertNotNull;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.Credential;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.ez.cas.support.authentication.principal.EduPlatformCredential;
import com.ez.cas.support.authentication.principal.EzUserPasswordCredential;
import com.ez.cas.support.authentication.principal.PlatformPersonidCredential;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月12日
 * @Version 
 */
@ContextHierarchy({
	@ContextConfiguration(locations = {"classpath:META-INF/spring/ezConfiguration.xml"})
})
public class EasytntFaceAuthenticationHandlerTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private EasytntFaceAuthenticationHandler handler;
	
	@Test
	public void test()throws Exception{
		HttpServletRequest request = PowerMockito.mock(HttpServletRequest.class);
		PowerMockito.when(request.getParameter("username")).thenReturn("root").thenReturn("root").thenReturn(null);
		PowerMockito.when(request.getParameter("password")).thenReturn("123456").thenReturn("123456").thenReturn(null);
		assertNotNull(handler);
		Credential credential = new EzUserPasswordCredential().newInstanceOf(request);
		handler.authenticate(credential);
		
		PowerMockito.when(request.getParameter("platformUrl")).thenReturn("http://www.wuhaneduyun.cn:10013/aamif/rest").thenReturn("http://www.wuhaneduyun.cn:10013/aamif/rest").thenReturn(null);
		PowerMockito.when(request.getParameter("personid")).thenReturn("11ad039a5bdc4497aa1b01f61246283c").thenReturn("11ad039a5bdc4497aa1b01f61246283c").thenReturn(null);
		credential = new PlatformPersonidCredential().newInstanceOf(request);
		handler.authenticate(credential);
		
		PowerMockito.when(request.getHeader("Referer")).thenReturn("http://open.sw-cloud.net:30001");
		PowerMockito.when(request.getParameter("ticket")).thenReturn("MTQ0MzVlNjIxMTUyODRlODc4NDlkMDA4NmEyMWZlOTc4MTQ5MjEzNDgxNjczOA==");
		PowerMockito.when(request.getParameter("platformcode")).thenReturn("469026");
		PowerMockito.when(request.getParameter("tk")).thenReturn("469026");
		PowerMockito.when(request.getParameter("s")).thenReturn("469026");
		credential = new EduPlatformCredential().newInstanceOf(request);
		handler.authenticate(credential);
	}
}
