/**
 * 
 */
package com.z.gateway.service.support;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.z.gateway.service.MvcRestRegistryService;

/**
 * mvc rest服务注册器：
 * 所遵守的注册格式 rootpath(持久节点）/context/providers （持久节点）/服务信息（ip:port）(临时节点 ）
 * @author Administrator
 *
 */
public class ZkMvcRestRegistryServiceImpl implements MvcRestRegistryService {

	private String rootPath;

	private String zkServers;
	private String context;

	private String ip;
	private String port;

	private static final String PROVIDERS = "/providers";


	public void init() {
		
		if (zkServers == null) {
			throw new RuntimeException("zkServers is null,please set it");
		}
		
		if(context==null) {
			throw new RuntimeException("context is null,please set it");
		}
		if (rootPath == null) {
			//throw new RuntimeException("rootPath is null,please set it");
			rootPath="mvcrest";
		}
		if (!rootPath.startsWith("/")) {
			rootPath = "/" + rootPath;
		}
		if(!context.startsWith("/")) {
			context="/"+context;
		}
		regist();
	}

	@Override
	public void regist() {
		zkClient = new ZkClient(zkServers, 5000);

		if (!zkClient.exists(rootPath)) {
			zkClient.createPersistent(rootPath);
		}
		secondPath = rootPath + context;
		if (!zkClient.exists(secondPath)) {
			zkClient.createPersistent(secondPath);
		}
		thirdPath=secondPath+PROVIDERS;
		if(!zkClient.exists(thirdPath)) {
			zkClient.createPersistent(thirdPath);
		}
		createEphemeral();
		zkClient.subscribeStateChanges(new IZkStateListener() {

			@Override
			public void handleStateChanged(KeeperState state) throws Exception {

			}

			@Override
			public void handleNewSession() throws Exception {
				System.out.println("重建...");
				createEphemeral();
			}
		});

	}

	private ZkClient zkClient;
	private String secondPath;
	private String thirdPath;

	private void createEphemeral() {
		String forthpath = thirdPath + "/" + "rest://"+ip + ":" + port+"/"+context+"/VERSION";
		if (!zkClient.exists(forthpath)) {
			zkClient.createEphemeral(forthpath);
		}
		System.out.println("提供的服务节点名称为：" + forthpath);
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
