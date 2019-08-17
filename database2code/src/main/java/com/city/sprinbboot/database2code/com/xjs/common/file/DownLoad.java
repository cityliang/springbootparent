package com.city.sprinbboot.database2code.com.xjs.common.file;

import java.io.*;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 文件下载类
 */
public class DownLoad {

    public static ResponseEntity<byte[]> downLoadFile(String filePath, HttpServletRequest request) {
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        return downLoadFile(fileName, filePath, request);
    }

    /**
     * 文件下载公共方法
     *
     * @param filePath 文件路径
     * @param request
     * @return
     */
    public static ResponseEntity<byte[]> downLoadFile(String fileName, String filePath, HttpServletRequest request) {
        File file = new File(filePath);
		/*String[] paths = filePath.split("\\/");
		if(paths.length <= 1){
			paths = filePath.split("\\\\");
		}
		String fileName = paths[paths.length-1];*/
        if (!file.exists()) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        HttpStatus httpState = HttpStatus.NOT_FOUND;
        ResponseEntity<byte[]> entity = null;
        try {
            headers.add("Content-Type", "application/vnd.ms-excel");
            headers.add("Content-Disposition", "attachment;filename="
                    + getFileName(fileName, request));
            httpState = HttpStatus.OK;
            entity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                    headers, httpState);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private static String getFileName(String fileName, HttpServletRequest request) throws UnsupportedEncodingException {
        String Agent = request.getHeader("User-Agent");
        if (null != Agent) {
            Agent = Agent.toLowerCase();
            if (Agent.indexOf("firefox") != -1) {
                fileName = new String(fileName.getBytes(), "iso8859-1");
            } else if (Agent.indexOf("msie") != -1) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
        }
        return fileName;
    }

}
