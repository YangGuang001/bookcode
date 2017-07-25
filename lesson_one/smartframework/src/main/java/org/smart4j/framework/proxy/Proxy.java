package org.smart4j.framework.proxy;

/**
 * Created by yz on 2017/7/22.
 */
public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
