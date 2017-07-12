/**
 * 
 */
package com.aldb.gateway.service.support;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.aldb.gateway.service.LoadBalancerService;

/**
 * @author Administrator
 *
 */
public class DefaultLoadBalancerServiceImpl implements LoadBalancerService {

    @Override
    public String chooseOne(Set<String> set) {

        return getRandomElement(set);
    }

    private ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();

    }

    private int getRandomInt(int max) {
        return getRandom().nextInt(max);
    }

    private String getRandomElement(Set<String> set) {
        int rn = getRandomInt(set.size());
        int i = 0;
        String r = null;
        for (String e : set) {
            if (i == rn) {
                r = e;
                break;
            }
            i++;
        }
        if (r == null) { //如果为空，取第一个
            for (String e : set) {
                r = e;
                break;
            }
        }
        return r;
    }
}
