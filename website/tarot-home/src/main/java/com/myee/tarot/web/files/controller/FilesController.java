package com.myee.tarot.web.files.controller;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.myee.tarot.core.web.JQGridRequest;
import com.myee.tarot.core.web.JQGridResponse;
import com.myee.tarot.web.files.FileDTO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class FilesController {

    private static final File DOWNLOAD_HOME = new File("F:/ESD-USB");

    @RequestMapping(value = "/admin/files/list.html")
    public
    @ResponseBody
    JQGridResponse processListFiles(@ModelAttribute JQGridRequest req, HttpServletRequest http) {
        JQGridResponse resp = new JQGridResponse();
        File dir = DOWNLOAD_HOME;
        if (null != req.getNodeid()) {
            dir = FileUtils.getFile(req.getNodeid());
        }
        Map<String, FileDTO> resMap = Maps.newHashMap();
        listFiles(dir, resMap);
        List<FileDTO> dtos = Lists.newArrayList(resMap.values());
        Collections.sort(dtos);
        resp.getRows().addAll(dtos);
        return resp;
    }

    private void listFiles(File parentFile, Map<String, FileDTO> resMap) {
        if (!parentFile.exists() || !parentFile.isDirectory() || null == parentFile.listFiles()) {
            return;
        }
        for (File file : parentFile.listFiles()) {
            FileDTO resourceVo = new FileDTO(file, DOWNLOAD_HOME);
            resMap.put(file.getName(), resourceVo);
        }
    }
}
