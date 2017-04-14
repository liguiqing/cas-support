/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.principal;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.Credential;

/**
 * 教育云平台凭证
 * 
 * @author liguiqing
 * @Date 2017年4月11日
 * @Version
 */
public class EduPlatformCredential extends AbstractEzCredenttial {

	private String personId;

	private  String tk;

	private  String ticket;

	private  String platformcode;

	private  String s;

	private String tkParam = "tk";

	private String ticketParam = "ticket";

	private String platformcodeParam = "platformcode";

	private String sParam = "s";
	
	private String referer = "";

	public EduPlatformCredential() {

	}
	
	private EduPlatformCredential(String tk, String ticket, String platformcode, String s) {
		super();
		this.tk = tk;
		this.ticket = ticket;
		this.platformcode = platformcode;
		this.s = s;
	}

	@Override
	public Credential newInstanceOf(HttpServletRequest request) {
		if (this.supports(request, this.tkParam, this.ticketParam, this.platformcodeParam)) {
			EduPlatformCredential credential = new EduPlatformCredential(request.getParameter(this.tkParam),
					request.getParameter(this.ticketParam), request.getParameter(this.platformcodeParam),
					request.getParameter(this.sParam));
			
			credential.referer = request.getHeader("Referer");
			return credential;
		}
		return null;
	}

	@Override
	public String getId() {
		return this.personId;
	}

	public String getTkParam() {
		return tkParam;
	}

	public void setTkParam(String tkParam) {
		this.tkParam = tkParam;
	}

	public String getTicketParam() {
		return ticketParam;
	}

	public void setTicketParam(String ticketParam) {
		this.ticketParam = ticketParam;
	}

	public String getPlatformcodeParam() {
		return platformcodeParam;
	}

	public void setPlatformcodeParam(String platformcodeParam) {
		this.platformcodeParam = platformcodeParam;
	}

	public String getsParam() {
		return sParam;
	}

	public void setsParam(String sParam) {
		this.sParam = sParam;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getTk() {
		return tk;
	}

	public String getTicket() {
		return ticket;
	}

	public String getPlatformcode() {
		return platformcode;
	}

	public String getReferer() {
		return referer;
	}

	public String getS() {
		return s;
	}
}
