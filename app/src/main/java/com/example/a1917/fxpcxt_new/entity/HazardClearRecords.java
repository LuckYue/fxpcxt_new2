package com.example.a1917.fxpcxt_new.entity;

import java.io.Serializable;
import java.util.Date;

public class HazardClearRecords implements Serializable {
	private Long id;
	private Long hazardId;
	private String hazardName;
	private Long enterpriseId;
	private String enterpriseName;
	private Long checkerId;
	private String checkerName;
	private String checkImg;
	private String checkReception;
	private Date checkTime;
	private Boolean status;
	private String hazardLevel;
	private Long changerId;
	private String changerName;
	private String changeImg;
	private String changeReception;
	private Date changeTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getHazardId() {
		return hazardId;
	}
	public void setHazardId(Long hazardId) {
		this.hazardId = hazardId;
	}
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public Long getCheckerId() {
		return checkerId;
	}
	public void setCheckerId(Long checkerId) {
		this.checkerId = checkerId;
	}
	public String getCheckImg() {
		return checkImg;
	}
	public void setCheckImg(String checkImg) {
		this.checkImg = checkImg;
	}
	public String getCheckReception() {
		return checkReception;
	}
	public void setCheckReception(String checkReception) {
		this.checkReception = checkReception;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getHazardLevel() {
		return hazardLevel;
	}
	public void setHazardLevel(String hazardLevel) {
		this.hazardLevel = hazardLevel;
	}
	public Long getChangerId() {
		return changerId;
	}
	public void setChangerId(Long changerId) {
		this.changerId = changerId;
	}
	public String getChangeImg() {
		return changeImg;
	}
	public void setChangeImg(String changeImg) {
		this.changeImg = changeImg;
	}
	public String getChangeReception() {
		return changeReception;
	}
	public void setChangeReception(String changeReception) {
		this.changeReception = changeReception;
	}
	public Date getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}
	public String getHazardName() {
		return hazardName;
	}
	public void setHazardName(String hazardName) {
		this.hazardName = hazardName;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public String getChangerName() {
		return changerName;
	}
	public void setChangerName(String changerName) {
		this.changerName = changerName;
	}
	
}
