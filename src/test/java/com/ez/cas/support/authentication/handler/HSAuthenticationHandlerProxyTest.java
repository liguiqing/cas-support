/**
 * <p><b>© 2016 深圳市易考试乐学测评有限公司</b></p>
 * 
 **/
package com.ez.cas.support.authentication.handler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;

/**
 * 
 * @author liguiqing
 * @Date 2017年8月24日
 * @Version 
 */
public class HSAuthenticationHandlerProxyTest {
	private static Logger logger = LoggerFactory.getLogger(HSAuthenticationHandlerProxyTest.class);
	
	private String userInfoUrl = "https://demo.userapi.hseduyun.com/usersApi/getTecherLoginBaseInfo?userId=";
	@Test
	public void testGetUserInfo()throws Exception{
		CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(getConnectionManager()).build();
		String userJson = getUser(httpclient,"2246370");
		
		Map user = JsonPath.parse(userJson).read("$.data");
		assertNotNull(user);
		assertEquals(user.get("userId"),334);
	}
	
	private String getUser(CloseableHttpClient httpclient, String personId) {
		try {
			String url = this.userInfoUrl+personId;
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
					logger.debug("User is {}" , json);
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

	private HttpClientConnectionManager getConnectionManager(){
		SSLConnectionSocketFactory sslsf = null;
		SSLContextBuilder builder = null;

		builder = new SSLContextBuilder();
		//全部信任 不做身份鉴定
		try {
			builder.loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true);
			sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);

			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("https", sslsf)
					.register("http", new PlainConnectionSocketFactory())
					.build();
			return new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (KeyStoreException e) {
			logger.error(e.getMessage());
		} catch (KeyManagementException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
}
