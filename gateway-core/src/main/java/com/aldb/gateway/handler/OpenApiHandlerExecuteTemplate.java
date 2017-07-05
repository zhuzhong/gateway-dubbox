/**
 * 
 */
package com.aldb.gateway.handler;

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Context;

/**
 * @author Administrator
 *
 */
public interface OpenApiHandlerExecuteTemplate extends Chain{

    /**
     * @param blCtx
     */
    boolean execute(Context chainContext) throws Exception ;

}
