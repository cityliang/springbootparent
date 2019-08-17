package com.city.sprinbboot.database2code.modules.codegen.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.city.sprinbboot.database2code.com.xjs.common.util.PropertiesFileUtil;
import com.city.sprinbboot.database2code.com.xjs.common.util.StringUtil;
import com.city.sprinbboot.database2code.modules.codegen.model.ColumnEntity;
import com.city.sprinbboot.database2code.modules.codegen.model.TableEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


/**
 * 代码生成器   工具类
 */
public class GenUtils {

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("generator/Model.java.vm");
        templates.add("generator/Mapper.java.vm");
        //templates.add("generator/Mapper.xml.vm");
        templates.add("generator/Service.java.vm");
        templates.add("generator/ServiceImpl.java.vm");

        templates.add("generator/Controller.java.vm");
        templates.add("generator/index.jsp.vm");
        templates.add("generator/create.jsp.vm");
        templates.add("generator/show.jsp.vm");
        return templates;
    }

    public static List<String> getTemplates1() {
        List<String> templates = new ArrayList<String>();
        templates.add("generator/Model.java.vm");
        templates.add("generator/Mapper.java.vm");
        //templates.add("generator/Mapper.xml.vm");
        templates.add("generator/Service.java.vm");
        templates.add("generator/ServiceImpl.java.vm");

        templates.add("generator/workFlow/applayIndex.jsp.vm");
        templates.add("generator/workFlow/approval.jsp.vm");
        templates.add("generator/workFlow/approvalIndex.jsp.vm");
        templates.add("generator/workFlow/Controller.java.vm");
        templates.add("generator/workFlow/create.jsp.vm");
        templates.add("generator/workFlow/index.jsp.vm");
        templates.add("generator/workFlow/show.jsp.vm");
        return templates;
    }

    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip, String type) {
        //配置信息
        PropertiesFileUtil config = PropertiesFileUtil.getInstance("generator");
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.get("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));
        String columnName;
        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            columnName = column.get("columnName");
            if ("orgId".equals(columnName) || "orgCode".equals(columnName) || "orgName".equals(columnName)
                    || "createById".equals(columnName) || "createByName".equals(columnName) || "createDate".equals(columnName)
                    || "updateById".equals(columnName) || "updateByName".equals(columnName) || "updateDate".equals(columnName)
                    || "remarks".equals(columnName) || "delFlag".equals(columnName)) {

                continue;
            }
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.get(columnEntity.getDataType());
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = config.get("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "org.zhuo.cdpfMis" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("package", config.get("package"));
        String moduleName = config.get("moduleName");
        if (null == moduleName.trim() || "".equals(moduleName.trim())) {
            moduleName = className.toLowerCase();
        }
        map.put("moduleName", moduleName);
        map.put("author", config.get("author"));
        map.put("email", config.get("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("temp", "()");
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = new ArrayList<>();
        if ("1".equals(type)) {
            templates = getTemplates1();
        } else {
            templates = getTemplates();
        }
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), config.get("package"), config.get("moduleName"))));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                System.out.println("渲染模板失败，表名：" + tableEntity.getTableName());
            }
        }
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }


    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        //String packagePath = "main" + File.separator + "java" + File.separator;
        String packagePath = "";
        if (null == moduleName.trim() || "".equals(moduleName.trim())) {
            moduleName = className.toLowerCase();
        }
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }

        if (template.contains("Model.java.vm")) {
            return packagePath + "model" + File.separator + className + ".java";
        }

        if (template.contains("Mapper.java.vm")) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + "I" + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }


//        if (template.contains("Mapper.xml.vm" )) {
//            return packagePath + "mapper" + File.separator + className  + "Mapper.xml";
//        }

//        if (template.contains("menu.sql.vm" )) {
//            return className.toLowerCase() + "_menu.sql";
//        }
        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("index.jsp.vm")) {
            return moduleName + File.separator + StringUtil.toLowerCaseFirstOne(className) + File.separator + "index.jsp";
        }

        if (template.contains("show.jsp.vm")) {
            return moduleName + File.separator + StringUtil.toLowerCaseFirstOne(className) + File.separator + "show.jsp";
        }

        if (template.contains("create.jsp.vm")) {
            return moduleName + File.separator + StringUtil.toLowerCaseFirstOne(className) + File.separator + "create.jsp";
        }


        if (template.contains("applayIndex.jsp.vm")) {
            return moduleName + File.separator + StringUtil.toLowerCaseFirstOne(className) + File.separator + "applayIndex.jsp";
        }

        if (template.contains("approval.jsp.vm")) {
            return moduleName + File.separator + StringUtil.toLowerCaseFirstOne(className) + File.separator + "approval.jsp";
        }

        if (template.contains("approvalIndex.jsp.vm")) {
            return moduleName + File.separator + StringUtil.toLowerCaseFirstOne(className) + File.separator + "approvalIndex.jsp";
        }

        return null;
    }
}
