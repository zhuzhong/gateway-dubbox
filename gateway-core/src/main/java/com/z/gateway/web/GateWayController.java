/**
 * 
 */
package com.z.gateway.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.z.gateway.handler.OpenApiAcceptHandler;

/**
 * @author Administrator
 *
 */
@Controller
public class GateWayController {

    //private Logger logger=LoggerFactory.getLogger(GateWayController.class);
    
   @Autowired
    private OpenApiAcceptHandler acceptHandler;
    //这个供外部使用
    @RequestMapping(value = {"/**",""},method = {RequestMethod.POST,RequestMethod.GET})
    public void service(HttpServletRequest request, HttpServletResponse response) {
      // logger.info("receive request...");
       this.acceptHandler.acceptRequest(request, response);
    }
    
    
}
