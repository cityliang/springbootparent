package com.city.sprinbboot.database2code.modules.codegen.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.city.sprinbboot.database2code.modules.codegen.mapper.SysGeneratorMapper;
import com.city.sprinbboot.database2code.modules.codegen.model.TableEntity;
import com.city.sprinbboot.database2code.modules.codegen.service.ISysGeneratorService;
import com.city.sprinbboot.database2code.modules.codegen.utils.GenUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SysGeneratorServiceImpl extends
        ServiceImpl<SysGeneratorMapper, TableEntity> implements
        ISysGeneratorService {

    @Autowired
    private SysGeneratorMapper sysGeneratorMapper;

    @Override
    public List<Map<String, Object>> queryList(Map<String, Object> map) {
        return sysGeneratorMapper.queryList(map);
    }

    public Map<String, String> queryTable(String tableName) {
        return sysGeneratorMapper.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return sysGeneratorMapper.queryColumns(tableName);
    }

    @Override
    public byte[] generatorCode(String tableNames, String type) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        List<String> tableNameList = Arrays.asList(tableNames.split("-"));
        for (String tableName : tableNameList) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            // 生成代码
            GenUtils.generatorCode(table, columns, zip, type);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}