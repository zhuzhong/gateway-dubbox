/**
 * 
 */
package com.z.gateway.service.support;

import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

import com.z.gateway.service.support.DefaultVirtualServiceImpl;

/**
 * @author Administrator
 *
 */
public class VirtualServiceTest {

    private static DefaultVirtualServiceImpl virtualService;

    @BeforeClass
    public static void init() {
        virtualService = new DefaultVirtualServiceImpl();
        virtualService.setContext("gateway");
        virtualService.setIp("192.168.6.99");
        virtualService.setPort("8080");
        virtualService.setRootPath("gateway-api");
        virtualService.setZkServers("localhost:2181");
    }

    @Test
    public void regist() {
        virtualService.regist();
        System.out.println("oook");
        try {
            TimeUnit.SECONDS.sleep(1000000000L);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
