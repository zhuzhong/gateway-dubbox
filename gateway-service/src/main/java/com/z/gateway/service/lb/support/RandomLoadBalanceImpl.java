package com.z.gateway.service.lb.support;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.z.gateway.common.entity.ApiServerInfo;
import com.z.gateway.service.lb.LbKey;
import com.z.gateway.service.lb.LoadBalanceService;

/**
 * @author Administrator
 *
 */
public class RandomLoadBalanceImpl implements LoadBalanceService {

    @Override
    public ApiServerInfo chooseOne(LbKey key,List<ApiServerInfo> set) {

        return getRandomElement(set);
    }

    private ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();

    }

    private int getRandomInt(int max) {
        return getRandom().nextInt(max);
    }

    private ApiServerInfo getRandomElement(List<ApiServerInfo> set) {
        int rn = getRandomInt(set.size());
        int i = 0;
        ApiServerInfo r = null;
        for (ApiServerInfo e : set) {
            if (i == rn) {
                r = e;
                break;
            }
            i++;
        }
        if (r == null) { //如果为空，取第一个
            for (ApiServerInfo e : set) {
                r = e;
                break;
            }
        }
        return r;
    }
}
