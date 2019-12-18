/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import com.ez.cas.support.HttpClientBuilder;
import com.ez.cas.support.authentication.handler.db.ShiroMD5PasswordEncoder;
import com.ez.cas.support.authentication.principal.EzCredential;
import com.ez.cas.support.authentication.principal.HSEzCredential;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jasig.cas.authentication.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 *  寰烁CMS用户登录
 *  
 * @author liguiqing
 * @Date 2017年10月31日
 * @Version 
 */
public class HSCMSAuthenticationHandlerProxy extends AbstractAuthenticationHandlerProxy{
	private static Logger logger = LoggerFactory.getLogger(HSCMSAuthenticationHandlerProxy.class);
	
	private String userInfoUrl = "http://180.76.162.41:50038/usersApi/getUserAccount?userType=1&userId=";
	
	private String validateUserSQL = "select 1  from idm_user where userName = ?";
	
	private String insertUserSQL = "INSERT INTO  idm_user(userName,actived,systemRole) values(?,?,?)";
	
	private String updateUserPwdSQL = "update  idm_user set pwd=? where id=?";
	
	private String insertUserDetailSQL= "INSERT INTO idm_userdetail(nickName,isOnLine,userType,userId,account)"
			+ " VALUES (?,?,?,?,?)";
	
    private String userDetailSql = "SELECT b.id,b.userName,a.* FROM idm_userdetail a INNER JOIN idm_user b ON a.userId=b.id WHERE b.userName=?";
	
	private  JdbcTemplate jdbcTemplate;
	
	private ShiroMD5PasswordEncoder pwdEndocer;
	
	public HSCMSAuthenticationHandlerProxy(JdbcTemplate jdbcTemplate,ShiroMD5PasswordEncoder pwdEndocer) {
		super();
		this.jdbcTemplate = jdbcTemplate;
		this.pwdEndocer = pwdEndocer;
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public boolean supports(Credential credential) {
		return credential instanceof HSEzCredential;
	}

	@Override
	protected Map<String, Object> authentication(EzCredential credential) {
		CloseableHttpClient httpclient = HttpClientBuilder.getInstance().createHttpClient();
		try {
			HSEzCredential hsCredential = (HSEzCredential)credential;
			String personId =  hsCredential.getId();
			String userInfoJson = getUser(httpclient, personId);
			DocumentContext dc = JsonPath.parse(userInfoJson);
			
			if(!hasUser(dc)) {
				createUser(dc);
			}
			String userName =  dc.read("$.data.userName")+"";
			Map<String,Object> user =getUserDetail(userName);
			
			user.put("from", "db");
			return user;
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	private boolean hasUser(DocumentContext dc) {
		String userName =  dc.read("$.data.userName")+"";
		List l = jdbcTemplate.queryForList(this.validateUserSQL, new Object[] {userName});
		return l.size() > 0;
	}
	
	private void createUser(DocumentContext dc) {
		String userName =  dc.read("$.data.userName")+"";
		String rolType =  dc.read("$.data.roleType")+"";
		String userRealName =  dc.read("$.data.userName")+"";
		String sysRole = "0".equals(rolType)?"operate":"super";
		
		long userId = insertUser(userName,sysRole);
		String password = pwdEndocer.encode(userId+"", "123456");
		updatePwd(password,userId);
		insertUserDetail(userId,userRealName,userName);
	}
	
	private  long insertUser(final String userName,final String sysRole) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS);
				int z = 0;
			    ps.setString(++z, userName);
			    ps.setInt(++z, 0);
			    ps.setString(++z, sysRole);
			    return ps;
			}
			
		}, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	private Map<String, Object> getUser(String sql, String username) {
		try {
			return jdbcTemplate.queryForMap(sql, username);
		}catch(Exception e) {
			logger.warn("User not Fond with '{}' arg '{}'",sql,username);
		}
		return null;
	}
	
	private Map<String, Object> getUserDetail(String username) {

			Map<String,Object> user = getUser(this.userDetailSql, username);
			if(user != null) {
				user.put("from", "db");
				return user;
			}
		
		return null;
	}
	
	private void updatePwd(String pwd,long userId) {
		this.jdbcTemplate.update(updateUserPwdSQL, new Object[] {pwd,userId});
	}
	
	private void insertUserDetail(final long userId,final String realName,final String account) {
		this.jdbcTemplate.update(insertUserDetailSQL, new Object[] {realName,0,0,userId,account});
	}
	
	private String getUser(CloseableHttpClient httpclient, String personId) {
		try {
			String url = this.userInfoUrl+personId+"&userType=1";
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Content-Type", "application/json; charset=utf-8");
			logger.debug("executing request {}", httpget.getURI());
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				StatusLine status = response.getStatusLine();
				if (status.getStatusCode() != 200) {
					logger.error("Principal ID from {} ",url);
					return null;
				}
					
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String json = EntityUtils.toString(entity);
					
					if (!"200".equals( JsonPath.read(json, "$.code"))) {
						logger.error("Principal ID from {} ",url);
						return null;
					}
					logger.debug("User from {}  is {}",this.userInfoUrl+personId,json);
					return json;
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage());
		} catch (ParseException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}
}
