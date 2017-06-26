package shop.imake.model;

import java.util.List;

/**
 * 拨打电话数据类
 */

public class TelephoneModel  {
    private List<PhoneBean> phone;

    public List<PhoneBean> getPhone() {
        return phone;
    }

    public void setPhone(List<PhoneBean> phone) {
        this.phone = phone;
    }

    public static class PhoneBean {
        /**
         * id : 1
         * val : 010-53358654
         * type : 1
         * type 1 是客服  2是对接电话
         */

        private int id;
        private String val;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
