package com.myee.tarot.web.apiold.controller.v10;

import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.apiold.eum.EvaluationLevelType;
import com.myee.tarot.apiold.service.EvaluationService;
import com.myee.tarot.apiold.view.EvaluationView;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.TypeConverter;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.web.ClientAjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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

}