package com.myee.tarot.web.apiold.controller.v10;

import com.google.common.collect.Maps;
import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.apiold.eum.EvaluationLevelType;
import com.myee.tarot.apiold.service.EvaluationService;
import com.myee.tarot.apiold.view.EvaluationView;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.*;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.web.ClientAjaxResult;
import com.myee.tarot.web.apiold.util.CommonLoginParam;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Info: 评价接口
 * User: enva.liang@clever-m.com
 * Date: 2016-01-25
 * Time: 15:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
public class EvaluationManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(EvaluationManageController.class);

    @Autowired
    private EvaluationService evaluationManageService;

    @Autowired
    private TableService tableManageService;

    private static final int MAX_EXPORT_COUNT = 50000;

    private static final int SEARCH = 1;

    private static final int EXPORT = 2;

    private static final int AVG = 1;

    private static final int DETAIL_LIST = 2;

    /**
     * 保存服务评价接口,30s之内重复提交的评价会覆盖，即30s之内只有最后一条评价有效
     *
     * @param evaluationView
     * @return
     */
    @RequestMapping(value = "/api/v10/evaluation/save")
    public ClientAjaxResult save(@ModelAttribute("evaluationView") EvaluationView evaluationView) {
        logger.info("保存评价开始");
        try {
            if (!conNotBlank(evaluationView)) {
                return ClientAjaxResult.failed("参数不正确...");
            }
            Table table = tableManageService.findById(evaluationView.getTableId());
            if (table == null) {
                return ClientAjaxResult.failed("餐桌不存在...");
            }
            Evaluation evaluation = evaluationManageService.getLatestByTableId(evaluationView.getTableId());
            if (evaluation != null && StringUtil.isBlank(TypeConverter.toString(evaluation.getTimeSecond()))) {
                return ClientAjaxResult.failed("数据出问题了...");
            }
            if (evaluation == null || (evaluation != null && ((DateTimeUtils.getShortDateTimeL() - evaluation.getTimeSecond()) > 300))) {
                Evaluation evaluation1 = new Evaluation(evaluationView);
                evaluation1.setTable(table);
                evaluation1.setEvaluCreated(new Date());
                evaluationManageService.update(evaluation1);
            } else if (evaluation != null) {
                evaluation.setDeviceRemark(evaluationView.getDeviceRemark());
                evaluation.setMealsRemark(evaluationView.getMealsRemark());
                evaluation.setFeelEnvironment(evaluationView.getFeelEnvironment() == null ? 0 : evaluationView.getFeelEnvironment());
                evaluation.setFeelFlavor(evaluationView.getFeelFlavor() == null ? 0 : evaluationView.getFeelFlavor());
                evaluation.setFeelService(evaluationView.getFeelService() == null ? 0 : evaluationView.getFeelService());
                evaluation.setFeelWhole(evaluationView.getFeelWhole() == null ? 0 : evaluationView.getFeelWhole());
                evaluation.setTimeSecond(DateTimeUtils.getShortDateTimeL());
                evaluation.setEvaluCreated(new Date());
                evaluationManageService.update(evaluation);
            }
            return ClientAjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    //评论参数为必须至少有一个输入
    boolean conNotBlank(EvaluationView evaluationView) {
        if (evaluationView == null || (evaluationView != null
                && StringUtil.isBlank(TypeConverter.toString(evaluationView.getTableId())))) {
            return false;
        } else if (evaluationView.getTimeSecond() == null) {
            return false;
        } else if (!StringUtil.isBlank(evaluationView.getDeviceRemark())
                && !StringUtil.isBlank(evaluationView.getMealsRemark()) && (evaluationView.getDeviceRemark().length() > 200 || evaluationView.getMealsRemark().length() > 200)) {
            return false;
        } else if (!StringUtil.isBlank(EvaluationLevelType.getName(evaluationView.getFeelWhole()))
                || !StringUtil.isBlank(EvaluationLevelType.getName(evaluationView.getFeelFlavor()))
                || !StringUtil.isBlank(EvaluationLevelType.getName(evaluationView.getFeelService()))
                || !StringUtil.isBlank(EvaluationLevelType.getName(evaluationView.getFeelEnvironment()))
                || !StringUtil.isBlank(evaluationView.getDeviceRemark())
                || !StringUtil.isBlank(evaluationView.getMealsRemark())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 分页/不分页显示评论list
     */
    @RequestMapping(value = "/admin/superman/evaluation/list", method = RequestMethod.POST)
    @ResponseBody
    public AjaxPageableResponse listDetailByPage(
            @RequestParam(value = "tableId", required = false) Long tableId,
            @RequestParam(value = "begin", required = false) Long begin,
            @RequestParam(value = "end", required = false) Long end, HttpServletRequest request) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        PageRequest pageRequest = new PageRequest();
        //根据当前用户获取切换的门店信息
        String sessionName = (String) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION);
        if (sessionName == null || request.getSession().getAttribute(sessionName) == null) {
            resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
            resp.setStatusMessage("请先切换门店");
            return resp;
        }
        MerchantStore merchantStore = (MerchantStore) request.getSession().getAttribute(sessionName);
        if ((begin == null || "".equals(begin)) && end != null) {
            resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
            resp.setStatusMessage("请设置查看的起始时间");
            return resp;
        }
        if ((end == null || "".equals(end)) && begin != null) {
            resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
            resp.setStatusMessage("请设置查看的截止时间");
            return resp;
        }
        if (begin != null && end != null) {
            if (DateTimeUtils.toMillis(end).compareTo(DateTimeUtils.toMillis(begin)) < 0) {
                resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
                resp.setStatusMessage("结束时间不能小于开始时间");
                return resp;
            }
            //毫秒数需要乘以1000才能转成正常的日期
            pageRequest.setBegin(DateTimeUtils.toMillis(begin * 1000));
            pageRequest.setEnd(DateTimeUtils.toMillis(end * 1000));
        }
        pageRequest.setTableId(tableId);
        pageRequest.setStoreId(merchantStore.getId());
        //查询评论详细
        PageResult<Evaluation> pageResultAvg = evaluationManageService.pageList(pageRequest, AVG);
        PageResult<Evaluation> pageResultList = evaluationManageService.pageList(pageRequest, DETAIL_LIST);
        Map map = calculateAvg(pageResultAvg, SEARCH);
        //为了前端评分参数显示不为NaN(not a number)，判断当查询结果为空时，扔个0或null就行了
        if (null != map && map.size() > 0) {
        } else {
            map = Maps.newHashMap();
            map.put("feelWhole", 0);
            map.put("feelEnvironment", 0);
            map.put("feelFlavor", 0);
            map.put("feelService", 0);
        }
        resp.setDataMap(map);
        List<Evaluation> evaluationList = pageResultList.getList();
        for (Evaluation evaluation : evaluationList) {
            resp.addDataEntry(objectToEntry(evaluation));
        }
        resp.setRecordsTotal(pageResultList.getRecordsTotal());
        resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
        resp.setStatusMessage("查询成功");
        return resp;
    }

    private Map<String, Object> objectToEntry(Evaluation evaluation) {
        Map map = Maps.newHashMap();
        map.put("id", evaluation.getId());
        map.put("active", evaluation.getActive());
        map.put("deviceRemark", evaluation.getDeviceRemark());
        map.put("evaluCreated", evaluation.getEvaluCreated());
        map.put("feelEnvironment", evaluation.getFeelEnvironment() / 2);
        map.put("feelWhole", evaluation.getFeelWhole() / 2);
        map.put("feelFlavor", evaluation.getFeelFlavor() / 2);
        map.put("feelService", evaluation.getFeelService() / 2);
        map.put("mealsRemark", evaluation.getMealsRemark());
        map.put("timeSecond", evaluation.getTimeSecond());
        map.put("tableName", evaluation.getTable().getName());
        map.put("storeName", evaluation.getTable().getStore().getName());
        map.put("merchantName", evaluation.getTable().getStore().getMerchant().getName());
        return map;
    }

    @RequestMapping(value = "/admin/superman/evaluation/exportCsv", method = RequestMethod.POST)
    @ResponseBody
    public void exportCsv(@RequestParam(value = "tableId", required = false) Long tableId,
                          @RequestParam(value = "begin") Long begin,
                          @RequestParam(value = "end") Long end, HttpServletRequest request, HttpServletResponse resp) {
        CSVWriter writer = null;
        try {
            //根据当前用户获取切换的门店信息
            String sessionName = (String) CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION);
            MerchantStore merchantStore = (MerchantStore) request.getSession().getAttribute(sessionName);
            PageRequest pageRequest = new PageRequest();
            //毫秒数乘以1000保证转换后的时间正常
            pageRequest.setBegin(DateTimeUtils.toMillis(begin * 1000));
            pageRequest.setEnd(DateTimeUtils.toMillis(end * 1000));
            pageRequest.setStoreId(merchantStore.getId());
            pageRequest.setTableId(tableId);
            //我们默认只输出前5W条数据
            pageRequest.setCount(MAX_EXPORT_COUNT);
            PageResult<Evaluation> pageResultAvg = evaluationManageService.pageList(pageRequest, AVG);
            PageResult<Evaluation> pageResultList = evaluationManageService.pageList(pageRequest, DETAIL_LIST);
            //计算平均值
            Map map = calculateAvg(pageResultAvg, EXPORT);
            resp.setHeader("Content-type", "text/csv;charset=gb2312");
            resp.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(DateTimeUtils.toShortDateTime(new Date()) + ".csv", "utf8"));
            writer = new CSVWriter(resp.getWriter());
            writer.writeNext(new String[]{
                    "品牌名称",
                    "店铺名称",
                    "餐桌名称",
                    "整体评价",
                    "口味评价",
                    "服务评价",
                    "环境评价",
                    "提交时间",
                    "用餐评价",
                    "设备评价"
            });
            //往csv第二行写总体评价
            writer.writeNext(new String[]{
                    null,
                    null,
                    null,
                    StringUtil.nullToString(map.get("feelWhole")),
                    StringUtil.nullToString(map.get("feelFlavor")),
                    StringUtil.nullToString(map.get("feelService")),
                    StringUtil.nullToString(map.get("feelEnvironment")),
                    null,
                    null,
                    null
            });
            List<Evaluation> list = pageResultList.getList();
            for (int i = 0; i < list.size(); i++) {
                //写csv文件
                writeCsvFile(writer, list.get(i), i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void writeCsvFile(CSVWriter writer, Evaluation evaluation, Integer i) {
        DecimalFormat df = new DecimalFormat("0.0");//格式化小数，不足的补0
        writer.writeNext(new String[]{
                StringUtil.nullToString(evaluation.getTable().getStore().getMerchant().getName()),
                StringUtil.nullToString(evaluation.getTable().getStore().getName()),
                StringUtil.nullToString(evaluation.getTable().getName()),
                StringUtil.nullToString(df.format((float) evaluation.getFeelWhole() / 20)),
                StringUtil.nullToString(df.format((float) evaluation.getFeelFlavor() / 20)),
                StringUtil.nullToString(df.format((float) evaluation.getFeelService() / 20)),
                StringUtil.nullToString(df.format((float) evaluation.getFeelEnvironment() / 20)),
                StringUtil.nullToString(DateTimeUtils.getDefaultDateString(evaluation.getEvaluCreated())),
                StringUtil.nullToString(evaluation.getMealsRemark()),
                StringUtil.nullToString(evaluation.getDeviceRemark())
        });
    }

    private Map calculateAvg(PageResult pageResult, int type) {
        //查询总体平均值
        int count = (int) pageResult.getRecordsTotal();
        int feelWhole = 0;
        int feelEnvironment = 0;
        int feelFlavor = 0;
        int feelService = 0;

        float feelWholeF = 0;
        float feelEnvironmentF = 0;
        float feelFlavorF = 0;
        float feelServiceF = 0;

        List<Evaluation> list = pageResult.getList();
        for (Evaluation evaluation : list) {
            if (type == SEARCH) {
                feelWhole += evaluation.getFeelWhole();
                feelEnvironment += evaluation.getFeelEnvironment();
                feelFlavor += evaluation.getFeelFlavor();
                feelService += evaluation.getFeelService();
            } else {
                feelWholeF += evaluation.getFeelWhole();
                feelEnvironmentF += evaluation.getFeelEnvironment();
                feelFlavorF += evaluation.getFeelFlavor();
                feelServiceF += evaluation.getFeelService();
            }
        }
        //因为存的参数放大了20倍，我们除以2后给前端，前端除以10即可
        if (list.size() > 0) {
            switch (type) {
                case 1: //查询
                    if (feelWhole > 0)
                        feelWhole = feelWhole / count / 2;
                    if (feelEnvironment > 0)
                        feelEnvironment = feelEnvironment / count / 2;
                    if (feelFlavor > 0)
                        feelFlavor = feelFlavor / count / 2;
                    if (feelService > 0)
                        feelService = feelService / count / 2;
                    break;
                case 2: //导出
                    if (feelWholeF > 0)
                        feelWholeF = feelWholeF / count / 20;
                    if (feelEnvironmentF > 0)
                        feelEnvironmentF = feelEnvironmentF / count / 20;
                    if (feelFlavorF > 0)
                        feelFlavorF = feelFlavorF / count / 20;
                    if (feelServiceF > 0)
                        feelServiceF = feelServiceF / count / 20;
                    break;
            }
        }
        Map<String, Object> map = Maps.newHashMap();
        DecimalFormat df = new DecimalFormat("0.0");//格式化小数，不足的补0
        map.put("feelWhole", type == SEARCH ? feelWhole : df.format(feelWholeF));
        map.put("feelEnvironment", type == SEARCH ? feelEnvironment : df.format(feelEnvironmentF));
        map.put("feelFlavor", type == SEARCH ? feelFlavor : df.format(feelFlavorF));
        map.put("feelService", type == SEARCH ? feelService : df.format(feelServiceF));
        return map;
    }
}