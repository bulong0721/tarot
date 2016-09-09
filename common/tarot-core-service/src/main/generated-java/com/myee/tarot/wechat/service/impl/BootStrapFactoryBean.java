package com.myee.tarot.wechat.service.impl;

import com.myee.djinn.remoting.netty.NettyServerConfig;
import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.djinn.server.operations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(BootStrapFactoryBean.class);

	@Value("${djinn.port}")
	private int djinnPort;
	@Autowired
	private CommonService commonService;
	@Autowired
	private DataStoreService dataStoreService;
	@Autowired
	private MealsService mealsService;
	private ServerBootstrap instance;

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
				BackendService serviceStub = new BackendServiceStub(commonService, dataStoreService, mealsService);
				instance.export(serviceStub, BackendService.class, CommonService.class, DataStoreService.class, MealsService.class);
			} catch (Exception e) {
				LOGGER.error("ServerBootstrap export error", e);
			}
			instance.start();
		}
		return instance;
	}
}
