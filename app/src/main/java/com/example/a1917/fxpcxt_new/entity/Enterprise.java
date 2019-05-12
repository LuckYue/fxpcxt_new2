package com.example.a1917.fxpcxt_new.entity;

import java.io.Serializable;

public class Enterprise implements Serializable {
	private Long id;
	private Long zoneId;
	private String zoneName;
	private String name;
	private String linkman;
	private String address;
	private String phone;
	private String fundTime;
	private int size;
	private int workers;
	private Long industryId;
	private String industryName;
	private String owerName;
	private String owerPost;
	private String owerPhone;
	private String owerOffice;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getZoneId() {
		return zoneId;
	}
	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFundTime() {
		return fundTime;
	}
	public void setFundTime(String fundTime) {
		this.fundTime = fundTime;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getWorkers() {
		return workers;
	}
	public void setWorkers(int workers) {
		this.workers = workers;
	}
	public Long getIndustryId() {
		return industryId;
	}
	public void setIndustryId(Long industryId) {
		this.industryId = industryId;
	}
	public String getOwerName() {
		return owerName;
	}
	public void setOwerName(String owerName) {
		this.owerName = owerName;
	}
	public String getOwerPost() {
		return owerPost;
	}
	public void setOwerPost(String owerPost) {
		this.owerPost = owerPost;
	}
	public String getOwerPhone() {
		return owerPhone;
	}
	public void setOwerPhone(String owerPhone) {
		this.owerPhone = owerPhone;
	}
	public String getOwerOffice() {
		return owerOffice;
	}
	public void setOwerOffice(String owerOffice) {
		this.owerOffice = owerOffice;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		linkman = linkman;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
}
