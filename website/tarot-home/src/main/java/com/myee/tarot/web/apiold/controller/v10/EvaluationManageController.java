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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
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

    /**
     * 保存服务评价接口,30s之内重复提交的评价会覆盖，即30s之内只有最后一条评价有效
     * @param evaluationView
     * @return
     */
    @RequestMapping(value = "/api/v10/evaluation/save")
    public ClientAjaxResult save(@ModelAttribute("evaluationView") EvaluationView evaluationView) {
        logger.info("保存评价开始");
        try {
            if(!conNotBlank(evaluationView)){
                return ClientAjaxResult.failed("参数不正确...");
            }
            Table table = tableManageService.findById(evaluationView.getTableId());
            if(table == null){
                return ClientAjaxResult.failed("餐桌不存在...");
            }
            Evaluation evaluation = evaluationManageService.getLatestByTableId(evaluationView.getTableId());
            if(evaluation != null && StringUtil.isBlank(TypeConverter.toString(evaluation.getTimeSecond()))){
                return ClientAjaxResult.failed("数据出问题了...");
            }
            if(evaluation == null || (evaluation != null && ((DateTimeUtils.getShortDateTimeL() - evaluation.getTimeSecond()) > 300))){
                Evaluation evaluation1 = new Evaluation(evaluationView);
                evaluation1.setTable(table);
                evaluation1.setEvaluCreated(new Date());
                evaluationManageService.update(evaluation1);
            }else if(evaluation != null){
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
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    //评论参数为必须至少有一个输入
    boolean conNotBlank(EvaluationView evaluationView){
        if(evaluationView == null || (evaluationView != null
                && StringUtil.isBlank(TypeConverter.toString(evaluationView.getTableId())))){
            return false;
        }else if(evaluationView.getTimeSecond() == null){
            return false;
        }else if(!StringUtil.isBlank(evaluationView.getDeviceRemark())
                && !StringUtil.isBlank(evaluationView.getMealsRemark()) && (evaluationView.getDeviceRemark().length() > 200 || evaluationView.getMealsRemark().length() > 200)){
            return false;
        }else if(!StringUtil.isBlank(EvaluationLevelType.getName(evaluationView.getFeelWhole()))
                || !StringUtil.isBlank(EvaluationLevelType.getName(evaluationView.getFeelFlavor()))
                || !StringUtil.isBlank(EvaluationLevelType.getName(evaluationView.getFeelService()))
                || !StringUtil.isBlank(EvaluationLevelType.getName(evaluationView.getFeelEnvironment()))
                || !StringUtil.isBlank(evaluationView.getDeviceRemark())
                || !StringUtil.isBlank(evaluationView.getMealsRemark()) ){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 分页/不分页显示评论list
     */
    @RequestMapping(value = "/admin/serviceEvaluation/list", method = RequestMethod.POST)
    @ResponseBody
    public AjaxPageableResponse listPage(@ModelAttribute("paginationView") PageRequest pageRequest,
                               @RequestParam(value = "tableId",required = false)Long tableId,
                               @RequestParam(value = "begin",required = false)Long begin,
                               @RequestParam(value = "end",required = false)Long end, HttpServletRequest request){
        AjaxPageableResponse resp = new AjaxPageableResponse();
        pageRequest.setCount(10);
        pageRequest.setPage(0);
        //根据当前用户获取切换的门店信息
        String sessionName = (String) ValidatorUtil.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION);
        if (sessionName == null || request.getSession().getAttribute(sessionName) == null) {
            resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
            resp.setStatusMessage("请先切换门店");
            return resp;
        }
        MerchantStore merchantStore = (MerchantStore) request.getSession().getAttribute(sessionName);
        if((begin == null || "".equals(begin)) && end != null){
            resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
            resp.setStatusMessage("请设置查看的起始时间");
            return resp;
        }
        if((end == null || "".equals(end) )&& begin != null){
            resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
            resp.setStatusMessage("请设置查看的截止时间");
            return resp;
        }
        if(begin != null && end != null) {
            if (DateTimeUtils.toMillis(end).compareTo(DateTimeUtils.toMillis(begin)) < 0) {
                resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
                resp.setStatusMessage("结束时间不能小于开始时间");
                return resp;
            }
            pageRequest.getFilter().put("begin", DateTimeUtils.toMillis(begin * 1000));
            pageRequest.getFilter().put("end", DateTimeUtils.toMillis(end * 1000));
        }
        pageRequest.getFilter().put("tableId", tableId);
        //返回的结果数值
        Map<String,Object> result = Maps.newHashMap();
        //查询总体平均值
        List<Evaluation> feelAverage = (List<Evaluation>)evaluationManageService.getFeelAverage(pageRequest);
        //为了前端评分参数显示不为NaN(not a number)，判断当查询结果为空时，扔个0或null就行了
        if(null != feelAverage && feelAverage.size() > 0){
        }
        else{
            feelAverage = new ArrayList<Evaluation>();
            Evaluation eTemp = new Evaluation();
            eTemp.setFeelEnvironment(0);
            eTemp.setFeelFlavor(0);
            eTemp.setFeelService(0);
            eTemp.setFeelWhole(0);
            feelAverage.add(eTemp);
        }
        //查询评论详细
        PageResult<Evaluation> pageResult = evaluationManageService.listInPage(pageRequest);

        List<Evaluation> evaluationList = pageResult.getList();
        for (Evaluation evaluation : evaluationList) {
            resp.addDataEntry(objectToEntry(evaluation));
        }
        resp.setRecordsTotal(pageResult.getRecordsTotal());
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
        map.put("feelEnvironment", evaluation.getFeelEnvironment());
        map.put("feelWhole", evaluation.getFeelWhole());
        map.put("feelFlavor", evaluation.getFeelFlavor());
        map.put("feelService", evaluation.getFeelService());
        map.put("mealsRemark", evaluation.getMealsRemark());
        map.put("timeSecond", evaluation.getTimeSecond());
        map.put("tableName", evaluation.getTable().getName());
        map.put("storeName", evaluation.getTable().getStore().getName());
        map.put("merchantName", evaluation.getTable().getStore().getMerchant().getName());
        return map;
    }
}