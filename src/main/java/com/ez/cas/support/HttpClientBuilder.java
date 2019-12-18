package com.ez.cas.support;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * http client
 * @author liguiqing
 * @Date 2017年10月26日
 * @Version
 */
public class HttpClientBuilder implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(HttpClientBuilder.class);

    private String[] supportedProtocols;

    private boolean sslabled = false;

    private static HttpClientBuilder httpClientBuilder = new HttpClientBuilder();

    private static ApplicationContext applicationContext;

    private HttpClientConnectionManager httpClientConnectionManager;

    public HttpClientBuilder() {
        this.supportedProtocols = new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"};
    }

    public void setSslabled(boolean sslabled) {
        this.sslabled = sslabled;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        HttpClientBuilder.applicationContext = applicationContext;
    }

    public CloseableHttpClient createHttpClient(){
        if(sslabled && Objects.isNull(this.httpClientConnectionManager)){
            this.createConnectionManager();
        }

        if(sslabled) {
            return HttpClients.custom().setConnectionManager(this.httpClientConnectionManager).build();
        }else{
            return HttpClients.createDefault();
        }
    }

    private void createConnectionManager(){
        SSLConnectionSocketFactory sslsf = null;
        SSLContextBuilder builder = null;

        builder = new SSLContextBuilder();
        //全部信任 不做身份鉴定
        try {
            builder.loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true);
            sslsf = new SSLConnectionSocketFactory(builder.build(), this.supportedProtocols, null, NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("https", sslsf)
                    .register("http", new PlainConnectionSocketFactory())
                    .build();
            this.httpClientConnectionManager =  new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        } catch (KeyStoreException e) {
            logger.error(e.getMessage());
        } catch (KeyManagementException e) {
            logger.error(e.getMessage());
        }
    }

    public static HttpClientBuilder getInstance(){
        HttpClientBuilder builder = null;
        if(!Objects.isNull(applicationContext)){
            builder = applicationContext.getBean(HttpClientBuilder.class);
        }
        if(Objects.isNull(builder)){
            return httpClientBuilder;
        }
        return builder;
    }
}
