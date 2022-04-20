

package com.elinxer.springcloud.platform.gateway.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.elinxer.springcloud.platform.core.constant.ContextConstant;
import com.elinxer.springcloud.platform.core.utils.ContextUtils;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 自定义Rule配置
 *
 * @author zhengqh
 * @date 2021/10/11
 */
public class GrayRule extends RoundRobinRule {

    public static class NacosServer extends Server {
        private final MetaInfo metaInfo;
        private final Instance instance;
        private final Map<String, String> metadata;

        public NacosServer(final Instance instance) {
            super(instance.getIp(), instance.getPort());
            this.instance = instance;
            this.metaInfo = new MetaInfo() {
                @Override
                public String getAppName() {
                    return instance.getServiceName();
                }

                @Override
                public String getServerGroup() {
                    return null;
                }

                @Override
                public String getServiceIdForDiscovery() {
                    return null;
                }

                @Override
                public String getInstanceId() {
                    return instance.getInstanceId();
                }
            };
            this.metadata = instance.getMetadata();
        }

        @Override
        public MetaInfo getMetaInfo() {
            return this.metaInfo;
        }

        public Instance getInstance() {
            return this.instance;
        }

        public Map<String, String> getMetadata() {
            return this.metadata;
        }
    }

    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        String version = ContextUtils.getGrayVersion();
        System.out.println("versioin:" + version);
        List<Server> targetList = null;
        List<Server> upList = lb.getReachableServers();

        if (StrUtil.isNotEmpty(version)) {
            targetList = upList.stream().filter(
                    server -> version.equals(
                            ((NacosServer) server).getMetadata().get(ContextConstant.GRAY_VERSION)
                    )
            ).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(targetList)) {
            targetList = upList.stream().filter(
                    server -> {
                        String metadataVersion = ((NacosServer) server).getMetadata().get(ContextConstant.GRAY_VERSION);
                        return StrUtil.isEmpty(metadataVersion);
                    }
            ).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(targetList)) {
            return getServer(targetList);
        }
        return super.choose(lb, key);
    }

    private Server getServer(List<Server> upList) {
        int nextInt = RandomUtil.randomInt(upList.size());
        return upList.get(nextInt);
    }
}

