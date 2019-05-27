package com.gerry.yitao.seckill.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/27 15:36
 * @Description:
 */
public class ImageUtil {
    /**
     * 转换BufferedImage 数据为byte数组
     *
     * @param bImage
     * Image对象
     * @param format
     * image格式字符串.如"gif","png"
     * @return byte数组
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 转换byte数组为BufferedImage
     *
     * @param bytes
     * @return Image
     */
    public static BufferedImage bytesToImage(byte[] bytes) {
        BufferedImage bufferedImage = null;
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        try {
            bufferedImage = ImageIO.read(arrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bufferedImage;
    }
}
