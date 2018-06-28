package cn.faury.fdk.shiro.controller;

import cn.faury.fdk.captcha.FdkCaptcha;
import cn.faury.fdk.captcha.autoconfigure.FdkCaptchaProperties;
import cn.faury.fdk.shiro.core.FdkWebSessionManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * 验证码请求
 */
@Controller
@RequestMapping("/")
@Api(value = "验证码控制器", tags = {"验证码控制器接口"})
public class CaptchaController {

    @Autowired
    FdkCaptcha fdkCaptcha;

    @Autowired
    FdkCaptchaProperties fdkCaptchaProperties;

    /**
     * 显示验证码
     */
    @GetMapping(FdkCaptchaProperties.REQUEST_URL)
    @ApiOperation(value = "生成验证码", notes = "根据配置文件生成验证码图片并返回")
    public void captcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = fdkCaptcha.createText();
            httpServletRequest.getSession().setAttribute(fdkCaptchaProperties.getSessionKey(), createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = fdkCaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader(FdkWebSessionManager.AUTHORIZATION_HEADER, httpServletRequest.getSession().getId());
        httpServletResponse.addCookie(new Cookie(FdkWebSessionManager.AUTHORIZATION_HEADER, httpServletRequest.getSession().getId()));
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
