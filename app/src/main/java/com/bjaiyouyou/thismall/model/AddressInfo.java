package com.bjaiyouyou.thismall.model;

/**
 * 收货地址数据类（旧）
 * @author kanbin
 * @date 2016/6/6
 * @see AddressInfoNew(新)
 */
public class AddressInfo {
    private String name;
    private String tel;
    private String address;
    private boolean isDefault;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }

    @Override
    public String toString() {
        return "AddressInfo{" +
                "name='" + name + '\'' +
                '}';
    }
}
