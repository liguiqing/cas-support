/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.ez.cas.support.authentication.handler.EduPlatformBuilder;
import com.ez.cas.support.authentication.handler.http.AuthenticationHandlerFromEduPlatform;
import com.ez.cas.support.authentication.handler.http.InvalidOrgSelector;
/**
 * 
 * @author liguiqing
 * @Date 2017年4月12日
 * @Version 
 */
@ContextHierarchy({
	@ContextConfiguration(locations = {"classpath:META-INF/spring/ezConfiguration.xml"})
})
public class EduPlatformBuilderTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private InvalidOrgSelector orgSelector; 
	
	@Test
	public void test()throws Exception{
		EduPlatformBuilder builder = new EduPlatformBuilder(jdbcTemplate,orgSelector);
		AuthenticationHandlerFromEduPlatform eduPlatform = builder.getEdutPlatform("http://open.sw-cloud.net:30001", "469026");
		assertNotNull(eduPlatform);
	}
}
