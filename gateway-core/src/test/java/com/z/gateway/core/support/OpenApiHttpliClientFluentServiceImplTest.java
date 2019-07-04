/**
 * 
 */
package com.z.gateway.core.support;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author zhuzhong
 *
 */
public class OpenApiHttpliClientFluentServiceImplTest {

    OpenApiHttpliClientFluentServiceImpl instance;

    @BeforeClass
    public void init() {

    }

    @Test
    public void doGet() {
        instance = new OpenApiHttpliClientFluentServiceImpl();
        instance.init();
        String s = instance.doGet("http://www.baidu.com", null);
        System.out.println(s);
    }
}
