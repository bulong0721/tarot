package com.myee.tarot.web.apiold.controller.v10;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.tarot.apiold.domain.*;
import com.myee.tarot.apiold.service.*;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ValidatorUtil;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.web.ClientAjaxResult;
import com.myee.tarot.web.apiold.util.CommonLoginParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
public class RollManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(RollManageController.class);

    @Autowired
    private RollMainService rollMainService;
    @Autowired
    private RollDetailService rollDetailService;
    @Autowired
    private RollMainPublishService rollMainPublishService;
    @Autowired
    private AdvertisementPublishService advertisementPublishService;
    @Autowired
    private PictureService pictureService;


//    @Autowired
//    private DetailTextManageService detailTextManageService;

    /**
     * 轮播主图列表
     *
     * @param orgId
     * @param type: 0或null：只显示商户,1：商户和商业的都需要显示
     * @return
     */
    @RequestMapping(value = "/api/v10/roll/main/list")
    public ClientAjaxResult queryRollMainList(@RequestParam(value = "orgId") Long orgId,
                                              @RequestParam(value = "type", required = false) Integer type) {
        logger.info("查询轮播主图列表,orgId:" + orgId);
        if ((type != null && type != Constants.API_OLD_TYPE_SHOP && type != Constants.API_OLD_TYPE_MUYE) || null == orgId) {
            return ClientAjaxResult.failed("参数错误");
        }
        Date now = new Date();
        List<Object> resultList = new ArrayList<Object>();
        List<Object> adPublishEntryList = new ArrayList<Object>();
        try {
            //获取在有效期内的本店活动rollMain
            List<RollMain> rollMainList = rollMainService.listByTypeStoreTime(orgId, Constants.API_OLD_TYPE_SHOP, now);
            //获取在有效期内的广告推送列表AdvertisementRollMain(CleverApp项目中叫法)，AdvertisementRollMain就是AdvertisementPublish(tarot项目中)
            List<AdvertisementPublish> adPublishList = advertisementPublishService.listByStoreTime(orgId, now);

            if (null != adPublishList && adPublishList.size() > 0) {
                for (AdvertisementPublish adPublish : adPublishList) {
                    adPublishEntryList.add(objectToEntry(adPublish));
                }
            }
            if (null != rollMainList && rollMainList.size() > 0) {
                for (RollMain rollMain : rollMainList) {
                    resultList.add(objectToEntry(rollMain, adPublishEntryList));
                }
            }

            //只有type=1的时候才获取商业木爷推送广告列表
            if (type == Constants.API_OLD_TYPE_MUYE) {
                //获取在有效期内的轮播推送列表,rollPublish
                List<RollMainPublish> rollMainPublishList = rollMainPublishService.listByStoreTime(orgId, now);
                if (null != rollMainPublishList && rollMainPublishList.size() > 0) {
                    for (RollMainPublish rollMainPublish : rollMainPublishList) {
                        resultList.add(objectToEntry(rollMainPublish, adPublishEntryList));
                    }
                }
            }

            return ClientAjaxResult.success(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(AdvertisementPublish adPublish) {
        Map entry = new HashMap();
        entry.put("advertisementId", adPublish.getAdvertisement().getId());
        entry.put("orderSeq", adPublish.getOrderSeq());
        entry.put("title", adPublish.getAdvertisement().getTitle());
        entry.put("description", adPublish.getAdvertisement().getDescription());
        entry.put("pictruePath", adPublish.getAdvertisement().getMainPic().getPicPath());
        entry.put("qiniuPath", adPublish.getAdvertisement().getMainPic().getPicQiniuPath());
        entry.put("picOrinName", adPublish.getAdvertisement().getMainPic().getOriginal());
        entry.put("timeEnd", adPublish.getTimeEnd());
        entry.put("timeStart", adPublish.getTimeStart());
        return entry;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(RollMainPublish rollMainPublish, List<Object> adPublishEntryList) {
        Map entry = new HashMap();
        entry.put("rollMainId", rollMainPublish.getRollMain().getId());
        entry.put("rollTime", rollMainPublish.getRollTime());
        entry.put("orderSeq", rollMainPublish.getOrderSeq());
        entry.put("title", rollMainPublish.getRollMain().getTitle());
        entry.put("description", rollMainPublish.getRollMain().getDescription());
        entry.put("pictruePath", rollMainPublish.getRollMain().getMainPic().getPicPath());
        entry.put("qiniuPath", rollMainPublish.getRollMain().getMainPic().getPicQiniuPath());
        entry.put("picOrinName", rollMainPublish.getRollMain().getMainPic().getOriginal());
        entry.put("timeEnd", rollMainPublish.getTimeEnd());
        entry.put("timeStart", rollMainPublish.getTimeStart());
        entry.put("type", rollMainPublish.getRollMain().getType());
        entry.put("advertisementList", adPublishEntryList);
        entry.put("rollDetailList", objectToEntryList(rollMainPublish.getRollMain().getRollDetailList()));

        return entry;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(RollMain rollMain, List<Object> adPublishEntryList) {
        Map entry = new HashMap();
        entry.put("rollMainId", rollMain.getId());
        entry.put("rollTime", rollMain.getRollTime());
        entry.put("orderSeq", rollMain.getOrderSeq());
        entry.put("title", rollMain.getTitle());
        entry.put("description", rollMain.getDescription());
        entry.put("pictruePath", rollMain.getMainPic().getPicPath());
        entry.put("qiniuPath", rollMain.getMainPic().getPicQiniuPath());
        entry.put("picOrinName", rollMain.getMainPic().getOriginal());
        entry.put("timeEnd", rollMain.getTimeEnd());
        entry.put("timeStart", rollMain.getTimeStart());
        entry.put("type", rollMain.getType());
        entry.put("advertisementList", adPublishEntryList);
        entry.put("rollDetailList", objectToEntryList(rollMain.getRollDetailList()));

        return entry;
    }

    private List<Map> objectToEntryList(List<RollDetail> rollDetailList) {
        if (rollDetailList == null || rollDetailList.size() == 0) {
            return null;
        }
        List<Map> result = new ArrayList<Map>();
        for (RollDetail rollDetail : rollDetailList) {
            Map entry = new HashMap();
            entry.put("rollDetailId", rollDetail.getId());
            entry.put("rollTime", rollDetail.getRollTime());
            entry.put("orderSeq", rollDetail.getOrderSeq());
            entry.put("pictruePath", rollDetail.getPic().getPicPath());
            entry.put("qiniuPath", rollDetail.getPic().getPicQiniuPath());
            entry.put("picOrinName", rollDetail.getPic().getOriginal());
            entry.put("title", rollDetail.getTitle());
            entry.put("description", rollDetail.getDescription());
            result.add(entry);
        }

        return result;
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


    /**
     * 以下是原app接口------------------------------------------------------------------------------
     */
    @RequestMapping(value = {"admin/superman/rollMain/save", "shop/superman/rollMain/save"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addRollMain(@RequestParam RollMain rollMain,
                                    HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            //根据当前用户获取切换的门店信息
            String sessionName = (String) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION);
            if (sessionName == null || request.getSession().getAttribute(sessionName) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"请先切换门店");
                return resp;
            }

            //获取当前用户是本店还是木爷账号
            Integer userType = (Integer) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_USERTYPE);
            if (!dataValidate(rollMain, userType)) {
                return AjaxResponse.failed(-1, "参数错误");
            }

            Picture picture = pictureService.findById(rollMain.getId());
            if (picture == null) {
                return AjaxResponse.failed(-1, "主图不存在");
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(sessionName);
            //本店活动不能超过10个
            if (userType == Constants.API_OLD_TYPE_SHOP) {
                Long existCount = rollMainService.countByStore(merchantStore1.getId());
                if (existCount > Constants.ROLL_MAIN_SHOP_MAX) {
                    return AjaxResponse.failed(-1, "本店视频不能超过" + Constants.ROLL_MAIN_SHOP_MAX + "个");
                }
            }

            rollMain.setStore(merchantStore1);
            rollMain.setType(userType);
            if (rollMain.getId() == null) {
                rollMain.setrMCreated(new Date());
                rollMain.setrMCreatedBy((Long) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_USERID));
            }
            rollMain.setrMUpdated(new Date());
            rollMain.setrMUpdatedBy((Long) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_USERID));
            rollMain = rollMainService.update(rollMain);

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", rollMain);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return resp;
    }

    /**
     * 1.不能为空
     * 2.详细不能超过15个
     * 3.开始时间和结束时间必须输入且开始时间必须小于等于结束时间(木爷资源可以不输入)
     * 4.图片ID不能为空
     * 5.本店优先级范围1-30，(木爷优先级范围100+,木爷优先级只有在推送的时候才能设置)
     *
     * @param rollMain
     * @return
     */
    boolean dataValidate(RollMain rollMain, Integer userType) {
        boolean flag = false;
        Integer orderSeq = rollMain.getOrderSeq();
        if (rollMain == null) {
            return false;
        } else if (rollMain != null && rollMain.getRollDetailList() != null && rollMain.getRollDetailList().size() > 15) {
            return false;
        } else if (!flag && (rollMain.getTimeStart() == null || rollMain.getTimeEnd() == null)) {
            return false;
        } else if (!flag && (rollMain.getTimeEnd()).compareTo(rollMain.getTimeStart()) < 0) {
            return false;
        } else if (rollMain.getMainPic().getId() == null) {
            return false;
//        }else if(orderSeq != null && userType == Constants.API_OLD_TYPE_MUYE && orderSeq < 100){//木爷优先级>100,木爷优先级只有在推送的时候才能设置
//            return false;
        } else if (orderSeq != null && userType == Constants.API_OLD_TYPE_SHOP && (orderSeq < 0 || orderSeq > 30)) {//本店优先级0-30之间
            return false;
        } else {
            return true;
        }
    }

    @RequestMapping(value = {"admin/superman/rollMain/paging", "shop/superman/rollMain/paging"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageRollMain(Model model, HttpServletRequest request, PageRequest pageRequest,
                                             @RequestParam(value = "type", required = false) Integer type) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            //根据当前用户获取切换的门店信息
            String sessionName = (String) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION);
            if (sessionName == null || request.getSession().getAttribute(sessionName) == null) {
                return AjaxPageableResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"请先切换门店");
            }
            MerchantStore merchantStore = (MerchantStore) request.getSession().getAttribute(sessionName);
            //获取当前用户是本店还是木爷账号
            Integer userType = (Integer) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_USERTYPE);
            if( userType == Constants.API_OLD_TYPE_SHOP ){//店铺账号，则只能查询type为SHOP的本店活动
                type = Constants.API_OLD_TYPE_SHOP;
            }

            PageResult<RollMain> pageList = rollMainService.pageByTypeStore(pageRequest,type,merchantStore.getId());
            List<RollMain> rollMainList = pageList.getList();
            for (RollMain rollMain : rollMainList) {
                resp.addDataEntry(objectToEntry(rollMain));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            logger.error("Error while paging products", e);
            return AjaxPageableResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "出错");
        }
        return resp;
    }

    @RequestMapping(value = {"admin/superman/rollMain/delete", "shop/superman/rollMain/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteDevice(@Valid @RequestBody RollMain rollMain, HttpServletRequest request) {
        try {
            AjaxResponse resp ;
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            //先手动删除所有该对象关联的属性，再删除该对象。因为关联关系是属性多对一该对象，关联字段放在属性表里，不能通过删对象级联删除属性。
            rollDetailService.deleteByRollMain(rollMain.getId());

            RollMain rollMain1 = rollMainService.findById(rollMain.getId());
            rollMainService.delete(rollMain1);
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.failed(-1,"在其他地方被使用，无法删除");
        }

    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(RollMain rollMain) {
        Map entry = new HashMap();
        entry.put("id", rollMain.getId());
        entry.put("title", rollMain.getTitle());
        entry.put("description", rollMain.getDescription());
        entry.put("pictruePath", rollMain.getMainPic().getPicPath());
        entry.put("qiniuPath", rollMain.getMainPic().getPicQiniuPath());
        entry.put("rollDetailList", objectToEntryList(rollMain.getRollDetailList()));

        //本店视频用参数
        entry.put("timeEnd", rollMain.getTimeEnd());
        entry.put("timeStart", rollMain.getTimeStart());
        entry.put("orderSeq", rollMain.getOrderSeq());

        return entry;
    }

    @RequestMapping(value = {"admin/superman/rollDetail/save", "shop/superman/rollDetail/save"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute RollMain rollMain, @Valid @RequestBody RollDetail rollDetail, HttpServletRequest request) throws Exception {
        AjaxResponse resp ;
        RollDetail entity = rollDetail;
        if (null != rollDetail.getId()) {
            entity = rollDetailService.findById(rollDetail.getId());
            entity.setDescription(rollDetail.getDescription());
            entity.setOrderSeq(rollDetail.getOrderSeq());
            entity.setTitle(rollDetail.getTitle());
            if(rollDetail.getPic().getId() != null){
                Picture picture = pictureService.findById(rollDetail.getPic().getId());
                entity.setPic(picture);
            }
        } else {
            entity.setRollMain(rollMain);
            entity.setCreated(new Date());
        }
        entity = rollDetailService.update(entity);
        entity.setRollMain(null);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", entity);
        return resp;
    }

    @RequestMapping(value = {"admin/superman/rollDetail/delete", "shop/superman/rollDetail/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAttribute(@Valid @RequestBody RollDetail rollDetail,  HttpServletRequest request) throws Exception {
        try {
            if (null != rollDetail.getId()) {
                RollDetail entity = rollDetailService.findById(rollDetail.getId());
                rollDetailService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            logger.error("Error delete productAttributes", e);
            return AjaxResponse.failed(-1,"删除设备类型属性异常");
        }
    }
}