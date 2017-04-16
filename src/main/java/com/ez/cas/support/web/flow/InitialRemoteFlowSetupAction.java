/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.web.flow;

import org.jasig.cas.web.flow.InitialFlowSetupAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
/**
 * 
 * @author liguiqing
 * @Date 2017年4月16日
 * @Version 
 */
public final class InitialRemoteFlowSetupAction extends AbstractAction{
	 private final Logger logger = LoggerFactory.getLogger(InitialRemoteFlowSetupAction.class);

	 private InitialFlowSetupAction actionProxy;
	 
	 public InitialRemoteFlowSetupAction(InitialFlowSetupAction actionProxy) {
		 this.actionProxy = actionProxy;
	 }
	 
	@Override
	protected Event doExecute(RequestContext context) throws Exception {
		try {
			return actionProxy.execute(context);
		}catch(Exception e) {
			logger.warn(e.getMessage());
		}
		return result("success");
	}

}
