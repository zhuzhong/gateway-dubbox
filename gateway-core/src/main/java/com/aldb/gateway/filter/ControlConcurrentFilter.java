/**
 * 
 */
package com.aldb.gateway.filter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 并发量filter限制
 * 
 * @author Administrator
 *
 */
public class ControlConcurrentFilter implements Filter {

    private int concurrentLimit = 10000; // 并发上限

    public void setConcurrentLimit(int concurrentLimit) {
        this.concurrentLimit = concurrentLimit;
    }

    /**
     * 这个个人认为应为单jvm的并发量控制，可以采用简单的方式
     */

    private static AtomicInteger times = new AtomicInteger();// 原子计数器

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        try {
            if (times.incrementAndGet() > concurrentLimit) {
                return;
            }
            chain.doFilter(request, response);
        } finally {
            times.decrementAndGet();
        }

    }

    @Override
    public void destroy() {

    }

}
