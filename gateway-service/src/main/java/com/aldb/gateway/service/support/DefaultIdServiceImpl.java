/**
 * 
 */
package com.aldb.gateway.service.support;

import java.util.concurrent.atomic.AtomicInteger;

import com.aldb.gateway.service.IdService;

/**
 * @author Administrator
 *
 */

public class DefaultIdServiceImpl implements IdService {

    private AtomicInteger ai = new AtomicInteger(1);

    @Override
    public String genInnerRequestId() {
        return String.valueOf(ai.getAndIncrement());
    }
    
    

}
