/**
 * 
 */
package com.z.gateway.core.support;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.z.gateway.core.OpenApiHttpClientService;

/**
 * @author sunff
 * 
 */
public class OpenApiHttpClientServiceImpl extends AbstractOpenApiHttpClientService implements OpenApiHttpClientService {

	private static Logger logger = LoggerFactory.getLogger(OpenApiHttpClientServiceImpl.class);
	private static PoolingHttpClientConnectionManager manager = null;
	private static CloseableHttpClient httpClient = null;

	

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
		// 注册访问协议相关的socket工厂
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", ssl//SSLConnectionSocketFactory.getSystemSocketFactory()
						)
				.build();
		// httpclient 工厂
		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
				DefaultHttpRequestWriterFactory.INSTANCE, DefaultHttpResponseParserFactory.INSTANCE);
		// dns解析器
		DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
		// 创建池化连接管理器
		manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory, dnsResolver);
		// 默认socket配置
		SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		manager.setDefaultSocketConfig(defaultSocketConfig);

		manager.setMaxTotal(this.maxTotal);// 设置整个连接池的最大连接数
		manager.setDefaultMaxPerRoute(this.defaultMaxPerRoute);// 每个路由最大连接数
		manager.setValidateAfterInactivity(this.validateAfterInactivity);

		RequestConfig defaultRequestConfig = RequestConfig.custom().setConnectTimeout(this.connectionTimeout)
				// 2s
				.setSocketTimeout(this.socketTimeout)
				// 5s
				.setConnectionRequestTimeout(this.connectionRequestTimeout).build();
		httpClient = HttpClients.custom().setConnectionManager(manager).setConnectionManagerShared(false)
				.evictIdleConnections(60, TimeUnit.SECONDS).evictExpiredConnections()
				.setConnectionTimeToLive(60, TimeUnit.SECONDS).setDefaultRequestConfig(defaultRequestConfig)
				.setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
				.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
				.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					httpClient.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	private CloseableHttpClient getHttpClient() {
		return httpClient;

	}

	private int maxTotal = 300;
	private int defaultMaxPerRoute = 200;
	private int validateAfterInactivity = 5000;
	private int connectionTimeout = 2000;// 2s
	private int socketTimeout = 5000;// 5s
	private int connectionRequestTimeout = 2000;

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public void setValidateAfterInactivity(int validateAfterInactivity) {
		this.validateAfterInactivity = validateAfterInactivity;
	}

	@Override
	public String doHttpsPost(String url, String reqData, Map<String, String> requestHeader) {
		return doPost(url, reqData, requestHeader);
	}

	@Override
	public String doPost(String url, String reqData, Map<String, String> requestHeader) {
		String body = null;
		org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url);
		// 将所有的header都传过去
		if (requestHeader != null && usingHead) {

			for (Map.Entry<String, String> kv : requestHeader.entrySet()) {
				if (kv.getKey().equalsIgnoreCase("Content-Length")) {
					continue;
				}
				httpPost.addHeader(kv.getKey(), kv.getValue());
			}

		}

		httpPost.setEntity(new StringEntity(reqData, "utf-8"));
		try {
			// 执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = getHttpClient().execute(httpPost);
			try {
				logger.info("response响应=" + JSON.toJSONString(response));
			} catch (Exception e) {
				logger.info("log response");
				e.printStackTrace();
			}

			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode=" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				// 获取结果实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					if (requestHeader != null) {
						requestHeader.clear();
						Header[] hs = response.getAllHeaders();
						for (Header h : hs) {
							requestHeader.put(h.getName(), h.getValue()); // 把头设回去
						}
					}
					// 按指定编码转换结果实体为String类型
					body = EntityUtils.toString(entity, "utf-8");
				}
				EntityUtils.consume(entity);

			}

			// 释放链接
			response.close();
		} catch (IOException e) {
			logger.error("url={}调用失败", e);
			e.printStackTrace();
		}
		return body;
	}

//    private String byte2String(HttpEntity entity) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            entity.writeTo(baos);
//            byte[] bs = baos.toByteArray();           
//          String result=  StringResponseUtil.encodeResp(bs);   
//            return result;
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
//    }
	/*
	 * private byte[] resbyte2(HttpEntity entity) { ByteArrayOutputStream baos = new
	 * ByteArrayOutputStream(); try { entity.writeTo(baos); byte[] bs =
	 * baos.toByteArray();
	 * 
	 * FileOutputStream fos=new FileOutputStream("e:/test1.jpg"); fos.write(bs);
	 * fos.flush(); fos.close();
	 * 
	 * String result= org.apache.commons.codec.binary.Base64.encodeBase64String(bs);
	 * byte[] bsnew= org.apache.commons.codec.binary.Base64.decodeBase64(result);
	 * 
	 * sun.misc.BASE64Encoder be=new sun.misc.BASE64Encoder(); String
	 * result=be.encode(bs);
	 * 
	 * sun.misc.BASE64Decoder db=new sun.misc.BASE64Decoder(); byte[]
	 * bsnew=db.decodeBuffer(result); fos=new FileOutputStream("e:/test2.jpg");
	 * fos.write(bsnew); fos.flush(); fos.close();
	 * 
	 * 
	 * 
	 * return bs; } catch (IOException e) { throw new IllegalStateException(e); } }
	 */

	@Override
	public String doGet(String webUrl, Map<String, String> requestHeader) {
		logger.info(String.format("run doGet method,weburl=%s", webUrl));
		String body = null;
		org.apache.http.client.methods.HttpGet httpGet = new org.apache.http.client.methods.HttpGet(webUrl);
		// 将所有的header都传过去
		if (requestHeader != null && usingHead) {

			for (Map.Entry<String, String> kv : requestHeader.entrySet()) {
				if (kv.getKey().equalsIgnoreCase("Content-Length")) {
					continue;
				}
				httpGet.addHeader(kv.getKey(), kv.getValue());
			}
		}
		try {
			CloseableHttpResponse response = getHttpClient().execute(httpGet);
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {

				if (entity != null) {
					if (requestHeader != null) {
						// requestHeader.clear();
						Header[] hs = response.getAllHeaders();
						for (Header h : hs) {
							requestHeader.put(h.getName(), h.getValue()); // 把头设回去
						}
					}
					body = EntityUtils.toString(entity, "utf-8");
					// body = byte2String(entity);
					// System.out.println("init return body hashCode=" + body.hashCode());
					// System.out.println("init return body=" + body);
				}

				EntityUtils.consume(entity);
			}
			// response.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
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
	public String doHttpsGet(String webUrl, Map<String, String> requestHeader) { // https
																					// 协议
		return doGet(webUrl, requestHeader);
	}

	@Override
	public String doHttpsGet(String webUrl, Map<String, String> paramMap, Map<String, String> requestHeader) {

		return doGet(webUrl, paramMap, requestHeader);
	}

}
