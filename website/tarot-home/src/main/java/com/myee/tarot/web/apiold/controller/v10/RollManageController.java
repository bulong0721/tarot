package com.myee.tarot.web.apiold.controller.v10;

import com.myee.tarot.apiold.domain.AdvertisementPublish;
import com.myee.tarot.apiold.domain.RollMain;
import com.myee.tarot.apiold.domain.RollMainPublish;
import com.myee.tarot.apiold.service.AdvertisementPublishService;
import com.myee.tarot.apiold.service.RollMainPublishService;
import com.myee.tarot.apiold.service.RollMainService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.weixin.domain.ClientAjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Info: 点点笔优惠专区轮播接口
 * User: enva.liang@clever-m.com
 * Date: 2016-01-25
 * Time: 15:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 * 20160901 chay:从cleverApp移植，由于数据库结构有修改，业务逻辑实现方式有变化
 */
@RestController
@Scope("prototype")
@RequestMapping("/api/v10")
public class RollManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(RollManageController.class);

    @Autowired
    private RollMainService rollMainService;
    @Autowired
    private RollMainPublishService rollMainPublishService;
    @Autowired
    private AdvertisementPublishService advertisementPublishService;



//    @Autowired
//    private DetailTextManageService detailTextManageService;

    /**
     * 轮播主图列表
     * @param orgId
     * @param type: 0或null：只显示商户,1：商户和商业的都需要显示
     * @return
     */
    @RequestMapping(value = "/roll/main/list")
    public ClientAjaxResult queryRollMainList(@RequestParam(value = "orgId") Long orgId,
                                              @RequestParam(value = "type", required = false) Integer type) {
        logger.info("查询轮播主图列表,orgId:" + orgId);
        if((type != null && type != Constants.API_OLD_TYPE_SHOP && type != Constants.API_OLD_TYPE_MUYE) || null == orgId){
            return ClientAjaxResult.failed("参数错误");
        }
        Date now = new Date();
        List<Object> resultList = new ArrayList<Object>();
        List<Object> adPublishEntryList = new ArrayList<Object>();
        try {
            //获取在有效期内的本店活动rollMain
            List<RollMain> rollMainList = rollMainService.listByTypeStoreTime(orgId, Constants.API_OLD_TYPE_SHOP,now);
            //获取在有效期内的广告推送列表AdvertisementRollMain(CleverApp项目中叫法)，AdvertisementRollMain就是AdvertisementPublish(tarot项目中)
            List<AdvertisementPublish> adPublishList = advertisementPublishService.listByStoreTime(orgId,now);

            if(null != adPublishList && adPublishList.size() >0){
                for (AdvertisementPublish adPublish : adPublishList) {
                    adPublishEntryList.add(objectToEntry(adPublish));
                }
            }
            if(null != rollMainList && rollMainList.size() >0){
                for (RollMain rollMain : rollMainList) {
                    resultList.add(objectToEntry(rollMain,adPublishEntryList));
                }
            }

            //只有type=1的时候才获取商业木爷推送广告列表
            if(type == Constants.API_OLD_TYPE_MUYE){
                //获取在有效期内的轮播推送列表,rollPublish
                List<RollMainPublish> rollMainPublishList  = rollMainPublishService.listByStoreTime(orgId,now);
                if( null != rollMainPublishList && rollMainPublishList.size() >0){
                    for (RollMainPublish rollMainPublish : rollMainPublishList) {
                        resultList.add(objectToEntry(rollMainPublish,adPublishEntryList));
                    }
                }
            }

            return ClientAjaxResult.success(resultList);
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(AdvertisementPublish adPublish) {
        Map entry = new HashMap();
        entry.put("advertisementId",adPublish.getAdvertisement().getId());
        entry.put("orderSeq",adPublish.getOrderSeq());
        entry.put("title",adPublish.getAdvertisement().getTitle());
        entry.put("description", adPublish.getAdvertisement().getDescription());
        entry.put("pictruePath", adPublish.getAdvertisement().getMainPic().getPicPath());
        entry.put("qiniuPath", adPublish.getAdvertisement().getMainPic().getPicQiniuPath());
        entry.put("picOrinName", adPublish.getAdvertisement().getMainPic().getOriginal());
        entry.put("timeEnd", adPublish.getTimeEnd());
        entry.put("timeStart", adPublish.getTimeStart());
        return entry;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(RollMainPublish rollMainPublish,List<Object> adPublishEntryList) {
        Map entry = new HashMap();
        entry.put("rollMainId",rollMainPublish.getRollMain().getId());
        entry.put("orderSeq", rollMainPublish.getOrderSeq());
        entry.put("title",rollMainPublish.getRollMain().getTitle());
        entry.put("description", rollMainPublish.getRollMain().getDescription());
        entry.put("pictruePath", rollMainPublish.getRollMain().getMainPic().getPicPath());
        entry.put("qiniuPath", rollMainPublish.getRollMain().getMainPic().getPicQiniuPath());
        entry.put("picOrinName", rollMainPublish.getRollMain().getMainPic().getOriginal());
        entry.put("timeEnd", rollMainPublish.getTimeEnd());
        entry.put("timeStart", rollMainPublish.getTimeStart());
        entry.put("type", rollMainPublish.getRollMain().getType());

        return entry;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(RollMain rollMain,List<Object> adPublishEntryList) {
        Map entry = new HashMap();
        entry.put("rollMainId",rollMain.getId());
        entry.put("orderSeq", rollMain.getOrderSeq());
        entry.put("title",rollMain.getTitle());
        entry.put("description", rollMain.getDescription());
        entry.put("pictruePath", rollMain.getMainPic().getPicPath());
        entry.put("qiniuPath", rollMain.getMainPic().getPicQiniuPath());
        entry.put("picOrinName", rollMain.getMainPic().getOriginal());
        entry.put("timeEnd", rollMain.getTimeEnd());
        entry.put("timeStart", rollMain.getTimeStart());
        entry.put("type", rollMain.getType());

        return entry;
    }

//    /**
//     * 轮播详细列表
//     * @param orgId
//     * @return
//     */
//    @RequestMapping(value = "/roll/detail/list")
//    public ClientAjaxResult queryRollDetailList(@RequestParam(value = "orgId") Long orgId,
//                                                @RequestParam(value = "rollMainId") Long rollMainId) {
//        logger.info("查询轮播详细列表,rollMainId:" + rollMainId);
//        try {
//            DetailText text = (DetailText)detailTextManageService.getEntity(new DetailText(orgId, rollMainId));
//            return ClientAjaxResult.success(text);
//        }  catch (Exception e) {
//            e.printStackTrace();
//            logger.error(e.getMessage(), e);
//            return ClientAjaxResult.failed("糟了...系统出错了...");
//        }
//    }

}