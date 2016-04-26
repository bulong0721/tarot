package com.myee.tarot.core.util.ajax;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjaxResponse implements JSONAware {

	public final static int RESPONSE_STATUS_SUCCESS           = 0;
	public final static int RESPONSE_STATUS_FAIURE            = -1;
	public final static int RESPONSE_STATUS_VALIDATION_FAILED = -2;
	public final static int RESPONSE_OPERATION_COMPLETED      = 9999;
	public final static int CODE_ALREADY_EXIST                = 9998;

	private int status;
	private List<Map<String, String>> data               = new ArrayList<Map<String, String>>();
	private Map<String, String>       dataMap            = new HashMap<String, String>();
	private Map<String, String>       validationMessages = new HashMap<String, String>();

	public Map<String, String> getValidationMessages() {
		return validationMessages;
	}

	public void setValidationMessages(Map<String, String> validationMessages) {
		this.validationMessages = validationMessages;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	protected List<Map<String,String>> getData() {
		return data;
	}
	
	public void addDataEntry(Map<String,String> dataEntry) {
		this.data.add(dataEntry);
	}
	
	public void addEntry(String key, String value) {
		dataMap.put(key, value);
	}
	
	
	public void setErrorMessage(Throwable t) {
		this.setStatusMessage(t.getMessage());
	}
	
	public void setErrorString(String t) {
		this.setStatusMessage(t);
	}
	

	public void addValidationMessage(String fieldName, String message) {
		this.validationMessages.put(fieldName, message);
	}

	private String statusMessage = null;


	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}


	protected String getJsonInfo() {

		StringBuilder returnString = new StringBuilder();
		returnString.append("{");
		returnString.append("\"response\"").append(":");
		returnString.append("{");
		returnString.append("\"status\"").append(":").append(this.getStatus());
		if (this.getStatusMessage() != null && this.getStatus() != 0) {
			returnString.append(",").append("\"statusMessage\"").append(":\"").append("\"");
		}
		return returnString.toString();

	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public String toJSONString() {
		StringBuilder returnString = new StringBuilder();

		returnString.append(getJsonInfo());

		if (this.getData().size() > 0) {
			returnString.append(",").append("\"data\"").append(":[");
			returnString.append(JSON.toJSONString(getData()));
			returnString.append("]");
		}

		if (this.getDataMap().size() > 0) {
			StringBuilder dataEntries = null;
			int count = 0;
			for (String key : this.getDataMap().keySet()) {
				if (dataEntries == null) {
					dataEntries = new StringBuilder();
				}
				
				dataEntries.append("\"").append(key).append("\"");
				dataEntries.append(":");
				dataEntries.append("\"").append(this.getDataMap().get(key)).append("\"");

				if(count<this.getDataMap().size()-1) {
					dataEntries.append(",");
				}
				count ++;
			}

			if(dataEntries!=null) {
				returnString.append(",").append(dataEntries.toString());
			}
		}
		
		if(CollectionUtils.isNotEmpty(this.getValidationMessages().values())) {
			StringBuilder dataEntries = null;
			int count = 0;
			for(String key : this.getValidationMessages().keySet()) {
				if(dataEntries == null) {
					dataEntries = new StringBuilder();
				}
				dataEntries.append("{");
				dataEntries.append("\"field\":\"").append(key).append("\"");
				dataEntries.append(",");
				dataEntries.append("\"message\":\"").append(this.getValidationMessages().get(key)).append("\"");
				dataEntries.append("}");

				if(count<this.getValidationMessages().size()-1) {
					dataEntries.append(",");
				}
				count ++;
			}
			
			returnString.append(",").append("\"validations\"").append(":[");
			if(dataEntries!=null) {
				returnString.append(dataEntries.toString());
			}
			returnString.append("]");

		}
		
		returnString.append("}}");

		
		return returnString.toString();

		
	}
	public Map<String,String> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String,String> dataMap) {
		this.dataMap = dataMap;
	}

}
