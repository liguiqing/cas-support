/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.principal;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.Credential;

/**
 * 
 * @author liguiqing
 * @Date 2017年4月10日
 * @Version 
 */
public class CredentialBuilder {
    
	private HashSet<EzCredential> credentials = new HashSet<>();
	
	public CredentialBuilder(Collection<EzCredential> ezCredentials) {
		credentials.addAll(ezCredentials);
	}
	
	public void register(EzCredential credential) {
		credentials.add(credential);
	}
	
	public Credential build(HttpServletRequest request) {
		for(EzCredential c:this.credentials) {
			Credential credential = c.newInstanceOf(request);
			if(credential != null)
				return credential;
		}
		return null;
	}
	
}
