/**
 * 
 */
package com.z.gateway.handler.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.chain.Chain;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.z.gateway.handler.OpenApiHandlerExecuteTemplate;

/**
 * @author Administrator
 *
 */
public class OpenApiServiceHandlerExecuteTemplateImpl implements OpenApiHandlerExecuteTemplate, Chain {

    private static Log logger = LogFactory.getLog(OpenApiServiceHandlerExecuteTemplateImpl.class);
    private List<Command> commands = new ArrayList<Command>();

    public OpenApiServiceHandlerExecuteTemplateImpl(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void addCommand(Command command) {
        this.commands.add(command);
    }

    @Override
    public boolean execute(Context context) throws Exception {
        logger.info("executing all handlers,have a good journey!");
        if (context == null || null == this.commands) {
            throw new IllegalArgumentException();
        }
        Iterator<Command> cmdIterator = commands.iterator();
        Command cmd = null;
        while (cmdIterator.hasNext()) {
            cmd = (Command) cmdIterator.next();
            if (cmd.execute(context)) {
                break; //有一个处理它了，就直接跳出
            }
        }
        return false;
    }

}
