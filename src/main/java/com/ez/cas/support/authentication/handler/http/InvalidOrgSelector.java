/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.http;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 查询用户所在机构是否有权限使用系统
 * 
 * @author liguiqing
 * @Date 2017年4月15日
 * @Version 
 */
public class InvalidOrgSelector {
	private static Logger logger = LoggerFactory.getLogger(InvalidOrgSelector.class);
	
	private JdbcTemplate jdbcTemplate;

	private String sql = "select 1 from base_eduschool  where code = ?  and  schoolYear = ?  and schoolTerm = ? ";
	
	public InvalidOrgSelector(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Cacheable(value = "ValidOrgsCache",key = "#orgId",unless="#result == false")
	public Boolean isValidOrg(String orgId) {
		Calendar now = Calendar.getInstance();
		String year = getSchoolYear(now);
		int term = getSchoolTerm(now);
		Object[] args = new Object[] {orgId,year,term};
		logger.debug(" Is Org  activee {}  {} " ,sql,args);
		List bs = jdbcTemplate.query(sql,args, new RowMapper<Boolean>() {
			@Override
			public Boolean mapRow(ResultSet rs, int arg1) throws SQLException {
				int i = rs.getInt(1);
				return i>0;
			}
		});
		
		return bs != null && bs.size()>0;
	}
	
	private String getSchoolYear(Calendar now) {
		if(now.get(Calendar.MONTH)<8 && now.get(Calendar.MONTH)>1) {
			return now.get(Calendar.YEAR)-1 + "-" +  now.get(Calendar.YEAR);
		}
		return now.get(Calendar.YEAR) + "-" + (now.get(Calendar.YEAR) +1);
	}
	
	private int getSchoolTerm(Calendar now) {
		if(now.get(Calendar.MONTH)<8 && now.get(Calendar.MONTH)>1) {
			return 2;
		}
		return 1;
	}
}
