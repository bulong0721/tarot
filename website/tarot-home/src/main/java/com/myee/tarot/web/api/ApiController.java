package com.myee.tarot.web.api;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ray.Fu on 2016/7/14.
 */
@Controller
public class ApiController {

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    /**
     * 机器自检及器件状态上传接口（非结构化数据，每天）
     * @param file
     * @return
     */
    @RequestMapping(value = "api/files/mSelfCheckStatUpload", method = RequestMethod.POST)
    @ResponseBody
    public Integer machineSelfCheckAndStatusUpload(@RequestParam("file") CommonsMultipartFile file, String path, Long storeId) {
        Integer result = 0;
        try {
            File dest = FileUtils.getFile(DOWNLOAD_HOME, String.valueOf(storeId), File.separator + path);
            dest.mkdirs();
            String fileName = file.getFileItem().getName();
            dest = FileUtils.getFile(dest.getPath(), File.separator + fileName);
            file.transferTo(dest);
            result = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
