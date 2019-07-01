package cn.onlov.evaluate.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class QrCodeUtil {

    private static String localHostIp;
    private static QrCodeUtil instance;
    private QrCodeUtil() {
    }
    public static QrCodeUtil getInstance() {
        if (instance == null) {
            instance = new QrCodeUtil();
        }
        return instance;
    }

    static {
        localHostIp = LocalHostUtils.getInstance().getLocalHostIp();
    }


    /**
     * 从图片生成文件的方法
     * @param urlMethod
     * @param hostPort
     * @param strings
     * @param wordsString
     * @return
     */
    public  File createQRCode(String urlMethod,String hostPort , Map<String, String> strings, LinkedHashMap<String, String> wordsString,int fontSize) {
        String url = localHostIp+":"+hostPort+"/"+urlMethod; // 生成url地址
        BufferedImage bimage = getBufferedImage( url, strings, wordsString,fontSize);

        File outfile = null;//创建临时文件;
        try {
            outfile = File.createTempFile("qrCode", ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outPath = outfile.getAbsolutePath();
        FileOutputStream out = null; // 先用一个特定的输出文件名
        try {
            out = new FileOutputStream(outPath);
            String formatName = outPath.substring(outPath.lastIndexOf(".") + 1);
            ImageIO.write(bimage, formatName, new File(outPath));
            System.out.println(outPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return outfile;
    }

    /**
     * 创建带文字的二维码
     * @param url
     * @param strings
     * @param wordsString
     * @return
     */
    private static BufferedImage getBufferedImage(String url, Map<String, String> strings, LinkedHashMap<String, String> wordsString,int fontSize) {
        Color markContentColor = Color.white;
        String qrcodePath = createCode(url, strings);

        ImageIcon imgIcon = new ImageIcon(qrcodePath);
        Image theImg = imgIcon.getImage();
        int width = 400;
        int height = 400;
        BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bimage.createGraphics();

        g.setColor(markContentColor);
        g.setFont(new Font(null, Font.BOLD, fontSize)); // 字体、字型、字号
        g.setBackground(new Color(255,0,0));
        g.drawImage(theImg, 0, 0, null);
        if (!wordsString.isEmpty()) {
            int stepSize = 80;
            for (String key : wordsString.keySet()) {
                g.drawString(key + "：" + wordsString.get(key), 50, height - stepSize); // 画文字
                stepSize = stepSize - 20;
            }
        }
        g.dispose();
        return bimage;
    }

    /**
     * 创建方法不带文字的二维码
     *
     * @return
     */

    public static String createCode(String url, Map<String, String> strings) {
        int width = 400, height = 300;
        String format = "png";
        String content = "";
        if (!strings.isEmpty()) {
            content = url+"?";
            for (String key : strings.keySet()) {
                content = content + key + "=" + strings.get(key)+"&";
            }
        }

        //定义二维码的参数
        HashMap map = new HashMap();
        //设置编码
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //设置纠错等级
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        map.put(EncodeHintType.MARGIN, 2);
        try {
            //生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height);
            final File file = File.createTempFile("qrCode", ".png");//创建临时文件
            Path file11 = file.toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file11);
            return file.getAbsolutePath();
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}