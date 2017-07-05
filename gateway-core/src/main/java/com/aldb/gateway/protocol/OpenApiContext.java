/**
 * 
 */
package com.aldb.gateway.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.chain.Context;

/**
 * @author Administrator
 *
 */
public class OpenApiContext extends HashMap implements Context {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final String SESSION_BEAN_CONTEXT_KEY = "ALD_OPENAPI_SESSION_BEAN_CONTEXT_KEY";

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return (super.get(key));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object key, Object value) {
        return (super.put(key, value));
    }

    @Override
    public Object remove(Object key) {
        return super.remove(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void putAll(Map m) {
        super.putAll(m);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public Set keySet() {
        return super.keySet();
    }

    @Override
    public Collection values() {
        return super.values();
    }

    @Override
    public Set entrySet() {
        return super.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public OpenApiHttpSessionBean getOpenApiHttpSessionBean() {
        return (OpenApiHttpSessionBean) this.get(this.SESSION_BEAN_CONTEXT_KEY);
    }

    public void setOpenApiHttpSessionBean(OpenApiHttpSessionBean sessionBean) {
        this.put(this.SESSION_BEAN_CONTEXT_KEY, sessionBean);
    }

}
