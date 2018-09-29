/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jasig.cas.authentication.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ez.cas.support.authentication.handler.http.AuthenticationHandlerFromEduPlatform;
import com.ez.cas.support.authentication.principal.EduPlatformCredential;
import com.ez.cas.support.authentication.principal.EzCredential;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月11日
 * @Version 
 */
public class EduPlateformAuthenticationHandlerProxy extends AbstractAuthenticationHandlerProxy{
	
	private static Logger logger = LoggerFactory.getLogger(EduPlateformAuthenticationHandlerProxy.class);
	
	private EduPlatformBuilder eduPlatformBuilder;
	
	public EduPlateformAuthenticationHandlerProxy(EduPlatformBuilder eduPlatformBuilder) {
		this.eduPlatformBuilder = eduPlatformBuilder;
	}
	
	@Override
	public boolean supports(Credential credential) {
		return credential instanceof EduPlatformCredential;
	}

	@Override
	protected Map<String, Object> authentication(EzCredential credential) {
		EduPlatformCredential myCredential = (EduPlatformCredential)credential;
		AuthenticationHandlerFromEduPlatform eduPlatform = getEduPlatform(myCredential);
		if(eduPlatform == null)
			return null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String personId = eduPlatform.doAuthentication(httpclient, myCredential.getTicket());
			if(personId !=null && personId.length() >0) {
				myCredential.setPersonId(personId);
				Map<String,Object> result = eduPlatform.getUserInfo(httpclient, personId);
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}
	
	private AuthenticationHandlerFromEduPlatform getEduPlatform(EduPlatformCredential myCredential ) {
		AuthenticationHandlerFromEduPlatform eduPlatform = null;
		if(myCredential.getReferer()==null) {
			eduPlatform = eduPlatformBuilder.getEdutPlatform(myCredential.getPlatformcode());
		}else {
			eduPlatform = eduPlatformBuilder.getEdutPlatform(myCredential.getReferer(),myCredential.getPlatformcode());
			if(eduPlatform == null)
				eduPlatform = eduPlatformBuilder.getEdutPlatform(myCredential.getPlatformcode());
		}
		return eduPlatform;
	}
}
