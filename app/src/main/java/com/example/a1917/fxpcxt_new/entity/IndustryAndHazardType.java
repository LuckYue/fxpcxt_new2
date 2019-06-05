package com.example.a1917.fxpcxt_new.entity;

public class IndustryAndHazardType {
	private Long id;
	private Long industyId;
	private String industryName;
	private String hazardType;
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getHazardType() {
		return hazardType;
	}
	public void setHazardType(String hazardType) {
		this.hazardType = hazardType;
	}
	public Long getIndustyId() {
		return industyId;
	}
	public void setIndustyId(Long industyId) {
		this.industyId = industyId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
