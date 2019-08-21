package com.winston.utils;


import com.winston.exception.ErrorException;
import com.winston.utils.result.CodeMsg;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
@Data
public class FileUtil {

    @Value("${file.path}")
    private String SAVE_DB_PATH;

    @Value("${file.pathHttp}")
    private String PATH_HTTP;

    private final static String IMAGE_TYPE_THUMBNAIL = "png";

    private File thumbnailsFile = null;

    private long videoTime = 15 * 1000L;

    public Map<String, String> excuteUpload(MultipartFile file) throws FileNotFoundException {

        Map<String, String> res = new HashMap<>();

        //此处保存位置分图片和视频，对应不同文件夹(.jpg)
        String substrname = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String filename = StringUtil.getRandomString(9) + substrname;

        File path = new File(SAVE_DB_PATH);



        if (!file.isEmpty()) {
            try {

                if (!path.exists()) {
                    path.mkdirs();
                }
                String uploadPath = SAVE_DB_PATH + filename;

                File excelFile = new File(uploadPath);
                file.transferTo(excelFile);
                // 获取视频时长
//                length = VideoUtil.ReadVideoTimeMs(excelFile);

                res.put("fileName", filename);
                res.put("filePath", uploadPath);
                return res;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 点击会提供对话框选择另存为：
     * response.setHeader( “Content-Disposition “, “attachment;filename= “+filename);
     * <p>
     * 通过IE浏览器直接选择相关应用程序插件打开：
     * response.setHeader( “Content-Disposition “, “inline;filename= “+fliename)
     * <p>
     * 下载前询问（是打开文件还是保存到计算机）
     * response.setHeader( “Content-Disposition “, “filename= “+filename);
     *
     * @param fileName
     * @param response
     * @return
     */
    public void downLoad(String fileName, HttpServletResponse response) {

        if (fileName != null) {
            //String realpath = request.getServletContext().getRealPath("/upload");
            String realpath = SAVE_DB_PATH;
            File file = new File(realpath + fileName);
            if (file.exists()) {
                response.setContentType("application/force-download"); //设置强制下载不打开
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);//设置文件名

                byte[] buffer = new byte[5120];

                try (FileInputStream fis = new FileInputStream(file);
                     BufferedInputStream bis = new BufferedInputStream(fis);
                     OutputStream os = response.getOutputStream()) {
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer, 0, 5120)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }

                } catch (FileNotFoundException e) {
                    log.info("文件未找到");
                } catch (IOException e) {
                    log.info("下载失败");
                }
            }
        }

    }

    /**
     * @return
     * @Author Winston
     * @Description 从网络URL中获取文件并保存到本地
     * @Date 16:57 2019/4/18
     * @Param
     **/
    public String getFileFromHttp(String httpurl, String fileType, String fileName) throws FileNotFoundException {

        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        String absoultePath = path.getAbsolutePath();
        //图片在项目中的地址(项目位置+图片名,带后缀名)
        String contextPath = absoultePath + SAVE_DB_PATH;

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(httpurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //防止屏蔽程序抓取而返回403错误
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10 * 1000);
            inputStream = connection.getInputStream();
            byte[] data = readInputStream(inputStream);

            // 文件保存位置
            File saveDir = new File(contextPath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName + "." + fileType);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            return saveDir + File.separator + fileName + "." + fileType;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return contextPath;
    }

    public byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public String getFileType(String fileUrl) {
        String fileType = "";
        if (fileUrl.toLowerCase().contains("png"))
            fileType = "png";
        if (fileUrl.toLowerCase().contains("jpg") || fileUrl.toLowerCase().contains("jpeg"))
            fileType = "jpeg";
        if (fileUrl.toLowerCase().contains("mp4"))
            fileType = "mp4";
        if (fileUrl.toLowerCase().contains("wmv"))
            fileType = "wmv";
        if (fileUrl.toLowerCase().contains("avi"))
            fileType = "avi";
        return fileType;
    }


    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String  文件夹路径  如  c:/fqf
     */
    public void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
            }
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath String  文件夹路径及名称  如c:/fqf
     * @return boolean
     */
    public boolean delFolder(String folderPath) {
        try {
            delAllFile(folderPath);  //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();  //删除空文件夹
            return true;
        } catch (Exception e) {
            log.info("删除文件夹失败");
        }
        return false;
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String  原文件路径  如：c:/fqf.txt
     * @param newPath String  复制后路径  如：f:/fqf.txt
     * @return boolean
     */
    public boolean copyFile(String fileName, String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int byteread = 0;
            File oldfile = new File(oldPath + fileName);
            if (oldfile.exists()) {  //文件存在时
                inStream = new FileInputStream(oldPath + fileName);  //读入原文件
                fs = new FileOutputStream(newPath + fileName);
                byte[] buffer = new byte[1024 * 5];

                while ((byteread = inStream.read(buffer)) != -1) {

                    fs.write(buffer, 0, byteread);
                }

            }
            return true;
        } catch (Exception e) {
            log.info("复制单个文件出错");
        }
        return false;
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String  原文件路径  如：c:/fqf
     * @param newPath String  复制后路径  如：f:/fqf/ff
     * @return boolean
     */
    public boolean copyFolder(String oldPath, String newPath) {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            (new File(newPath)).mkdirs();  //如果文件夹不存在  则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    input = new FileInputStream(temp);
                    output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();

                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }

            return true;
        } catch (Exception e) {
            log.info("复制文件夹所有文件失败");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    log.info("关闭输出流失败");
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.info("关闭输入流失败");
                }
            }

        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param path     String  文件路径及名称  如c:/fqf.txt
     * @param fileName 文件名
     * @return boolean
     */
    public boolean delFile(String path, String fileName) {
        try {
            String filePath = path + fileName;
            filePath = filePath.toString();
            File myDelFile = new File(filePath);
            boolean delete = myDelFile.delete();
            return delete;
        } catch (Exception e) {
            log.info("删除文件出错");
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param file 文件路径及名称  如c:/fqf.txt
     * @return boolean
     */
    public boolean delFile(File file) {
        try {
            boolean delete = file.delete();
            return delete;
        } catch (Exception e) {
            log.info("删除文件出错");
        }
        return false;
    }


    /**
     * 移动文件到指定目录 并将原来文件删除
     *
     * @param oldPath String  如：c:/fqf.txt
     * @param newPath String  如：d:/fqf.txt
     */
    public boolean moveFile(String fileName, String oldPath, String newPath) {
        return (copyFile(fileName, oldPath, newPath) && delFile(oldPath, fileName)) ? true : false;
    }


    /**
     * 移动文件夹到指定文件夹，并删除原来文件夹
     *
     * @param oldPath String  如：c:/fqf.txt
     * @param newPath String  如：d:/fqf.txt
     */
    public boolean moveFolder(String oldPath, String newPath) {
        return (copyFolder(oldPath, newPath) && delFolder(oldPath)) ? true : false;
    }

    public static boolean renameTo(String oldName, String newName, String path) {
        File oldFile = new File(path + oldName);
        if (!oldFile.exists()) {
            try {
                oldFile.createNewFile();
            } catch (IOException e) {
                log.info("新建文件失败");
            }
        }
        String rootPath = oldFile.getParent();
        File newFile = new File(rootPath + newName);

        if (oldFile.renameTo(newFile)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return a
     * @Author weigaosheng
     * @Description 图片压缩处理, 大于1024byte*1024byte就压缩
     * 上传图片不得超过10MB
     * @ate 17:22 208/12/30
     * @Param MultipartFile file 原始图片
     **/
    public File thumbnails(MultipartFile file) {

        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
//            String absoultePath = path.getAbsolutePath();
            System.out.println(file.getSize() + "-------------");
            if (file.getSize() > 1024 * 1024 * 10) {
                throw new ErrorException(CodeMsg.FILE_TOO_BIG);
            }
            String fileOriginName = file.getOriginalFilename();
            // 原文件后缀名
            String suffixName = fileOriginName.substring(fileOriginName.lastIndexOf('.') + 1);
            // 原文件名（不含后缀）
            String prefixName = fileOriginName.substring(0, fileOriginName.lastIndexOf('.'));
            //图片在项目中的地址(项目位置+图片名,带后缀名)
            File tempFile = new File(SAVE_DB_PATH);
            if (!tempFile.exists()) {
                // 生产临时图片文件
                tempFile.mkdirs();
            }
            String savePath = SAVE_DB_PATH + prefixName + "." + suffixName;
            // 临时写入项目路径
            file.transferTo(new File(savePath));

            /*
             * size(width,height) 若图片横比1024小，高比1024小，不变
             * 若图片横比1024小，高比1024大，高缩小到1024，图片比例不变 若图片横比1024大，高比1024小，横缩小到1024，图片比例不变
             * 若图片横比1024大，高比1024大，图片按比例缩小，横为1024或高为1024
             */
            if (file.getSize() >= 1024 * 1024) {
                Thumbnails.of(savePath).size(1024, 1024).outputQuality(1f).toFile(savePath);
            } else {
                Thumbnails.of(savePath).scale(1f).toFile(savePath);
            }
            //获取压缩后的图片
            thumbnailsFile = new File(savePath);
            return thumbnailsFile;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException(CodeMsg.FILE_YASUO_ERROR);
        }
    }

    /**
     * 截取视频第 limit 帧的图片
     *
     * @param filePath 视频路径
     * @return String 文件名
     * @throws FrameGrabber.Exception
     */
    public String getVideothumbnailURL(String filePath, int limit) {
        try {
            String picPath = null;
            FFmpegFrameGrabber ff = null;
            File outPut = null;
            String fileName = null;

            ff = new FFmpegFrameGrabber(filePath);
            ff.start();
            //获取视频总帧数
            int ffLength = ff.getLengthInFrames();
            Frame frame;
            int flag = 0;
            if (limit > ffLength) {
                return null;
            }
            while (flag < ffLength) {
                frame = ff.grabImage();
                /*
                对视频的第limit帧进行处理
                */
                if (frame != null && flag == limit) {
                    fileName = StringUtil.getRandomString(9) + flag + ".jpg";
                    picPath = SAVE_DB_PATH + fileName;
                    outPut = new File(picPath);
                    ImageIO.write(getExecuteFrame(frame), "jpg", outPut);
                }
                flag++;
            }
            ff.stop();
            return fileName;
        } catch (FrameGrabber.Exception e) {
            log.info("创建视频帧失败：{}", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BufferedImage getExecuteFrame(Frame frame) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

//
//    /**
//     * 生成图片的相对路径
//     *
//     * @return 图片的相对路径 例：pic/1.png
//     */
//    private String getPngPath() {
//        String contextPath = null;
//        try {
//            contextPath = ResourceUtils.getURL("classpath:").getPath() + SAVE_DB_PATH;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return contextPath + StringUtil.getRandomString(25) + ".png";
//    }
//
//    public File base64ToFile(String base64) {
//
//        try {
//            String randomString = StringUtil.getRandomString(16);
//            String fileName;
//            if (base64.startsWith("data:image/png;base64")) {
//                fileName = randomString + ".png";
//                base64 = base64.replaceAll("data:image/png;base64,", "");
//            } else {
//                fileName = randomString + ".mp4";
//                base64 = base64.replaceAll("data:video/mp4;base64,", "");
//            }
//
//
//            File dir = new File(SAVE_DB_PATH);
//            if (!dir.exists() && !dir.isDirectory()) {
//                dir.mkdirs();
//            }
//            byte[] bytes = Base64.getDecoder().decode(base64);
//            BufferedOutputStream bos = null;
//            FileOutputStream fos = null;
//            File file = new File(SAVE_DB_PATH + fileName);
//            fos = new FileOutputStream(file);
//            bos = new BufferedOutputStream(fos);
//            bos.write(bytes);
//
//            if (fileName.endsWith(".png")) {
//                if (file.length() >= 1024 * 1024) {
//                    Thumbnails.of(SAVE_DB_PATH + fileName).size(1024, 1024).outputQuality(1f).toFile(SAVE_DB_PATH + fileName);
//                } else {
//                    Thumbnails.of(SAVE_DB_PATH + fileName).scale(1f).toFile(SAVE_DB_PATH + fileName);
//                }
//                //获取压缩后的图片
//                thumbnailsFile = new File(SAVE_DB_PATH + fileName);
//                return thumbnailsFile;
//            } else if (fileName.endsWith(".mp4")) {
//                //读取时长
//                Long videoTimeMs = VideoUtil.ReadVideoTimeMs(file);
//                if (videoTimeMs > videoTime) {
//                    delFile(file);
//                    throw new ErrorException("请不要上传过大视频,允许上传最长时间为: "+videoTime/1000+" 秒的视频");
//                }
//                return file;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}
