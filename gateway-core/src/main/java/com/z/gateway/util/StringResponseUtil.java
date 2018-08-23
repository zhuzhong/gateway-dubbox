/**
 * 
 */
package com.z.gateway.util;

/**
 * @author sunff
 *
 */
public class StringResponseUtil {

    
    
    public static String encodeResp(byte[] bs) {
        String result=  org.apache.commons.codec.binary.Base64.encodeBase64String(bs);         
        return result;
        
    }
    
    public static byte[] decodeResp(String resp) {
        byte[] bsb= org.apache.commons.codec.binary.Base64.decodeBase64(resp);
        return bsb;
        
    }
}
