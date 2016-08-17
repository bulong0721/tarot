package com.myee.tarot.web.files.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.files.FileDTO;
import com.myee.tarot.web.files.HotfixSetVo;
import com.myee.tarot.web.files.HotfixVo;
import com.myee.tarot.web.files.TreeFileItem;
import com.myee.tarot.web.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class FilesController {

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;
    @Value("${cleverm.push.http}")
    private String DOWNLOAD_HTTP;
    private static final String GZIP_DIR   = "temp/";

    private static final String RESOURCE_TYPE_DIR   = "default";
    private static final String RESOURCE_TYPE_FILE  = "file";

    private static final String RESOURCE_PARAM_ID           = "id";
    private static final String RESOURCE_PARAM_OPERATION    = "operation";
    private static final String RESOURCE_PARAM_TEXT         = "text";
    private static final String RESOURCE_PARAM_TYPE         = "type";

    private static final String RESOURCE_OPERATION_CREATE    = "create_node";
    private static final String RESOURCE_OPERATION_DELETE    = "delete_node";
    private static final String RESOURCE_OPERATION_RENAME    = "rename_node";

   /* @RequestMapping(value = "/admin/files/list.html")
    public
    @ResponseBody
    JQGridResponse processListFiles(HttpServletRequest http) {
        JQGridResponse resp = new JQGridResponse();
        File dir = DOWNLOAD_HOME;
//        if (null != req.getNodeid()) {
//            dir = FileUtils.getFile(req.getNodeid());
//        }
        Map<String, FileDTO> resMap = Maps.newHashMap();
        listFiles(dir, resMap);
        List<FileDTO> dtos = Lists.newArrayList(resMap.values());
        Collections.sort(dtos);
        resp.getRows().addAll(dtos);
        return resp;
    }*/

    @RequestMapping(value = "admin/files/list")
    @ResponseBody
    public List<TreeFileItem> processListFiles(HttpServletRequest request, HttpServletResponse response) {
        List<TreeFileItem> tree = Lists.newArrayList();
        String id = request.getParameter(RESOURCE_PARAM_ID);
        File dir = null;
        if (id.equals("#")) {
            dir = new File(DOWNLOAD_HOME + File.separator + ((MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE)).getId());
            //resp.put("text",dir.getPath());
            //resp.put("id",dir.getPath());
        } else {
            dir = new File(id);
        }
//        if (null != req.getNodeid()) {
//            dir = FileUtils.getFile(req.getNodeid());
//        }
        Map<String, FileDTO> resMap = Maps.newHashMap();
        listFiles(dir, resMap, request);
        List<FileDTO> dtos = Lists.newArrayList(resMap.values());
        Collections.sort(dtos);
        for (FileDTO dto : dtos) {
            if (dto.isLeaf()) {
                continue;
            }
            TreeFileItem jt = new TreeFileItem();
            jt.setId(dto.getId());
            jt.setChildren(!dto.isLeaf());
            jt.setText(dto.getName());
            jt.setType(dto.isLeaf() ? RESOURCE_TYPE_FILE : RESOURCE_TYPE_DIR);
            jt.setLastModify(new Date(dto.getMtime()));
            jt.setDetailType(dto.getType());
            jt.setDownloadPath(getDownloadPath(dto.getId(),dto.isLeaf()));
            tree.add(jt);
        }
        return tree;
    }

    //根据FileDTO换算对应的下载文件的URL
    private String getDownloadPath(String filePath,Boolean isLeaf){
        if(isLeaf) {//是文件，才有下载链接
            String tempFilePath = filePath.replaceAll("\\\\", "/");//把路径中的反斜杠替换成斜杠
            String tempDownloadPath = DOWNLOAD_HOME.replaceAll("\\\\","/")+"/";//准备用于替换成url的下载文件夹路径
            String path = tempFilePath.replaceAll(tempDownloadPath,DOWNLOAD_HTTP);
            return path;
        }
        return "";
    }

    @RequestMapping(value = "admin/files/showList")
    @ResponseBody
    public AjaxResponse listFiles(HttpServletRequest request, HttpServletResponse response) {
        AjaxResponse resp = new AjaxResponse();
        List<TreeFileItem> tree = Lists.newArrayList();
        String id = request.getParameter(RESOURCE_PARAM_ID);
        if(id == null ){
            return AjaxResponse.failed(-1);
        }

        File dir = null;
        if (id.equals("#")) {
            dir = new File(DOWNLOAD_HOME + File.separator + ((MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE)).getId());
            //resp.put("text",dir.getPath());
            //resp.put("id",dir.getPath());
        } else {
            dir = new File(id);
        }
//        if (null != req.getNodeid()) {
//            dir = FileUtils.getFile(req.getNodeid());
//        }
        Map<String, FileDTO> resMap = Maps.newHashMap();
        listFiles(dir, resMap, request);
        List<FileDTO> dtos = Lists.newArrayList(resMap.values());
        Collections.sort(dtos);
        for (FileDTO dto : dtos) {
            if (!dto.isLeaf()) {
                continue;
            }
            TreeFileItem jt = new TreeFileItem();
            jt.setId(dto.getId());
            jt.setChildren(!dto.isLeaf());
            jt.setText(dto.getName());
            jt.setType(dto.isLeaf() ? RESOURCE_TYPE_FILE : RESOURCE_TYPE_DIR);
            jt.setLastModify(new Date(dto.getMtime()));
            jt.setDetailType(dto.getType());
            jt.setDownloadPath(getDownloadPath(dto.getId(),dto.isLeaf()));
            tree.add(jt);
        }
        resp.addEntry("tree", tree);
        return resp;
    }

    /**
     * 新建文件夹，重命名、删除文件和文件夹
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "admin/files/change")
    @ResponseBody
    public TreeFileItem changeFile(HttpServletRequest request, HttpServletResponse response) {
        AjaxResponse resp = new AjaxResponse();
        String operation = request.getParameter(RESOURCE_PARAM_OPERATION);
        String id = request.getParameter(RESOURCE_PARAM_ID);
        String text = request.getParameter(RESOURCE_PARAM_TEXT);
        String type = request.getParameter(RESOURCE_PARAM_TYPE);
        if (operation.equals(RESOURCE_OPERATION_CREATE)) {
            File file = new File(id, text);
            try {
                boolean isNew = false;
                if (type.equals(RESOURCE_TYPE_DIR)) {
                    //创建文件夹并不实际创建
                    //isNew = file.mkdirs();
                    isNew = true;
                }
                //20160708创建文件移到另一个接口
//                else if(type.equals("file")){
//                    if(!file.getParentFile().exists()){
//                        file.getParentFile().mkdirs();
//                    }
//                    if(!file.exists()){
//                        isNew = file.createNewFile();
//                    }
//                }
                if (isNew) {
                    return new TreeFileItem(file.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (operation.equals(RESOURCE_OPERATION_DELETE)) {
            try {
                File file = new File(id);
                //目前不允许删除文件夹
                if(file.isDirectory()){
                    return null;
                }

                boolean isCopy = copyToRecycle(file);//复制文件到回收站
                if(isCopy){ //复制成功后执行删除
                    boolean isDelete = delete(file);

                    if (isDelete) {
                        TreeFileItem tree = new TreeFileItem();
                        tree.setStatus("OK");
                        return tree;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (operation.equals(RESOURCE_OPERATION_RENAME)) {
            try {
                boolean isRename = false;
                File file = new File(id);
                String parent = file.getParent();
                File newFile = new File(parent, text);
                if (file.exists()) {
                    if (file.exists()) {
                        isRename = file.renameTo(newFile);
                    }
                    if (isRename) {
                        return new TreeFileItem(newFile.getPath());
                    }
                } else {
                    return new TreeFileItem(newFile.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param file
     * @param path
     * @param request
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @RequestMapping(value = {"services/public/files/upload","admin/files/create"})
    @ResponseBody
    public AjaxResponse createResource(@RequestParam("file") CommonsMultipartFile file, String path, @RequestParam(value = "storeId",required = false)Long storeId,HttpServletRequest request) throws IllegalStateException, IOException {
        AjaxResponse resp = AjaxResponse.success();
        try {
            String type = request.getParameter("type");
            if (type == null) {
                resp = AjaxResponse.failed(-1);
                resp.setErrorString("参数错误");
                return resp;
            }
            String storeIdStr;
            if(storeId == null ) {
                MerchantStore merchantStore  = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
                storeIdStr = String.valueOf(merchantStore.getId());
            }
            else {
                storeIdStr = String.valueOf(storeId);
            }
            if(StringUtil.isNullOrEmpty(storeIdStr) || StringUtil.equals("null", StringUtil.toLowerCase(storeIdStr))) {
                return AjaxResponse.failed(-1,"店铺ID不能为空");
            }
            File dest  = FileUtils.getFile(DOWNLOAD_HOME, storeIdStr, File.separator + path);

            if (type.equals(RESOURCE_TYPE_DIR)) {
                dest.mkdirs();
            } else if (!file.isEmpty() && type.equals(RESOURCE_TYPE_FILE)) {
//                System.out.println(file.getFileItem().getName());
                dest.mkdirs();
                String fileName = file.getFileItem().getName();
                dest = FileUtils.getFile(dest.getPath(), File.separator + fileName);
                file.transferTo(dest);
                TreeFileItem jt = new TreeFileItem();
                jt.setId(dest.getPath());
                jt.setChildren(false);
                jt.setText(fileName);
                jt.setType("file");
                jt.setLastModify(new Date(dest.lastModified()));
                jt.setDetailType(FilenameUtils.getExtension(fileName));
                jt.setDownloadPath(DOWNLOAD_HTTP + storeIdStr + "/" + path + "/" + file.getFileItem().getName());
                resp.addEntry("tree", jt);
            }
            //20160708文本编辑放到另一个接口，以后再做
//        else if (!StringUtil.isNullOrEmpty(vo.getContent(), true)) {
//            FileUtils.writeStringToFile(dest, vo.getContent());
//        }
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(-1);
            resp.setErrorString("出错");
        }
        return resp;
    }

    /**
     * 用zip压缩文件，源文件的文件名不能包含空格
     * @param pushRes
     * @param compress
     * @param request
     * @return
     */
    @RequestMapping("admin/files/packResource")
    @ResponseBody
    public HotfixSetVo packResource(String pushRes, boolean compress,HttpServletRequest request) {
        HotfixSetVo hotfixSetVo = new HotfixSetVo();
        List<TreeFileItem> resList = JSON.parseArray(pushRes, TreeFileItem.class);
        Set<HotfixVo> hotfixSet = Sets.newHashSet();
        hotfixSetVo.setPublisher(currentUser(request).getName());
        if (compress) {
            String basePath = DOWNLOAD_HOME+ "/"+ GZIP_DIR;
            String fileName = "";
            /*File gzFile = new File(basePath);
            gzFile.mkdirs(); //建立压缩文件根目录*/
            File zipFile = new File(basePath);
            zipFile.mkdirs(); //建立压缩文件根目录
            if(null != resList){
                for (TreeFileItem treeFileItem : resList) {
                    //用gz压缩文件:gz里面的文件名也会变成md5的名字，不太友好
                    /*fileName = CryptoUtil.md5(treeFileItem.getText()) + ".gz";
                    gzFile = FileUtils.getFile(basePath, fileName);
                    File source = FileUtils.getFile(treeFileItem.getId());
                    if(!gzFile.exists()){//如果压缩文件不存在，则执行压缩
                        GZipUtil.doCompressFile(source,gzFile);
                    }*/
                    //用zip压缩文件，源文件的文件名不能包含空格
//                    fileName = CryptoUtil.md5(treeFileItem.getText()) + ".zip";
//                    zipFile = FileUtils.getFile(basePath, fileName);
//                    if(!zipFile.exists()){//如果压缩文件不存在，则执行压缩
//                        ZipUtil.compress(treeFileItem.getId(),treeFileItem.getText(), zipFile.getPath());
//                    }
//                    hotfixSet.add(new HotfixVo(fileName, getGzipUrl(zipFile), null, true));//将压缩文件信息写入接口返回信息中
                }
            }
        } else {
            if (resList != null && resList.size() > 0) {
                for (TreeFileItem treeFileItem : resList) {
                    if (RESOURCE_TYPE_FILE == treeFileItem.getType()) {
                        String targetDir = treeFileItem.getId();
                        hotfixSet.add(new HotfixVo(treeFileItem.getText(), getDownloadPath(treeFileItem.getId(),true), targetDir, false));
                    }
                }
            }
        }
        hotfixSetVo.setHotfixSet(hotfixSet);
        return hotfixSetVo;
    }

    public AdminUser currentUser(HttpServletRequest request){
        return (AdminUser) request.getSession().getAttribute(Constants.ADMIN_USER);
    }

    String getGzipUrl(File zipFile) {
        return DOWNLOAD_HTTP + GZIP_DIR + zipFile.getName();
    }

    private void listFiles(File parentFile, Map<String, FileDTO> resMap, HttpServletRequest request) {
        if (!parentFile.exists() || !parentFile.isDirectory() || null == parentFile.listFiles()) {
            return;
        }
        for (File file : parentFile.listFiles()) {
            FileDTO resourceVo = new FileDTO(file, new File(DOWNLOAD_HOME + File.separator + ((MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE)).getId()));
            resMap.put(file.getName(), resourceVo);
        }
    }

    /**
     * 循环删除文件夹及文件
     * @param file
     * @return
     */
    public static boolean delete(File file) {
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        delete(files[i]);
                    }
                    file.delete();

                }
                return true;
            } else {
                System.out.println("所删除的文件不存在！" + '\n');
                return false;
            }
        } catch (Exception e) {
            System.out.print("unable to delete the folder!");
        }
        return false;
    }

    /**
     * 复制文件至回收站
     * @param file
     * @return
     */
    public boolean copyToRecycle(File file) {
        try {
            if (file.exists()) {
                String tempFilePath = file.getPath().replaceAll("\\\\", "/");//把路径中的反斜杠替换成斜杠
                String tempDownloadPath = DOWNLOAD_HOME.replaceAll("\\\\","/")+"/";//准备用于替换成url的下载文件夹路径
                String tempTargetPath = (DOWNLOAD_HOME + File.separator + "deleted" + File.separator).replaceAll("\\\\","/");
                String targetPath = tempFilePath.replaceAll(tempDownloadPath, tempTargetPath);
//                targetPath = targetPath.replaceAll("/","\\\\");//把路径转回linux兼容
                if (file.isFile()) {
                    // Destination directory
                    File dir = new File(targetPath);
                    File parentPath = new File(dir.getParent());
                    parentPath.mkdirs();
                    // Move file to new directory
                    boolean success = file.renameTo(dir);
                } else if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        copyToRecycle(files[i]);
                    }
                }
                return true;
            } else {
                System.out.println("所删除的文件不存在！" + '\n');
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("unable to delete the folder!");
        }
        return false;
    }

}
