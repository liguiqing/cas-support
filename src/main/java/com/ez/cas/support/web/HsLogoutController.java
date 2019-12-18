/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.web;

import com.jiasheng.api.utils.SsoUtil;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author liguiqing
 * @Date 2017年10月26日
 * @Version 
 */
public class HsLogoutController extends AbstractController{

	private String logoutFollowService = "http://ks.easytnt.com:89/cas/hslogin?service=http://ks.easytnt.com:89/ez/caslogin";
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.debug("Logout to " + logoutFollowService);
		SsoUtil.logOut(request,response,logoutFollowService);
		String viewName = getSignInView(request);
		return new ModelAndView(viewName);
	}

	protected String getSignInView(HttpServletRequest request) {
		String service = ServletRequestUtils.getStringParameter(request, "service", null);
		if(service == null)
			service = ServletRequestUtils.getStringParameter(request, "ezService", "");
		logger.debug("log out to "+service+" after hslogout ");
		return "redirect:/hslogin" + (service.length() > 0 ? "?service=" + service : "");
	}

	public void setLogoutFollowService(String logoutFollowService) {
		this.logoutFollowService = logoutFollowService;
	}
}
