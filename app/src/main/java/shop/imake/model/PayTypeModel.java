package shop.imake.model;

import java.util.List;

/**
 * Created by SONY on 2017/6/7.
 */

public class PayTypeModel {

    private List<PayTypesBean> pay_types;

    public List<PayTypesBean> getPay_types() {
        return pay_types;
    }

    public void setPay_types(List<PayTypesBean> pay_types) {
        this.pay_types = pay_types;
    }

    public static class PayTypesBean {
        /**
         * id : 1
         * name : 支付宝
         * icon :
         * pay_param : alipay
         * is_open : 1
         * is_exchange : 0
         */

        private int id;
        private String name;
        private String icon;
        private String pay_param;
        private int is_open;
        private int is_exchange;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPay_param() {
            return pay_param;
        }

        public void setPay_param(String pay_param) {
            this.pay_param = pay_param;
        }

        public int getIs_open() {
            return is_open;
        }

        public void setIs_open(int is_open) {
            this.is_open = is_open;
        }

        public int getIs_exchange() {
            return is_exchange;
        }

        public void setIs_exchange(int is_exchange) {
            this.is_exchange = is_exchange;
        }
    }
}
