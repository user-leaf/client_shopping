package shop.imake.model;

/**
 * 要上传的商品列表中的商品Model
 *
 * @author JackB
 * @date 2016/7/18
 */
public class OrderMakeUploadModel {
    private long product_id;
    private long product_size_id;
    private int number;

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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "OrderMakeUploadModel{" +
                "product_id=" + product_id +
                ", product_size_id=" + product_size_id +
                ", number=" + number +
                '}';
    }
}
