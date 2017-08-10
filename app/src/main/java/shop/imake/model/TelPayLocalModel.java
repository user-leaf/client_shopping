package shop.imake.model;

/**
 * 手机充值归属地址类
 */

public class TelPayLocalModel {

    /**
     * code : 200
     * message : 成功
     * data : {"mobilenum":"18333618642","area":"河北廊坊","operator":"移动"}
     */

    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * mobilenum : 18333618642
         * area : 河北廊坊
         * operator : 移动
         */

        private String mobilenum;
        private String area;
        private String operator;

        public String getMobilenum() {
            return mobilenum;
        }

        public void setMobilenum(String mobilenum) {
            this.mobilenum = mobilenum;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }
    }
}
