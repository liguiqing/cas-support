package com.ez.cas.support.web;

import com.jiasheng.api.JiashengOAuthToken;
import com.jiasheng.api.utils.JiashengUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class TokenController extends AbstractController {
    private static Logger logger = LoggerFactory.getLogger(TokenController.class);

    private String tenantId;

    private String clientId;

    private String clientSecret;

    private String passportServerUrl = "https://passport.yceduyun.com/passport";

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("passport_server_url",passportServerUrl);
        String token =  JiashengUtil.getTokenByClientCredentials(clientId,clientSecret,request,response).getAccessToken();
        Map<String,String> model = new HashMap<>();
        model.put("token", token);
        model.put("tenantId",tenantId);
        ModelAndView view = new ModelAndView("token");
        view.addAllObjects(model);
        return view;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
