package com.myee.tarot.apiold.view;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UploadTokenView implements Serializable {

	private String uptoken;
	
//	private String save_key;

	public UploadTokenView(){
	}

	public String getUptoken() {
		return uptoken;
	}

	public void setUptoken(String uptoken) {
		this.uptoken = uptoken;
	}

}
