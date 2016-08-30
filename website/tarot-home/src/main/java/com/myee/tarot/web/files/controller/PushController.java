package com.myee.tarot.web.files.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.myee.djinn.dto.PushResourceDTO;
import com.myee.djinn.dto.ResponseData;
import com.myee.djinn.endpoint.OrchidService;
import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.resource.domain.PushResource;
import com.myee.tarot.resource.service.PushResourceService;
import com.myee.tarot.web.files.FileType;
import com.myee.tarot.web.files.vo.FileItem;
import com.myee.tarot.web.util.StringUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class PushController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushController.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;
    @Value("${cleverm.push.http}")
    private String DOWNLOAD_HTTP;

    @Autowired
    private ServerBootstrap serverBootstrap;

    @Autowired
    private PushResourceService pushResourceService;

    @RequestMapping(value = "admin/file/search", method = RequestMethod.POST)
    @ResponseBody
    public AjaxPageableResponse searchResource(@RequestParam("node") String parentNode, HttpServletRequest request) {
        MerchantStore store = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        File template = getResFile(100L, parentNode);
        Map<String, FileItem> resMap = Maps.newLinkedHashMap();
        listFiles(template, resMap, 100L, store.getId());
        if (100L != store.getId()) {
            File dir = getResFile(store.getId(), parentNode);
            listFiles(dir, resMap, store.getId(), store.getId());
        }
        return new AjaxPageableResponse(Lists.<Object>newArrayList(resMap.values()));
    }

    @RequestMapping(value = "admin/file/create")
    @ResponseBody
    public AjaxPageableResponse createResource(@RequestParam(value = "file", required = false) CommonsMultipartFile file, @RequestParam("entityText") String entityText, HttpServletRequest request) {
        MerchantStore store = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        JSONObject jsonObject = JSON.parseObject(entityText);
        if ("/".equals(jsonObject.getString("salt"))) {
            jsonObject.remove("salt");
            jsonObject.put("salt", store.getId());
        }
        FileItem vo = JSON.parseObject(jsonObject.toJSONString(), FileItem.class);
        Long orgID = vo.getSalt();
        boolean editorModel = false;
        Map<String, FileItem> resMap = Maps.newLinkedHashMap();
        try {
            File dest = FileUtils.getFile(DOWNLOAD_HOME, Long.toString(orgID), vo.getPath()); //新增文件父路径
            String currPath = vo.getCurrPath() == null ? "" : vo.getCurrPath();
            if (dest.exists() && dest.isFile() && !StringUtil.isNullOrEmpty(vo.getContent(), true)) {  //如果文件已经存在，则为编辑模式,
                editorModel = true;
                //删除原文件，创建一个新的文件，将内容写入
                String name = vo.getName();
                String path = dest.getAbsolutePath().substring(0, dest.getAbsolutePath().lastIndexOf(File.separator));
                dest.delete();
                File desFile = new File(path + File.separator + name);
                desFile.createNewFile();
                FileUtils.writeStringToFile(desFile, vo.getContent());
                dest = new File(path);  //用于查找文件
            }
            if (!editorModel && !dest.exists())   //新增文件夹和文件需要检测文件夹是否存在
                dest.mkdirs();
            if (!editorModel && file != null && !file.isEmpty()) {  //文件上传
                String fileName = file.getFileItem().getName();

                File desDir = new File(dest + File.separator + currPath);
                if (!desDir.exists())
                    desDir.mkdirs();
                File desFile = new File(desDir + File.separator + fileName);
                desFile.createNewFile();
                file.transferTo(desFile);
            }
            if (!editorModel && !StringUtil.isNullOrEmpty(vo.getContent(), true)) { //界面输入文件内容
                String name = vo.getName();
                File desDir = new File(dest + File.separator + currPath);
                if (!desDir.exists())
                    desDir.mkdirs();

                File desFile = new File(desDir + File.separator + name);
                desFile.createNewFile();
                FileUtils.writeStringToFile(desFile, vo.getContent());
            }
            listFiles(dest, resMap, store.getId(), store.getId());
        } catch (IOException e) {
            LOGGER.error("create file error", e);
        }
        return new AjaxPageableResponse(Lists.<Object>newArrayList(resMap.values()));
    }

    @RequestMapping(value = "admin/file/delete", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public AjaxResponse deleteResource(@RequestParam("salt") Long orgID, @RequestParam("path") String path, HttpServletRequest request) {
        String message = "";
        //不允许删除别的店铺下的资源
        MerchantStore store = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        Map<String, FileItem> resMap = Maps.newLinkedHashMap();
        if (!store.getId().equals(orgID)) {
            message = "不允许删除别的店铺下的资源";
            return AjaxResponse.failed(-1, message);
        } else {
            File resFile = getResFile(orgID, path);
            if (resFile.isDirectory()) {
                try {
                    getFiles(resFile);
                } catch (StopMsgException e) {
                    message = "该目录下还有文件，请先删除文件";
                    return AjaxResponse.failed(-2, message);
                }
            }
            boolean isMove = moveToRecycle(resFile);//移动文件到回收站
            if (isMove) { //移动成功后执行删除
                if (resFile.exists()) {
                    resFile.delete();
                }
                if (path.lastIndexOf(File.separator) != -1) {
                    File parentPathFile = getResFile(orgID, path.substring(0, path.lastIndexOf(File.separator)));
                    listFiles(parentPathFile, resMap, store.getId(), store.getId());
                }
                return new AjaxPageableResponse(Lists.<Object>newArrayList(resMap.values()));
            } else {
                message = "所删除的文件不存在！";
                return AjaxResponse.failed(-3, message);
            }
        }
    }

    @RequestMapping("admin/content/get")
    @ResponseBody
    public AjaxResponse getContentText(@RequestParam("data") String data, HttpServletRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        Map<String, Object> map = new HashMap<String, Object>();
        boolean flag = true;
        FileItem fileItem = JSON.parseObject(data, FileItem.class);
        MerchantStore store = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        Long orgID = store.getId();
        if (orgID != fileItem.getSalt()) {
            return AjaxResponse.failed(-1, "文件不属于该店铺，不能修改");
        }
        String fileName = fileItem.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        File resFile = getResFile(orgID, fileItem.getPath());
        String type = FileType.getFileType(resFile.getAbsolutePath());
        if (!resFile.exists()) {
            return AjaxResponse.failed(-2, "文件不存在");
        }
        if ((null != type && !type.matches(Constants.ALLOW_EDITOR_TEXT)) || !suffix.matches(Constants.ALLOW_EDITOR_TEXT)) {
            return AjaxResponse.failed(-5, "不支持该格式文件在线编辑。");
        }
        if (resFile.length() > 4096L) {
            return AjaxResponse.failed(-3, "超过文本读取大小限制(4K)。");
        }
        String value = "";
        try {
            value = FileUtils.readFileToString(resFile, "utf-8");
        } catch (IOException ie) {
            LOGGER.error("读取文件错误", ie);
            return AjaxResponse.failed(-4, "读取文件错误");
        } finally {
            map.put("message", value);
            ajaxResponse.addDataEntry(map);
        }
        return ajaxResponse;
    }

    @RequestMapping(value = "admin/file/download", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse exportResource(@RequestParam("salt") Long orgID, @RequestParam("path") String path) {
        String url = DOWNLOAD_HTTP + orgID.toString() + File.separator + path;
        AjaxResponse ajaxResponse = new AjaxResponse().success();
        Map<String, Object> map = Maps.newHashMap();
        map.put("url", url);
        ajaxResponse.addDataEntry(map);
        return ajaxResponse;
    }

    private void listFiles(File parentFile, Map<String, FileItem> resMap, Long orgID, Long storeId) {
        if (!parentFile.exists() || !parentFile.isDirectory() || null == parentFile.listFiles()) {
            return;
        }
        String prefix = FilenameUtils.concat(DOWNLOAD_HOME, Long.toString(orgID));
        for (File file : parentFile.listFiles()) {
            FileItem fileItem = FileItem.toResourceModel(file, orgID, storeId);
            fileItem.setPath((trimStart(fileItem.getPath(), prefix)).replace(Constants.BACKSLASH, Constants.SLASH));
            fileItem.setUrl(DOWNLOAD_HTTP + orgID + Constants.SLASH + fileItem.getPath().replace(Constants.BACKSLASH, Constants.SLASH));
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
    public AjaxResponse pushResource(@Valid @RequestBody PushResourceDTO pushResourceDTO) {
        OrchidService eptService = null;
        if (pushResourceDTO == null
                || StringUtil.isNullOrEmpty(pushResourceDTO.getUniqueNo())
                || pushResourceDTO.getAppId() == null
                || pushResourceDTO.getAppId() == 0) {
            return AjaxResponse.failed(-1, "数据有误");
        }
        try {
            eptService = serverBootstrap.getClient(OrchidService.class, pushResourceDTO.getUniqueNo());
        } catch (Exception e) {
            return AjaxResponse.failed(-2, "连接客户端错误");
        }
        if (eptService == null) {
            return AjaxResponse.failed(-3, "获取接口出错");
        }
        if (pushResourceDTO.getContent() == null || pushResourceDTO.getContent().size() <= 0) {
            return AjaxResponse.failed(-4, "推送内容为空或格式错误");
        }
        Boolean isSuccess = null;
        PushResource pushResource = new PushResource();
        try {
            //入库
            isSuccess = eptService.pushResource(pushResourceDTO);
            pushResource.setAppId(pushResourceDTO.getAppId());
            pushResource.setContent(JSONArray.toJSONString(pushResourceDTO.getContent()));
            pushResource.setStoragePath(pushResourceDTO.getStoragePath());
            pushResource.setTimeout(pushResourceDTO.getTimeout());
            pushResource.setUniqueNo(pushResourceDTO.getUniqueNo());
            if (isSuccess != null && isSuccess) {
                pushResource.setSuccess(true);
                pushResourceService.update(pushResource);
                return AjaxResponse.success("推送成功");
            } else {
                pushResource.setSuccess(false);
                pushResourceService.update(pushResource);
                return AjaxResponse.failed(-6, "发送失败，客户端出错");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.failed(-5, "客户端不存在或网络无法连接");
        }
        return null;
    }

    final static Charset charset = Charset.forName("UTF8");

    /**
     * 移动文件至回收站
     *
     * @param file
     * @return
     */
    public boolean moveToRecycle(File file) {
        try {
            if (file.exists()) {
                String tempFilePath = file.getPath().replaceAll("\\\\", "/");//把路径中的反斜杠替换成斜杠
                String tempDownloadPath = DOWNLOAD_HOME.replaceAll("\\\\", "/") + "/";//准备用于替换成url的下载文件夹路径
                String tempTargetPath = (DOWNLOAD_HOME + File.separator + "deleted" + File.separator).replaceAll("\\\\", "/");
                String targetPath = tempFilePath.replaceAll(tempDownloadPath, tempTargetPath);
                // Destination directory
                File dir = new File(targetPath);
                File parentPath = new File(dir.getParent());
                parentPath.mkdirs();
                // Move file to new directory
                boolean success = file.renameTo(dir);
                return true;
            } else {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    moveToRecycle(files[i]);
                }
                LOGGER.info("所删除的文件不存在！" + '\n');
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("unable to delete the folder!");
        }
        return false;
    }

    /*
     * 通过递归得到某一路径下所有的目录及其文件
    */
    static boolean getFiles(File root) {
        File[] files = root.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                /*
                 * 递归调用
                */
                getFiles(file);
            } else {
                // 跳出
                throw new StopMsgException();
            }
        }
        return false;
    }

    static class StopMsgException extends RuntimeException {
    }
}
