/**
 * 
 */
package com.z.gateway.service.support;

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
import com.z.gateway.service.support.ZkDubboRegistryReaderServiceImpl.ServiceProvider;

/**
 * mvc rest 服务zk注册器内容读取器服务实现
 * 
 * @author sunff
 *
 */
public class ZkMvcRestryReaderSeviceImpl implements RegistryReaderService {

	@Override
	public List<ApiServerInfo> queryApiInterfaceByApiId(ApiServerInfoReq req) {

		logger.info("现在从zk中获取相应的mvcrestful后端服务器,req={}", req);
		List<String> sets = hosts.get(req.getApiId());

		if (sets != null) {
			return sets.stream().map(a -> {
				ApiServerInfo apiInterface = new ApiServerInfo();
				apiInterface.setApiId(req.getApiId());
				apiInterface.setProtocol(CommonCodeConstants.HTTP);
				apiInterface.setHostAddress(a);
				return apiInterface;
			}).collect(Collectors.toList());
		}
		return null;
	}

	private String rootPath;

	private String zkServers;
	private ZkClient zkClient;

	private static final String SLASH = "/";

	public void init() {
		zkClient = new ZkClient(zkServers, 5000);
		if (!rootPath.startsWith(SLASH)) {
			rootPath = SLASH + rootPath;
		}

//	        if (loadBalancerService == null) {
//	            loadBalancerService = new RandomLoadBalanceImpl();
//	        }
		logger.info("rootPath={},init hosts", rootPath);
		runaway(zkClient, rootPath);
	}

	private static Logger logger = LoggerFactory.getLogger(ZkMvcRestryReaderSeviceImpl.class);
	private static final ConcurrentHashMap<String/* context_path */, List<String/* host:port */>> hosts = new ConcurrentHashMap<String, List<String>>();

	private static final String PROVIDERS = "/providers";

	private void runaway(final ZkClient zkClient, final String path) {
		zkClient.unsubscribeAll();
		ConcurrentHashMap<String, List<String>> newHosts = new ConcurrentHashMap<String, List<String>>();
		zkClient.subscribeChildChanges(path, new IZkChildListener() {

			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				/*
				 * System.out.println(parentPath + " 's child changed, currentChilds:" +
				 * currentChilds);
				 */
				logger.info("{}'s child changed, currentChilds:{}", parentPath, currentChilds);
				// 一级节点的子节点发生变化
				runaway(zkClient, path); // 重新再来

			}

		});

		List<String> firstGeneration = zkClient.getChildren(path); // 二级节点
																	// /mvcrest/context
		if (firstGeneration != null && firstGeneration.size() > 0) {
			for (String child : firstGeneration) {
				String firstNextPath = path + "/" + child;
				zkClient.subscribeChildChanges(firstNextPath, new IZkChildListener() {

					public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
						/*
						 * System.out.println(parentPath + " 's child changed, currentChilds:" +
						 * currentChilds);
						 */
						logger.info("{}'s child changed, currentChilds:{}", parentPath, currentChilds);
						// 2级节点的子节点发生
						runaway(zkClient, path); // 重新再来

					}

				});

				List<String> secondGeneration = zkClient.getChildren(firstNextPath); // 三级子节点
																						// /mvcrest/context/providers
				if (secondGeneration != null && secondGeneration.size() > 0) {
					for (String secondChild : secondGeneration) {
						if (secondChild.startsWith(PROVIDERS)) {
							String secondNextPath = firstNextPath + "/" + secondChild;
							zkClient.subscribeChildChanges(secondNextPath, new IZkChildListener() {

								public void handleChildChange(String parentPath, List<String> currentChilds)
										throws Exception {
									/*
									 * System.out .println(parentPath + " 's child changed, currentChilds:" +
									 * currentChilds);
									 */
									logger.info("{}'s child changed, currentChilds:{}", parentPath, currentChilds);
									// 3级节点的子节点发生
									runaway(zkClient, path); // 重新再来

								}

							});

							List<String> thirdGeneration = zkClient.getChildren(secondNextPath);// 4级子节点
							// /mvcrest/context/providers/rest://localhost:8080/context/
							if (thirdGeneration != null && thirdGeneration.size() > 0) {
								for (String thirdChild : thirdGeneration) {

									zkClient.subscribeChildChanges(thirdChild, new IZkChildListener() {

										@Override
										public void handleChildChange(String parentPath, List<String> currentChilds)
												throws Exception {
											logger.info("{}'s child changed, currentChilds:{}", parentPath,
													currentChilds);
											// 4级节点的子节点发生
											runaway(zkClient, path); // 重新再来
										}
									});
									/*
									 * 样例 rest://10.148.16.27:8480/demo/VERSION
									 * 
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

		synchronized (PROVIDERS) {
			hosts.clear();
			hosts.putAll(newHosts);
		}
	}

}
