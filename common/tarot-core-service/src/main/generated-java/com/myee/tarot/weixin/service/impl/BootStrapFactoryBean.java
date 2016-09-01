package com.myee.tarot.weixin.service.impl;

import com.myee.djinn.remoting.netty.NettyServerConfig;
import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.djinn.server.operations.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by Ray.Fu on 2016/6/30.
 */
@Service
@Scope("singleton")
public class BootStrapFactoryBean extends AbstractFactoryBean<ServerBootstrap> {
    @Value("${djinn.port}")
    private int               djinnPort;
    @Autowired
    private OperationsService operationsService;

    private static ServerBootstrap instance;

    @Override
    public Class<?> getObjectType() {
        return ServerBootstrap.class;
    }

    @Override
    protected synchronized ServerBootstrap createInstance() throws Exception {
        if (null == instance) {
            final NettyServerConfig nettyServerConfig = new NettyServerConfig();
            nettyServerConfig.setListenPort(djinnPort);
            instance = new ServerBootstrap(nettyServerConfig);
            instance.initialize();
            try {
                instance.export(operationsService, OperationsService.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance.start();
            System.out.println("here");
        }
        return instance;
    }
}
