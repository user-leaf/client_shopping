package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 邀请页验证联系人是否是注册会员 数据类
 *
 * User: JackB
 * Date: 2016/8/16
 */
public class ContactMemberModel {

    /**
     * phone : 13161211424
     * isRegister : true
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String phone;
        private boolean isRegister;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public boolean isIsRegister() {
            return isRegister;
        }

        public void setIsRegister(boolean isRegister) {
            this.isRegister = isRegister;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "phone='" + phone + '\'' +
                    ", isRegister=" + isRegister +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ContactMemberModel{" +
                "data=" + data +
                '}';
    }
}
