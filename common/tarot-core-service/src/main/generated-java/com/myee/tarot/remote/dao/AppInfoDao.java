package com.myee.tarot.remote.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.metric.domain.AppInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface AppInfoDao extends GenericEntityDao<Long, AppInfo> {
    List<AppInfo> listBySystemMetricsId(Long systemMetricsId);

    List<AppInfo> listByBoardNoPeriod(String boardNo, Long now, Long period, String nodeName);

    List<AppInfo> listByCreateTime(Date date);

    void deleteByTime(Date date);
}
