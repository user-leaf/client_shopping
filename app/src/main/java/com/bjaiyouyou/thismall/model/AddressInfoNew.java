package com.bjaiyouyou.thismall.model;

import java.io.Serializable;
import java.util.List;

/**
 * 收货地址数据类（新）
 *
 * @author kanbin
 * @date 2016/7/30
 */
public class AddressInfoNew {

    /**
     * id : 55
     * is_default : true
     * contact_person : 联系人
     * contact_phone : 18210001111
     * province : 北京市
     * city : 北京市
     * district : 朝阳区
     * street : 0
     * address_detail : 北京像素南区
     * created_at : 2016-07-31 18:04:03
     * updated_at : 2016-07-31 18:04:03
     */

    private List<MemberAddressesBean> member_addresses;

    public List<MemberAddressesBean> getMember_addresses() {
        return member_addresses;
    }

    public void setMember_addresses(List<MemberAddressesBean> member_addresses) {
        this.member_addresses = member_addresses;
    }

    public static class MemberAddressesBean implements Serializable{
        private long id;
        private boolean is_default;
        private String contact_person;
        private String contact_phone;
        private String province;
        private String city;
        private String district;
        private String street;
        private String address_detail;
        private String created_at;
        private String updated_at;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public boolean isIs_default() {
            return is_default;
        }

        public void setIs_default(boolean is_default) {
            this.is_default = is_default;
        }

        public String getContact_person() {
            return contact_person;
        }

        public void setContact_person(String contact_person) {
            this.contact_person = contact_person;
        }

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getAddress_detail() {
            return address_detail;
        }

        public void setAddress_detail(String address_detail) {
            this.address_detail = address_detail;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        @Override
        public String toString() {
            return "MemberAddressesBean{" +
                    "id=" + id +
                    ", is_default=" + is_default +
                    '}';
        }
    }
}
