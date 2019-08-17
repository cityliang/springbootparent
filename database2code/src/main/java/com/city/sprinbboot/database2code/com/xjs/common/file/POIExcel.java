package com.city.sprinbboot.database2code.com.xjs.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;


/**
 * 导入导出Excel公共类
 */
public class POIExcel {

    //导入的excel文件的两种后缀类型
    private static final String Excel_XLS = "xls";
    private static final String Excel_XLSX = "xlsx";
    //标题行
    private static final int TITLE_POSITION = 0;
    //所属辖区行
    private static final int SSXQ_POSITION = 3;
    //表头行
    private static final int HEAD_POSITION = 4;
    //文本开始行
    private static final int CONTENT_POSITION = 5;

    /**
     * 读取Excel文件数据
     *
     * @param filePath 文件路径
     * @return
     */
    public static List<HashMap<String, String>> importExcel(String filePath) {
        //存放读取的数据集
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        //存放读取的bean
        HashMap<String, String> bean = new HashMap<String, String>();
        try {
            //检查文件
            fileCheck(filePath);
            //获取Workbook对象
            Workbook workbook = getWorkbook(filePath);
            /**读文件bean页--开始**/
            Sheet sheetBean = workbook.getSheetAt(1);//bean页
            Row enRow = sheetBean.getRow(0);//第一行,即node
            Row zhRow = sheetBean.getRow(2);//第三行,即node中文名
            int lastBean = enRow.getLastCellNum();//最后一列
            for (int t = 0; t < lastBean; t++) {
                Cell enCell = enRow.getCell(t);
                Cell zhCell = zhRow.getCell(t);
                String enCellValue = getCellValue(enCell, true);//该列的node
                String zhCellValue = getCellValue(zhCell, true);//该列的node中文名
                bean.put(zhCellValue, enCellValue);
            }
            /**读文件bean页--结束**/
            /**读取数据页--开始**/
            Sheet sheetData = workbook.getSheetAt(0);//数据页
            Row titleRow = sheetData.getRow(TITLE_POSITION);//标题行
            if (titleRow != null) {
                int lastRowIndex = sheetData.getLastRowNum();//获取当前sheet页的最后一行
                //读取所属辖区
                String ssxq = null;//存放拼接好的所属辖区中文名称
                Row ssxqRow = sheetData.getRow(SSXQ_POSITION);//所属辖区行
                int lastNum = ssxqRow.getLastCellNum();
                for (int s = 1; s < lastNum; s++) {
                    Cell cell = ssxqRow.getCell(s);//当前单元格
                    String cellValue = getCellValue(cell, true);
                    if (StringUtils.isNotBlank(cellValue)) {
                        if (ssxq == null) {
                            ssxq = cellValue;
                        } else {
                            ssxq = ssxq + "." + cellValue;
                        }
                    }
                }
                //读取首行，即 表头
                Row headRow = sheetData.getRow(HEAD_POSITION);//表头行
                ArrayList<String> list_cn = new ArrayList<String>();//存放中文表头的集合
                int firstIndex = headRow.getFirstCellNum();//首列
                int lastIndex = headRow.getLastCellNum();//最后一列
                for (int i = firstIndex; i < lastIndex; i++) {
                    Cell cell = headRow.getCell(i);//当前单元格对象
                    String cellValue = getCellValue(cell, true);
                    cellValue = cellValue.replace("*", "");
                    list_cn.add(cellValue);
                }
                //list_cn.remove(list_cn.size()-1);
                for (String str : list_cn) {
                    String nodeName = bean.get(str);//获取文表头对应的字段名称
                    Collections.replaceAll(list_cn, str, nodeName);//将表头的中文名称替换成对应的字段名
                }
                //读取数据行
                if (StringUtils.isNotBlank(ssxq)) {//如果未填所属辖区，则视为空excel
                    for (int rowIndex = CONTENT_POSITION; rowIndex <= lastRowIndex; rowIndex++) {
                        HashMap<String, String> map = new HashMap<String, String>();//一行数据一个map
                        map.put("SSXQ", ssxq);
                        Row currentRow = sheetData.getRow(rowIndex);// 当前行
                        //int firstColumnIndex = currentRow.getFirstCellNum(); // 首列
                        int emptyNum = firstIndex;//空值的列数
                        //int lastColumnIndex = currentRow.getLastCellNum();// 最后一列
                        for (int columnIndex = firstIndex; columnIndex < lastIndex; columnIndex++) {
                            Cell currentCell = currentRow.getCell(columnIndex);// 当前单元格对象
                            String currentCellValue = getCellValue(currentCell, true);// 当前单元格的值
                            if (StringUtils.isBlank(currentCellValue)) {
                                emptyNum++;
                            }
                            if (columnIndex < list_cn.size()) {
                                String key = list_cn.get(columnIndex);
                                if (StringUtils.isNotBlank(key)) {
                                    map.put(key, currentCellValue);
                                }
                            }
                        }
                        if (emptyNum != lastIndex) {
                            list.add(map);
                        }
                    }
                }
            }
            /**读取数据页--结束**/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileFormatException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 文件检查
     *
     * @param filePath 文件路径
     */
    private static void fileCheck(String filePath) throws FileNotFoundException, FileFormatException {
        File file = new File(filePath);
        //检查文件是否存在
        if (!file.exists()) {
            throw new FileNotFoundException("导入的文件不存在:" + filePath);
        }
        if (!(filePath.endsWith(Excel_XLS) || filePath.endsWith(Excel_XLSX))) {
            throw new FileFormatException("导入的文件不是excel文件！");
        }
    }

    /**
     * 获取Workbook对象
     * xls:HSSFWorkbook
     * xlsx:XSSFWorkbook
     *
     * @param filePath 文件路径
     * @return
     */
    private static Workbook getWorkbook(String filePath) {
        Workbook workbook = null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (filePath.endsWith(Excel_XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (filePath.endsWith(Excel_XLSX)) {
                workbook = new XSSFWorkbook(is);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return workbook;
    }

    /**
     * 获取单元格数据
     *
     * @param cell       单元格对象
     * @param treatAsStr 为true时，当做文本来取值（不会把1取成1.0）
     * @return
     */
    private static String getCellValue(Cell cell, boolean treatAsStr) {
        if (cell == null) {
            return "";
        }

        if (treatAsStr) {
            // 临时把它当做文本来读取
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    /**
     * 导出Excel文件
     *
     * @param type     文件类型（xls或xlsx）
     * @param title    文件名（也是标题）
     * @param ssxq     所属辖区
     * @param headName 表头（表格列名）
     * @param headIds  字段名
     * @param dataList 需要导出的数据
     * @param filePath 导出文件的路劲（文件夹的路劲）
     * @return
     */
    public static boolean exportExcel(String filePath, String type, String title, String ssxq, String[] headName, String[] headIds,
                                      List<HashMap<String, String>> dataList) {
        FileOutputStream xls = null;
        boolean flag = false;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            if ("xls".equals(type)) {
                HSSFWorkbook wb = exportXLSExcel(title, ssxq, headName, headIds, dataList);
                xls = new FileOutputStream(filePath + "/" + title + ".xls");
                wb.write(xls);
                flag = true;
            } else if ("xlsx".equals(type)) {
                XSSFWorkbook wb = exportXLSXExcel(title, ssxq, headName, headIds, dataList);
                xls = new FileOutputStream(filePath + "/" + title + ".xlsx");
                wb.write(xls);
                flag = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                xls.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return flag;
        }
    }

    /**
     * 导出xls类型Excel
     *
     * @param title    文件名（也是标题）
     * @param headName 表头（表格列名）
     * @param dataList 需要导出的数据
     * @return
     */
    private static HSSFWorkbook exportXLSExcel(String title, String ssxq, String[] headName, String[] headIds,
                                               List<HashMap<String, String>> dataList) {
        // 创建excel工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //获得数据集大小
        int dataNumber = dataList.size();
        int sheetNumber = (int) Math.ceil((float) dataNumber / 50000);//分sheet页，没页sheet最多存放50000条数据
        for (int i = 0; i < sheetNumber; i++) {
            // 创建第一个sheet（页），并命名
            HSSFSheet sheet = wb.createSheet(title + String.valueOf(i + 1));
            // 设置默认宽度
            sheet.setDefaultColumnWidth(20);
            // 创建三种单元格格式
            HSSFCellStyle styleTitle = wb.createCellStyle();
            styleTitle = setXLSTitleStyle(wb, styleTitle);
            HSSFCellStyle styleHead = wb.createCellStyle();
            styleHead = setXLSHeadStyle(wb, styleHead);
            HSSFCellStyle styleBody = wb.createCellStyle();
            styleBody = setXLSBodyStyle(wb, styleBody);
            //标题
            int headLen = headName.length;
			/*if(headLen > 10){
				headLen = 10;
			}*/
            HSSFRow rowTiele = sheet.createRow(0);//第一行,即标题行
            rowTiele.setHeight((short) 600);//设置行高
            int hb = headName.length - 1;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headLen - 1));//设置合并单元格
            HSSFCell cellTitle = rowTiele.createCell(0);
            cellTitle.setCellValue(title);
            cellTitle.setCellStyle(styleTitle);
            //所属辖区
			/*HSSFRow rowSxxq1 = sheet.createRow(1);
			sheet.addMergedRegion(new CellRangeAddress(1,2,0,0));
			String[] zhSsxq = new String[]{"填报单位所属辖区","省","市","市/区/县","乡镇/街道","社区/村"};
			for (int j = 0; j < zhSsxq.length; j++) {
				HSSFCell cellSsxq = rowSxxq1.createCell(j);
				cellSsxq.setCellValue(zhSsxq[j]);
				cellSsxq.setCellStyle(styleHead);
			}
			if(!ssxq.isEmpty()){
				String[] ssxqs = ssxq.split("\\/");
				int ssxqLen = ssxqs.length;
				HSSFRow rowSxxq2 = sheet.createRow(2);
				for(int s = 1;s <= ssxqLen;s++){
					String value = ssxqs[s];
					if(value != null && !"".equals(value)){
						HSSFCell cellSsxq = rowSxxq2.createCell(s);
						cellSsxq.setCellValue(value);
						cellSsxq.setCellStyle(styleBody);
					}
				}
			}*/
            //表头
            HSSFRow rowHead = sheet.createRow(1);//第二行，即表头行
            HSSFCell cellHead;
            for (int y = 0; y < headName.length; y++) {
                cellHead = rowHead.createCell(y);
                cellHead.setCellValue(headName[y]);
                cellHead.setCellStyle(styleHead);
            }
            try {
                //表体
                int rowBodyNum = 2;//表体起始行
                int headIdNum = headIds.length;
                for (int z = i * 50000; z < dataList.size(); z++) {
                    if (z >= (i + 1) * 50000) {
                        break;
                    }
                    HashMap<String, String> map = dataList.get(z);
                    HSSFRow rowBody = sheet.createRow(rowBodyNum);
                    for (int x = 0; x < headIdNum; x++) {
                        HSSFCell cellBody = rowBody.createCell(x);
                        cellBody.setCellValue(map.get(headIds[x]));
                        cellBody.setCellStyle(styleBody);
                    }
                    if (StringUtils.isNotBlank(map.get("errors"))) {
                        HSSFCell cellBody = rowBody.createCell(headIdNum);
                        cellBody.setCellValue(map.get("errors"));
                        cellBody.setCellStyle(styleBody);
                    }
                    rowBodyNum++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wb;
    }

    /**
     * 导出xlsx类型Excel
     *
     * @param title    文件名（也是标题）
     * @param ssxq     所属辖区
     * @param headName 表头（表格列名）
     * @param headIds  字段名
     * @param dataList 需要导出的数据
     * @return
     */
    private static XSSFWorkbook exportXLSXExcel(String title, String ssxq, String[] headName, String[] headIds,
                                                List<HashMap<String, String>> dataList) {
        // 创建excel工作簿
        XSSFWorkbook wb = new XSSFWorkbook();
        //获得数据集大小
        int dataNumber = dataList.size();
        int sheetNumber = (int) Math.ceil((float) dataNumber / 50000);//分sheet页，每页sheet最多存放50000条数据
        for (int i = 0; i < sheetNumber; i++) {
            // 创建第一个sheet（页），并命名
            XSSFSheet sheet = wb.createSheet(title + String.valueOf(i + 1));
            // 设置默认宽度
            sheet.setDefaultColumnWidth(20);
            // 创建三种单元格格式
            XSSFCellStyle styleTitle = wb.createCellStyle();
            styleTitle = setXLSXTitleStyle(wb, styleTitle);
            XSSFCellStyle styleHead = wb.createCellStyle();
            styleHead = setXLSXHeadStyle(wb, styleHead);
            XSSFCellStyle styleBody = wb.createCellStyle();
            styleBody = setXLSXBodyStyle(wb, styleBody);
            //标题
            int headLen = headName.length;
			/*if(headLen > 10){
				headLen = 10;
			}*/
            XSSFRow rowTiele = sheet.createRow(0);//第一行,即标题行
            rowTiele.setHeight((short) 600);//设置行高
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headLen - 1));//设置合并单元格
            XSSFCell cellTitle = rowTiele.createCell(0);
            cellTitle.setCellValue(title);
            cellTitle.setCellStyle(styleTitle);
            //所属辖区
			/*XSSFRow rowSxxq1 = sheet.createRow(1);
			sheet.addMergedRegion(new CellRangeAddress(1,2,0,0));
			String[] zhSsxq = new String[]{"填报单位所属辖区","省","市","市/区/县","乡镇/街道","社区/村"};
			for (int j = 0; j < zhSsxq.length; j++) {
				XSSFCell cellSsxq = rowSxxq1.createCell(j);
				cellSsxq.setCellValue(zhSsxq[j]);
				cellSsxq.setCellStyle(styleHead);
			}
			if(!ssxq.isEmpty()){
				String[] ssxqs = ssxq.split("\\/");
				int ssxqLen = ssxqs.length;
				XSSFRow rowSxxq2 = sheet.createRow(2);
				for(int s = 1;s < ssxqLen;s++){
					String value = ssxqs[s];
					if(value != null && !"".equals(value)){
						XSSFCell cellSsxq = rowSxxq2.createCell(s);
						cellSsxq.setCellValue(value);
						cellSsxq.setCellStyle(styleBody);
					}
				}
			}*/
            //表头
            XSSFRow rowHead = sheet.createRow(1);//第二行，即表头行
            XSSFCell cellHead;
            for (int y = 0; y < headName.length; y++) {
                cellHead = rowHead.createCell(y);
                cellHead.setCellValue(headName[y]);
                cellHead.setCellStyle(styleHead);
            }
            try {
                //表体
                int rowBodyNum = 2;//表体起始行
                int headIdNum = headIds.length;
                for (int z = i * 50000; z < dataList.size(); z++) {
                    if (z >= (i + 1) * 50000) {
                        break;
                    }
                    HashMap<String, String> map = dataList.get(z);
                    XSSFRow rowBody = sheet.createRow(rowBodyNum);
                    for (int x = 0; x < headIdNum; x++) {
                        XSSFCell cellBody = rowBody.createCell(x);
                        cellBody.setCellValue(map.get(headIds[x]));
                        cellBody.setCellStyle(styleBody);
                    }
                    if (StringUtils.isNotBlank(map.get("errors"))) {
                        XSSFCell cellBody = rowBody.createCell(headIdNum);
                        cellBody.setCellValue(map.get("errors"));
                        cellBody.setCellStyle(styleBody);
                    }
                    rowBodyNum++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wb;
    }

    /**
     * 设置xls标题样式
     *
     * @param wb    workbook对象
     * @param sheet sheet页对象
     * @param style cell样式对象
     */
    private static HSSFCellStyle setXLSTitleStyle(HSSFWorkbook wb, HSSFCellStyle style) {
        //设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样样式
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     * 设置xls表头样式
     *
     * @param wb    workbook对象
     * @param style cell样式对象
     */
    private static HSSFCellStyle setXLSHeadStyle(HSSFWorkbook wb, HSSFCellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样样式
        style.setFont(font);
        return style;
    }

    /**
     * 设置xls表体样式
     *
     * @param wb    workbook对象
     * @param style cell样式对象
     */
    private static HSSFCellStyle setXLSBodyStyle(HSSFWorkbook wb, HSSFCellStyle style) {
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成字体
        HSSFFont font2 = wb.createFont();
        // font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font2.setFontHeightInPoints((short) 11);
        font2.setColor(IndexedColors.BLACK.getIndex());
        // 把字体应用到当前的样样式
        style.setFont(font2);
        return style;
    }

    /**
     * 设置xlsx标题样式
     *
     * @param sheet sheet页对象
     * @param style cell样式对象
     */
    private static XSSFCellStyle setXLSXTitleStyle(XSSFWorkbook wb, XSSFCellStyle style) {
        //设置字体
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样样式
        style.setFont(font);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        return style;
    }

    /**
     * 设置xlsx表头样式
     *
     * @param wb    workbook对象
     * @param style cell样式对象
     */
    private static XSSFCellStyle setXLSXHeadStyle(XSSFWorkbook wb, XSSFCellStyle style) {
        //边框
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        //水平居中
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //上下居中
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 生成字体
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样样式
        style.setFont(font);
        return style;
    }

    /**
     * 设置xlsx表体样式
     *
     * @param wb    workbook对象
     * @param style cell样式对象
     */
    private static XSSFCellStyle setXLSXBodyStyle(XSSFWorkbook wb, XSSFCellStyle style) {
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 生成字体
        XSSFFont font2 = wb.createFont();
        font2.setFontHeightInPoints((short) 11);
        font2.setColor(IndexedColors.BLACK.getIndex());
        // 把字体应用到当前的样样式
        style.setFont(font2);
        return style;
    }

    public static void main(String[] args) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("测试");
        XSSFDrawing p = sheet.createDrawingPatriarch();
        XSSFComment comment = p.createCellComment(new XSSFClientAnchor(1, 1, 6, 1, 3, 3, 5, 6));
        comment.setString(new XSSFRichTextString("批注内容"));
        XSSFCell cell = sheet.createRow(0).createCell(1, 5);
        cell.setCellComment(comment);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream("D:/测试EXCEL.xlsx");
            wb.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
