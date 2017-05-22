package shop.imake.model;

/**
 * 任务页签到数据
 *
 * User: JackB
 * Date: 2016/8/15
 */
public class SignInInfo {


    /**
     * sign_in_number : 1
     * is_sign_in : true
     */

    private int sign_in_number;
    private boolean is_sign_in;
    /**
     * sign_in_continuous_number : 3
     * today_get_gold : 11
     */

    private int sign_in_continuous_number;
    private int today_get_gold;

    public int getSign_in_number() {
        return sign_in_number;
    }

    public void setSign_in_number(int sign_in_number) {
        this.sign_in_number = sign_in_number;
    }

    public boolean isIs_sign_in() {
        return is_sign_in;
    }

    public void setIs_sign_in(boolean is_sign_in) {
        this.is_sign_in = is_sign_in;
    }

    public int getSign_in_continuous_number() {
        return sign_in_continuous_number;
    }

    public void setSign_in_continuous_number(int sign_in_continuous_number) {
        this.sign_in_continuous_number = sign_in_continuous_number;
    }

    public int getToday_get_gold() {
        return today_get_gold;
    }

    public void setToday_get_gold(int today_get_gold) {
        this.today_get_gold = today_get_gold;
    }

    @Override
    public String toString() {
        return "SignInInfo{" +
                "sign_in_number=" + sign_in_number +
                ", is_sign_in=" + is_sign_in +
                ", sign_in_continuous_number=" + sign_in_continuous_number +
                ", today_get_gold=" + today_get_gold +
                '}';
    }
}
