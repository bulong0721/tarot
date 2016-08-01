package com.myee.tarot.web.files.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.myee.djinn.dto.ResponseData;
import com.myee.djinn.endpoint.OrchidService;
import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.tarot.catalog.domain.Notification;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.apiold.BusinessException;
import com.myee.tarot.web.files.vo.FileItem;
import com.myee.tarot.web.files.vo.PushDTO;
import com.myee.tarot.web.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class PushController {

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;
    @Value("${cleverm.push.http}")
    private String DOWNLOAD_HTTP;

    @Autowired
    private ServerBootstrap serverBootstrap;

    @RequestMapping("admin/file/search")
    @ResponseBody
    public AjaxPageableResponse searchResource(@RequestParam("node") String parentNode, HttpServletRequest request) {
//        if ("/".equals(parentNode)) {
//            FileItem root = new FileItem();
//            root.setPath("/");
////            root.setResTypeName("目录");
//            return new AjaxPageableResponse(Arrays.<Object>asList(root));
//        }
        MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.ADMIN_STORE);
        File template = getResFile(100L, parentNode);
        Map<String, FileItem> resMap = Maps.newLinkedHashMap();
        listFiles(template, resMap, 100L, store.getId());
        if (100L != store.getId()) {
            File dir = getResFile(store.getId(), parentNode);
            listFiles(dir, resMap, store.getId(), store.getId());
        }
        return new AjaxPageableResponse(Lists.<Object>newArrayList(resMap.values()));
    }

    @RequestMapping("admin/file/create")
    @ResponseBody
    public FileItem createResource(long orgID, @RequestParam("resFile") CommonsMultipartFile file, String entityText) throws IllegalStateException, IOException {
        FileItem vo = JSON.parseObject(entityText, FileItem.class);
        File dest = getResFile(orgID, vo.getPath());
        vo.setSalt(orgID);
//        if (1 == vo.getResType()) {
//            dest.mkdirs();
//        } else
        if (!file.isEmpty()) {
            dest.mkdirs();
            file.transferTo(dest);
        } else if (!StringUtil.isNullOrEmpty(vo.getContent(), true)) {
            FileUtils.writeStringToFile(dest, vo.getContent());
        }
        return vo;
    }

    @RequestMapping("admin/content/get")
    @ResponseBody
    public String getContentText(Long orgID, String absPath) {
        File resFile = getResFile(orgID, absPath);
        if (!resFile.exists()) {
            return "";
        }
        if (resFile.length() > 4096L) {
            throw new BusinessException("超过文本读取大小限制。");
        }
        try {
            String value = FileUtils.readFileToString(resFile, "utf-8");
            return value;
        } catch (IOException ie) {
            throw new BusinessException(ie);
        }
    }

    @RequestMapping("admin/file/delete")
    @ResponseBody
    @Transactional
    public boolean deleteResource(@RequestParam("salt") Long orgID, String path, HttpServletRequest request) {
        MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.ADMIN_STORE);
        if(store == null ){
            return false;
        }
        if (store.getId() != orgID) {
            return false;
        }
        File resFile = getResFile(orgID, path);
        if (resFile.exists()) {
            FileUtils.deleteQuietly(resFile);
        }
        return true;
    }

    private void listFiles(File parentFile, Map<String, FileItem> resMap, Long orgID, Long storeId) {
        if (!parentFile.exists() || !parentFile.isDirectory() || null == parentFile.listFiles()) {
            return;
        }
        String prefix = FilenameUtils.concat(DOWNLOAD_HOME, Long.toString(orgID));
        String prefixHttp = FilenameUtils.concat(DOWNLOAD_HTTP, Long.toString(orgID));
        for (File file : parentFile.listFiles()) {
            FileItem fileItem = FileItem.toResourceModel(file, orgID, storeId);
            fileItem.setPath(trimStart(fileItem.getPath(), prefix));
            fileItem.setUrl(FilenameUtils.concat(prefixHttp, fileItem.getPath()));
            resMap.put(file.getName(), fileItem);
        }
    }

    private File getResFile(Long orgID, String absPath) {
        return FileUtils.getFile(DOWNLOAD_HOME, Long.toString(orgID), absPath);
    }

    String trimStart(String absPath, String prefix) {
        String result = absPath;
        if (result.startsWith(prefix)) {
            result = result.substring(prefix.length() + 1);
        }
        return result;
    }

    @RequestMapping(value = "admin/file/push", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse pushResource(@Valid @RequestBody PushDTO pushDTO) {
        OrchidService eptService = null;
        try {
            eptService = serverBootstrap.getClient(OrchidService.class, pushDTO.getUniqueNo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        AjaxResponse resp = new AjaxResponse();
        String pushStr = JSONObject.toJSONString(pushDTO);
        ResponseData rd = eptService.sendNotification(pushStr);
        if(rd != null && rd.isSuccess()) {
            resp = AjaxResponse.success();
        } else {
            resp = AjaxResponse.failed(-1);
        }
        return resp;
    }

    @RequestMapping(value = "admin/table/push", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse pushTable(String tableStr, String mbNum) {
        String tableStrTest = "{\"id\":1,\"tableZone\":{\"id\":1,\"name\":\"A区\"},\"description\":\"测试2\",\"name\":\"测试1\",\"tableType\":{\"id\":2,\"name\":\"大桌1\"}}";
        String mbNumStr = "Gaea-23#";
        mbNum = mbNumStr;
        OrchidService eptService = null;
        ResponseData rd = null;
        AjaxResponse resp = new AjaxResponse();
        try {
            eptService = serverBootstrap.getClient(OrchidService.class, mbNum);
            String pushTableStr = JSONObject.toJSONString(tableStrTest);
            rd = eptService.sendNotification(pushTableStr);
            if(rd != null) {
                resp = AjaxResponse.success();
            } else {
                resp = AjaxResponse.failed(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    final static Charset charset = Charset.forName("UTF8");

    static String toClientUUID(long shopId) {
        String rawId = String.format("shopId:%08d", shopId);
        return Base64.encodeBase64String(rawId.getBytes(charset));
    }

}
