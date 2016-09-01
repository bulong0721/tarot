package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.TasteDao;
import com.myee.tarot.apiold.domain.TasteInfo;
import com.myee.tarot.apiold.service.TasteService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class TasteServiceImpl extends GenericEntityServiceImpl<Long, TasteInfo> implements TasteService {

    protected TasteDao tasteDao;

    @Autowired
    public TasteServiceImpl(TasteDao tasteDao) {
        super(tasteDao);
        this.tasteDao = tasteDao;
    }
}
