package com.myee.tarot.resource.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.resource.dao.UpdateConfigDao;
import com.myee.tarot.resource.domain.QUpdateConfig;
import com.myee.tarot.resource.domain.UpdateConfig;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Chay on 2016/12/15.
 */
@Repository
public class UpdateConfigDaoImpl extends GenericEntityDaoImpl<Long, UpdateConfig> implements UpdateConfigDao {

    public PageResult<UpdateConfig> page(WhereRequest whereRequest) throws ParseException {
        PageResult<UpdateConfig> pageList = new PageResult<UpdateConfig>();
        QUpdateConfig qUpdateConfig = QUpdateConfig.updateConfig;
        JPQLQuery<UpdateConfig> query = new JPAQuery(getEntityManager());
        query.from(qUpdateConfig);
		if (whereRequest.getQueryObj() != null) {
			JSONObject map = JSON.parseObject(whereRequest.getQueryObj());
			Object obj = map.get(Constants.SEARCH_OPTION_TYPE);
			if (obj != null && !StringUtil.isBlank(obj.toString())) {
				query.where(qUpdateConfig.type.eq(obj.toString()));
			}
			obj = map.get(Constants.SEARCH_UPDATE_CONFIG_SEE_TYPE);
			if (obj != null && !StringUtil.isBlank(obj.toString())) {
				query.where(qUpdateConfig.seeType.eq(obj.toString()));
			}
			//TODO  临时处理，前端未找到好的处理方式
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			obj = map.get(Constants.SEARCH_BEGIN_DATE);
			if (obj != null && !StringUtil.isBlank(obj.toString())) {
				String dateString = obj.toString();
				dateString = dateString.replace("T", " ");
				dateString = dateString.replace("Z", " ");
				query.where(qUpdateConfig.createTime.after(format.parse(dateString)));
			}
			obj = map.get(Constants.SEARCH_END_DATE);
			if (obj != null && !StringUtil.isBlank(obj.toString())) {
				String dateString = obj.toString();
				dateString = dateString.replace("T", " ");
				dateString = dateString.replace("Z", " ");
				query.where(qUpdateConfig.createTime.before(format.parse(dateString)));
			}
		}
        pageList.setRecordsTotal(query.fetchCount());

        query.orderBy(qUpdateConfig.createTime.desc());
        if( whereRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
        }
		List<UpdateConfig> lists = query.fetch();
        pageList.setList(lists);
        return pageList;
    }
}
