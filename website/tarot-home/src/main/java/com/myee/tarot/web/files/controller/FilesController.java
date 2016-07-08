package com.myee.tarot.web.files.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.files.FileDTO;
import com.myee.tarot.web.files.JSTreeDTO;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class FilesController {

//    private static final File DOWNLOAD_HOME = new File(Constants.DOWNLOAD_HOME );

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

    @RequestMapping(value = "/admin/files/list")
    @ResponseBody
    public List<JSTreeDTO> processListFiles(HttpServletRequest request,HttpServletResponse response) {
        List<JSTreeDTO> tree = Lists.newArrayList();
        String id = request.getParameter("id");
        File dir = null;
        if(id.equals("#")){
            dir = new File(Constants.DOWNLOAD_HOME + File.separator + ((MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE)).getId() );
            //resp.put("text",dir.getPath());
            //resp.put("id",dir.getPath());
        }else{
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
            if(dto.isLeaf()){
                continue;
            }
            JSTreeDTO jt = new JSTreeDTO();
            jt.setId(dto.getId());
            jt.setChildren(!dto.isLeaf());
            jt.setText(dto.getName());
            jt.setType(dto.isLeaf() ? "file" : "default");
            jt.setLastModify(new Date(dto.getMtime()));
            jt.setDetailType(dto.getType());
            tree.add(jt);
        }
        return tree;
    }


    @RequestMapping(value = "/admin/files/showList")
    @ResponseBody
    public AjaxResponse listFiles(HttpServletRequest request,HttpServletResponse response) {
        AjaxResponse resp = new AjaxResponse();
        List<JSTreeDTO> tree = Lists.newArrayList();
        String id = request.getParameter("id");
        File dir = null;
        if(id.equals("#")){
            dir = new File(Constants.DOWNLOAD_HOME + File.separator + ((MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE)).getId() );
            //resp.put("text",dir.getPath());
            //resp.put("id",dir.getPath());
        }
        else{
            dir = new File(id);
        }
//        if (null != req.getNodeid()) {
//            dir = FileUtils.getFile(req.getNodeid());
//        }
        Map<String, FileDTO> resMap = Maps.newHashMap();
        listFiles(dir, resMap,request);
        List<FileDTO> dtos = Lists.newArrayList(resMap.values());
        Collections.sort(dtos);
        for (FileDTO dto : dtos) {
            if(!dto.isLeaf()){
                continue;
            }
            JSTreeDTO jt = new JSTreeDTO();
            jt.setId(dto.getId());
            jt.setChildren(!dto.isLeaf());
            jt.setText(dto.getName());
            jt.setType(dto.isLeaf() ? "file" : "default");
            jt.setLastModify(new Date(dto.getMtime()));
            tree.add(jt);
        }
        resp.addEntry("tree",tree);
        return resp;
    }

    /**
     * 新建文件夹，重命名、删除文件和文件夹
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/admin/files/change")
    @ResponseBody
    public  JSTreeDTO changeFile(HttpServletRequest request,HttpServletResponse response) {
        AjaxResponse resp = new AjaxResponse();
        String operation = request.getParameter("operation");
        String id = request.getParameter("id");
        String text = request.getParameter("text");
        String type = request.getParameter("type");
        if(operation.equals("create_node")){
            File file = new File(id,text);
            try {
                boolean isNew = false;
                if(type.equals("default")){
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
                if(isNew){
                    return new JSTreeDTO(file.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(operation.equals("delete_node")){
            try {
                File file = new File(id);
                boolean isDelete = delete(file);
                if(isDelete){
                    JSTreeDTO tree = new JSTreeDTO();
                    tree.setStatus("OK");
                    return tree;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(operation.equals("rename_node")){
            try {
                boolean isRename = false;
                File file =  new File(id);
                String parent = file.getParent();
                File newFile = new File(parent,text);
                if(file.exists()){
                    if(file.exists()){
                        isRename = file.renameTo(newFile);
                    }
                    if(isRename){
                        return new JSTreeDTO(newFile.getPath());
                    }
                }else{
                    return new JSTreeDTO(newFile.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 上传文件
     * @param file
     * @param path
     * @param request
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @RequestMapping("/admin/files/create")
    @ResponseBody
    public AjaxResponse createResource(@RequestParam("resFile") CommonsMultipartFile file, String path, HttpServletRequest request) throws IllegalStateException, IOException {
        AjaxResponse resp = new AjaxResponse();
        try {
            String operation = request.getParameter("operation");
            String id = request.getParameter("id");
            String text = request.getParameter("text");
            String type = request.getParameter("type");
            if(type == null){
                resp = AjaxResponse.failed(-1);
                resp.setErrorString("参数错误");
                return resp;
            }

            MerchantStore merchantStore = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            File dest = FileUtils.getFile(Constants.DOWNLOAD_HOME, String.valueOf(merchantStore.getId()),File.separator + path);


            if (type.equals("default")) {
                dest.mkdirs();
            } else if (!file.isEmpty() && type.equals("file")) {
                System.out.println(file.getFileItem().getName());
                dest.mkdirs();
                dest = FileUtils.getFile(dest.getPath(), File.separator + file.getFileItem().getName());
                file.transferTo(dest);
            }
            //20160708文本编辑放到另一个接口，以后再做
//        else if (!StringUtil.isNullOrEmpty(vo.getContent(), true)) {
//            FileUtils.writeStringToFile(dest, vo.getContent());
//        }
            resp = AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(-1);
            resp.setErrorString("出错");
        }
        return resp;
    }

    private void listFiles(File parentFile, Map<String, FileDTO> resMap,HttpServletRequest request) {
        if (!parentFile.exists() || !parentFile.isDirectory() || null == parentFile.listFiles()) {
            return;
        }
        for (File file : parentFile.listFiles()) {
            FileDTO resourceVo = new FileDTO(file, new File(Constants.DOWNLOAD_HOME + File.separator + ((MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE)).getId() ));
            resMap.put(file.getName(), resourceVo);
        }
    }

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
}
