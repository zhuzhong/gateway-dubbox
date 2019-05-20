package com.z.gateway.service.support;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z.gateway.common.entity.ApiServerInfo;
import com.z.gateway.common.util.CommonCodeConstants;
import com.z.gateway.service.ApiServerInfoReq;
import com.z.gateway.service.RegistryReaderService;

/**
 * zk dubbo rest服务注册器内容读取器服务实现类
 * 
 * @author Administrator
 *
 */
public class ZkDubboRegistryReaderServiceImpl implements RegistryReaderService {

    @Override
    public List<ApiServerInfo> queryApiInterfaceByApiId(ApiServerInfoReq req) {
        logger.info("现在从zk中获取相应的后端服务器,req={}", req);
        List<String> sets = hosts.get(req.getApiId());
        // if (sets != null) {
        // String hostAddress = loadBalancerService.chooseOne(new
        // LbKey(req.getApiId(),req.getApiId()), sets);
        // ApiServerInfo apiInterface = new ApiServerInfo();
        // apiInterface.setApiId(req.getApiId());
        // apiInterface.setProtocol(CommonCodeConstants.HTTP);
        // apiInterface.setHostAddress(hostAddress);
        // return apiInterface;
        // }

        if (sets != null) {
           /* return sets.stream().map(a -> {
                ApiServerInfo apiInterface = new ApiServerInfo();
                apiInterface.setApiId(req.getApiId());
                apiInterface.setProtocol(CommonCodeConstants.HTTP);
                apiInterface.setHostAddress(a);
                return apiInterface;
            }).collect(Collectors.toList());
            */
            
            List<ApiServerInfo> l=sets.stream().map(a->{
                ApiServerInfo c= new ApiServerInfo();
                c.setApiId(req.getApiId());
                
                c.setProtocol(CommonCodeConstants.HTTP);
                c.setHostAddress(a);
                return c;
            }).collect(Collectors.toList());
            return l;
        }
        return null;
    }

    // --------准备数据部分----------------------------------------------------
    private static Logger logger = LoggerFactory.getLogger(ZkDubboRegistryReaderServiceImpl.class);

    private static final String REST = "rest";

    private static final String PROVIDERS = "providers";

    private static final String REST_SLASH = REST + "://";
    private static final String SLASH = "/";
    // private static final String UTF_8 = "utf-8";

    private String rootPath;

    private String zkServers;
    private ZkClient zkClient;

    // private LoadBalanceService loadBalancerService;

    public void init() {
        zkClient = new ZkClient(zkServers, 5000);
        if (!rootPath.startsWith(SLASH)) {
            rootPath = SLASH + rootPath;
        }

        logger.info("rootPath={},init hosts", rootPath);
        runaway(zkClient, rootPath);
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    private static final ConcurrentHashMap<String/* context_path */, List<String/*
                                                                                 * host
                                                                                 * :
                                                                                 * port
                                                                                 */>> hosts = new ConcurrentHashMap<String, List<String>>();

    private void runaway(final ZkClient zkClient, final String path) {
        zkClient.unsubscribeAll();
        ConcurrentHashMap<String, List<String>> newHosts = new ConcurrentHashMap<String, List<String>>();
        zkClient.subscribeChildChanges(path, new IZkChildListener() {

            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                /*
                 * System.out.println(parentPath +
                 * " 's child changed, currentChilds:" + currentChilds);
                 */
                logger.info("{}'s child changed, currentChilds:{}", parentPath, currentChilds);
                // 一级节点的子节点发生变化
                runaway(zkClient, path); // 重新再来

            }

        });

        List<String> firstGeneration = zkClient.getChildren(path); // 二级节点
                                                                   // /dubbo-online/com.z.test.Testapi
        if (firstGeneration != null && firstGeneration.size() > 0) {
            for (String child : firstGeneration) {
                String firstNextPath = path + "/" + child;
                zkClient.subscribeChildChanges(firstNextPath, new IZkChildListener() {

                    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                        /*
                         * System.out.println(parentPath +
                         * " 's child changed, currentChilds:" + currentChilds);
                         */
                        logger.info("{}'s child changed, currentChilds:{}", parentPath, currentChilds);
                        // 2级节点的子节点发生
                        runaway(zkClient, path); // 重新再来

                    }

                });

                List<String> secondGeneration = zkClient.getChildren(firstNextPath); // 三级子节点
                                                                                     // /dubbo-online/com.z.test.Testapi/providers
                if (secondGeneration != null && secondGeneration.size() > 0) {
                    for (String secondChild : secondGeneration) {
                        if (secondChild.startsWith(PROVIDERS)) {
                            String secondNextPath = firstNextPath + "/" + secondChild;
                            zkClient.subscribeChildChanges(secondNextPath, new IZkChildListener() {

                                public void handleChildChange(String parentPath, List<String> currentChilds)
                                        throws Exception {
                                    /*
                                     * System.out .println(parentPath +
                                     * " 's child changed, currentChilds:" +
                                     * currentChilds);
                                     */
                                    logger.info("{}'s child changed, currentChilds:{}", parentPath, currentChilds);
                                    // 3级节点的子节点发生
                                    runaway(zkClient, path); // 重新再来

                                }

                            });

                            List<String> thirdGeneration = zkClient.getChildren(secondNextPath);// 4级子节点
                            // /dubbo-online/com.z.test.Testapi/providers/rest://localhost:8080/context
                            if (thirdGeneration != null && thirdGeneration.size() > 0) {
                                for (String thirdChild : thirdGeneration) {
                                    if (thirdChild.startsWith(REST)) {
                                        /*
                                         * 样例 rest://10.148.16.27:8480/demo/
                                         * com.z.m.facade.api. DemoFacadeService
                                         */
                                        ServiceProvider sp = new ServiceProvider(thirdChild);
                                        String contextPath = sp.getContextPath();
                                        String host = sp.getHost();
                                        List<String> hostSets = newHosts.get(contextPath);
                                        if (hostSets == null) {
                                            hostSets = new ArrayList<String>();
                                            newHosts.put(contextPath, hostSets);
                                        }
                                        hostSets.add(host);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        synchronized (PROVIDERS) {
            hosts.clear();
            hosts.putAll(newHosts);
        }
    }

    static class ServiceProvider {

        private String host;
        private String contextPath;
        private String provider;

        public ServiceProvider(String provider) {
            try {
                this.provider = URLDecoder.decode(provider, CommonCodeConstants.MDF_CHARSET_UTF_8);
            } catch (UnsupportedEncodingException e) {
                logger.error("地址解码错误{}", e);
                this.provider = provider;
            }
            parse();
        }

        private void parse() {
            String subString = provider.substring(REST_SLASH.length());

            int indexOfFirstSlash = subString.indexOf(SLASH);

            host = subString.substring(0, indexOfFirstSlash);
            String subSubString = subString.substring(indexOfFirstSlash + 1);
            int indexOfSecondSlash = subSubString.indexOf(SLASH);
            contextPath = subSubString.substring(0, indexOfSecondSlash);
        }

        public String getHost() {
            return host;
        }

        public String getContextPath() {
            return contextPath;
        }

    }

}
