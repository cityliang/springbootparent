package com.city.sprinbboot.database2code.com.xjs.common.util;

import java.io.File;
import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

/**
 */
public class ZhuoResourcesUtil implements InitializingBean, ServletContextAware {

    private static Logger _log = LoggerFactory.getLogger(ZhuoResourcesUtil.class);

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        _log.info("===== 开始解压zhuo-resources =====");
        // String version = PropertiesFileUtil.getInstance("zhuo-admin-client.properties").get("zhuo.admin.version");
        // _log.info("zhuo-resources.jar 版本: {}", version);
        String jarPath = servletContext.getRealPath("/WEB-INF/lib/zhuo-resources-1.0.0.jar");
        _log.info("" + this.getClass().getClassLoader().getResource("/").getPath());
        _log.info("zhuo-resources.jar 包路径: {}", jarPath);
        String resources = servletContext.getRealPath("/") + "";
        _log.info("zhuo-resources.jar 解压到: {}", resources);
        JarUtil.decompress(jarPath, resources);
        _log.info("===== 解压platform-admin完成 =====");
    }

    public void traverseFolder2(String path) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        traverseFolder2(file2.getAbsolutePath());
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

}
