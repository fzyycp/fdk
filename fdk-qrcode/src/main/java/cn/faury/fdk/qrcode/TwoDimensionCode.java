package cn.faury.fdk.qrcode;

import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


/**
 * 二维码工具类
 */
public class TwoDimensionCode {

    // 二维码内容
    String encoderContent;

    // 二维码图片路径
    String imgPath;

    String tdName;

    public String getTdName() {
        return tdName;
    }

    public void setTdName(String tdName) {
        this.tdName = tdName;
    }

    /**
     * 取得encoderContent
     * 
     * @return encoderContent
     */
    public String getEncoderContent() {
        return encoderContent;
    }

    /**
     * 设置encoderContent
     * 
     * @param encoderContent
     */
    public void setEncoderContent(String encoderContent) {
        this.encoderContent = encoderContent;
    }

    // =========================================二维码生成码=============================================//
    /**
     * 生成二维码(QRCode)图片-----------------
     * 
     * @param content
     *            存储内容
     * @param imgPath
     *            图片路径
     * @param imgType
     *            图片类型
     * @param size
     *            二维码尺寸
     */
    public void imageTwoCode(String name, String content, String imgPath, String imgType, int size) {
        try {
            BufferedImage bufImg = this.encoderTwoCode(name, content, imgType, size);

            File imgFile = new File(imgPath);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(QRCode)图片的---------
     * 
     * @param content
     *            存储内容
     * @param imgType
     *            图片类型
     * @param size
     *            二维码尺寸
     * @return
     */
    public BufferedImage encoderTwoCode(String name, String content, String imgType, int size) {
        BufferedImage bufImg = null;
        try {
            Qrcode qrcodeHandler = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect('L');
            // Numeric 数字\Alphanumeric 英文字母\Binary 二进制\Kanji 汉字
            qrcodeHandler.setQrcodeEncodeMode('B');
            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
            // qrcodeHandler.setQrcodeVersion(5); // 139*139
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");
            // 图片尺寸
            // int imgWidth = 115;
            // int imgHeight = 135;
            int imgWidth = 103;
            int imgHeight = 105;
            /*
             * if (StringKit.notBlank(name)) { imgHeight = 125; }
             */
            if (name.length() % 7 == 0) {
				imgHeight = imgHeight + 14 * (name.length() / 7);
            } else {
				imgHeight = imgHeight + 14 * (name.length() / 7 + 1);
            }

            // int imgWidth = 300;
            // int imgHeight = 300;
            bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            // 设置背景颜色
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgWidth, imgHeight);

            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            int mulriple = 3;
            if (contentBytes.length > 0 && contentBytes.length < 100) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * mulriple + pixoff, i * mulriple + pixoff, mulriple, mulriple);
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
            }

            for (int i = 0; i < name.length(); i += 7) {
                String temp = name.substring(i, i + 7 > name.length() ? name.length() : i + 7) + "\r\n";
                Font font = new Font("Serif", Font.BOLD, 10);
                FontRenderContext context = gs.getFontRenderContext();
                Rectangle2D bounds = font.getStringBounds(temp, context);
                double x = ((imgWidth - bounds.getWidth()) / 2) - 5;
                gs.drawString(temp, (int) x, (int) (127 - bounds.getHeight() + 2 * i));// 渲染字符串
            }
            /*
             * String
             * path=getRequest().getSession().getServletContext().getRealPath
             * ("/image/logo/logo.png"); BufferedImage img = ImageIO.read(new
             * File(path));//实例化一个Image对象。 gs.drawImage(img,
             * (int)(imgWidth-img.getWidth())/2,
             * (int)(imgHeight-img.getHeight()-bounds.getHeight())/2,
             * null);//渲染图片 x:为中间位置 y：为中间位置
             */

            gs.dispose();
            bufImg.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImg;
    }

    /**
     * 
     * encodeTwoCode:生成二维码. <br/>
     * 
     * @author baotx
     * @param name
     *            标题
     * @param content
     *            编码内容
     * @param imgType
     *            图片类型
     * @param size
     *            尺寸
     * @return 图片流
     * @since JDK 1.7
     */
    public BufferedImage encodeTwoCode(String name, String content, String imgType, int size) {
        BufferedImage bufImg = null;
        try {
            Qrcode qrcodeHandler = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect('L');
            // Numeric 数字\Alphanumeric 英文字母\Binary 二进制\Kanji 汉字
            qrcodeHandler.setQrcodeEncodeMode('B');
            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
            qrcodeHandler.setQrcodeVersion(size); // 139*139
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes("utf-8");
            // 图片尺寸
            int imgSize = 67 + 12 * (size - 1);
            int imgWidth = imgSize + 2;
            int imgHeight = imgSize + 20;
            if (name.length() % 7 == 0) {
                imgHeight = imgHeight + 10 * (name.length() / 7);
            } else {
                imgHeight = imgHeight + 10 * (name.length() / 7 + 1);
            }

            // int imgWidth = 300;
            // int imgHeight = 300;
            bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            // 设置背景颜色
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgWidth, imgHeight);

            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            int mulriple = 3;
            if (contentBytes.length > 0 && contentBytes.length < 100) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * mulriple + pixoff, i * mulriple + pixoff, mulriple, mulriple);
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
            }

            for (int i = 0; i < name.length(); i += 7) {
                String temp = name.substring(i, i + 7 > name.length() ? name.length() : i + 7) + "\r\n";
                Font font = new Font("Serif", Font.BOLD, 10);
                FontRenderContext context = gs.getFontRenderContext();
                Rectangle2D bounds = font.getStringBounds(temp, context);
                double x = ((imgWidth - bounds.getWidth()) / 2) - 5;
                gs.drawString(temp, (int) x, (int) (imgHeight - bounds.getHeight() + 2 * i));// 渲染字符串
            }
            /*
             * String
             * path=getRequest().getSession().getServletContext().getRealPath
             * ("/image/logo/logo.png"); BufferedImage img = ImageIO.read(new
             * File(path));//实例化一个Image对象。 gs.drawImage(img,
             * (int)(imgWidth-img.getWidth())/2,
             * (int)(imgHeight-img.getHeight()-bounds.getHeight())/2,
             * null);//渲染图片 x:为中间位置 y：为中间位置
             */

            gs.dispose();
            bufImg.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImg;
    }

    /**
     * 解析二维码（QRCode）
     * 
     * @param imgPath
     *            图片路径
     * @return
     */
    public String decoderQRCode(String imgPath) {
        // QRCode 二维码图片的文件
        File imageFile = new File(imgPath);
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(imageFile);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return content;
    }

    /**
     * 解析二维码（QRCode）
     * 
     * @param input
     *            输入流
     * @return
     */
    public String decoderQRCode(InputStream input) {
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(input);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new TwoDimensionCodeImage(bufImg)), "utf-8");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return content;
    }
}
