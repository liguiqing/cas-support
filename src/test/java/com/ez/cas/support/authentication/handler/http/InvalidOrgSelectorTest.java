/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.http;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月15日
 * @Version 
 */

@ContextHierarchy({
	@ContextConfiguration(locations = {"classpath:META-INF/spring/ezConfiguration.xml"})
})
public class InvalidOrgSelectorTest  extends AbstractJUnit4SpringContextTests{

	@Autowired
	private InvalidOrgSelector selector;
	
	@Test
	public void test()throws Exception{
		assertNotNull(selector);
		selector.isValidOrg("123456");
		selector.isValidOrg("974b1a8ef51249a0845ea8ced9af85a0");
		selector.isValidOrg("123456");
		selector.isValidOrg("974b1a8ef51249a0845ea8ced9af85a0");
		selector.isValidOrg("123456");
		selector.isValidOrg("974b1a8ef51249a0845ea8ced9af85a0");
		selector.isValidOrg("123456");
		selector.isValidOrg("974b1a8ef51249a0845ea8ced9af85a0");
	}
}
