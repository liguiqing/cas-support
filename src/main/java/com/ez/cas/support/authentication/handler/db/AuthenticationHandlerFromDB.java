/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler.db;

import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version
 */
public class AuthenticationHandlerFromDB {
	private static Logger logger = LoggerFactory.getLogger(AuthenticationHandlerFromDB.class);
	
	private String[] sqls;
	
	private String userDetailSql ;

	private JdbcTemplate jdbcTemplate;
	
	private ShiroMD5PasswordEncoder passwordEncoder;
	 
	public AuthenticationHandlerFromDB(JdbcTemplate jdbcTemplate,ShiroMD5PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
		this.passwordEncoder = passwordEncoder;
	}

	public Boolean doAuthentication(String username, String password) {
		logger.debug("find user from db {}",username);
		if (sqls != null) {
			for (String sql : sqls) {
				
				Map<String, Object> user = getUser(sql, username);
				if (user != null) {
					logger.debug("User found with {} -->  {}",sql,user);
					Boolean success = validatePassword(user,password);
					if(success) {
						return success;
					}
				}
				logger.debug("User not found  with {}",sql);
			}
		}
		return Boolean.FALSE;
	}
	
	@Cacheable(value = "UserCache",key = "#username")
	public Map<String, Object> getUserDetail(String username) {
		Map<String,Object> user =  jdbcTemplate.queryForMap(this.userDetailSql, username);
		user.put("from", "db");
		return user;
	}

	private Boolean validatePassword(Map<String,Object> user,String password) {
		String encodedPassword = this.passwordEncoder.encode(user.get("id").toString(), password);
		if (user.get("password").equals(encodedPassword)) {
			return Boolean.TRUE;
		}
		logger.warn("password not correct {}",password);
		return Boolean.FALSE;
	}
	
	private Map<String, Object> getUser(String sql, String username) {
		try {
			return jdbcTemplate.queryForMap(sql, username);
		}catch(Exception e) {
			logger.warn("User not Fond with '{}' arg '{}'",sql,username);
		}
		return null;
	}

	public void setSqls(String[] sqls) {
		this.sqls = sqls;
	}

	public void setUserDetailSql(String userDetailSql) {
		this.userDetailSql = userDetailSql;
	}
	
	
}
