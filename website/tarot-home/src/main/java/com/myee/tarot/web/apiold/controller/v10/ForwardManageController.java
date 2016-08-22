package com.myee.tarot.web.apiold.controller.v10;

import com.myee.tarot.web.apiold.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: enva.liang@clever-m.com
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@Controller
@Scope("prototype")
public class ForwardManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(ForwardManageController.class);

    /**
     * 代驾重定向
     * @return
     */
    @RequestMapping(value = "/api/v10/forward/driveCar")
    public String driveCar(){
        return "redirect:http://page.kuaidadi.com/m/dj.html?cmpid=aad10qov";
    }

}