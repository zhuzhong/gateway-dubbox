package com.aldb.gateway.protocol;

import com.aldb.gateway.handler.OpenApiHandlerExecuteTemplate;

public class OpenApiHttpReqTask extends AbstractTask {
    private OpenApiHttpSessionBean httpSessionBean;
    private OpenApiHandlerExecuteTemplate handlerExecuteTemplate;

    public OpenApiHttpReqTask(OpenApiHttpSessionBean httpSessionBean,
            OpenApiHandlerExecuteTemplate handlerExecuteTemplate) {
        this.httpSessionBean = httpSessionBean;
        this.handlerExecuteTemplate = handlerExecuteTemplate;
    }

    public OpenApiHttpSessionBean getHttpSessionBean() {
        return httpSessionBean;
    }

    public void setHttpSessionBean(OpenApiHttpSessionBean httpSessionBean) {
        this.httpSessionBean = httpSessionBean;
    }

    public OpenApiHandlerExecuteTemplate getHandlerExecuteTemplate() {
        return handlerExecuteTemplate;
    }

    public void setHandlerExecuteTemplate(OpenApiHandlerExecuteTemplate handlerExecuteTemplate) {
        this.handlerExecuteTemplate = handlerExecuteTemplate;
    }

    @Override
    public OpenApiHttpSessionBean doBussiness() throws Exception {
        OpenApiContext context = new OpenApiContext();
        context.setOpenApiHttpSessionBean(httpSessionBean);
        this.handlerExecuteTemplate.execute(context);
        return context.getOpenApiHttpSessionBean();
    }
}
