package com.city.sprinbboot.database2code.modules.codegen.mapper;

import com.city.sprinbboot.database2code.core.common.mapper.SuperMapper;
import com.city.sprinbboot.database2code.modules.codegen.model.TableEntity;

import java.util.List;
import java.util.Map;

public interface SysGeneratorMapper extends SuperMapper<TableEntity> {
    List<Map<String, Object>> queryList(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);
}