package com.pureTec.common;

public class ResultData{
	
	int code;
	Object data;
	String error;
	
	public ResultData() {
		// TODO Auto-generated constructor stub
	}
	
	public ResultData(int code,Object data) {
		this.code = code ;
		this.data = data ;
		// TODO Auto-generated constructor stub
	}


	
	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}


	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
	
	
	
	
}