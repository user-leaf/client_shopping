package com.bjaiyouyou.thismall.model;

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
     * push_money_details_type1 : [{"push_money":"5000.00","type":1,"zhonghuiquan":"1500","remarks":"销售众汇卡","number":3}]
     * push_money_details_type2 : [{"push_money":"1000.00","type":2,"zhonghuiquan":"100","remarks":"","number":1}]
     * push_money_details_type3 : [{"push_money":"","type":1,"zhonghuiquan":"","remarks":"","number":0}]
     * push_money_details_type4 : [{"push_money":"","type":1,"zhonghuiquan":"","remarks":"","number":0}]
     */

    private int usable_push_money;
    private String push_money;
    private int zhonghuiquan;
    private int all_push_money;
    /**
     * push_money : 5000.00
     * type : 1
     * zhonghuiquan : 1500
     * remarks : 销售众汇卡
     * number : 3
     */

    private List<PushMoneyDetailsType1Bean> push_money_details_type1;
    /**
     * push_money : 1000.00
     * type : 2
     * zhonghuiquan : 100
     * remarks :
     * number : 1
     */

    private List<PushMoneyDetailsType2Bean> push_money_details_type2;
    /**
     * push_money :
     * type : 1
     * zhonghuiquan :
     * remarks :
     * number : 0
     */

    private List<PushMoneyDetailsType3Bean> push_money_details_type3;
    /**
     * push_money :
     * type : 1
     * zhonghuiquan :
     * remarks :
     * number : 0
     */

    private List<PushMoneyDetailsType4Bean> push_money_details_type4;

    public int getUsable_push_money() {
        return usable_push_money;
    }

    public void setUsable_push_money(int usable_push_money) {
        this.usable_push_money = usable_push_money;
    }

    public String getPush_money() {
        return push_money;
    }

    public void setPush_money(String push_money) {
        this.push_money = push_money;
    }

    public int getZhonghuiquan() {
        return zhonghuiquan;
    }

    public void setZhonghuiquan(int zhonghuiquan) {
        this.zhonghuiquan = zhonghuiquan;
    }

    public int getAll_push_money() {
        return all_push_money;
    }

    public void setAll_push_money(int all_push_money) {
        this.all_push_money = all_push_money;
    }

    public List<PushMoneyDetailsType1Bean> getPush_money_details_type1() {
        return push_money_details_type1;
    }

    public void setPush_money_details_type1(List<PushMoneyDetailsType1Bean> push_money_details_type1) {
        this.push_money_details_type1 = push_money_details_type1;
    }

    public List<PushMoneyDetailsType2Bean> getPush_money_details_type2() {
        return push_money_details_type2;
    }

    public void setPush_money_details_type2(List<PushMoneyDetailsType2Bean> push_money_details_type2) {
        this.push_money_details_type2 = push_money_details_type2;
    }

    public List<PushMoneyDetailsType3Bean> getPush_money_details_type3() {
        return push_money_details_type3;
    }

    public void setPush_money_details_type3(List<PushMoneyDetailsType3Bean> push_money_details_type3) {
        this.push_money_details_type3 = push_money_details_type3;
    }

    public List<PushMoneyDetailsType4Bean> getPush_money_details_type4() {
        return push_money_details_type4;
    }

    public void setPush_money_details_type4(List<PushMoneyDetailsType4Bean> push_money_details_type4) {
        this.push_money_details_type4 = push_money_details_type4;
    }

    public static class PushMoneyDetailsType1Bean {
        private String push_money;
        private int type;
        private String zhonghuiquan;
        private String remarks;
        private int number;

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
    }

    public static class PushMoneyDetailsType2Bean {
        private String push_money;
        private int type;
        private String zhonghuiquan;
        private String remarks;
        private int number;

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
    }

    public static class PushMoneyDetailsType3Bean {
        private String push_money;
        private int type;
        private String zhonghuiquan;
        private String remarks;
        private int number;

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
    }

    public static class PushMoneyDetailsType4Bean {
        private String push_money;
        private int type;
        private String zhonghuiquan;
        private String remarks;
        private int number;

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
    }
}
