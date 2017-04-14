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

/**
 * 
 * @author liguiqing
 * @Date 2017年4月12日
 * @Version 
 */
public class AuthenticationHandlerFromEduPlatformTest {
	private static Logger logger = LoggerFactory.getLogger(AuthenticationHandlerFromEduPlatformTest.class);
	
	private String homeUrl="http://open.sw-cloud.net:30001";
	private String appId="D855A1C2B67E0D002A455A07EDAAA921";
	private String appKey = "B73A61321F54C9144A1AADA8F24E18EE";

	@Test
	public void test() throws Exception{
		AuthenticationHandlerFromEduPlatform eduPlatform = new AuthenticationHandlerFromEduPlatform(homeUrl,appId,appKey);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String personid = eduPlatform.doAuthentication(httpclient, "eTc4ZDM1ZTA2YWM0NTQ1ODVhNGUxNzBkYTYyMTMyYWQwMTQ5MjEzMTQyNDQ2NQ==");
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
