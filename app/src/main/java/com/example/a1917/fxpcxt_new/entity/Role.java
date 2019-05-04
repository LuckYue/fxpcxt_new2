package com.example.a1917.fxpcxt_new.entity;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable{
	private Long id;
	private String name;
	private Boolean status;
	private List<Function> functions;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public List<Function> getFunctions() {
		return functions;
	}
	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}
	
}
