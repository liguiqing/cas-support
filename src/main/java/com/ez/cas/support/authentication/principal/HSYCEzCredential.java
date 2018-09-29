/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiasheng.api.JiashengApiException;
import com.jiasheng.api.JiashengOAuthException;
import com.jiasheng.api.utils.SsoUtil;

/**
 * 山西寰烁运城单点登录凭证
 * 
 * @author liguiqing
 * @Date 2018年9月29日
 * @Version 
 */
public class HSYCEzCredential extends HSEzCredential {
	private static Logger logger = LoggerFactory.getLogger(HSYCEzCredential.class);
	
	public HSYCEzCredential() {
		super();
	}
	
	public HSYCEzCredential(String fromUrl) {
		super( fromUrl);
	}
	
	@Override
	protected String getUserJson(HttpServletRequest request) {
		logger.debug("Login to YC Plamtform");
		String userJson = null;
		try {
			userJson = SsoUtil.getUser(request);
		} catch (JiashengApiException e) {
			logger.error(e.getErrorMsg());
		} catch (JiashengOAuthException e) {
			logger.error(e.getErrorDesp());
		}
		return userJson;
	}
}
