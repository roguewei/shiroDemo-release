package com.winston.controller;

import com.winston.properties.SecurityProperties;
import com.winston.utils.imageCode.ImageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @ClassName ValidateCodeController
 * @Description 测试图形验证码
 * @Author Winston
 * @Date 2019/4/15 21:13
 * @Version 1.0
 **/
@RestController
public class ValidateCodeController {

    // session的key
    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    // 操作session的工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 1、根据随机数生成图片
        ImageCode imageCode = createImageCode(new ServletWebRequest(request));
        // 2、将随机数保存到session
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
        // 3、将生成的图片写到接口的响应中
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());

    }

    private ImageCode createImageCode(ServletWebRequest request) {
        // ServletRequestUtils.getIntParameter是从请求里面获取配置参数，如果没有传递参数则使用配置文件中的默认配置
        int width = ServletRequestUtils.getIntParameter(request.getRequest(),
                "width", securityProperties.getCode().getImage().getWidth());
        int height = ServletRequestUtils.getIntParameter(request.getRequest(),
                "height", securityProperties.getCode().getImage().getHeight());;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0,0, width, height);
        g.setFont(new Font("Times new Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for(int i=0; i<155; i++){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x+xl, y+yl);
        }

        String sRand = "";
        for(int i=0; i< securityProperties.getCode().getImage().getLength(); i++){
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20,
                    random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i+6, 16);
        }
        g.dispose();
        return new ImageCode(image, sRand, securityProperties.getCode().getImage().getExpireIn());
    }


     /*
     * @Author Winston
     * @Description //生成随机背景条纹
     * @Date 21:47 2019/4/15
     * @Param
     * @return
     **/
     private Color getRandColor(int fc, int bc){
         Random random = new Random();
         if(fc > 255){
             fc = 255;
         }
         if(bc >255){
             bc = 255;
         }
         int r = fc + random.nextInt(bc - fc);
         int g = fc + random.nextInt(bc - fc);
         int b = fc + random.nextInt(bc - fc);
         return new Color(r, g, b);
     }

}
