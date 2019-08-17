package com.city.sprinbboot.database2code.com.xjs.common.file;


import com.city.sprinbboot.database2code.com.xjs.common.util.PropertiesFileUtil;
import com.city.sprinbboot.database2code.com.xjs.common.util.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 导入导出数据处理类
 */
public class ImAndExportDataUtil {

    /**
     * 导入字典项数据处理
     *
     * @param list 验证通过的导入数据集
     * @param menu 缓存key
     * @return
     */
    public static List<HashMap<String, String>> getImportData(List<HashMap<String, String>> list, String menu) {
        List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        List<String> redisList = RedisUtil.getList(menu);//获取功能字段缓存
        for (HashMap<String, String> map : list) {
            for (String redis : redisList) {
                String[] rediss = redis.split("-");
                if (rediss[0].equals("DICT")) {//判断是否是字典项
                    String[] dictNodes = rediss[1].split("\\|");
                    String[] zhNodes = rediss[2].split("\\|");
                    List<String> dictList = RedisUtil.getList(zhNodes[0]);//获取字典项缓存
                    for (String dict : dictList) {
                        String[] dicts = dict.split("-");
                        if (map.get(dictNodes[1]).equals(dicts[3])) {
                            map.put(dictNodes[1], dicts[1]);
                        }
                    }
                } else if (rediss[0].equals("RADIO")) {//判断是否是单选项
                    if ("是".equals(map.get(rediss[1]))) {
                        map.put(rediss[1], "1");
                    } else if ("否".equals(map.get(rediss[1]))) {
                        map.put(rediss[1], "0");
                    }
                } else if (rediss[0].equals("STATE")) {
                    if ("存在".equals(map.get(rediss[1]))) {
                        map.put(rediss[1], "0");
                    } else if ("不存在".equals(map.get(rediss[1]))) {
                        map.put(rediss[1], "1");
                    }
                }
            }
            result.add(map);
        }
        return result;
    }

    /**
     * 导入错误信息导出的字段处理
     *
     * @param menu 缓存key
     * @return
     */
    public static HashMap<String, Object> disposeData(String menu) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        List<String> list = RedisUtil.getList(menu);
        String[] newEnNode = new String[list.size()];//拼接需要导出的字段英文
        String[] newZhNode = new String[list.size()];//拼接需要导出的字段中文
        for (int i = 0; i < list.size(); i++) {
            String[] strs = list.get(i).split("-");
            if (strs[0].equals("DICT")) {
                String[] str = strs[1].split("\\|");
                String[] zhNodes = strs[2].split("\\|");
                newEnNode[i] = str[0];
                newZhNode[i] = zhNodes[1];
            } else {
                newEnNode[i] = strs[1];
                newZhNode[i] = strs[2];
            }
        }
        result.put("enNode", newEnNode);
        result.put("zhNode", newZhNode);
        return result;
    }

    /**
     * 获取客户端IP
     *
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
//		String ip = request.getHeader("x-forwarded-for");//获取客户端ip
//		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
//            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
//            if( ip.indexOf(",")!=-1 ){
//                ip = ip.split(",")[0];
//            }
//        }
//		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("Proxy-Client-IP");
//		}
//		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("X-Real-IP");
//        }
//		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getRemoteAddr();
//		}
        return "haiNan";
    }

    /**
     * 导出字段处理
     *
     * @param nodes 导出字段
     */
    public static HashMap<String, Object> disposeNodes(String[] nodes) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String[] nodeEnName = new String[nodes.length];//存放字段英文
        String[] nodeZhName = new String[nodes.length];//存放字段中文
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();//存放字典项
        HashMap<String, String> dictMap = new HashMap<String, String>();
        String radioNode = null;
        String stateNode = null;
        for (int i = 0; i < nodes.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            String[] strs = nodes[i].split("-");
            if (strs[0].equals("DICT")) {//判断是否是字典项
                String[] str = strs[1].split("\\|");
                String[] zhNodes = strs[2].split("\\|");
                dictMap.put(str[1], str[0]);
                map.put("enNode", str[1]);
                map.put("zhNode", strs[2]);
                list.add(map);
                nodeEnName[i] = str[0];
                nodeZhName[i] = zhNodes[1];
            } else {
                if (strs[0].equals("RADIO")) {//判断是否是单选项
                    if (radioNode == null) {
                        radioNode = strs[1];
                    } else {
                        radioNode = radioNode + "-" + strs[1];
                    }
                    map.put("enNode", strs[1]);
                    map.put("zhNode", strs[2]);
                    list.add(map);
                } else if (strs[0].equals("STATE")) {
                    if (stateNode == null) {
                        stateNode = strs[1];
                    } else {
                        stateNode = stateNode + "-" + strs[1];
                    }
                    map.put("enNode", strs[1]);
                    map.put("zhNode", strs[2]);
                    list.add(map);
                }
                nodeEnName[i] = strs[1];
                nodeZhName[i] = strs[2];
            }
        }
        result.put("nodeEnName", nodeEnName);
        result.put("nodeZhName", nodeZhName);
        result.put("list", list);
        result.put("dictMap", dictMap);
        result.put("radioNode", radioNode);
        result.put("stateNode", stateNode);
        return result;
    }

    /**
     * 导出数据处理方法
     *
     * @param value     导出数据集
     * @param list      数据字典字段集
     * @param dictMap   字典项字段和ID字段集
     * @param radioNode 单选字段
     * @param stateNode 状态字段
     */
    public static List<HashMap<String, String>> getExportData(List<HashMap<String, String>> value, List<HashMap<String,
            String>> list, HashMap<String, String> dictMap, String radioNode, String stateNode) {
        List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        for (HashMap<String, String> m : value) {
            for (HashMap<String, String> map : list) {
                String zhNode = map.get("zhNode");
                String enNode = map.get("enNode");
                if (!"".equals(radioNode) && radioNode != null && radioNode.indexOf(enNode) != -1) {//判断是否是单选字段
                    String str = m.get(enNode);
                    if (str.equals("1")) {
                        m.put(enNode, "是");
                    } else {
                        m.put(enNode, "否");
                    }
                } else if (!"".equals(stateNode) && stateNode != null && stateNode.indexOf(enNode) != -1) {
                    String str = m.get(enNode);
                    if (str.equals("0")) {
                        m.put(enNode, "存在");
                    } else {
                        m.put(enNode, "不存在");
                    }
                } else {
                    String[] zhNodes = zhNode.split("\\|");
                    if (RedisUtil.getJedis().exists(zhNodes[0])) {//判断key是否存在
                        List<String> dicts = RedisUtil.getList(zhNodes[0]);
                        String dictValue = m.get(enNode);
                        for (String dict : dicts) {
                            String[] ds = dict.split("-");
                            if (ds[1].equals(dictValue)) {
                                m.remove(enNode);
                                enNode = dictMap.get(enNode);
                                m.put(enNode, ds[2]);
                            }
                        }
                    }
                }
            }
            result.add(m);
        }
        return result;
    }

    /**
     * 获得导入Excel时上传文件的路径
     *
     * @param menu 菜单拼音缩写
     */
    public static String getExcelUploadPath(String menu, HttpServletRequest request) {
        String filePath = PropertiesFileUtil.getInstance("fileManage").get("importPath");//获取导入上传文件存放地址
        String ip = getClientIp(request);//获取客户端ip
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String date = sdf.format(new Date());
        String path = filePath + "/" + ip + "/" + menu + "/" + date;
        return path;
    }

    /**
     * 获得导出Excel是服务器文件存放路径
     *
     * @param menu 菜单拼音缩写
     */
    public static String getExcelExportPath(String menu, HttpServletRequest request) {
        String filePath = PropertiesFileUtil.getInstance("fileManage").get("exportPath");//获取导出文件存放根路径
        String ip = getClientIp(request);//获取客户端ip
        String path = filePath + "/" + ip + "/" + menu;//以ip区分存放
        return path;
    }

    /**
     * <p>根据menu获取导出文件名 </p>
     *
     * @param menu 菜单缩写
     * @return
     * @auther lubd
     * @date Jan 10, 2018 2:00:48 PM
     */
    public static String getExportFileName(String menu) {
        String fileName = null;
        switch (menu) {
            case "shzz":
                fileName = "社会组织";
                break;
            case "fgyzjjzz":
                fileName = "非公有制经济组织";
                break;
            case "fwjl":
                fileName = "服务记录";
                break;
            case "hlhx":
                fileName = "护路护线";
                break;
            case "pcqk":
                fileName = "排查情况";
                break;
            case "sgqk":
                fileName = "事故情况";
                break;
            case "sjxlaj":
                fileName = "涉及线、路案（事）件";
                break;
            case "jdwlaqgl":
                fileName = "寄递物流安全管理";
                break;
            case "zddqpczz":
                fileName = "重点地区排查整治";
                break;
            case "czf":
                fileName = "出租房";
                break;
            case "czrk":
                fileName = "常住人口";
                break;
            case "hjjt":
                fileName = "户籍家庭";
                break;
            case "hjrk":
                fileName = "户籍人口";
                break;
            case "jwry":
                fileName = "境外人员";
                break;
            case "ldrk":
                fileName = "流动人口";
                break;
            case "lsry":
                fileName = "留守人员";
                break;
            case "lygl":
                fileName = "楼宇管理";
                break;
            case "syfw":
                fileName = "实有房屋";
                break;
            case "azbwxry":
                fileName = "艾滋病危险人员";
                break;
            case "sqjzry":
                fileName = "社区矫正人员";
                break;
            case "xdry":
                fileName = "吸毒人员";
                break;
            case "xmsfry":
                fileName = "刑满释放人员";
                break;
            case "zdsfry":
                fileName = "重点上访人员";
                break;
            case "jszahz":
                fileName = "肇事肇祸等严重精神障碍患者";
                break;
            case "zdqsn":
                fileName = "重点青少年";
                break;
            case "cxsqggaqspjk":
                fileName = "城乡社区公共安全视频监控";
                break;
            case "fwcy":
                fileName = "服务成员";
                break;
            case "fwtd":
                fileName = "服务团队";
                break;
            case "ldz":
                fileName = "楼栋长";
                break;
            case "qfqzdw":
                fileName = "群防群治队伍";
                break;
            case "qfqzzz":
                fileName = "群防群治组织";
                break;
            case "ztdasj":
                fileName = "重特大案（事）件";
                break;
            case "zzdw":
                fileName = "综治队伍";
                break;
            case "zzjg":
                fileName = "综治机构";
                break;
            case "zzldzrz":
                fileName = "综治领导责任制";
                break;
            case "slwxxzx":
                fileName = "视联网信息中心";
                break;
            case "zzzx":
                fileName = "综治中心";
                break;
            case "xx":
                fileName = "学校";
                break;
            case "mashr":
                fileName = "命案受害人";
                break;
            case "majbxx":
                fileName = "命案基本信息";
                break;
            case "mafzxyr":
                fileName = "命案犯罪嫌疑人";
                break;
        }
        return fileName;
    }

}
