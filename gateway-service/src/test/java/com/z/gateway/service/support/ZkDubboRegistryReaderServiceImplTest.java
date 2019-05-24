/**
 * 
 */
package com.z.gateway.service.support;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.z.gateway.common.entity.ApiServerInfo;
import com.z.gateway.common.util.CommonCodeConstants;

/**
 * @author zhuzhong
 *
 */
public class ZkDubboRegistryReaderServiceImplTest {

    @Test
    public void streamMap() {
        /*
         * return sets.stream().map(a->{ ApiServerInfo apiInterface = new
         * ApiServerInfo(); apiInterface.setApiId(req.getApiId());
         * apiInterface.setProtocol(CommonCodeConstants.HTTP);
         * apiInterface.setHostAddress(a); return apiInterface;
         * }).collect(Collectors.toList());
         */
        List<String> sets = Arrays.asList("a", "b", "c", "d");
        List<String> s = sets.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(s);

        sets.stream().map(a->new CartDTO(a, 23)).forEach(a->{
            System.out.println(a);
        });
        
        
        System.out.println("-----------------");
        sets.stream().map(a->{
            CartDTO c= new CartDTO(a, 23);
            return c;
        }).forEach(a->{
            System.out.println(a);
        });
        
        System.out.println("-----------------");
        sets.stream().map(a->{
            ApiServerInfo c= new ApiServerInfo();
            c.setHostAddress(a);
            return c;
        }).forEach(a->{
            System.out.println(a);
        });
        
        System.out.println("#######");
        sets.stream().map(a->{
            ApiServerInfo c= new ApiServerInfo();
            c.setApiId("900");
            
            c.setProtocol(CommonCodeConstants.HTTP);
            c.setHostAddress(a);
            return c;
        }).forEach(a->{
            System.out.println(a);
        });
        System.out.println("................");
      System.out.println(  sets.stream().map(a->{
            ApiServerInfo c= new ApiServerInfo();
            c.setApiId("900");
            
            c.setProtocol(CommonCodeConstants.HTTP);
            c.setHostAddress(a);
            return c;
        }).collect(Collectors.toList())
              );
      System.out.println("###................");
     List<ApiServerInfo> l=sets.stream().map(a->{
          ApiServerInfo c= new ApiServerInfo();
          c.setApiId("900");
          
          c.setProtocol(CommonCodeConstants.HTTP);
          c.setHostAddress(a);
          return c;
      }).collect(Collectors.toList());
     System.out.println(l);
    }
}
