package com.aldb.gateway.protocol;

import java.util.concurrent.Callable;

public abstract class AbstractTask implements Callable<OpenApiHttpSessionBean>{

	public AbstractTask(){
	}
	
	@Override
	public OpenApiHttpSessionBean call() throws Exception {
	    OpenApiHttpSessionBean obj = doBussiness();
		return obj;
	}

	
	public abstract OpenApiHttpSessionBean doBussiness() throws Exception;

}
