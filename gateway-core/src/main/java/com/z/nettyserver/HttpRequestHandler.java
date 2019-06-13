package com.z.nettyserver;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.z.gateway.filter.GateWayRequestWrapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    public HttpRequestHandler(DispatcherServlet servlet) {
		this.servlet = servlet;
		this.servletContext = servlet.getServletConfig().getServletContext();
	}

	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
    	logger.error(e.getMessage(),e);
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
    	boolean flag = HttpMethod.POST.equals(fullHttpRequest.method())
                || HttpMethod.GET.equals(fullHttpRequest.method());
        
        Map<String, String>  parammap = getRequestParams(ctx,fullHttpRequest);
        if(flag && ctx.channel().isActive()){
           
            MockHttpServletRequest servletRequest =new MockHttpServletRequest(servletContext);
            // headers
            for (String name : fullHttpRequest.headers().names()) {
                for (String value : fullHttpRequest.headers().getAll(name)) {
                    servletRequest.addHeader(name, value);
                }
            }
            String uri = fullHttpRequest.uri();
            uri = new String(uri.getBytes("ISO8859-1"), "UTF-8");
            uri = URLDecoder.decode(uri, "UTF-8");
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
            String path = uriComponents.getPath();
            path = URLDecoder.decode(path, "UTF-8");
            servletRequest.setRequestURI(path);
            servletRequest.setServletPath(path);
            servletRequest.setMethod(fullHttpRequest.method().name());

            if (uriComponents.getScheme() != null) {
                servletRequest.setScheme(uriComponents.getScheme());
            }
            if (uriComponents.getHost() != null) {
                servletRequest.setServerName(uriComponents.getHost());
            }
            if (uriComponents.getPort() != -1) {
                servletRequest.setServerPort(uriComponents.getPort());
            }

            ByteBuf content = fullHttpRequest.content();
            content.readerIndex(0);
            byte[] data = new byte[content.readableBytes()];
            content.readBytes(data);
            servletRequest.setContent(data);

            try {
                if (uriComponents.getQuery() != null) {
                    String query = UriUtils.decode(uriComponents.getQuery(),"UTF-8");
                    servletRequest.setQueryString(query);
                }
                if(parammap!=null&&parammap.size()>0){
                	for (String key : parammap.keySet()) {
                        servletRequest.addParameter(UriUtils.decode(key,"UTF-8"), UriUtils.decode(parammap.get(key) == null ? "": parammap.get(key), "UTF-8"));
                    }
                }
                
            } catch (UnsupportedEncodingException ex) {
            	ex.printStackTrace();
            }
            //在这里增加filter
            GateWayRequestWrapper requestWrapper = new GateWayRequestWrapper((HttpServletRequest)servletRequest);
            //HTTP请求、GET/POST
            MockHttpServletResponse servletResponse = new MockHttpServletResponse();
            servletResponse.setCharacterEncoding(CharsetUtil.UTF_8.name());
            this.servlet.service(requestWrapper, servletResponse);
            
            //this.servlet.service(servletRequest,servletResponse);

            HttpResponseStatus status = HttpResponseStatus.valueOf(servletResponse.getStatus());
            
            String result =new String(servletResponse.getContentAsByteArray(), CharsetUtil.UTF_8);
                    
                    // servletResponse.getContentAsString();
            result = StringUtils.isEmpty(result)?"":result;
           // System.out.println("返回的内容="+result);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,//Unpooled.copiedBuffer(result,CharsetUtil.UTF_8)
                    Unpooled.copiedBuffer(servletResponse.getContentAsByteArray())
                    );
            response.headers().set("Content-Type", "text/json;charset=UTF-8");
            response.headers().set("Access-Control-Allow-Origin", "*");
            response.headers().set("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With,X-File-Name");
            response.headers().set("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
            response.headers().set("Content-Length", Integer.valueOf(response.content().readableBytes()));
            response.headers().set("Connection", "keep-alive");
            ChannelFuture writeFuture = ctx.writeAndFlush(response);
            writeFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

	 /**
     * 获取post请求、get请求的参数保存到map中
     */
    private Map<String, String> getRequestParams(ChannelHandlerContext ctx, HttpRequest req){
        Map<String, String>requestParams=new HashMap<String, String>();
        // 处理get请求  
        if (req.getMethod() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());  
            Map<String, List<String>> parame = decoder.parameters();  
            Iterator<Entry<String, List<String>>> iterator = parame.entrySet().iterator();
            while(iterator.hasNext()){
                Entry<String, List<String>> next = iterator.next();
                requestParams.put(next.getKey(), next.getValue().get(0));
            }
        }
         // 处理POST请求  
        if (req.getMethod() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(  
                    new DefaultHttpDataFactory(false), req);  
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas(); //
            for(InterfaceHttpData data:postData){
                if (data.getHttpDataType() == HttpDataType.Attribute) {  
                    MemoryAttribute attribute = (MemoryAttribute) data;  
                    requestParams.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        return requestParams;
    }

	private static final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);
	private final DispatcherServlet servlet;
	private final ServletContext servletContext;
}
