/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.ez.cas.support.authentication.principal.CredentialBuilder;

/**
 * 通过CAS登录
 * 配置的路径为/cas/ez/login
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public class EzCasLoginController extends AbstractController{

	private CentralAuthenticationService centralAuthenticationService;
	
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
	
	private CredentialBuilder credentialBuilder;
	
	public EzCasLoginController(CentralAuthenticationService centralAuthenticationService,
			CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator,CredentialBuilder credentialBuilder) {
		this.logger.debug("Costomlize login for EzCasLoginController");
		this.centralAuthenticationService = centralAuthenticationService;
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
		this.credentialBuilder = credentialBuilder;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("EzLogin");
		bindTicketGrantingTicket(request, response);
		String viewName = getSignInView(request);
		return new ModelAndView(viewName);
	}
	
	protected void bindTicketGrantingTicket(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Credential credential = this.credentialBuilder.build(request);
		if(credential == null) {
			this.logger.warn("Credential is null !");
			return ;
		}
		this.logger.debug(credential.toString());
		TicketGrantingTicket ticketGrantingTicket = this.centralAuthenticationService.createTicketGrantingTicket(credential);
		this.ticketGrantingTicketCookieGenerator.addCookie(request, response, ticketGrantingTicket.getId());
	}

	protected String getSignInView(HttpServletRequest request) {
		String service = ServletRequestUtils.getStringParameter(request, "service", null);
		if(service == null)
			service = ServletRequestUtils.getStringParameter(request, "ezService", "");
		return "redirect:/login" + (service.length() > 0 ? "?service=" + service : "");
	}

}
