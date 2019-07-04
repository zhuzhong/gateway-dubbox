/**
 * 
 */
package com.z.gateway.handler.support;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.z.gateway.common.exception.OpenApiException;
import com.z.gateway.common.exception.OpenApiServiceErrorEnum;
import com.z.gateway.handler.ThreadPoolHandler;
import com.z.gateway.protocol.AbstractTask;
import com.z.gateway.protocol.OpenApiHttpSessionBean;

/**
 * @author Administrator
 * 
 */
public class ThreadPoolHandlerImpl implements ThreadPoolHandler {

	private static Logger logger = LoggerFactory.getLogger(ThreadPoolHandlerImpl.class);

	private ThreadPoolTaskExecutor taskExecutor;

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	public Object addTask(AbstractTask task) {
		try {
			FutureTask<OpenApiHttpSessionBean> tsFutre = new FutureTask<OpenApiHttpSessionBean>(task);
			taskExecutor.execute(tsFutre);
			while (!tsFutre.isDone()) {

				try { 
					TimeUnit.MICROSECONDS.sleep(200);
				} catch (InterruptedException e) {
					logger.error(String.format("exception happend on executing task with ", e.getMessage()));
				}

			}
			return tsFutre.get();
		} catch (TaskRejectedException e) {
			logger.error("the queue reached max deepth");
			throw new OpenApiException(OpenApiServiceErrorEnum.SYSTEM_QUEUE_DEEPTH);
		} catch (Throwable e) {
			Throwable throwable = e.getCause();
			if (throwable instanceof OpenApiException) {
				throw (OpenApiException) throwable;
			}
			logger.error(String.format("exception happend on executing task with %s", e.getMessage()));
			OpenApiException ex = new OpenApiException(OpenApiServiceErrorEnum.SYSTEM_BUSY, throwable);
			throw ex;
		}
	}

}
