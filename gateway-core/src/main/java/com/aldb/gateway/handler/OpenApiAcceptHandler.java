/**
 * 
 */
package com.aldb.gateway.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 *
 */
public interface OpenApiAcceptHandler {

    /**
     * @param request
     * @param response
     */
    void acceptRequest(HttpServletRequest request, HttpServletResponse response);
    
}
