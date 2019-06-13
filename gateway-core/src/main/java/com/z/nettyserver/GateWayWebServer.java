package com.z.nettyserver;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class GateWayWebServer {

    // private static Logger logger =
    // LoggerFactory.getLogger(GateWayWebServer.class);

    public static void main(String[] args) {
        Integer port = 8480;
        String contextPath = "/gw";
        if (args.length == 2) {
            port = Integer.valueOf(args[0]);
            contextPath = args[1];
            if (!contextPath.startsWith("/")) {
                contextPath = "/" + contextPath;
            }
        } else {
            System.out.println("GateWayWebServer参数不是两个，所以使用默认值,port=8480,contextpath=gw");
        }

        new GateWayWebServer(port, contextPath).start();
    }

    private final int port;
    private final String contextPath;

    public GateWayWebServer() {
        this(8080, "/gw");
    }

    public GateWayWebServer(int port, String path) {
        this.port = port;
        this.contextPath = path;

    }

    public void start() {
        try {
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                    "classpath:/spring/netty/applicationContext-netty.xml");
            ctx.start();

            MockServletContext servletContext = new MockServletContext();
            servletContext.setContextPath(contextPath);

            XmlWebApplicationContext mvcContext = new XmlWebApplicationContext();
            mvcContext.setServletContext(servletContext);

            mvcContext.setConfigLocation("classpath*:spring/netty/spring-mvc-netty.xml");
            mvcContext.setParent(ctx);

            MockServletConfig servletConfig = new MockServletConfig(mvcContext.getServletContext(),
                    "dispatcherServlet");
            DispatcherServlet dispatcherServlet = new DispatcherServlet(mvcContext);

            dispatcherServlet.init(servletConfig);

            NettyHttpServer server = new NettyHttpServer(port, dispatcherServlet);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("启动失败");
        }
        
    }

   /* private DispatcherServlet getDispatcherServlet(ApplicationContext ctx) {

        MockServletContext servletContext = new MockServletContext();
        servletContext.setContextPath(contextPath);

        XmlWebApplicationContext mvcContext = new XmlWebApplicationContext();
        mvcContext.setServletContext(servletContext);

        mvcContext.setConfigLocation("classpath*:spring/netty/spring-mvc-netty.xml");
        mvcContext.setParent(ctx);

        MockServletConfig servletConfig = new MockServletConfig(mvcContext.getServletContext(), "dispatcherServlet");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(mvcContext);
        try {
            dispatcherServlet.init(servletConfig);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return dispatcherServlet;
    }*/
}
