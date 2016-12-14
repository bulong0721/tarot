package com.myee.tarot.web.remoteMonitor.controller;

import com.myee.djinn.dto.enums.LiveNetworkEventType;
import com.myee.djinn.endpoint.EndpointInterface;
import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.service.ProductUsedService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/22.
 */
@Controller
public class DeviceCameraLiveController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCameraLiveController.class);

    @Value("${nginx_rtmp_url}")
    public String NGINX_RTMP_URL;

    @Autowired
    private ProductUsedService productUsedService;
    @Autowired
    private ServerBootstrap serverBootstrap;

    /**
     * 开启摄像头直播数据显示
     * @param productId 产品的ID
     * @return
     */
    @RequestMapping(value = "admin/deviceCamera/show",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse showCameras(@RequestParam("productId")Long productId ){
        try {
            AjaxResponse resp = new AjaxResponse();
            //通知agent开启推流 todo
            ProductUsed productUsed = productUsedService.findById(productId);
            if(productUsed!=null){
                List<DeviceUsed> deviceUsedList = productUsed.getDeviceUsed();
                for (DeviceUsed deviceUsed : deviceUsedList) {
                    //获取平板设备
                    if(deviceUsed.getDevice().getName().equals(Constants.DEVICE_NAME_PAD)){
                        String boardNo = deviceUsed.getBoardNo();
                        deviceUsed.setRtmpUrl(NGINX_RTMP_URL + boardNo);
                        resp.addDataEntry(deviceUsedToMap(deviceUsed));
                    }
                }
            }
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.failed(-1,"直播开启出问题啦");
        }
    }

    /**
     * 开启关闭推流任务
     * @param boardNo
     * @param action
     * @return
     */
    @RequestMapping(value = "admin/deviceCamera/action",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse startLiveCamera(@RequestParam("boardNo")String boardNo,
                                        @RequestParam("action")Integer action){
        try {
            AjaxResponse resp = new AjaxResponse();
            //通知agent开启推流 todo
            String pushUrl = NGINX_RTMP_URL + boardNo;
            boolean bool = false;
            EndpointInterface endpointInterface = serverBootstrap.getClient(EndpointInterface.class, boardNo);
            if(action == Constants.RTMP_START){
                if(LOGGER.isDebugEnabled()){
                    LOGGER.info("板号："+ boardNo + "正在开启推流");
                }
//                bool = endpointInterface.liveNetwork(pushUrl, LiveNetworkEventType.CONNECT);
            }else if(action == Constants.RTMP_STOP) {
                if(LOGGER.isDebugEnabled()){
                    LOGGER.info("板号："+ boardNo + "正在关闭推流");
                }
//                bool = endpointInterface.liveNetwork(pushUrl, LiveNetworkEventType.CLOSE);
            }else {
                if(LOGGER.isDebugEnabled()){
                    LOGGER.info("板号："+ boardNo + "正在更新推流time");
                }
//                bool = endpointInterface.liveNetwork(pushUrl, LiveNetworkEventType.UPDATETIME);
            }
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            System.out.print(bool);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.failed(-1,"推流处理出现问题啦");
        }
    }

    /**
     * 把deviceUsed转换成返回前端的Map
     *
     * @param deviceUsed
     * @return
     */
    private Map deviceUsedToMap(DeviceUsed deviceUsed) {
        Map deviceUsedM = new HashMap();
        deviceUsedM.put("id", deviceUsed.getId());
        deviceUsedM.put("name", deviceUsed.getName());
        deviceUsedM.put("boardNo", deviceUsed.getBoardNo());
        deviceUsedM.put("rtmpUrl", deviceUsed.getRtmpUrl());
        return deviceUsedM;
    }

}
