package com.myee.tarot.djinn.service.impl;

import com.alibaba.fastjson.JSON;
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
import com.myee.tarot.metrics.domain.AppInfo;
import com.myee.tarot.metrics.domain.MetricsInfo;
import com.myee.tarot.remote.service.AppInfoService;
import com.myee.tarot.remote.service.MetricsInfoService;
import com.myee.tarot.remote.service.SystemMetricsService;
import com.myee.tarot.remote.util.MetricsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/9/6.
 */
@Service
public class DataStoreServiceImpl implements DataStoreService, TransactionalAspectAware {

	@Autowired
	private SelfCheckLogService selfCheckLogService;
	@Autowired
	private SystemMetricsService systemMetricsService;
	@Autowired
	private AppInfoService appInfoService;
	@Autowired
	private MetricsInfoService metricsInfoService;

	@Autowired
	private DeviceUsedService deviceUsedService;

	@Override
	public int receiveLog(long orgId, UploadResourceType fileType, String logText) throws RemoteException {
		return 0;
	}

	@Override
	public int receiveText(long orgId, String text) throws RemoteException {
		return 0;
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
	public boolean uploadSystemMetrics(List<SystemMetrics> list) throws RemoteException {
		if(list == null){
			return false;
		}

		return MetricsUtil.updateSystemMetrics(list, deviceUsedService,appInfoService,metricsInfoService, systemMetricsService);
	}

}
