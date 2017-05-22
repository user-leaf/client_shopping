package shop.imake.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class MyIncomeModel {

    /**
     * usable_push_money : 200
     * push_money : 0
     * zhonghuiquan : 200
     * all_push_money : 0
     * push_money_details : [{"push_money":"11000.00","type":1,"zhonghuiquan":"3100","remarks":"直接联盟商家收益","number":3,"word1":"共对接","word2":"家","word3":"对接收益"},{"push_money":"1000.00","type":2,"zhonghuiquan":"100","remarks":"协助好友对接联盟商家收益","number":1,"word1":"共对接","word2":"家","word3":"对接收益"}]
     */

    private double usable_push_money;
    private String push_money;
    private double zhonghuiquan;
    private double all_push_money;
    /**
     * push_money : 11000.00
     * type : 1
     * zhonghuiquan : 3100
     * remarks : 直接联盟商家收益
     * number : 3
     * word1 : 共对接
     * word2 : 家
     * word3 : 对接收益
     */

    private List<PushMoneyDetailsBean> push_money_details;

    public double getUsable_push_money() {
        return usable_push_money;
    }

    public void setUsable_push_money(double usable_push_money) {
        this.usable_push_money = usable_push_money;
    }

    public String getPush_money() {
        return push_money;
    }

    public void setPush_money(String push_money) {
        this.push_money = push_money;
    }

    public double getZhonghuiquan() {
        return zhonghuiquan;
    }

    public void setZhonghuiquan(double zhonghuiquan) {
        this.zhonghuiquan = zhonghuiquan;
    }

    public double getAll_push_money() {
        return all_push_money;
    }

    public void setAll_push_money(double all_push_money) {
        this.all_push_money = all_push_money;
    }

    public List<PushMoneyDetailsBean> getPush_money_details() {
        return push_money_details;
    }

    public void setPush_money_details(List<PushMoneyDetailsBean> push_money_details) {
        this.push_money_details = push_money_details;
    }

    public static class PushMoneyDetailsBean {
        private String push_money;
        private int type;
        private String zhonghuiquan;
        private String remarks;
        private int number;
        private String word1;
        private String word2;
        private String word3;

        public String getPush_money() {
            return push_money;
        }

        public void setPush_money(String push_money) {
            this.push_money = push_money;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getZhonghuiquan() {
            return zhonghuiquan;
        }

        public void setZhonghuiquan(String zhonghuiquan) {
            this.zhonghuiquan = zhonghuiquan;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getWord1() {
            return word1;
        }

        public void setWord1(String word1) {
            this.word1 = word1;
        }

        public String getWord2() {
            return word2;
        }

        public void setWord2(String word2) {
            this.word2 = word2;
        }

        public String getWord3() {
            return word3;
        }

        public void setWord3(String word3) {
            this.word3 = word3;
        }
    }

}
