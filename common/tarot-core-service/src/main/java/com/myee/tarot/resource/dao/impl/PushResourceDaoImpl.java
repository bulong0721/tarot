package com.myee.tarot.resource.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.resource.dao.PushResourceDao;
import com.myee.tarot.resource.domain.PushResource;
import org.springframework.stereotype.Repository;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
@Repository
public class PushResourceDaoImpl extends GenericEntityDaoImpl<Long, PushResource> implements PushResourceDao {
}
