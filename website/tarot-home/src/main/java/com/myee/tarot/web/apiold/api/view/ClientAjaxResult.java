package com.myee.tarot.web.apiold.api.view;

/**
 * 报文放回格式
 * @author hsy
 *
 */
public class ClientAjaxResult {
	private String desc;//错误描述
	private Object data; //返回数据
	private String code = "1000";//1000 :  访问正常  ,1001：当前接口弃用需要客户端强制升级 ,1002：维护中 (提示消息放入errorMsg 字段中),1003：当前访问的接口有新版本可使用, 1004： jsession失效 ,1005：接口异常或错误
	private String err = "";//错误码
	private String ext;//附加信息

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ClientAjaxResult(Object data, String desc){
		this.desc=desc;
		this.data=data;		
	}

    public ClientAjaxResult(Object data, String desc, String code, String err){
        this.desc=desc;
        this.data=data;
        this.code = code;
        this.err = err;
    }

	public static final ClientAjaxResult success() {
		return new ClientAjaxResult("", "");
	}

	public static final ClientAjaxResult success(Object data) {
		return new ClientAjaxResult( data, "");
	}

    public static final ClientAjaxResult success(Object data,  String msg) {
        return new ClientAjaxResult(data,msg);
    }


	public static final ClientAjaxResult failed() {
		return new ClientAjaxResult("", "", "1005","");
	}
	
	public static final ClientAjaxResult failed(String errorMsg) {
		return new ClientAjaxResult("", errorMsg, "1005", "");
	}
	
	public static final ClientAjaxResult failed(String desc, String code) {
		return new ClientAjaxResult("", desc, code, "");
	}
	public static final ClientAjaxResult failed(String desc, String code,String err) {
		return new ClientAjaxResult("", desc, code, err);
	}

	public static final ClientAjaxResult failed(Object data, String desc) {
		return new ClientAjaxResult(data, desc, "1005", "");
	}
	
	public String getDesc() {
		return desc;
	}

	public Object getData() {
		return data;
	}

    public String getErr() {
        return err;
    }
}
