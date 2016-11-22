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
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chay on 2016/9/6.
 */
public class FileValidCreateUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileValidCreateUtil.class);

    /**
     * 保存base64图片
     * @param imgBase64
     * @param path 保存路径及文件名
     */
    public static Boolean createBase64Img(
            String imgBase64,
            String path){
        // 对字节数组字符串进行Base64解码并生成图片
        if (imgBase64 == null) { // 图像数据为空
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            //拆分头和数据，java解码不能有头，而html显示必须有头
            String[] base64String = imgBase64.split(",");
            if(base64String.length != 2){
                return false;
            }
            byte[] b = decoder.decodeBuffer(base64String[1]);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            File dest = FileUtils.getFile(path);
            if(!dest.exists()){
                FileUtils.getFile(dest.getParent()).mkdirs();
                dest.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();

//            ByteArrayInputStream bais = new ByteArrayInputStream(b);
//            BufferedImage bi1 =ImageIO.read(bais);
//            ImageIO.write(bi1, "png", dest);//不管输出什么格式图片，此处不需改动

            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     *
     * @param inputFile
     * @return
     * @throws IOException
     */
    public static String fileMD5(String inputFile) throws IOException {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        FileInputStream fileInputStream = null;
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest =MessageDigest.getInstance("MD5");
            // 使用DigestInputStream
            fileInputStream = new FileInputStream(inputFile);
            digestInputStream = new DigestInputStream(fileInputStream,messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer =new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0);
            // 获取最终的MessageDigest
            messageDigest= digestInputStream.getMessageDigest();
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            String md5 = byteArrayToHex(resultByteArray);
            LOGGER.info("文件"+ inputFile +"的md5:" + md5);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage());
            return null;
        } finally {
            try {
                if(digestInputStream != null)
                    digestInputStream.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            try {
                if(fileInputStream != null)
                    fileInputStream.close();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    //下面这个函数用于将字节数组换成成16进制的字符串
    public static String byteArrayToHex(byte[] byteArray) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < byteArray.length; n++) {
            stmp = (Integer.toHexString(byteArray[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < byteArray.length - 1) {
                hs = hs + "";
            }
        }
        // return hs.toUpperCase();
        return hs;

        // 首先初始化一个字符数组，用来存放每个16进制字符
      /*char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
      // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
      char[] resultCharArray =new char[byteArray.length * 2];
      // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
      int index = 0;
      for (byte b : byteArray) {
         resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
         resultCharArray[index++] = hexDigits[b& 0xf];
      }
      // 字符数组组合成字符串返回
      return new String(resultCharArray);*/

    }

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
