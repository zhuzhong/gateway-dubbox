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

/** 限流 filter
 * @author Administrator
 *
 */
public class RatelimitFilter implements Filter{

    
    /**
     * 关于限流，可以作的很大，也可以很小，
     * 可以基于全集群，也可以基于单个jvm,
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
     
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
     
        
    }

    @Override
    public void destroy() {
     
        
    }

}
