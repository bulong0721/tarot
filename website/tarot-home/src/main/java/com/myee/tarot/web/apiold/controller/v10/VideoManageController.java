package com.myee.tarot.web.apiold.controller.v10;

import com.myee.tarot.apiold.domain.VideoBusiness;
import com.myee.tarot.apiold.domain.VideoPublish;
import com.myee.tarot.apiold.service.VideoBusinessService;
import com.myee.tarot.apiold.service.VideoPublishService;
import com.myee.tarot.apiold.view.VideoBusinessView;
import com.myee.tarot.core.Constants;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.weixin.domain.ClientAjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Info: 视频接口
 * User: enva.liang@clever-m.com
 * Date: 2016-01-25
 * Time: 15:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
@Scope("prototype")
@RequestMapping("/api/v10")
public class VideoManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(VideoManageController.class);

    @Autowired
    private VideoBusinessService videoBusinessManageService;
    @Autowired
    private VideoPublishService videoPublishManageService;

    @Value("${Demand_Video_Switch}")
    private boolean Demand_Video_Switch;
    /**
     * 点点笔广告视频
     * @param orgId
     * @param type: 0或null只显示商户,1需要显示商业
     * @return
     */
    @RequestMapping(value = "/video/list")
    public ClientAjaxResult queryVideoList(@RequestParam(value = "orgId") Long orgId,
                                              @RequestParam(value = "type", required = false) Integer type) {
        logger.info("查询点点笔广告视频列表,orgId:" + orgId);
        try {
            //取待机广告视频和点播视频，让pad每天开机的时候就开始下载点播视频，这样在点播的时候才会流畅
            Date now = new Date();
            //获取本店视频列表，每个店铺限定2个，一个待机视频，一个点播视频
            List<VideoBusiness> listBusiness = videoBusinessManageService.listByTypeStoreTime(orgId, Constants.API_OLD_TYPE_SHOP, now);
            List<VideoPublish> listPublish = null;
            //获取推送商业视频列表
            if(type == 1){
                listPublish = videoPublishManageService.listByStoreTime(orgId, now);
            }

            if( listBusiness == null && listPublish == null ){
                return ClientAjaxResult.success();
            }

            List<VideoBusinessView> list = new ArrayList<VideoBusinessView>();
            if(listBusiness != null && listBusiness.size() > 0){
                for(VideoBusiness videoBusiness:listBusiness){
                    //-----------------
                    if(Demand_Video_Switch) {
                        if (videoBusiness.getKind() == 2) {//20160712去掉点播视频，功能暂时不用了
                            continue;
                        }
                    }
                    //-----------------
                    VideoBusinessView videoBusinessDTView = new VideoBusinessView(videoBusiness);
                    list.add(videoBusinessDTView);
                }
            }
            if(listPublish != null && listPublish.size() > 0){
                for(VideoPublish videoPublish:listPublish){
                    VideoBusinessView videoBusinessDTView = new VideoBusinessView(videoPublish);
                    list.add(videoBusinessDTView);
                }
            }

            return ClientAjaxResult.success(list);

        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    @RequestMapping(value = "/video/get")
    public ClientAjaxResult queryVideoById(@RequestParam(value = "vBId") Long vBId,
                                              @RequestParam(value = "type", required = false) Integer type) {
        logger.info("根据视频商业属性ID查询点点笔广告视频,videoBusinessId:" + vBId);
        try {
            VideoBusiness vB = videoBusinessManageService.findByIdType(vBId,type);
            if(vB == null){
                return ClientAjaxResult.success();
            }
            VideoBusinessView vBV1 = new VideoBusinessView(vB);
            return ClientAjaxResult.success(vBV1);
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }



}