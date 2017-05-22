package shop.imake.model;

/**
 * Created by Alice
 * 2017/5/13
 */
public class CommissionModel {

    /**
     * push_money : {"all_push_money":null,"push_money":"0"}
     */

    private PushMoneyBean push_money;

    public PushMoneyBean getPush_money() {
        return push_money;
    }

    public void setPush_money(PushMoneyBean push_money) {
        this.push_money = push_money;
    }

    public static class PushMoneyBean {
        /**
         * all_push_money : null
         * push_money : 0
         */

        private String  all_push_money;
        private String push_money;

        public String getAll_push_money() {
            return all_push_money;
        }

        public void setAll_push_money(String all_push_money) {
            this.all_push_money = all_push_money;
        }

        public String getPush_money() {
            return push_money;
        }

        public void setPush_money(String push_money) {
            this.push_money = push_money;
        }
    }
}
