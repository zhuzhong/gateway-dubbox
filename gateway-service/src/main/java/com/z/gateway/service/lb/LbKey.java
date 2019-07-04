/**
 * 
 */
package com.z.gateway.service.lb;

import java.io.Serializable;

/**
 * @author sunff
 *
 */
public class LbKey implements Serializable{

    
    private final String apiId;
    private final String version;
    public String getApiId() {
        return apiId;
    }
    public String getVersion() {
        return version;
    }
    public LbKey(String apiId, String version) {
        super();
        this.apiId = apiId;
        this.version = version;
    }
  
    
    
}
