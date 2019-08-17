package com.city.sprinbboot.database2code.com.xjs.common.file;

import java.io.*;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


/**
 * 文件上传类
 */
public class UpLoad {

    /**
     * 文件上传公共方法
     * filePath 上传文件保存路劲
     * fileName 文件保存名(为空时与选择的文件同名)
     *
     * @param request
     * @return
     */
    public static String upLoadFile(String filePath, String fileName, HttpServletRequest request) {
        File fi = new File(filePath);
        //检查文件夹是否存在
        if (!fi.exists()) {
            fi.mkdirs();
        }
        String filesName = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {//判断request是否有文件上传
            MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) request;
            Iterator<String> iterator = mhsr.getFileNames();
            while (iterator.hasNext()) {
                MultipartFile multipartFile = mhsr.getFile(iterator.next());
                if (multipartFile != null) {
                    File file = null;
                    if (fileName != null && !"".equals(fileName)) {
                        if ("blob".equals(multipartFile.getOriginalFilename())) {
                            file = new File(filePath + "/" + fileName + ".jpg");
                            filesName = fileName + ".jpg";
                        } else {
                            String[] strs = multipartFile.getOriginalFilename().split("\\.");
                            file = new File(filePath + "/" + fileName + "." + strs[1]);
                            filesName = fileName + "." + strs[1];
                        }
                    } else {
                        file = new File(filePath + "/" + multipartFile.getOriginalFilename());
                        filesName = multipartFile.getOriginalFilename();
                    }
                    try {
                        multipartFile.transferTo(file);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return filesName;
    }

}
