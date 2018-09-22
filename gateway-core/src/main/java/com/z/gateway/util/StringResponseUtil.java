/**
 * 
 */
package com.z.gateway.util;

import java.io.UnsupportedEncodingException;

/**
 * @author sunff
 *
 */
public class StringResponseUtil {

    /*
     * 想多了，关于上传，以及获取图形验证码之类的需求，可以通过业务方法转换为对应的Base64方式去解决； 而不需要网关接口去处理；
     * 
     * 1.对于上传，前端应用 将文件转换成base64 字符串，给网关，网关传给后端服务，然后后端服务自行将base64字符解析，并生成
     * 相应的文件并进行存储;
     * 
     * 2.获取图形验证码，即类似图形下载，文件下载之类的需求，将后台服务将其转换为相应的base64字符串，
     * 经网关返回给前端应用，前端应用自行将base64字符串进行解析，生成相应的文件或者图片等；
     * 
     * 经过这种转换的目的是，因为base64文件格式较大，如果经过原来方式更改，则将会使所有的接口长度增大，不利于优化， 再者restful
     * 格式的java上传也是有问题的。
     */

    private static String encode = "UTF-8";

    public static String encodeResp(byte[] bs) {
        /*
         * String result=
         * org.apache.commons.codec.binary.Base64.encodeBase64String(bs); return
         * result;
         */
        try {
            return new String(bs, encode);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decodeResp(String resp) {
        /*
         * byte[] bsb=
         * org.apache.commons.codec.binary.Base64.decodeBase64(resp); return
         * bsb;
         */
        try {
                return resp.getBytes(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }
}
