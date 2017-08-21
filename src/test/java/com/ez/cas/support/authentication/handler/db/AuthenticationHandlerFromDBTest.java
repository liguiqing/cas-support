/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.db;

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
public class AuthenticationHandlerFromDBTest extends AbstractJUnit4SpringContextTests {
		
	@Autowired
	AuthenticationHandlerFromDB  db;
	
	@Test
	public void test()throws Exception{
		assertNotNull(db);
		Boolean b = db.doAuthentication("root", "root");
		assertTrue(b);
		b = db.doAuthentication("root@qq.com", "root");
		assertTrue(b);
		b = db.doAuthentication("13477776867", "root");
		assertTrue(b);
		
		b = db.doAuthentication("root", "1234561");
		assertTrue(!b);
		Map<String,Object> user = db.getUserDetail("root");
		assertTrue(Boolean.TRUE);
		
		//b = db.doAuthentication("root", "1234561");
	}
}
