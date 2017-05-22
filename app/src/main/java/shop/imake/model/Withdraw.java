package shop.imake.model;

/**
 * 提现信息数据
 * @author Alice
 *Creare 2016/9/18 20:10
 */
public class Withdraw {

    /**
     * all_can_drawings_amount : 0
     * week_can_drawings_amount : 200
     * today_can_drawings_number : 10
     */

    private double all_can_drawings_amount;
    private double week_can_drawings_amount;
    private double today_can_drawings_number;

    public double getAll_can_drawings_amount() {
        return all_can_drawings_amount;
    }

    public void setAll_can_drawings_amount(int all_can_drawings_amount) {
        this.all_can_drawings_amount = all_can_drawings_amount;
    }

    public double getWeek_can_drawings_amount() {
        return week_can_drawings_amount;
    }

    public void setWeek_can_drawings_amount(int week_can_drawings_amount) {
        this.week_can_drawings_amount = week_can_drawings_amount;
    }

    public double getToday_can_drawings_number() {
        return today_can_drawings_number;
    }

    public void setToday_can_drawings_number(int today_can_drawings_number) {
        this.today_can_drawings_number = today_can_drawings_number;
    }
}
