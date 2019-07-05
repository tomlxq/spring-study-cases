package com.example.ribbon;

import java.util.List;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;

/**
 * 用{@link AbstractLoadBalancerRule} 实现接口 {@link IRule}
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/7/2
 */
public class FirstServerForeverServer extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    /**
     * 永远返回第一个服务器
     * 
     * @param key
     * @return
     */
    @Override
    public Server choose(Object key) {
        List<Server> serverList = getLoadBalancer().getAllServers();
        return serverList.get(0);
    }
}
