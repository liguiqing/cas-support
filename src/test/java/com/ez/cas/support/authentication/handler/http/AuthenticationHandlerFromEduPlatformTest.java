/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.http;

import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AuthenticationHandlerFromEduPlatformTest  extends AbstractJUnit4SpringContextTests{
	private static Logger logger = LoggerFactory.getLogger(AuthenticationHandlerFromEduPlatformTest.class);
	
	private String homeUrl="http://open.sw-cloud.net:30001";
	private String appId="D855A1C2B67E0D002A455A07EDAAA921";
	private String appKey = "B73A61321F54C9144A1AADA8F24E18EE";

	@Autowired
	private InvalidOrgSelector orgSelector; 
	
	
	@Test
	public void test() throws Exception{
		AuthenticationHandlerFromEduPlatform eduPlatform = new AuthenticationHandlerFromEduPlatform(orgSelector,homeUrl,appId,appKey);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String personid = eduPlatform.doAuthentication(httpclient, "bWE2MDgxZTdmNmIxZjQ1NTliMDNjMGM5ZDFhODY3NTNiMTQ5Mjc1MzAxNzU0Mw==");
		if(personid != null) {
			Map<String,Object> userInfo = eduPlatform.getUserInfo(httpclient, personid);
			logger.debug(userInfo+"1");
			userInfo = eduPlatform.getUserInfo(httpclient, personid);
			logger.debug(userInfo+"2");
			userInfo = eduPlatform.getUserInfo(httpclient, personid);
			logger.debug(userInfo+"3");
		}
		httpclient.close();
	}
}
