package shop.imake.model;

import java.util.List;

/**
 * 商品详情类
 * @author Alice
 *Creare 2016/8/1 12:04
 */
public class ProductDetail {

    /**
     * id : 961
     * name : 奥妙净蓝全效水清莲香洗衣液
     * score : 0
     * onsell : 1
     * product_type : 0
     * deleted_at : null
     * updated_at : 2016-10-15 14:35:33
     * if_rush_to_purchasing : false
     * images : [{"deleted_at":null,"image_path":"http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/15/product_825","image_base_name":"default_0.jpg","updated_at":"2016-10-15 12:00:47"}]
     * sizes : [{"id":372,"name":"奥妙净蓝全效水清莲香洗衣液3000ml","bar_code":"6902088707496","price":"39.00","rush_price":"0.00","weight":"3","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":99,"presented_gold":3,"sale_state":1,"deleted_at":null,"updated_at":"2016-12-19 14:27:29","if_rush_to_purchasing":false,"is_frame_product":true}]
     * details : []
     * time_frame : null
     */

    private ProductBean product;

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public static class ProductBean {
        private int id;
        private String name;
        private int score;
        private int onsell;
        private int product_type;
        private Object deleted_at;
        private String updated_at;
        private boolean if_rush_to_purchasing;
        private Object time_frame;
        /**
         * deleted_at : null
         * image_path : http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/15/product_825
         * image_base_name : default_0.jpg
         * updated_at : 2016-10-15 12:00:47
         */

        private List<ImagesBean> images;
        /**
         * id : 372
         * name : 奥妙净蓝全效水清莲香洗衣液3000ml
         * bar_code : 6902088707496
         * price : 39.00
         * rush_price : 0.00
         * weight : 3
         * jd_price : 0.00
         * tb_price : 0.00
         * supper_market_price : 0.00
         * stock : 0
         * integration_price : 0
         * sales_volume : 0
         * stocks : 99
         * presented_gold : 3
         * sale_state : 1
         * deleted_at : null
         * updated_at : 2016-12-19 14:27:29
         * if_rush_to_purchasing : false
         * is_frame_product : true
         */

        private List<SizesBean> sizes;
        private List<?> details;

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

        public int getOnsell() {
            return onsell;
        }

        public void setOnsell(int onsell) {
            this.onsell = onsell;
        }

        public int getProduct_type() {
            return product_type;
        }

        public void setProduct_type(int product_type) {
            this.product_type = product_type;
        }

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public boolean isIf_rush_to_purchasing() {
            return if_rush_to_purchasing;
        }

        public void setIf_rush_to_purchasing(boolean if_rush_to_purchasing) {
            this.if_rush_to_purchasing = if_rush_to_purchasing;
        }

        public Object getTime_frame() {
            return time_frame;
        }

        public void setTime_frame(Object time_frame) {
            this.time_frame = time_frame;
        }

        public List<ImagesBean> getImages() {
            return images;
        }

        public void setImages(List<ImagesBean> images) {
            this.images = images;
        }

        public List<SizesBean> getSizes() {
            return sizes;
        }

        public void setSizes(List<SizesBean> sizes) {
            this.sizes = sizes;
        }

        public List<?> getDetails() {
            return details;
        }

        public void setDetails(List<?> details) {
            this.details = details;
        }

        public static class ImagesBean {
            private Object deleted_at;
            private String image_path;
            private String image_base_name;
            private String updated_at;

            public Object getDeleted_at() {
                return deleted_at;
            }

            public void setDeleted_at(Object deleted_at) {
                this.deleted_at = deleted_at;
            }

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

        public static class SizesBean {
            private int id;
            private String name;
            private String bar_code;
            private String price;
            private String rush_price;
            private String weight;
            private String jd_price;
            private String tb_price;
            private String supper_market_price;
            private int stock;
            private int integration_price;
            private int sales_volume;
            private int stocks;
            private int presented_gold;
            private int sale_state;
            private Object deleted_at;
            private String updated_at;
            private boolean if_rush_to_purchasing;
            private boolean is_frame_product;

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

            public String getRush_price() {
                return rush_price;
            }

            public void setRush_price(String rush_price) {
                this.rush_price = rush_price;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
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

            public int getStocks() {
                return stocks;
            }

            public void setStocks(int stocks) {
                this.stocks = stocks;
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

            public Object getDeleted_at() {
                return deleted_at;
            }

            public void setDeleted_at(Object deleted_at) {
                this.deleted_at = deleted_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public boolean isIf_rush_to_purchasing() {
                return if_rush_to_purchasing;
            }

            public void setIf_rush_to_purchasing(boolean if_rush_to_purchasing) {
                this.if_rush_to_purchasing = if_rush_to_purchasing;
            }

            public boolean isIs_frame_product() {
                return is_frame_product;
            }

            public void setIs_frame_product(boolean is_frame_product) {
                this.is_frame_product = is_frame_product;
            }
        }
    }
}
