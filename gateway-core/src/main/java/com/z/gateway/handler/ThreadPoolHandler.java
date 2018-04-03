/**
 * 
 */
package com.z.gateway.handler;

import com.z.gateway.protocol.AbstractTask;

/**
 * @author Administrator
 *
 */
public interface ThreadPoolHandler {

    
    public Object addTask(AbstractTask task);
}
