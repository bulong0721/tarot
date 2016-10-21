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
import com.myee.tarot.remote.service.AppInfoService;
import com.myee.tarot.remote.service.MetricDetailService;
import com.myee.tarot.remote.service.MetricInfoService;
import com.myee.tarot.remote.service.SystemMetricsService;
import com.myee.tarot.remote.util.MetricsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private MetricInfoService metricInfoService;
    @Autowired
    private MetricDetailService metricDetailService;
    @Autowired
    private DeviceUsedService deviceUsedService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

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
