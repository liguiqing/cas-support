/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jasig.cas.authentication.Credential;

/**
 * 教育云免登录凭证
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public class PlatformPersonidCredential  extends AbstractEzCredenttial {

	private String personIdParam = "personid" ;
	
	private String ltParam = "lt" ;
	
	private String executionParam = "execution";
	
	private String platformUrlParam = "platformUrl" ;
	
	private String personId ;
	
	private String lt ;
	
	private String execution ;
	
	private String platformUrl ;
	
	public PlatformPersonidCredential() {
		
	}
	
	public PlatformPersonidCredential(String personIdParam, String ltParam, String executionParam, String platformUrlParam) {
		super();
		this.personIdParam = personIdParam;
		this.ltParam = ltParam;
		this.executionParam = executionParam;
		this.platformUrlParam = platformUrlParam;
	}

	public String getPersonId() {
		return personId;
	}

	public String getLt() {
		return lt;
	}

	public String getExecution() {
		return execution;
	}

	public String getPlatformUrl() {
		return platformUrl;
	}

	@Override
	public String getId() {
		return this.personId;
	}

	@Override
	public Credential newInstanceOf(HttpServletRequest request) {
		if(supports(request,this.personIdParam,this.platformUrlParam)) {
			PlatformPersonidCredential credential=  new PlatformPersonidCredential();
			credential.personId = request.getParameter(this.personIdParam);
			credential.lt = request.getParameter(this.ltParam);
			credential.execution = request.getParameter(this.executionParam);
			credential.platformUrl = request.getParameter(this.platformUrlParam);
			return credential;
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(this.personId).append(this.platformUrl).build();
	}
}
