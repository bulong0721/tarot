package com.myee.tarot.web.apiold.controller;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.apiold.domain.Material;
import com.myee.tarot.apiold.domain.Picture;
import com.myee.tarot.apiold.service.MaterialFileKindService;
import com.myee.tarot.apiold.service.PictureService;
import com.myee.tarot.core.util.*;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.apiold.util.CommonLoginParam;
import com.myee.tarot.web.apiold.util.FileValidCreateUtil;
import com.myee.tarot.web.apiold.util.QiniuStoreClient;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chay on 2016/9/5.
 */
@Controller
@RequestMapping(value = {"admin/superman/picture", "shop/superman/picture"})
public class PictureManageController extends BaseController{

    private Logger LOGGER = LoggerFactory.getLogger(PictureManageController.class);
    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private MaterialFileKindService materialFileKindService;

    @RequestMapping(value = "/tokenAndKey")
    @ResponseBody
    public AjaxResponse qiniuTokenAndKey(){
        AjaxResponse resp = AjaxResponse.success();
        resp.addEntry("uptoken",QiniuStoreClient.getUploadTokenAndKey(getCommConfig()).getUptoken());
        return resp;
//        return AjaxResult.success(QiniuStoreClient.getUploadTokenAndKey(user.getClientId(), user.getOrgId(), user.getUserId(), fileType));
    }

    /**
     * picture
     */
    /**
     * 图片文件上传
     * @param file
     * @param kind  图片文件作用:图片菜品种类（未定义）(作为文件夹分类用),0：未分类，1：优惠专区，2：广告图片，3：菜品图片,4:点点笔截图...
     * @param qiniuPath
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addPicture(@RequestParam("file") CommonsMultipartFile file,
                                   @RequestParam(value = "kind") Integer kind,
                                   @RequestParam(value = "qiniuPath") String qiniuPath,
                                     HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            if(file.isEmpty() ){
                return AjaxResponse.failed(-1,"文件不能为空");
            }
            //根据当前用户获取切换的门店信息
            String sessionName = (String)CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION);
            if (sessionName == null || request.getSession().getAttribute(sessionName) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(sessionName);

            //调用通用上传文件方法，成功则返回material，失败则返回null
            Material material = FileValidCreateUtil.validCreateFile(file,DOWNLOAD_HOME,merchantStore1.getId(),materialFileKindService);
            if(material == null){
                return AjaxResponse.failed(-1,"保存文件失败，请上传jpg、png、bmp格式");
            }
            //图片文件已经通过其他接口上传，这里值需要更新图片文件信息到数据库即可
            Integer userType = (Integer) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_USERTYPE);

            Picture picture = new Picture();
            picture.setDay(DateTimeUtils.getYear());
            picture.setMonth(DateTimeUtils.getYearMonth());
            picture.setDay(DateTimeUtils.getYearMonthDay());
            picture.setType(userType);
            picture.setKind(kind);
            picture.setOriginal(material.getOriginal());
            picture.setPicPath(material.getMaterialPath());
            picture.setPicQiniuPath(material.getQiniuPath());
            picture.setPreviewPath(material.getPreviewPath());
            picture.setStore(merchantStore1);
            if(picture.getId() == null){
                picture.setvCreated(new Date());
                picture.setvCreatedBy((Long)CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_USERID));
            }


            picture = pictureService.update(picture);

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", picture);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
            return resp;
        }
        return resp;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse delPicture(@RequestBody Picture picture, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        try {
            //根据当前用户获取切换的门店信息
            String sessionName = (String)CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION);
            if (sessionName == null || request.getSession().getAttribute(sessionName) == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "请先切换门店");
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(sessionName);

            if (picture.getId() == null || StringUtil.isNullOrEmpty(picture.getId().toString())) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "参数错误");
            }
            Picture picture1 = pictureService.findById(picture.getId());
            if (picture1 == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "图片不存在");
            }
            //本店用户只能按当前切换门店删除本店图片，管理员可以删除type为1的所有
            Integer userType = (Integer) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_USERTYPE);
            if (userType != picture1.getType()) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "没有权限");
            }
            if(userType == Constants.API_OLD_TYPE_SHOP && merchantStore1.getId() != picture1.getStore().getId()){
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "请切换到对应门店删除该门店下的图片");
            }

            //采用假删除
//            File file = new File(DOWNLOAD_HOME+"/"+picture1.getPicPath());
//            boolean flag = FileUtils.deleteQuietly(file);
            picture1.setActive(false);
            pictureService.update(picture1);
            return AjaxResponse.success();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"出错");
        }
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getPicture(@RequestBody Picture picture, HttpServletRequest request) throws Exception {
        AjaxResponse resp = AjaxResponse.success();
        try {
            //根据当前用户获取切换的门店信息
            String sessionName = (String)CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION);
            if (sessionName == null || request.getSession().getAttribute(sessionName) == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "请先切换门店");
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(sessionName);

            if (picture.getId() == null || StringUtil.isNullOrEmpty(picture.getId().toString())) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "参数错误");
            }
            Picture picture1 = pictureService.findById(picture.getId());
            if (picture1 == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "图片不存在");
            }
            //本店用户只能按当前切换门店删除本店图片，管理员可以删除type为1的所有
            Integer userType = (Integer) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_USERTYPE);
            if (userType != picture1.getType()) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "没有权限");
            }
            if(userType == Constants.API_OLD_TYPE_SHOP && merchantStore1.getId() != picture1.getStore().getId()){
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "请切换到对应门店获取该门店下的图片");
            }

            resp.addDataEntry(objectToEntry(picture1));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"出错");
        }
    }

    @RequestMapping(value = "/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pagePictures(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        String sessionName = (String)CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION);
        if (sessionName == null || request.getSession().getAttribute(sessionName) == null) {
            return AjaxPageableResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"请先切换门店");
        }
        MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(sessionName);

        //本店用户只能按当前切换门店查看本店图片，管理员可以查看type为1的所有
        Integer userType = (Integer) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_USERTYPE);
        PageResult<Picture> pageList = null;
        if(userType == Constants.API_OLD_TYPE_MUYE){
            pageList = pictureService.pageByTypeStore(userType, null, pageRequest);
        }
        else if( userType == Constants.API_OLD_TYPE_SHOP ){
            pageList = pictureService.pageByTypeStore(userType, merchantStore1.getId(), pageRequest);
        }

        if(pageList != null){
            List<Picture> pictureList = pageList.getList();
            for (Picture picture : pictureList) {
                resp.addDataEntry(objectToEntry(picture));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        }

        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Picture picture) {
        Map entry = new HashMap();
        entry.put("id", picture.getId());
        entry.put("previewPath", picture.getPreviewPath());
        entry.put("picPath", picture.getPicPath());
        entry.put("picQiniuPath", picture.getPicQiniuPath());
        entry.put("picOrinName", picture.getOriginal());
        entry.put("kind", picture.getKind());
        //该路径是给前端预览用的，因为前端我们约定好了图片预览路径是tree.downloadPath
        Map downloadPath = new HashMap();
        //预览图片优先级：七牛>本地缩略图>本地原图
        if(!StringUtil.isBlank(picture.getPicQiniuPath())){
            downloadPath.put("downloadPath",picture.getPicQiniuPath());
        }
        else if( !StringUtil.isBlank(picture.getPreviewPath()) ){
            downloadPath.put("downloadPath",picture.getPreviewPath());
        }
        else if( !StringUtil.isBlank(picture.getPicPath()) ){
            downloadPath.put("downloadPath",picture.getPicPath());
        }
        entry.put("tree", downloadPath);
        return entry;
    }
}
