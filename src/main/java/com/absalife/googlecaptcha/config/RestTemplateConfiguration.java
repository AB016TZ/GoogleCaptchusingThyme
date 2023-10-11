/**
 * 
 */
package com.absalife.googlecaptcha.config;

/**
 * @author AB016TZ
 *
 */
/**
 * 
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Collections;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * @author AB016TZ
 *
 */
@Configuration
public class RestTemplateConfiguration{
	
	public RestTemplateConfiguration() {
		
	}
	@Value("${trust-store}")
    private Resource trustStore;

    @Value("${trust-store-password}")
    private String trustStorePassword;
    
    @Bean
	public RestTemplate getRestTemplate() throws Exception {
		SSLContext sslContext = new SSLContextBuilder()
			      .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
			      .build();
			    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
			    HttpClient httpClient = HttpClients.custom()
			      .setSSLSocketFactory(socketFactory)
			      .build();
			    HttpComponentsClientHttpRequestFactory factory = 
			      new HttpComponentsClientHttpRequestFactory(httpClient);
			    return new RestTemplate(factory);
	}

}


