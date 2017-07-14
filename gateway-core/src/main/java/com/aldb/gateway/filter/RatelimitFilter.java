/**
 * 
 */
package com.aldb.gateway.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.aldb.gateway.util.SystemConfigParaUtil;

/**
 * 限流 filter
 * 
 * @author Administrator
 *
 */
public class RatelimitFilter implements Filter {

    /**
     * 关于限流，可以作的很大，也可以很小， 可以基于全集群，也可以基于单个jvm,
     * 现在应用guava的令牌桶算法来解决限流的问题
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {

        SystemConfigParaUtil.acquire();
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }

}
