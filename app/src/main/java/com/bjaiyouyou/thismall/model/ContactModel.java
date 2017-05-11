package com.bjaiyouyou.thismall.model;

/**
 * 通讯录联系人 数据类
 * User: JackB
 * Date: 2016/5/16
 */
public class ContactModel {

	private String id;
	private String name;
	private String tel;
	private String sortLetters;  // 显示数据拼音的首字母
	private boolean isRegister; // 是否已注册
	private boolean isVip; // 是否是会员

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public boolean isRegister() {
		return isRegister;
	}

	public void setRegister(boolean register) {
		isRegister = register;
	}

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean vip) {
		isVip = vip;
	}

	@Override
	public String toString() {
		return "ContactModel{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", tel='" + tel + '\'' +
				", sortLetters='" + sortLetters + '\'' +
				", isRegister=" + isRegister +
				'}';
	}
}
