package com.myee.tarot.web.files.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.PageResult;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.apiold.BusinessException;
import com.myee.tarot.web.files.vo.ResourceVo;
import com.myee.tarot.web.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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


    @RequestMapping("file/search")
    @ResponseBody
    public AjaxPageableResponse searchResource(@RequestParam("node") String parentNode, HttpServletRequest request) {
        if ("root".equals(parentNode)) {
            ResourceVo root = new ResourceVo();
            root.setName("/");
            root.setPath("/");
//            root.setResTypeName("目录");
            root.setResType(1);
            return new AjaxPageableResponse(Arrays.<Object>asList(root));
        }
        MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.ADMIN_STORE);
        File template = getResFile(100L, parentNode);
        Map<String, ResourceVo> resMap = Maps.newLinkedHashMap();
        listFiles(template, resMap, 100L);
        if (100L != store.getId()) {
            File dir = getResFile(store.getId(), parentNode);
            listFiles(dir, resMap, store.getId());
        }
        return new AjaxPageableResponse(Lists.<Object>newArrayList(resMap.values()));
    }

    @RequestMapping("file/create")
    @ResponseBody
    public ResourceVo createResource(long orgID, @RequestParam("resFile") CommonsMultipartFile file, String entityText) throws IllegalStateException, IOException {
        ResourceVo vo = JSON.parseObject(entityText, ResourceVo.class);
        File dest = getResFile(orgID, vo.getPath());
        vo.setSalt(Long.toString(orgID));
        if (1 == vo.getResType()) {
            dest.mkdirs();
        } else if (!file.isEmpty()) {
            dest.mkdirs();
            file.transferTo(dest);
        } else if (!StringUtil.isNullOrEmpty(vo.getContent(), true)) {
            FileUtils.writeStringToFile(dest, vo.getContent());
        }
        return vo;
    }

    @RequestMapping("content/get")
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

    @RequestMapping("file/delete")
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

    private void listFiles(File parentFile, Map<String, ResourceVo> resMap, Long orgID) {
        if (!parentFile.exists() || !parentFile.isDirectory() || null == parentFile.listFiles()) {
            return;
        }
        String prefix = FilenameUtils.concat(DOWNLOAD_HOME, Long.toString(orgID));
        for (File file : parentFile.listFiles()) {
            ResourceVo resourceVo = ResourceVo.toResourceModel(file, orgID);
            resourceVo.setPath(trimStart(resourceVo.getPath(), prefix));
            resMap.put(file.getName(), resourceVo);
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



}
