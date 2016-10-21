package com.myee.tarot.remote.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.metric.domain.AppInfo;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface AppInfoService extends GenericEntityService<Long, AppInfo> {
    List<AppInfo> listBySystemMetricsId(Long systemMetricsId);

    List<AppInfo> listByBoardNoPeriod(String boardNo, Long now, Long period, String nodeName);
}
