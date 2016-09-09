package com.myee.tarot.web.apiold.util;

import com.myee.tarot.apiold.domain.Material;
import com.myee.tarot.apiold.domain.MaterialFileKind;
import com.myee.tarot.apiold.service.MaterialFileKindService;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ImageCompress;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.TypeConverter;
import com.myee.tarot.web.files.FileType;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by chay on 2016/9/6.
 */
public class FileValidCreateUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileValidCreateUtil.class);

    /**
     * 小超人验证并上传文件接口
     *
     * @param file
     * @param downloadHome  文件存储的基础路径,比如var/opt/push,或e:/download_home
     * @param storeId
     * @return 我们以material类封装返回结果，在具体业务逻辑上可以转换到Picture或Video对象
     */
    public static Material validCreateFile(
            CommonsMultipartFile file,
            String downloadHome,
            Long storeId,
            MaterialFileKindService materialFileKindService) {
        Material material = new Material();

        //获得文件扩展名
        String fileKindString = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).trim().toLowerCase();
        if(StringUtil.isBlank(fileKindString)){
            return null;
        }

        String type = FileType.getFileType(file);
        //小超人存储路径后面的生成规则都是一样的，只有最前面的文件夹根据文件类型不同是不一样的
        Long tempMillis = System.currentTimeMillis();
        Long shortDate = TypeConverter.toLong(DateTimeUtils.getShortDate());
        String pathCommon = shortDate + "/" + tempMillis + fileKindString;

        int fileKind = validateFileKind(fileKindString,file);
        if(fileKind == Constants.FILE_VALID_TYPE_APK || fileKind == Constants.FILE_VALID_TYPE_BIN || fileKind == Constants.FILE_VALID_TYPE_TEXT){
            pathCommon = Constants.UPLOAD_MATERIAL_PATH + pathCommon;
        }
        else if(fileKind == Constants.FILE_VALID_TYPE_AUDIO){
            pathCommon = Constants.UPLOAD_AUDIO_PATH + pathCommon;
        }
        else if(fileKind == Constants.FILE_VALID_TYPE_IMAGE){
            pathCommon = Constants.UPLOAD_IMAGE_PATH + pathCommon;
        }
        else if(fileKind == Constants.FILE_VALID_TYPE_VIDEO){
            pathCommon = Constants.UPLOAD_VIDEO_PATH + pathCommon;
        }
        else {
            pathCommon = Constants.UPLOAD_DEFAULT_PATH + pathCommon;
        }

        File dest = FileUtils.getFile(downloadHome, Long.toString(storeId), pathCommon);
        material.setMaterialPath(storeId + "/" + pathCommon);
        material.setOriginal(file.getOriginalFilename());
        MaterialFileKind materialFileKind = materialFileKindService.findById(Long.parseLong(fileKind+""));
        material.setMaterialFileKind(materialFileKind);
        try {
            if (!file.isEmpty()) {
                dest.mkdirs();
                file.transferTo(dest);
                material.setMaterialSize(dest.length());
                //图片文件还要生成缩略图并保存
                if(fileKind == Constants.FILE_VALID_TYPE_IMAGE){
                    ImageCompress imgCom  = new ImageCompress(dest.getAbsolutePath());
                    String pathPre = Constants.UPLOAD_IMAGE_PATH
                            + shortDate+"/"+"240_240";
                    File pathPreview = FileUtils.getFile(downloadHome, Long.toString(storeId), pathPre);
                    pathPreview.mkdirs();
                    imgCom.resizeFix(240, 240, pathPreview + "/" + tempMillis + fileKindString);
                    material.setPreviewPath(storeId + "/" + pathPre);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.toString());
            return null;
        }
        return material;
    }

    //验证文件扩展名类型符合规则,以后添加其他文件后可以增加,类型与数据库一一对应
    private static int validateFileKind(String fileKindString,CommonsMultipartFile file) {
        int tempKind = Constants.FILE_VALID_TYPE_DEFAULT;
        String type = FileType.getFileType(file);

        if(fileKindString.equals(".apk")) {//apk不知道头文件是什么，只验证扩展名
            tempKind = Constants.FILE_VALID_TYPE_APK;
        }
        else if(fileKindString.equals(".txt")){//txt不知道头文件是什么，只验证扩展名
            tempKind = Constants.FILE_VALID_TYPE_TEXT;
        }
        else if(fileKindString.equals(".mp3")||fileKindString.equals(".wmv")||fileKindString.equals(".wav")){
            if(type.matches(Constants.OFF_ALLOW_AUDIO)) {
                tempKind = Constants.FILE_VALID_TYPE_AUDIO;
            }
        }
        else if(fileKindString.equals(".rmvb")||fileKindString.equals(".mp4")||fileKindString.equals(".avi")){
            if(type.matches(Constants.OFF_ALLOW_VIDEO)){
                tempKind = Constants.FILE_VALID_TYPE_VIDEO;
            }
        }
        else if(fileKindString.equals(".jpg")||fileKindString.equals(".png")||fileKindString.equals(".bmp")){
            if(type.matches(Constants.OFF_ALLOW_IMAGE)){
                tempKind = Constants.FILE_VALID_TYPE_IMAGE;
            }
        }
        else if(fileKindString.equals(".bin")||fileKindString.equals(".img")){
            tempKind = Constants.FILE_VALID_TYPE_BIN;
        }
        return tempKind;
    }
}
