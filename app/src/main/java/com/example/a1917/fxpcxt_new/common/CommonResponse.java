package com.example.a1917.fxpcxt_new.common;

import java.io.Serializable;


public class CommonResponse<T> implements Serializable {

	private static final long serialVersionUID = -8879455279107918136L;
	private static final int SUCCESS_CODE = 200;
	private static final int ERRROR_CODE = 999;
	private String message;
	private T data;
	private int code;

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public int getCode() {
		return code;
	}

	public boolean isSuccess() {
		return code == SUCCESS_CODE;
	}

	public CommonResponse() {
	}

	public static <T> CommonResponse<T> error(String message){
		return new CommonResponse<T>(null,ERRROR_CODE,message);
	}

	public static <T> CommonResponse<T> success(T data) {
		return new CommonResponse<T>(data,SUCCESS_CODE,null);
	}

	public CommonResponse(T data, int code, String msg) {
		this.data = data;
		this.code = code;
		this.message = msg;
	}

}

