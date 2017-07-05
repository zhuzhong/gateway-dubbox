/**
 * 
 */
package com.aldb.gateway.service.support;

import org.springframework.beans.factory.InitializingBean;

import com.aldb.gateway.common.util.NetworkUtil;
import com.aldb.gateway.service.IdService;
import com.sohu.idcenter.IdWorker;

/**
 * @author sunff
 *
 */
public class SnowFlakeIdServiceImpl implements IdService, InitializingBean {

	private IdWorker idWorker;

	@Override
	public String genInnerRequestId() {
		return String.valueOf(idWorker.getId());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		int workerId = Math.abs(NetworkUtil.getLocalMac().hashCode()) % 31;
		int dataCenterId = Math.abs(NetworkUtil.getLocalHost().hashCode()) % 31;
		idWorker = new IdWorker(workerId, dataCenterId, workerId + dataCenterId);

	}
}
