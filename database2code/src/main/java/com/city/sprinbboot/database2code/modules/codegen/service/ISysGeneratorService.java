package com.city.sprinbboot.database2code.modules.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.city.sprinbboot.database2code.modules.codegen.model.TableEntity;

import java.util.List;
import java.util.Map;


public interface ISysGeneratorService extends IService<TableEntity> {

    List<Map<String, Object>> queryList(Map<String, Object> map);

    byte[] generatorCode(String tableNames, String type);
}