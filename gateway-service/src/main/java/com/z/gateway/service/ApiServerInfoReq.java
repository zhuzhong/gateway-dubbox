/**
 * 
 */
package com.z.gateway.service;

import java.io.Serializable;

/**
 * @author sunff
 *
 */
public class ApiServerInfoReq implements Serializable {

    
    /**
     * 
     */
    private static final long serialVersionUID = -7561622828429127925L;
    private final String apiId;
    private final String version;
    public String getApiId() {
        return apiId;
    }
    public String getVersion() {
        return version;
    }
    public ApiServerInfoReq(String apiId, String version) {
        super();
        this.apiId = apiId;
        this.version = version;
    }
    
    
}
