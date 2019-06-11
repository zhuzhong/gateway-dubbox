package com.z.nettyserver;

import javax.servlet.ServletException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class GateWayWebServer {

    //private static Logger logger = LoggerFactory.getLogger(GateWayWebServer.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext.xml");
        ctx.start();
        Integer port = 8080;
        DispatcherServlet servlet = getDispatcherServlet(ctx);
        NettyHttpServer server = new NettyHttpServer(port, servlet);
        server.start();
     

    }

    public static DispatcherServlet getDispatcherServlet(ApplicationContext ctx) {

        XmlWebApplicationContext mvcContext = new XmlWebApplicationContext();

        mvcContext.setConfigLocation("classpath*:spring/spring-mvc.xml");
        mvcContext.setParent(ctx);
        MockServletConfig servletConfig = new MockServletConfig(mvcContext.getServletContext(), "dispatcherServlet");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(mvcContext);
        try {
            dispatcherServlet.init(servletConfig);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return dispatcherServlet;
    }
}
