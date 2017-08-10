package shop.imake.model;

import java.util.List;

/**
 * 手机充值历史
 */

public class TelPayHistoryModel {
    private List<Bean> beanList;

    public List<Bean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<Bean> beanList) {
        this.beanList = beanList;
    }

    public static class Bean {
        private String telNum;
        private String name;
        private String local;


        public String getTelNum() {
            return telNum;
        }

        public void setTelNum(String telNum) {
            this.telNum = telNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

    }
}
