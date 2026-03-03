package com.vendingmachine.resttemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

//@Configuration
public class RestTemplateConfig {

	//private CloseableHttpClient httpClient;
	
	@Bean(name = "defauletRestTemplate")
	public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
    	 SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
         factory.setReadTimeout(15000);//讀取超時時間，單位為ms
         factory.setConnectTimeout(10000);//連線超時時間，單位為ms
         return factory;
    }
    
    @Bean(name = "httpsRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate(httpRequestFactory());
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
    	HttpComponentsClientHttpRequestFactory hc = new HttpComponentsClientHttpRequestFactory();
    	//hc.setHttpClient(new HttpClient());
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }
    
    @Bean
    public HttpClient httpClient() {
    	TrustStrategy trustStrategy = (X509Certificates, s) -> true;
        try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build();
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient = HttpClients.custom()
					//.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
					//						.setSSLSocketFactory(socketFactory).build()
					.build();
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			//requestFactory.setHttpClient(httpClient);
			requestFactory.setConnectionRequestTimeout(5000);
			requestFactory.setConnectTimeout(10000);
			return null;
		} catch (Exception e) {
			throw new RuntimeException();
		}
    }
    	
    	
        
    
    
}
