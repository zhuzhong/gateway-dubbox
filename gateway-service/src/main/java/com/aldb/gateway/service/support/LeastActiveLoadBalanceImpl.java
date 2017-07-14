package com.aldb.gateway.service.support;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.aldb.gateway.service.LoadBalanceService;

/**
 * 最少活动的
 * 
 * @author Administrator
 *
 */
public class LeastActiveLoadBalanceImpl implements LoadBalanceService {

    private static ConcurrentHashMap<String, AtomicInteger> usedTimes = new ConcurrentHashMap<>();

    private final Random random = new Random();

    @Override
    public String chooseOne(String context, String version, List<String> hosts) {
        if (hosts == null || hosts.size() == 0) {
            return null;
        }

        int length = hosts.size(); // 总个数
        int leastActive = -1; // 最小的活跃数
        int leastCount = 0; // 相同最小活跃数的个数
        int[] leastIndexs = new int[length]; // 相同最小活跃数的下标

        for (int i = 0; i < length; i++) {
            String invoker = hosts.get(i);
            int active = getActive(context, invoker); // 活跃数

            if (leastActive == -1 || active < leastActive) { // 发现更小的活跃数，重新开始
                leastActive = active; // 记录最小活跃数
                leastCount = 1; // 重新统计相同最小活跃数的个数
                leastIndexs[0] = i; // 重新记录最小活跃数下标

            } else if (active == leastActive) { // 累计相同最小的活跃数
                leastIndexs[leastCount++] = i; // 累计相同最小活跃数下标

            }
        }
        String host;
        if (leastCount == 1) {
            // 如果只有一个最小则直接返回
            host = hosts.get(leastIndexs[0]);

        } else {
            // 如果活动数都相等，则随机分配
            host = hosts.get(leastIndexs[random.nextInt(leastCount)]);
        }
        addActive(context, host);
        return host;

    }

    private int getActive(String context, String host) {
        AtomicInteger active = usedTimes.get(countKey(context, host));
        if (active == null) {
            usedTimes.put(countKey(context, host), new AtomicInteger(0));
            return 0;
        } else {
            return active.get();
        }
    }

    private void addActive(String context, String host) {
        usedTimes.get(countKey(context, host)).incrementAndGet();
    }

    private String countKey(String context, String host) {
        return context + "-" + host;
    }
}
