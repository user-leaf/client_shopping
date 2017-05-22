package shop.imake.model;

/**
 * 订单详情--重新购买--批量添加到购物车
 *
 * User: JackB
 * Date: 2016/8/25
 */
public class AddAlltoCartNew {

    /**
     * product_id : 1
     * product_size_id : 1
     * number : 1
     */

    private long product_id;
    private long product_size_id;
    private long number;

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getProduct_size_id() {
        return product_size_id;
    }

    public void setProduct_size_id(long product_size_id) {
        this.product_size_id = product_size_id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
