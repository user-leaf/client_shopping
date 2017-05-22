package shop.imake.model;

/**
 * 退款进度（退款详情）页数据类
 *
 * User: JackB
 * Date: 2016/9/10
 */
public class OrderReturnDealModel {

    /**
     * id : re_y1u944PmfnrTHyvnL0nD0iD1
     * object : refund
     * order_no : y1u944PmfnrTHyvnL0nD0iD1
     * amount : 9
     * created : 1409634160
     * succeed : true
     * status : succeeded
     * time_succeed : 1409634192
     * description : Refund Description
     * failure_code : null
     * failure_msg : null
     * metadata : {}
     * charge : ch_L8qn10mLmr1GS8e5OODmHaL4
     * charge_order_no : 123456789
     * transaction_no : 2004450349201512090096425284
     */

    private String id;
    private String object;
    private String order_no;
    private int amount;
    private long created;
    private boolean succeed;
    private String status;
    private int time_succeed;
    private String description;
    private Object failure_code;
    private Object failure_msg;
    private String charge;
    private String charge_order_no;
    private String transaction_no;
    /**
     * metadata : {}
     * refund_type : 0
     */

    private int refund_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTime_succeed() {
        return time_succeed;
    }

    public void setTime_succeed(int time_succeed) {
        this.time_succeed = time_succeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getFailure_code() {
        return failure_code;
    }

    public void setFailure_code(Object failure_code) {
        this.failure_code = failure_code;
    }

    public Object getFailure_msg() {
        return failure_msg;
    }

    public void setFailure_msg(Object failure_msg) {
        this.failure_msg = failure_msg;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getCharge_order_no() {
        return charge_order_no;
    }

    public void setCharge_order_no(String charge_order_no) {
        this.charge_order_no = charge_order_no;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }

    public int getRefund_type() {
        return refund_type;
    }

    public void setRefund_type(int refund_type) {
        this.refund_type = refund_type;
    }
}
