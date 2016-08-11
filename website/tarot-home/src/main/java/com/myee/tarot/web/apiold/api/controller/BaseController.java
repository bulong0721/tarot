package com.myee.tarot.web.apiold.api.controller;

import com.myee.tarot.campaign.service.impl.redis.RedisUtil;
import com.myee.tarot.web.apiold.api.util.CommConfig;
import com.myee.tarot.apiold.view.MobileClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Scope("prototype")
public class BaseController {

	@Autowired
	private RedisUtil redisUtil;

    @Autowired
    private CommConfig commConfig;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

	protected MobileClient mobileClient = new MobileClient();

	public MobileClient getMobileClient() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request != null) {
            String appv = request.getHeader("appv");  //版本
            String cha = request.getHeader("cha");    //渠道
            String ct = request.getHeader("ct");      //客户端时间
            String dev = request.getHeader("dev");    //设备
            String dpi = request.getHeader("dpi");    //像素
            String net = request.getHeader("net");  //网络
            String os = request.getHeader("os");    //ios、android
            String osv = request.getHeader("osv");   //系统版本
            String pos = request.getHeader("pos");   //经纬度
			String uuid = request.getHeader("uuid"); //uuid
            String accessToken = request.getHeader("token"); // 登录后用于鉴权的凭据

            if(StringUtils.isNotEmpty(appv)){
                mobileClient.setAppv(appv);
            }
            if (StringUtils.isNotEmpty(cha)) {
                mobileClient.setCha(cha);
            }
            if (StringUtils.isNotEmpty(ct)) {
                mobileClient.setCt(ct);
            }
            if (StringUtils.isNotEmpty(dev)) {
                mobileClient.setDev(dev);
            }
            if (StringUtils.isNotEmpty(dpi)) {
                mobileClient.setDpi(dpi);
            }
            if (StringUtils.isNotEmpty(net)) {
                mobileClient.setNet(net);
            }
            if (StringUtils.isNotEmpty(osv)) {
                mobileClient.setOsv(osv);
            }
            if (StringUtils.isNotEmpty(uuid)) {
                mobileClient.setUuid(uuid);
            }
            if (StringUtils.isNotEmpty(os)) {
                mobileClient.setAppOs(os);
            }
            if(StringUtils.isNotEmpty(accessToken)){
                mobileClient.setAccessToken(accessToken);
            }
            if (StringUtils.isNotEmpty(pos)) {
                mobileClient.setPos(pos);
            }
		}
		return mobileClient;
	}

    /**Spring会根据注解注入request和response*/
    @ModelAttribute
    public void setRequestAndResponse(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    protected HttpServletRequest getRequest(){
        return request;
    }

    protected CommConfig getCommConfig(){
        return commConfig;
    }

}
