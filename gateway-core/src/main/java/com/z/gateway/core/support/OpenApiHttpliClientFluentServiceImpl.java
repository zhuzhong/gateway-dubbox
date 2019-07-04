/**
 * 
 */
package com.z.gateway.core.support;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z.gateway.core.OpenApiHttpClientService;

/**这个类暂时不要用，不太稳定
 * @author sunff
 *
 */
public class OpenApiHttpliClientFluentServiceImpl extends AbstractOpenApiHttpClientService
		implements OpenApiHttpClientService {

	private Logger logger = LoggerFactory.getLogger(OpenApiHttpliClientFluentServiceImpl.class);
	private final static int DefaultMaxPerRoute = 200;
	private final static int DefaultMaxTotal = 400;
	private static PoolingHttpClientConnectionManager manager;
	private static HttpClient client;
	private static Executor executor;

		
	private int maxPerRoute=DefaultMaxPerRoute;
	private int maxTotal=DefaultMaxTotal;
	
	
	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	@Override
	public void init() {
		initHttpClient();
	}

	private void initHttpClient() {
		LayeredConnectionSocketFactory ssl = null;
		try {
			ssl = SSLConnectionSocketFactory.getSystemSocketFactory();
		} catch (final SSLInitializationException ex) {
			final SSLContext sslcontext;
			try {
				sslcontext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
				sslcontext.init(null, null, null);
				ssl = new SSLConnectionSocketFactory(sslcontext);
			} catch (final SecurityException ignore) {
			} catch (final KeyManagementException ignore) {
			} catch (final NoSuchAlgorithmException ignore) {
			}
		}
		final Registry<ConnectionSocketFactory> sfr = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", ssl != null ? ssl : SSLConnectionSocketFactory.getSocketFactory()).build();

		manager = new PoolingHttpClientConnectionManager(sfr);
		manager.setDefaultMaxPerRoute(maxPerRoute);
		manager.setMaxTotal(maxTotal);
		
		client = HttpClientBuilder.create().setConnectionManager(manager).build();
		executor = Executor.newInstance(client);
	}

	@Override
	public String doGet(String webUrl, Map<String, String> requestHeader) {

		try {
			Request r=Request.Get(webUrl);
			 if (requestHeader != null && usingHead) {
	                for(Map.Entry<String, String> kv:requestHeader.entrySet()) {
	                    if (kv.getKey().equalsIgnoreCase("Content-Length")) {
	                        continue;
	                    }
	                        r.addHeader(kv.getKey(), kv.getValue());
	                }
	            }
			return executor.execute(r.useExpectContinue()
					).returnContent().asString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "no content return";
	}

	@Override
	public String doPost(String url, String reqData, Map<String, String> requestHeader) {

		try {
			Request r=Request.Post(url);
			
			// 将所有的header都传过去
	       
	        
	        if (requestHeader != null && usingHead) {
				for(Map.Entry<String, String> kv:requestHeader.entrySet()) {
				    if (kv.getKey().equalsIgnoreCase("Content-Length")) {
                        continue;
                    }
						r.addHeader(kv.getKey(), kv.getValue());
				}
			}
			return executor.execute(r
			        .useExpectContinue()
			        .bodyString(reqData, ContentType.APPLICATION_JSON)
			        )
			        .returnContent().asString();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return "no content return";
	}

	@Override
	public String doGet(String webUrl, Map<String, String> paramMap, Map<String, String> requestHeader) {

		logger.info(String.format("run doGet2 method,weburl=%s", webUrl));
		String url = webUrl;
		// 设置编码格式
		String queryString = createLinkString(paramMap);
		url = url + "?" + queryString;
		return doGet(url, requestHeader);
	}

	@Override
	public String doHttpsGet(String webUrl, Map<String, String> requestHeader) {

		return doGet(webUrl, requestHeader);
	}

	@Override
	public String doHttpsGet(String webUrl, Map<String, String> paramMap, Map<String, String> requestHeader) {
		return doGet(webUrl, paramMap, requestHeader);
	}

	@Override
	public String doHttpsPost(String url, String reqData, Map<String, String> requestHeader) {
		return doPost(url, reqData, requestHeader);
	}

}
