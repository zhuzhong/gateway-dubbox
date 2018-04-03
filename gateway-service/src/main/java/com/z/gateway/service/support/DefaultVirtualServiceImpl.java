/**
 * 
 */
package com.z.gateway.service.support;

import org.I0Itec.zkclient.ZkClient;

import com.z.gateway.service.VirtualService;

/**
 * @author Administrator
 *
 */
public class DefaultVirtualServiceImpl implements VirtualService {

    private String rootPath;

    private String zkServers;
    private String context;

    private String ip;
    private String port;

    private static final String GATEWAY = "/provider";

    /*
     * 所遵守的注册格式 rootpath(持外节点）/gateway （持久节点）/服务信息（ip:port#context）(临时节点
     * ）(non-Javadoc)
     * 
     * @see com.z.gateway.service.VirtualService#regist()
     */

    public void init() {
        if (rootPath == null) {
            throw new RuntimeException("rootPath is null,please set it");
        }
        if (zkServers == null) {
            throw new RuntimeException("zkServers is null,please set it");
        }
        if (!rootPath.startsWith("/")) {
            rootPath = "/" + rootPath;
        }
        regist();
    }

    @Override
    public void regist() {
        ZkClient zkClient;
        zkClient = new ZkClient(zkServers, 5000);

        if (!zkClient.exists(rootPath)) {
            zkClient.createPersistent(rootPath);
        }
        String secondPath = rootPath + GATEWAY;
        if (!zkClient.exists(secondPath)) {
            zkClient.createPersistent(secondPath);
        }

        String thirdpath = secondPath + "/" + ip + ":" + port + "#" + context;
        if (!zkClient.exists(thirdpath)) {
            zkClient.createEphemeral(thirdpath);
        }
        System.out.println("提供的服务节点名称为：" + thirdpath);
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

}
