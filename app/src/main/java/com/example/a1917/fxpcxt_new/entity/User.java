package com.example.a1917.fxpcxt_new.entity;

import java.io.Serializable;

/**
 * 用户信息表
 * @author 1917满眼绿意
 *
 */
public class User implements Serializable {
	/*
	 * 用户编号
	 */
	private Long id;
	private String account;
	private String password;
	private String name;
	/*
	 * 所属单位
	 */
	private Long unitId;
	private String unitName;
	private String enterprise;
	public User(){

	}
	public User(Long id, String account, String password, String name, Long unitId, String unitName, Long roleId, Boolean status, String phone, Long orgId) {
		this.id = id;
		this.account = account;
		this.password = password;
		this.name = name;
		this.unitId = unitId;
		this.unitName = unitName;
		this.roleId = roleId;
		this.status = status;
		this.phone = phone;
		this.orgnazationId = orgId;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Long getOrgId() {
		return orgnazationId;
	}
	public void setOrgId(Long orgnazationId) {
		this.orgnazationId = orgnazationId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	private Long roleId;
	/*
	 * 状态(禁用)
	 */
	private Boolean status;
	private String phone;
	/**
	 * 所属组织
	 */
	private Long  orgnazationId;
	private String orgnazationName;
	public String getOrgName(){

		return orgnazationName;
	}
	public void setOrgName(String orgName){
		this.orgnazationName=orgName;
	}
}
