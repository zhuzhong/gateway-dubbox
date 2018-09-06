package com.z.gateway.service.lb.support;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.z.gateway.service.lb.LbKey;
import com.z.gateway.service.lb.LoadBalanceService;

/**
 * @author Administrator
 *
 */
public class RandomLoadBalanceImpl implements LoadBalanceService {

    @Override
    public String chooseOne(LbKey key,List<String> set) {

        return getRandomElement(set);
    }

    private ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();

    }

    private int getRandomInt(int max) {
        return getRandom().nextInt(max);
    }

    private String getRandomElement(List<String> set) {
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
