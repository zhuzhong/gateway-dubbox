package com.aldb.gateway.service.support;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import com.aldb.gateway.common.entity.ApiInterface;
import com.aldb.gateway.service.ApiInterfaceService;

/**
 * @author Administrator
 *
 */
public class ZkApiInterfaceServiceImpl implements ApiInterfaceService {

	private String rootPath;

	private String zkServers;
	

	public void init() {

		zkClient = new ZkClient(zkServers, 5000);
		if (!rootPath.startsWith("/")) {
			rootPath = "/" + rootPath;
		}
		runaway(zkClient, rootPath);
	}

	
	private ZkClient zkClient;
	private ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();

	}

	private int getRandomInt(int max) {
		return getRandom().nextInt(max);
	}

	private String getRandomElement(Set<String> set) {
		int rn = getRandomInt(set.size());
		int i = 0;
		for (String e : set) {
			if (i == rn) {
				return e;
			}
			i++;
		}
		return null;
	}

	@Override
	public ApiInterface findOne(String apiId, String version) {
		Set<String> sets = hosts.get(apiId);
		if (sets != null) {
			String hostAddress = getRandomElement(sets);
			ApiInterface apiInterface = new ApiInterface();
			apiInterface.setApiId(apiId);
			apiInterface.setProtocol("http");
			apiInterface.setHostAddress(hostAddress);
			return apiInterface;
		}
		return null;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void setZkServers(String zkServers) {
		this.zkServers = zkServers;
	}

	private static ConcurrentHashMap<String, Set<String>> hosts = new ConcurrentHashMap<String, Set<String>>();

	private void runaway(final ZkClient zkClient, final String path) {
		zkClient.unsubscribeAll();
		ConcurrentHashMap<String, Set<String>> newHosts = new ConcurrentHashMap<String, Set<String>>();
		zkClient.subscribeChildChanges(path, new IZkChildListener() {

			public void handleChildChange(String parentPath,
					List<String> currentChilds) throws Exception {
				System.out.println(parentPath
						+ " 's child changed, currentChilds:" + currentChilds);
				// 一级节点的子节点发生变化
				runaway(zkClient, path); // 重新再来

			}

		});

		List<String> firstGeneration = zkClient.getChildren(path); // 二级节点
																	// /dubbo-online/com.aldb.test.Testapi
		if (firstGeneration != null && firstGeneration.size() > 0) {
			for (String child : firstGeneration) {
				String firstNextPath = path + "/" + child;
				zkClient.subscribeChildChanges(firstNextPath,
						new IZkChildListener() {

							public void handleChildChange(String parentPath,
									List<String> currentChilds)
									throws Exception {
								System.out.println(parentPath
										+ " 's child changed, currentChilds:"
										+ currentChilds);
								// 2级节点的子节点发生
								runaway(zkClient, path); // 重新再来

							}

						});

				List<String> secondGeneration = zkClient
						.getChildren(firstNextPath); // 三级子节点
														// /dubbo-online/com.aldb.test.Testapi/providers
				if (secondGeneration != null && secondGeneration.size() > 0) {
					for (String secondChild : secondGeneration) {
						if (secondChild.startsWith("providers")) {
							String secondNextPath = firstNextPath + "/"
									+ secondChild;
							zkClient.subscribeChildChanges(secondNextPath,
									new IZkChildListener() {

										public void handleChildChange(
												String parentPath,
												List<String> currentChilds)
												throws Exception {
											System.out
													.println(parentPath
															+ " 's child changed, currentChilds:"
															+ currentChilds);
											// 3级节点的子节点发生
											runaway(zkClient, path); // 重新再来

										}

									});

							List<String> thirdGeneration = zkClient
									.getChildren(secondNextPath);// 4级子节点
																	// /dubbo-online/com.aldb.test.Testapi/rest://localhost:8080
							if (thirdGeneration != null
									&& thirdGeneration.size() > 0) {
								for (String thirdChild : thirdGeneration) {
									if (thirdChild.startsWith("rest")) {
										/*
										 * 样例
										 * rest://10.148.16.27:8480/magicmall/
										 * com.aldb.magicmall.facade.api.
										 * MallFacadeService
										 */
										ServiceProvider sp = new ServiceProvider(
												thirdChild);
										String contextPath = sp
												.getContextPath();
										String host = sp.getHost();
										Set<String> hostSets = newHosts
												.get(contextPath);
										if (hostSets == null) {
											hostSets = new HashSet<String>();
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

		hosts = newHosts;
		//print(hosts);
	}

	private void print(ConcurrentHashMap<String, Set<String>> hosts) {
		System.out.println("----begin print-----");
		if (hosts != null && !hosts.isEmpty()) {
			for (Map.Entry<String, Set<String>> entry : hosts.entrySet()) {
				System.out.println(entry.getKey() + "---"
						+ lineSet(entry.getValue()));
			}
		}

		System.out.println("----end print-----");
	}

	private String lineSet(Set<String> sets) {
		StringBuilder sb = new StringBuilder();
		for (String str : sets) {
			sb.append(str);
			sb.append(",");
		}
		return sb.toString();
	}

	private static class ServiceProvider {
		private String host;
		private String contextPath;
		private String demo;

		public ServiceProvider(String demo) {
			try {
				this.demo = URLDecoder.decode(demo, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.demo = demo;
			}
			parse();
		}

		private void parse() {
			String subString = demo.substring("rest://".length());

			int indexOfFirstSlash = subString.indexOf("/");

			host = subString.substring(0, indexOfFirstSlash);
			String subSubString = subString.substring(indexOfFirstSlash + 1);
			int indexOfSecondSlash = subSubString.indexOf("/");
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
