package com.myee.tarot.customer.service.impl;

import com.myee.tarot.customer.domain.ChallengeQuestion;
import com.myee.tarot.customer.dao.ChallengeQuestionDao;
import com.myee.tarot.customer.service.ChallengeQuestionService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class ChallengeQuestionServiceImpl extends GenericEntityServiceImpl<java.lang.Long, ChallengeQuestion> implements ChallengeQuestionService {

    protected ChallengeQuestionDao dao;

    @Autowired
    public ChallengeQuestionServiceImpl(ChallengeQuestionDao dao) {
        super(dao);
        this.dao = dao;
    }

}

