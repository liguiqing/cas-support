/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ez.cas.support.authentication.handler.http.AuthenticationHandlerFromEduPlatform;
import com.ez.cas.support.authentication.handler.http.InvalidOrgSelector;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月12日
 * @Version
 */
public class EduPlatformBuilder {
	private static Logger logger = LoggerFactory.getLogger(EduPlatformBuilder.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private InvalidOrgSelector orgSelector;

	private String sql = "select appId,appKey from base_educloud where address = ? and  platformCode=? and active=1";
	
	private String sql2 = "select appId,appKey,address from base_educloud where  platformCode=? and active=1";

	public EduPlatformBuilder(JdbcTemplate jdbcTemplate,InvalidOrgSelector orgSelector) {
		this.jdbcTemplate = jdbcTemplate;
		this.orgSelector = orgSelector;
	}
	
	@Cacheable(value = "CloudCache",key = "#address" ,unless="#result == null")
	public AuthenticationHandlerFromEduPlatform getEdutPlatform(final String address, final String platformCode) {
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] {address,platformCode},new RowMapper<AuthenticationHandlerFromEduPlatform>() {
				@Override
				public AuthenticationHandlerFromEduPlatform mapRow(ResultSet rs, int arg1) throws SQLException {
					String appId = rs.getString("appId");
					String appKey = rs.getString("appKey");
					return new AuthenticationHandlerFromEduPlatform(orgSelector,address,appId,appKey);
				}			
			});
		}catch(Exception e) {
			logger.warn(e.getMessage());
		}
		return null;
	}
	
	@Cacheable(value = "CloudCache",key = "#platformCode")
	public AuthenticationHandlerFromEduPlatform getEdutPlatform( final String platformCode) {
		try {
			return jdbcTemplate.queryForObject(sql2, new Object[] {platformCode},new RowMapper<AuthenticationHandlerFromEduPlatform>() {
				@Override
				public AuthenticationHandlerFromEduPlatform mapRow(ResultSet rs, int arg1) throws SQLException {
					String appId = rs.getString("appId");
					String appKey = rs.getString("appKey");
					String address = rs.getString("address");
					return new AuthenticationHandlerFromEduPlatform(orgSelector,address,appId,appKey);
				}			
			});
		}catch(Exception e) {
			logger.warn(e.getMessage());
		}
		return null;
	}
	
	public void setOrgSelector(InvalidOrgSelector orgSelector) {
		this.orgSelector = orgSelector;
	}
}
