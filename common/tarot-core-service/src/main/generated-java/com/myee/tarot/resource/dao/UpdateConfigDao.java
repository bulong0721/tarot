package com.myee.tarot.resource.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.resource.domain.UpdateConfig;

import java.text.ParseException;

/**
 * Created by Chay on 2016/12/15.
 */
public interface UpdateConfigDao extends GenericEntityDao<Long, UpdateConfig> {

    PageResult<UpdateConfig> page(WhereRequest pageRequest) throws ParseException;
}
