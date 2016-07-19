package com.myee.tarot.web.log;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.djinn.dto.ResponseData;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.DeviceUsedAttribute;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.log.domain.SelfCheckLog;
import com.myee.tarot.log.service.SelfCheckLogService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.admin.controller.product.AttributeDTO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
@Controller
public class ReportDataManager {

    private Logger logger = LoggerFactory.getLogger(ReportDataManager.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    private SelfCheckLogService selfCheckLogService;

    @RequestMapping(value = "services/uploadStructureData")
    @ResponseBody
    public ResponseData reportData(@RequestParam("resFile") CommonsMultipartFile file, Long storeId,HttpServletRequest request) {
        logger.info("导入excel数据到数据库(app端点击数据的收集),storeId:" + storeId);
        try {
            /*String type = FileType.getFileType(file);
            if(!type.matches(OFF_ALLOW_EXCEL)){
                return ResponseData.errorData("格式不正确,只能为xls格式");
            }*/
            String downloadHome = DOWNLOAD_HOME;
            final File dest = getResFile(storeId, "SelfCheckLog" + "/" + DateTime.getShortDateTime() + "/" + file.getOriginalFilename(),downloadHome);
            if (!file.isEmpty()) {
                dest.mkdirs();
                file.transferTo(dest);
                //异步插入数据库
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<ExcelData> dataList = null;
                        try {
                            dataList = (List) ObjectExcelRead.readExcel(dest.getAbsolutePath(), 1, 0, 0);//执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
                        } catch (Exception e) {
                            logger.error("读取readExcel失败!", e);
                        }
                        int totalSize = dataList.size();
                        if(dataList != null && totalSize > 0){
                            for(int i = 0; i < totalSize; i++){
                                SelfCheckLog selfCheckLog = new SelfCheckLog();
                                ExcelData excelData = dataList.get(i);
                                selfCheckLog.setLength(excelData.getString("var0") == null ? null : TypeConverter.toInteger(excelData.getString("var0")));//pad上传的ID
                                selfCheckLog.setModuleId(excelData.getString("var1") == null ? null : TypeConverter.toInteger(excelData.getString("var1")));
                                selfCheckLog.setFunctionId(excelData.getString("var2") == null ? null : TypeConverter.toInteger(excelData.getString("var2")));
                                selfCheckLog.setData(excelData.getString("var3") == null ? null : TypeConverter.toString(excelData.getString("var3")));
                                selfCheckLog.setTime(excelData.getString("var4") == null ? null : TypeConverter.toLong(excelData.getString("var4")));
                                selfCheckLog.setEventLevel(excelData.getString("var5") == null ? null : TypeConverter.toInteger(excelData.getString("var5")));
                                try {
                                    selfCheckLogService.update(selfCheckLog);
                                } catch (ServiceException e) {
                                    logger.error("message:SelfCheckLog保存失败!",e);
                                }
                            }
                        }
                    }
                }).start();
            }
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ResponseData.errorData("糟了...系统出错了...");
        }
        return ResponseData.successData("success");
    }

    static File getResFile(Long orgID, String absPath, String downloadHome) {
        return FileUtils.getFile(downloadHome, Long.toString(orgID), absPath);
    }

    @RequestMapping(value = "admin/selfCheckLog/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse getSelfCheckLogList(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            /*PageResult<SelfCheckLog> pageResult = selfCheckLogService.pageByParams(merchantStore1.getId(), pageRequest);
            List<SelfCheckLog> selfCheckLogList = pageResult.getList();
            for (SelfCheckLog deviceUsed : selfCheckLogList) {
                resp.addDataEntry(objectToEntry(deviceUsed));
            }
            resp.setRecordsTotal(pageResult.getRecordsTotal());*/
        } catch (Exception e) {
            logger.error("Error while paging products", e);
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(SelfCheckLog selfCheckLog) {
        Map entry = new HashMap();
        entry.put("id",selfCheckLog.getId());
        entry.put("data",selfCheckLog.getData());
        entry.put("level",selfCheckLog.getEventLevelLog().getLevel());
        entry.put("moduleName",selfCheckLog.getModuleLog().getModuleName());
        entry.put("functionName",selfCheckLog.getModuleLog().getFunctionName());
        entry.put("length",selfCheckLog.getLength());
        entry.put("time",DateTime.toNormalDateTime(selfCheckLog.getTime()));
        return entry;
    }


}
