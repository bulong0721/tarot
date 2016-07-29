package com.myee.tarot.weixin.service.impl;

import com.myee.djinn.remoting.netty.NettyServerConfig;
import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.djinn.server.operations.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;

/**
 * Created by Ray.Fu on 2016/6/30.
 */
@Service
@Scope("singleton")
public class BootStrapFacatoryBean extends AbstractFactoryBean<ServerBootstrap> {
    @Value("${djinn.port}")
    private int djinnPort;
    @Autowired
    private OperationsService operationsService;

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    protected ServerBootstrap createInstance() throws Exception {
        final NettyServerConfig nettyServerConfig = new NettyServerConfig();
        nettyServerConfig.setListenPort(djinnPort);
        ServerBootstrap bootstrap = new ServerBootstrap(nettyServerConfig);
        bootstrap.initialize();
        try {
            bootstrap.export(operationsService, OperationsService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bootstrap.start();
        System.out.println("here");
        return bootstrap;
    }
}
