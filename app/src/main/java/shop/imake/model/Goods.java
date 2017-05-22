package shop.imake.model;

import java.util.List;

/**
 *
 * @author Alice
 *Creare 2016/7/11 12:11
 * 购买历史记录类
 * 显示抢购详情
 *
 */
public class Goods {


    /**
     * total : 1
     * per_page : 6
     * current_page : 1
     * last_page : 1
     * next_page_url : null
     * prev_page_url : null
     * from : 1
     * to : 1
     * data : [{"id":29,"rush_to_purchase_time_frame_id":2,"product_id":53,"product_size_id":155,"updated_at":"2016-07-26 02:47:21","product":{"id":53,"name":"潘婷400洗发水","score":0,"product_type":1,"updated_at":"2016-08-31 19:19:58"},"size":{"id":155,"name":"潘婷洗发水","bar_code":"","price":"29.00","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"presented_gold":0,"sale_state":1,"updated_at":"2016-08-31 17:39:35"},"image":{"image_path":"http://139.129.167.60/storage/app/public/product/2016/09/07/product_193","image_base_name":"default_0.jpg","updated_at":"2016-09-07 09:39:50"}}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private Object next_page_url;
    private Object prev_page_url;
    private int from;
    private int to;
    /**
     * id : 29
     * rush_to_purchase_time_frame_id : 2
     * product_id : 53
     * product_size_id : 155
     * updated_at : 2016-07-26 02:47:21
     * product : {"id":53,"name":"潘婷400洗发水","score":0,"product_type":1,"updated_at":"2016-08-31 19:19:58"}
     * size : {"id":155,"name":"潘婷洗发水","bar_code":"","price":"29.00","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"presented_gold":0,"sale_state":1,"updated_at":"2016-08-31 17:39:35"}
     * image : {"image_path":"http://139.129.167.60/storage/app/public/product/2016/09/07/product_193","image_base_name":"default_0.jpg","updated_at":"2016-09-07 09:39:50"}
     */

    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public Object getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(Object next_page_url) {
        this.next_page_url = next_page_url;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private int rush_to_purchase_time_frame_id;
        private int product_id;
        private int product_size_id;
        private String updated_at;
        /**
         * id : 53
         * name : 潘婷400洗发水
         * score : 0
         * product_type : 1
         * updated_at : 2016-08-31 19:19:58
         */

        private ProductBean product;
        /**
         * id : 155
         * name : 潘婷洗发水
         * bar_code :
         * price : 29.00
         * jd_price : 0.00
         * tb_price : 0.00
         * supper_market_price : 0.00
         * stock : 0
         * integration_price : 0
         * sales_volume : 0
         * presented_gold : 0
         * sale_state : 1
         * updated_at : 2016-08-31 17:39:35
         */

        private SizeBean size;
        /**
         * image_path : http://139.129.167.60/storage/app/public/product/2016/09/07/product_193
         * image_base_name : default_0.jpg
         * updated_at : 2016-09-07 09:39:50
         */

        private ImageBean image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRush_to_purchase_time_frame_id() {
            return rush_to_purchase_time_frame_id;
        }

        public void setRush_to_purchase_time_frame_id(int rush_to_purchase_time_frame_id) {
            this.rush_to_purchase_time_frame_id = rush_to_purchase_time_frame_id;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getProduct_size_id() {
            return product_size_id;
        }

        public void setProduct_size_id(int product_size_id) {
            this.product_size_id = product_size_id;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public SizeBean getSize() {
            return size;
        }

        public void setSize(SizeBean size) {
            this.size = size;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public static class ProductBean {
            private int id;
            private String name;
            private int score;
            private int product_type;
            private String updated_at;

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

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public int getProduct_type() {
                return product_type;
            }

            public void setProduct_type(int product_type) {
                this.product_type = product_type;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }
        }

        public static class SizeBean {
            private int id;
            private String name;
            private String bar_code;
            private String price;
            private String jd_price;
            private String tb_price;
            private String supper_market_price;
            private int stock;
            private int integration_price;
            private int sales_volume;
            private int presented_gold;
            private int sale_state;
            private String updated_at;

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

            public String getBar_code() {
                return bar_code;
            }

            public void setBar_code(String bar_code) {
                this.bar_code = bar_code;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getJd_price() {
                return jd_price;
            }

            public void setJd_price(String jd_price) {
                this.jd_price = jd_price;
            }

            public String getTb_price() {
                return tb_price;
            }

            public void setTb_price(String tb_price) {
                this.tb_price = tb_price;
            }

            public String getSupper_market_price() {
                return supper_market_price;
            }

            public void setSupper_market_price(String supper_market_price) {
                this.supper_market_price = supper_market_price;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public int getIntegration_price() {
                return integration_price;
            }

            public void setIntegration_price(int integration_price) {
                this.integration_price = integration_price;
            }

            public int getSales_volume() {
                return sales_volume;
            }

            public void setSales_volume(int sales_volume) {
                this.sales_volume = sales_volume;
            }

            public int getPresented_gold() {
                return presented_gold;
            }

            public void setPresented_gold(int presented_gold) {
                this.presented_gold = presented_gold;
            }

            public int getSale_state() {
                return sale_state;
            }

            public void setSale_state(int sale_state) {
                this.sale_state = sale_state;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }
        }

        public static class ImageBean {
            private String image_path;
            private String image_base_name;
            private String updated_at;

            public String getImage_path() {
                return image_path;
            }

            public void setImage_path(String image_path) {
                this.image_path = image_path;
            }

            public String getImage_base_name() {
                return image_base_name;
            }

            public void setImage_base_name(String image_base_name) {
                this.image_base_name = image_base_name;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }
        }
    }
}
