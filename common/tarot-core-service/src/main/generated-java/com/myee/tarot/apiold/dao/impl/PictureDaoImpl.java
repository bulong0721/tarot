package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.PictureDao;
import com.myee.tarot.apiold.domain.Picture;
import com.myee.tarot.apiold.domain.QPicture;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class PictureDaoImpl extends GenericEntityDaoImpl<Long, Picture> implements PictureDao {

    public PageResult<Picture> pageByTypeStore(Integer userType, Long storeId, PageRequest pageRequest){
        PageResult<Picture> pageList = new PageResult<Picture>();
        QPicture qPicture = QPicture.picture;

        JPQLQuery<Picture> query = new JPAQuery(getEntityManager());

        query.from(qPicture);
        if(storeId != null){
//            query.where(qPicture.store.id.eq(storeId));
        }
        query.where((qPicture.type.eq(userType)).and(qPicture.active.eq(true)))
                .orderBy(qPicture.id.desc());

        pageList.setRecordsTotal(query.from(qPicture).fetchCount());
        if (pageRequest.getCount() > 0) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
