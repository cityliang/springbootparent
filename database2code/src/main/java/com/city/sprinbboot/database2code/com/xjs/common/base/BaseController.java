package com.city.sprinbboot.database2code.com.xjs.common.base;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.city.sprinbboot.database2code.com.xjs.common.entity.Org;
import com.city.sprinbboot.database2code.com.xjs.common.entity.User;
import com.city.sprinbboot.database2code.com.xjs.common.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSON;

/**
 * 控制器基类
 */
public abstract class BaseController {

    private final static Logger _log = LoggerFactory
            .getLogger(BaseController.class);

    public Org org;

    public String cmsIdTemp;

    @ModelAttribute
    public void populateModel(String cmsId, ModelMap modelMap) {
        if (StringUtils.isNotBlank(cmsId)) {
            cmsIdTemp = cmsId;
        } else {
            cmsIdTemp = "";
        }
        modelMap.put("cmsId", cmsId);
    }

    /*
     * @ModelAttribute public void populateModel(@ModelAttribute Org org) {
     * //所属辖区 组织编码 记录，供其它页面跳转使用 if (null != org) { this.org = org; String
     * orgCode = org.getOrgCode1(); if (null != orgCode) { Subject subject =
     * SecurityUtils.getSubject(); User user = new User(); try {
     * BeanUtils.copyProperties(user,subject.getPrincipal()); } catch
     * (IllegalAccessException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); } catch (InvocationTargetException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } Map<String,String> map
     * = new HashMap<String,String>(); map.put("orgCode1", orgCode);
     * map.put("orgNames1", org.getOrgNames1()); map.put("orgRank1",
     * org.getOrgRank1()); map.put("orgId1", org.getOrgId1()); if
     * (RedisUtil.isExists(user.getId())) { if (!RedisUtil.getMap(user.getId(),
     * "orgCode1").equals(orgCode)) { RedisUtil.remove(user.getId());
     * RedisUtil.set(user.getId(), map); } }else { RedisUtil.set(user.getId(),
     * map); } } }else { org = new Org(); } }
     */
    public String getRealPath(HttpServletRequest request, String filePath) {
        return new StringBuilder()
                .append(request.getSession().getServletContext()
                        .getRealPath("")).append(File.separator)
                .append(filePath).toString();
    }

    public String getRealPathBasic(HttpServletRequest request, String filePath) {

        String path = request.getSession().getServletContext().getRealPath("/") + File.separator + "resources";
        String last = path.charAt(path.length() - 1) + "";
        String frist = filePath.charAt(0) + "";
        if (last.equals(File.separator)) {
            if ((frist.equals("\\")) || (frist.equals("/")))
                path = path + filePath.substring(1);
            else {
                path = path + filePath;
            }
        } else if ((frist.equals("\\")) || (frist.equals("/")))
            path = path + filePath;
        else {
            path = path + File.separator + filePath;
        }

        return path;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public User getSessionUser() {
        User upmsUser = new User();
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        if (null == username) {
            if (null != cmsIdTemp && !"".equals(cmsIdTemp)) {
                if (RedisUtil.isExists("cms_" + cmsIdTemp)) {
                    String userId = RedisUtil.get("cms_" + cmsIdTemp);
                    upmsUser = JSON.parseObject(userId, User.class);
                }

            }
            return upmsUser;
        }

        upmsUser = (User) RedisUtil.getObject(username);
        return upmsUser;
    }

    /**
     * 统一异常处理
     *
     * @param request
     * @param response
     * @param exception
     */
    @ExceptionHandler
    public String exceptionHandler(HttpServletRequest request,
                                   HttpServletResponse response, Exception exception) {
        _log.error("统一异常处理：", exception);
        request.setAttribute("ex", exception);
        // 重复提交，重新复制
        String tempTokenKey = (String) request.getAttribute("tempTokenKey");
        String tempTokenValue = (String) request.getAttribute("tempTokenValue");
        RedisUtil.set(tempTokenKey, tempTokenValue, 600);
        if (null != request.getHeader("X-Requested-With")
                && request.getHeader("X-Requested-With").equalsIgnoreCase(
                "XMLHttpRequest")) {
            request.setAttribute("requestHeader", "ajax");
            // response.getWriter().write(tokenString);
        }
        // shiro没有权限异常
        if (exception instanceof UnauthorizedException) {
            return "/403.jsp";
        }
        // shiro会话已过期异常
        if (exception instanceof InvalidSessionException) {
            return "/error.jsp";
        }
        return "/error.jsp";
    }

    /**
     * 返回jsp视图
     *
     * @param path
     * @return
     */
    public static String jsp(String path) {
        return path.concat(".jsp");
    }

    /**
     * 返回thymeleaf视图
     *
     * @param path
     * @return
     */
    public static String thymeleaf(String path) {
        //return "".concat("1/mooc/").concat(path);
        return path;
    }

    public Org getOrg() {
        return org;
    }

    public void setOrg(Org org) {
        this.org = org;
    }

    /**
     * 下载方法使用
     *
     * @param fileName 文件名称 比如:xxx.pdf
     * @param body
     * @param request
     * @return
     */
    public ResponseEntity<byte[]> getResponseEntity(String fileName,
                                                    byte[] body, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus httpState = HttpStatus.NOT_FOUND;
        try {

            headers.add("Content-Type", "application/vnd.ms-excel");
            headers.add("Content-Disposition", "attachment;filename="
                    + getFileName(fileName, request));
            httpState = HttpStatus.OK;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body,
                    headers, httpState);
            return entity;
        }
    }

    public String getFileName(String fileName, HttpServletRequest request)
            throws UnsupportedEncodingException {
        // String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
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

    public String getCmsIdTemp() {
        return cmsIdTemp;
    }

    public void setCmsIdTemp(String cmsIdTemp) {
        this.cmsIdTemp = cmsIdTemp;
    }


}
