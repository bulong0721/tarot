package com.myee.tarot.djinn.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myee.djinn.dto.DataUploadInfoDTO;
import com.myee.djinn.dto.UploadResourceType;
import com.myee.djinn.dto.metrics.SystemMetrics;
import com.myee.djinn.rpc.RemoteException;
import com.myee.djinn.server.operations.DataStoreService;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.service.TransactionalAspectAware;
import com.myee.tarot.datacenter.domain.SelfCheckLog;
import com.myee.tarot.datacenter.domain.SelfCheckLogVO;
import com.myee.tarot.datacenter.service.SelfCheckLogService;
import com.myee.tarot.remote.service.AppInfoService;
import com.myee.tarot.remote.service.MetricDetailService;
import com.myee.tarot.remote.service.MetricInfoService;
import com.myee.tarot.remote.service.SystemMetricsService;
import com.myee.tarot.remote.util.MetricsUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Martin on 2016/9/6.
 */
@Service
public class DataStoreServiceImpl implements DataStoreService, TransactionalAspectAware {

	private static final Logger LOG = LoggerFactory.getLogger(DataStoreServiceImpl.class);
	@Autowired
	private SelfCheckLogService selfCheckLogService;
	@Autowired
	private SystemMetricsService systemMetricsService;
	@Autowired
	private AppInfoService appInfoService;
	@Autowired
	private MetricInfoService metricInfoService;
	@Autowired
	private MetricDetailService metricDetailService;
	@Autowired
	private DeviceUsedService deviceUsedService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Value("${cleverm.push.dirs}")
	private String DOWNLOAD_HOME;

	@Override
	public int receiveLog(long orgId, UploadResourceType fileType, String logText) throws RemoteException {
		return 0;
	}

	@Override
	public int receiveText(long orgId, String text) throws RemoteException {
		return 0;
	}

	@Override
	public String readTextFile(long orgId, String path) throws RemoteException {
		LOG.info("orgId= {}  path= {} DOWNLOAD_HOME={}", orgId, path, DOWNLOAD_HOME);
		String fileData = "";
		StringBuffer sb = new StringBuffer();
		sb.append(DOWNLOAD_HOME).append(File.separator).append(orgId).append(File.separator).append(path);
		String filePath = sb.toString();
		LOG.info(" File path is ==========" + filePath);
		File file = new File(filePath);
		if (!file.exists()) {
			return fileData;
		}
		try {
			fileData = FileUtils.readFileToString(file, "utf-8");
		} catch (IOException e) {
			LOG.error(" read file error ", e);
		}

		return fileData;
	}

	@Override
	public boolean uploadData(DataUploadInfoDTO dataUploadInfoDTO) throws RemoteException {
		if (dataUploadInfoDTO != null && "selfCheckLog".equals(dataUploadInfoDTO.getType().getValue())) {
			SelfCheckLogVO selfCheckLogVO = JSON.parseObject(dataUploadInfoDTO.getData(), SelfCheckLogVO.class);
			try {
				SelfCheckLog scl = selfCheckLogService.update(new SelfCheckLog(selfCheckLogVO));
				if (scl != null) {
					return true;
				}
			} catch (ServiceException e) {
				System.out.println("error: " + e.toString());
			}
		}
		return false;
	}

	@Override
	public boolean uploadSystemMetrics(final List<SystemMetrics> list) throws RemoteException {
		if (list == null) {
			return false;
		}

		//异步插入数据库
		//用线程池代替原来的new Thread方法
		taskExecutor.submit(new Runnable() {
			@Override
			public void run() {
				List<SystemMetrics> list1 = JSON.parseArray(JSON.toJSONString(list), SystemMetrics.class);
				MetricsUtil.updateSystemMetrics(list1, deviceUsedService, appInfoService, metricInfoService, metricDetailService, systemMetricsService);
			}
		});

		return true;
	}

}
