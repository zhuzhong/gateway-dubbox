/**
 * 
 */
package com.z.gateway.core.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
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

import com.z.gateway.common.util.CommonCodeConstants;
import com.z.gateway.core.OpenApiHttpClientService;

/**
 * @author sunff
 * 
 */
public class OpenApiHttpClientServiceImpl implements OpenApiHttpClientService {

    private static Log logger = LogFactory.getLog(OpenApiHttpClientServiceImpl.class);
    private static PoolingHttpClientConnectionManager manager = null;
    private static CloseableHttpClient httpClient = null;

    public void init() {
        initHttpClient();
    }

    private void initHttpClient() {
        // 注册访问协议相关的socket工厂
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSystemSocketFactory()).build();
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
    public String doHttpsPost(String url, String reqData, String contentType, String traceId) {
        return doPost(url, reqData, contentType, traceId);
    }

    @Override
    public String doPost(String url, String reqData, String contentType, String traceId) {
        String body = "";
        org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url);
        httpPost.setHeader("Content-type", contentType);
        httpPost.setHeader(CommonCodeConstants.TRACE_ID, traceId);
        httpPost.setEntity(new StringEntity(reqData, "utf-8"));
        try {
            // 执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = getHttpClient().execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                // 获取结果实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, "utf-8");
                }
                EntityUtils.consume(entity);
            }

            // 释放链接
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    
    @Override
    public String doGet(String webUrl, String traceId) {
        logger.info(String.format("run doGet method,weburl=%s", webUrl));
        String body = "";
        org.apache.http.client.methods.HttpGet httpGet = new org.apache.http.client.methods.HttpGet(webUrl);
        httpGet.setHeader(CommonCodeConstants.TRACE_ID, traceId);
        try {
            CloseableHttpResponse response = getHttpClient().execute(httpGet);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                if (entity != null) {
                    body = EntityUtils.toString(entity, "utf-8");
                }
                EntityUtils.consume(entity);
            }
            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }

    @Override
    public String doGet(String webUrl, Map<String, String> paramMap, String traceId) {
        logger.info(String.format("run doGet method,weburl=%s", webUrl));
        String url = webUrl;
        // 设置编码格式
        String queryString = createLinkString(paramMap);
        url = url + "?" + queryString;
        return doGet(url, traceId);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * 
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

public static void main(String args[]){
     OpenApiHttpClientServiceImpl p=new OpenApiHttpClientServiceImpl();
     p.init();
     System.out.println(p.doHttpsGet("https://www.baidu.com/","110"));
    }
    @Override
    public String doHttpsGet(String webUrl, String traceId) { // https 协议
        return doGet(webUrl, traceId);
    }

    @Override
    public String doHttpsGet(String webUrl, Map<String, String> paramMap, String traceId) {

        return doGet(webUrl, paramMap, traceId);
    }

    /*
     * public String doPost(String url, String reqData, String contentType,
     * String params) { return null; }
     * 
     * public Map<String, String> HttpGet(String webUrl, Map paramMap) {
     * 
     * return null; }
     * 
     * public Map<String, String> HttpGet(String url, String method, Map
     * paramMap) {
     * 
     * return null; }
     * 
     * public String HttpPost(String webUrl, Map paramMap) { return null; }
     * 
     * public String HttpPost(String url, String method, Map paramMap) {
     * 
     * return null; }
     */
}
