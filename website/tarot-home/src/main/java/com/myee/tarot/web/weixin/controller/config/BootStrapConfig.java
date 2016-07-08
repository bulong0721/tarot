package com.myee.tarot.web.weixin.controller.config;

import com.myee.djinn.remoting.netty.NettyServerConfig;
import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.djinn.server.operations.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Ray.Fu on 2016/6/30.
 */
public class BootStrapConfig {
    @Value("${djinn.port}")
    private int djinnPort;
    @Autowired
    private OperationsService operationsService;

    void serverBootstrapInit() {
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
//        return bootstrap;
    }
}
