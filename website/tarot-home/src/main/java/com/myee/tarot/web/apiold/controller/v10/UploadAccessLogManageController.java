package com.myee.tarot.web.apiold.controller.v10;

import com.myee.tarot.apiold.domain.UploadAccessLog;
import com.myee.tarot.apiold.service.UploadAccessLogService;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.TypeConverter;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.web.apiold.util.ExcelData;
import com.myee.tarot.web.files.FileType;
import com.myee.tarot.web.ClientAjaxResult;
import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Info: 视频接口
 * User: enva.liang@clever-m.com
 * Date: 2016-01-25
 * Time: 15:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
public class UploadAccessLogManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UploadAccessLogManageController.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    private TableService tableService;

    @Autowired
    private UploadAccessLogService uploadAccessLogManageService;

//    @Autowired
//    private ProductStatusService productStatusLogManageService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    //需要人为计算点击行为停留时间的action表，因为apk那边没办法监测这些行为
    private final static int[] needCountAction = {6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 31,32};
    //不需要人为计算点击行为停留时间的action表，因为是瞬发行为
    private final static int[] avoidCountAction = {1, 2, 3, 4, 5, 28, 29, 30,33,34,35};


    /**
     * 导入excel数据到数据库(app端点击数据的收集)
     * 20160823点点笔有笔方案在使用的接口，当老后台停用后再开启该接口
     * @param file
     * @return
     */
    @RequestMapping(value = "/api/v10/uploadAccessLog/save")
    public ClientAjaxResult queryVideoList(@RequestParam(value = "orgId") Long orgId,
                                           @RequestParam(value = "tableId") Long tableId,
                                           @RequestParam(value = "path") String path,
                                           @RequestParam(value = "resFile") CommonsMultipartFile file) {
        logger.info("导入excel数据到数据库(app端点击数据的收集),orgId:" + orgId + "路径,path:" + path);
        try {
            String type = FileType.getFileType(file);
            if (!type.matches(Constants.OFF_ALLOW_EXCEL)) {
                return ClientAjaxResult.failed("格式不正确,只能为xls格式");
            }
            final File dest = getResFile(orgId, path + "/" + tableId + "/" + DateTimeUtils.getShortDateTime() + "/" + file.getOriginalFilename());
            if (!file.isEmpty()) {
                dest.mkdirs();
                file.transferTo(dest);
                //异步插入数据库
                //用线程池代替原来的new Thread方法
                taskExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        List<ExcelData> dataList = null;
                        try {
                            //TODO 20160823点点笔老后台关闭时，再打开该接口
//                            dataList = (List) ObjectExcelRead.readExcel(dest.getAbsolutePath(), 1, 0, 0);//执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
                            int totalSize = dataList.size();
                            if (dataList != null && totalSize > 0) {
                                Date now = new Date();
                                for (int i = 0; i < totalSize; i++) {
                                    UploadAccessLog upLog = new UploadAccessLog();
                                    ExcelData excelData = dataList.get(i);
                                    //餐桌信息如果不存在，则跳过该条数据，作为无用数据
                                    Table table = tableService.findById( TypeConverter.toLong(excelData.getString("var6")) );
                                    if(table == null){
                                        continue;
                                    }
                                    upLog.setUploadAccessId(excelData.getString("var0") == null ? null : TypeConverter.toLong(excelData.getString("var0")));//pad上传的ID
                                    upLog.setActionId(excelData.getString("var1") == null ? null : TypeConverter.toLong(excelData.getString("var1")));//一级功能ID
                                    upLog.setTimePoit(excelData.getString("var2") == null ? null : DateTimeUtils.toDate(excelData.getString("var2")));//点击时间
                                    //有些停留时间apk没法监控，必须后台计算离下一次操作的时间间隔作为停留时间
                                    upLog.setTimeStay(checkCountStayTime(dataList, i));
//                                upLog.setClientId(excelData.getString("var4") == null ? null : TypeConverter.toLong(excelData.getString("var4")));//品牌ID
//                                upLog.setOrgId(excelData.getString("var5") == null ? null : TypeConverter.toLong(excelData.getString("var5")));//餐厅ID
//                                upLog.setTableId(excelData.getString("var6") == null ? null : TypeConverter.toLong(excelData.getString("var6")));//餐桌ID
                                    upLog.setTable(table);
                                    upLog.setDescription(excelData.getString("var7") == null ? null : excelData.getString("var7"));//描述
                                    upLog.setLevelSecondId(excelData.getString("var8") == null ? null : TypeConverter.toLong(excelData.getString("var8")));//二级功能ID
                                    upLog.setCreated(now);
                                    //数据库增加了tableId+uploadAccessId的联合主键，不让插入重复数据
                                    try {
                                        uploadAccessLogManageService.update(upLog);
                                    } catch (ServiceException e) {
                                        logger.error(e.getMessage(), e);
                                        continue;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("读取readExcel失败!", e);
                        }
                    }
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
        return ClientAjaxResult.success();
    }

    /**
     * 导入csv数据到数据库(app端点击数据的收集)
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadAccessLog/saveCSV")
    public ClientAjaxResult uploadCSV(@RequestParam(value = "orgId") Long orgId,
                                      @RequestParam(value = "tableId") Long tableId,
                                      @RequestParam(value = "path") String path,
                                      @RequestParam(value = "resFile") CommonsMultipartFile file) {
        logger.info("导入CSV数据到数据库(app端点击数据的收集),orgId:" + orgId + "路径,path:" + path);
        try {
            String fileKindString = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).trim().toLowerCase();

            if (!fileKindString.equals(".csv")) {
                return ClientAjaxResult.failed("格式不正确,只能为.csv格式");
            }
            final File dest = getResFile(orgId, path + "/" + tableId + "/" + DateTimeUtils.getShortDateTime() + "/" + file.getOriginalFilename());
            if (!file.isEmpty()) {
                dest.mkdirs();
                file.transferTo(dest);
                //异步插入数据库
                //用线程池代替原来的new Thread方法
                taskExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dest), "UTF-8"));
                            CSVReader csvReader = new CSVReader(br);
                            String[] strs = csvReader.readNext();//读一行，第一行是抬头，暂时没用

                            List<String[]> list = csvReader.readAll();//读剩下全部，如果文件太大，有风险，可能会内存溢出
                            if (list != null) {
                                Date now = new Date();
                                for (int i = 0; i < list.size(); i++) {
                                    String[] ss = list.get(i);
                                    //餐桌信息如果不存在，则跳过该条数据，作为无用数据
                                    Table table = tableService.findById(TypeConverter.toLong(ss[6]));
                                    if(table == null){
                                        continue;
                                    }
                                    UploadAccessLog upLog = new UploadAccessLog();
                                    upLog.setUploadAccessId(ss[0] == null ? null : TypeConverter.toLong(ss[0]));//pad上传的ID
                                    upLog.setActionId(ss[1] == null ? null : TypeConverter.toLong(ss[1]));//一级功能ID
                                    upLog.setTimePoit(ss[2] == null ? null : new Date(Long.parseLong(ss[2])));//点击时间
                                    //有些停留时间apk没法监控，必须后台计算离下一次操作的时间间隔作为停留时间
                                    upLog.setTimeStay(checkCountStayTime2(list, i));
//                                    upLog.setClientId(ss[4] == null ? null : TypeConverter.toLong(ss[4]));//品牌ID
//                                    upLog.setOrgId(ss[5] == null ? null : TypeConverter.toLong(ss[5]));//餐厅ID
//                                    upLog.setTableId(ss[6] == null ? null : TypeConverter.toLong(ss[6]));//餐桌ID
                                    upLog.setTable(table);
                                    upLog.setDescription(ss[7] == null ? null : ss[7]);//描述
                                    upLog.setLevelSecondId(ss[8] == null || ss[8].equals("null") ? null : TypeConverter.toLong(ss[8]));//二级功能ID
                                    upLog.setCreated(now);
                                    //数据库增加了tableId+uploadAccessId的联合主键，不让插入重复数据
                                    try {
                                        uploadAccessLogManageService.update(upLog);
                                    } catch (Exception e) {
                                        logger.error(e.getMessage(), e);
                                        continue;
                                    }
                                }
                            }

                            //因为要处理停留时间，没办法一行一行读数据去代替全部读出
//                            Iterator<String []> iterator = csvReader.iterator();
//                            Date now = new Date();
//                            while(iterator.hasNext()){
//                                String[] ss = iterator.next();
//                                if(ss != null){
//                                    UploadAccessLog upLog = new UploadAccessLog();
//                                    upLog.setUploadAccessId(ss[0] == null ? null : TypeConverter.toLong(ss[0]));//pad上传的ID
//                                    upLog.setActionId(ss[1] == null ? null : TypeConverter.toLong(ss[1]));//一级功能ID
//                                    upLog.setTimePoit(ss[2] == null ? null : new Date(Long.parseLong(ss[2])));//点击时间
//                                    //有些停留时间apk没法监控，必须后台计算离下一次操作的时间间隔作为停留时间
//                                    upLog.setTimeStay(checkCountStayTime2(list, i));//因为要处理停留时间，没办法一行一行读数据
//                                    upLog.setClientId(ss[4] == null ? null : TypeConverter.toLong(ss[4]));//品牌ID
//                                    upLog.setOrgId(ss[5] == null ? null : TypeConverter.toLong(ss[5]));//餐厅ID
//                                    upLog.setTableId(ss[6] == null ? null : TypeConverter.toLong(ss[6]));//餐桌ID
//                                    upLog.setDescription(ss[7] == null ? null : ss[7]);//描述
//                                    upLog.setLevelSecondId(ss[8] == null || ss[8].equals("null") ? null : TypeConverter.toLong(ss[8]));//二级功能ID
//                                    upLog.setCreated(now);
//                                    uploadAccessLogManageService.addEntity(upLog);
//                                }
//                            }

                            csvReader.close();
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("fail");
        }
        return ClientAjaxResult.success("success");
    }

    /**
     * 判断是否是需要人为计算停留时间，如果需要则计算，不需要则返回空
     *
     * @param dataList
     * @param i
     * @return
     */
    private Long checkCountStayTime(List<ExcelData> dataList, int i) {
        try {
            //当actionId(对应数组的var1)需要被后台计算，且该条记录不是最后一条记录时，计算该此行为和下一次行为时间差作为停留时间
            ExcelData excelData = dataList.get(i);
            int dataLength = dataList.size();
            if (excelData != null
                    && !ArrayUtils.contains(avoidCountAction, Integer.parseInt(excelData.getString("var1")))//先排除不用计算的瞬发事件，提供运算速度
                    && ArrayUtils.contains(needCountAction, Integer.parseInt(excelData.getString("var1")))
                    && i < dataLength - 1
                    && dataList.get(i + 1).getString("var2") != null) {
                //var2是点击时间
                //计算时间差作为停留时间，判断规则：
                //若下一次事件是瞬发事件，则不统计该次时间差，继续遍历循环直到下一次事件不是瞬发事件，统计时间差，
                // 或者循环遍历到最后一条数据，统计时间差
                int j = 0;
                for (j = i + 1; j < dataLength; j++) {
                    if (ArrayUtils.contains(avoidCountAction, Integer.parseInt(dataList.get(j).getString("var1")))
                            && j != dataLength - 1) {
                        continue;
                    } else if (ArrayUtils.contains(needCountAction, Integer.parseInt(dataList.get(j).getString("var1")))
                            || j == dataLength - 1) {//j指向数组最后一个元素，即使它是瞬发事件，也要计算时间差
                        break;
                    }
                }

                long time = DateTimeUtils.toDate(dataList.get(j).getString("var2")).getTime() - DateTimeUtils.toDate(excelData.getString("var2")).getTime();
                long c = time % 1000 == 0 ? time / 1000 : time / 1000 + 1;
                c = (c <= 0 || c>= 7776000) ? 0 : c; //为负数,或差了3个月的则置0，防止错误数据影响
                return c;//ms数除以1000转换为s
            } else {
                //excel上传的停留时间单位是秒
                return excelData.getString("var3") == null ? 0L : TypeConverter.toLong(excelData.getString("var3"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return 0L;
    }

    /**
     * 判断是否是需要人为计算停留时间，如果需要则计算，不需要则返回空
     *
     * @param dataList
     * @param i
     * @return
     */
    private Long checkCountStayTime2(List<String[]> dataList, int i) {
        try {
            //当actionId(对应数组的var1)需要被后台计算，且该条记录不是最后一条记录时，计算该此行为和下一次行为时间差作为停留时间
            String[] csvData = dataList.get(i);
            int dataLength = dataList.size();
            if (csvData != null
                    && !ArrayUtils.contains(avoidCountAction, Integer.parseInt(csvData[1]))//先排除不用计算的瞬发事件，提供运算速度
                    && ArrayUtils.contains(needCountAction, Integer.parseInt(csvData[1]))
                    && i < dataLength - 1
                    && dataList.get(i + 1)[2] != null) {
                //var2是点击时间
                //计算时间差作为停留时间，判断规则：
                //若下一次事件是瞬发事件，则不统计该次时间差，继续遍历循环直到下一次事件不是瞬发事件，统计时间差，
                // 或者循环遍历到最后一条数据，统计时间差
                int j = 0;
                for (j = i + 1; j < dataLength; j++) {
                    if (ArrayUtils.contains(avoidCountAction, Integer.parseInt(dataList.get(j)[1]))
                            && j != dataLength - 1) {
                        continue;
                    } else if (ArrayUtils.contains(needCountAction, Integer.parseInt(dataList.get(j)[1]))
                            || j == dataLength - 1) {//j指向数组最后一个元素，即使它是瞬发事件，也要计算时间差
                        break;
                    }
                }

                long time = Long.parseLong(dataList.get(j)[2]) - Long.parseLong(csvData[2]);//新格式直接是ms数
                long c = time % 1000 == 0 ? time / 1000 : time / 1000 + 1;
                c = (c <= 0 || c>= 7776000) ? 0 : c; //为负数,或差了3个月的则置0，防止错误数据影响
                return c;//ms数除以1000转换为s
            } else {
                //csv上传的停留时间单位是毫秒
                return csvData[3] == null ? 0L : TypeConverter.toLong(csvData[3]) / 1000;//新方式timestay可能会为0
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return 0L;
    }

    /**
     * 上传点点笔产品状态日志接口，每天2次
     * 20160817因为要加新字段，先迁移到点点笔api后台
     * 20160823因为字段还不稳定，先不移植到tarot
     * @param jsonStatus
     * @param resp
     * @return
     */
/*    @RequestMapping("uploadProductStatusLog/save")
    @ResponseBody
    public ClientAjaxResult uploadProductStatusLog(@RequestParam("jsonStatus") String jsonStatus,
                                                   HttpServletResponse resp) {
        List<ProductStatusLog> productStatusLogList = new ArrayList<ProductStatusLog>();
        try {
//            System.out.println("#######jsonStatus:" + jsonStatus);
            if (jsonStatus != null && !"".equals(jsonStatus)) {
                if (jsonStatus.startsWith("[")) {
                    productStatusLogList = JSON.parseArray(jsonStatus, ProductStatusLog.class);
                } else {
                    productStatusLogList.add(JSON.parseObject(jsonStatus, ProductStatusLog.class));
                }

                if (productStatusLogList == null) {
                    return ClientAjaxResult.success();
                }
                Date now = new Date();
                for (ProductStatusLog productStatusLog : productStatusLogList) {
                    //店铺、品牌ID不能为空
                    if (productStatusLog.getOrgId() == null) {
                        continue;
                    }
                    productStatusLog.setCreated(now);
                    productStatusLogManageService.addEntityBySeq(productStatusLog);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed();
        }
        return ClientAjaxResult.success();
    }*/

    /**
     * 缓存上传接口:由某店铺下PC发起缓存请求。
     * 20160817 已迁移到tarot
     *
     * @param orgID 店铺ID
     * @param entry 缓存key，供查询使用
     * @param value 缓存的值value
     * @param resp
     * @return
     */
    @RequestMapping("catchBytes/save")
    @ResponseBody
    public ClientAjaxResult catchToFile(@RequestParam("orgID") Long orgID, @RequestParam("entry") String entry, @RequestParam("value") byte[] value, HttpServletResponse resp) {
        try {
            if (value.length > 100000) {
                return ClientAjaxResult.failed("错误:数据大小超过100KB限制");
            }
            String fileName = File.separator + entry + ".bin";
            File dest = FileUtils.getFile(DOWNLOAD_HOME, String.valueOf(orgID), File.separator + "catch");
            dest.mkdirs();
            dest = FileUtils.getFile(dest.getPath(), fileName);

            FileOutputStream fos = new FileOutputStream(dest);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
//            System.out.println(new String(value,"UTF-8"));
            osw.write(new String(value, "UTF-8"));
            osw.flush();

            return ClientAjaxResult.success("", "缓存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ClientAjaxResult.failed("缓存失败");
        }
    }

    /**
     * 缓存读取接口:由某店铺下PC发起读取缓存请求。
     * 20160817 已迁移到tarot
     *
     * @param orgID 店铺ID
     * @param entry 缓存key，供查询使用
     * @param resp  读取的缓存的值
     * @return
     */
    @RequestMapping("catchBytes/get")
    public ClientAjaxResult readCatchFile(@RequestParam("orgID") Long orgID, @RequestParam("entry") String entry, HttpServletResponse resp) {
        FileInputStream fis = null;
        String fileContent = "";
        byte[] result = null;
        try {

            String fileName = File.separator + entry + ".bin";
            File dest = FileUtils.getFile(DOWNLOAD_HOME, String.valueOf(orgID), File.separator + "catch" + fileName);
            if (!dest.exists()) {
                return ClientAjaxResult.failed("缓存不存在，读取失败");
            }

            fis = new FileInputStream(dest);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                fileContent += line;
            }
//            result = fileContent.getBytes("UTF8");

//            OutputStream os = new BufferedOutputStream(resp.getOutputStream());
//            resp.setHeader("Content-type", "application/octet-stream;charset=utf8");
//            os.write(result);// 输出文件
//            os.flush();
//            os.close();
            return ClientAjaxResult.success(fileContent, "读取成功");

        } catch (Exception e) {
            e.printStackTrace();
            return ClientAjaxResult.failed("读取失败");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e1) {
                    return ClientAjaxResult.failed("读取失败");
                }
            }
        }
    }

    File getResFile(Long orgID, String absPath) {
        return FileUtils.getFile(DOWNLOAD_HOME, Long.toString(orgID), absPath);
    }
}