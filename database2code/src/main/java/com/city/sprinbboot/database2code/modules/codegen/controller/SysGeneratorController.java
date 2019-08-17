package com.city.sprinbboot.database2code.modules.codegen.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.city.sprinbboot.database2code.com.xjs.common.page.PageResponse;
import com.city.sprinbboot.database2code.modules.codegen.service.ISysGeneratorService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代码生成管理
 *
 * @author xiejs
 */
@Controller
@RequestMapping({"/cdpfMis/sysGenerator"})
public class SysGeneratorController {

    @Autowired
    private ISysGeneratorService sysGeneratorService;

    @RequestMapping(value = {"/index"}, method = {RequestMethod.GET})
    public String index() {
        return "/cdpfMis/sysGenerator/index.jsp";
    }

    @RequestMapping(value = {"/list"}, method = {RequestMethod.GET})
    @ResponseBody
    public Object list(@ModelAttribute PageResponse page) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != page.getParameter()) {
            map.put("tableName", page.getParameter());
        }
        List<Map<String, Object>> list = sysGeneratorService.queryList(map);
//		Map<String, Object> result = new HashMap<>();
//        result.put("rows", list);
//        result.put("total", 10);
        return list;
    }

    @RequestMapping(value = "/code/{tableNames}", method = RequestMethod.GET)
    public void code(@PathVariable("tableNames") String tableNames, HttpServletRequest request, HttpServletResponse response) {


        byte[] data = sysGeneratorService.generatorCode(tableNames, "0");

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"zhuo_code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        try {
            IOUtils.write(data, response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/code1/{tableNames}", method = RequestMethod.GET)
    public void code1(@PathVariable("tableNames") String tableNames, HttpServletRequest request, HttpServletResponse response) {


        byte[] data = sysGeneratorService.generatorCode(tableNames, "1");

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"zhuo_code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        try {
            IOUtils.write(data, response.getOutputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}