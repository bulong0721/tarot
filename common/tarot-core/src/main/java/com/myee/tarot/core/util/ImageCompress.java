package com.myee.tarot.core.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Info: 图片压缩
 * User: Gary.zhang@clever-m.com
 * Date: 2016-03-08
 * Time: 10:26
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class ImageCompress {
    private Image image;
    private int width;
    private int height;

    /**
     * 构造函数
     */
    public ImageCompress(String fileName) throws IOException {
        File file = new File(fileName);// 读入文件
        image = ImageIO.read(file);      // 构造Image对象
        width = image.getWidth(null);    // 得到源图宽
        height = image.getHeight(null);  // 得到源图长
    }

    /**
     * 按照宽度还是高度进行压缩
     *
     * @param w int 最大宽度
     * @param h int 最大高度
     */
    public void resizeFix(int w, int h, String destFileName) throws IOException {
        if (width / height > w / h) {
            resizeByWidth(w,destFileName);
        } else {
            resizeByHeight(h,destFileName);
        }
    }

    /**
     * 以宽度为基准，等比例放缩图片
     *
     * @param w int 新宽度
     */
    public void resizeByWidth(int w,String destFileName) throws IOException {
        int h = height * w / width;
        resize(w, h, destFileName);
    }

    /**
     * 以高度为基准，等比例缩放图片
     *
     * @param h int 新高度
     */
    public void resizeByHeight(int h, String destFileName) throws IOException {
        int w = width * h / height;
        resize(w, h, destFileName);
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param w int 新宽度
     * @param h int 新高度
     */
    public void resize(int w, int h, String destFileName) throws IOException {
        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(image, 0, 0, w, h, null); // 绘制缩小后的图
        File destFile = new File(destFileName);
//        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
        // 可以正常实现bmp、png、gif转jpg
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        encoder.encode(bufferedImage); // JPEG编码
//        out.close();

        String formatName = destFileName.substring(destFileName.lastIndexOf(".") + 1);
        ImageIO.write(bufferedImage, /*"GIF"*/ formatName /* format desired */, new File(destFileName) /* target */ );

    }
}
