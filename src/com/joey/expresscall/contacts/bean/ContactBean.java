package com.joey.expresscall.contacts.bean;

import java.io.Serializable;
import java.util.HashMap;

public class ContactBean implements Serializable {

	private int contactId; //id
	private String desplayName;//姓名
	private String phoneNum; // 电话号码
	private String sortKey; // 排序用的
	private Long photoId; // 图片id
	private String lookUpKey; 
	private int selected = 0;
	private String formattedNumber;
	private String pinyin; // 姓名拼音

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getDesplayName() {
		return desplayName;
	}

	public void setDesplayName(String desplayName) {
		this.desplayName = desplayName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public String getLookUpKey() {
		return lookUpKey;
	}

	public void setLookUpKey(String lookUpKey) {
		this.lookUpKey = lookUpKey;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public String getFormattedNumber() {
		return formattedNumber;
	}

	public void setFormattedNumber(String formattedNumber) {
		this.formattedNumber = formattedNumber;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public HashMap<String,Object> getMap(){
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("phoneNum",getPhoneNum());
		map.put("displayName",getDesplayName());
		map.put("sortKey",getSortKey());
		return  map;
	}


}