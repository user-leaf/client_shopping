package shop.imake.model;

import java.util.List;

/**
 * 历史购买类
 * @author Alice
 *Creare 2016/8/15 15:19
 *
 *
 */
public class HistoryBuy {


    /**
     * total : 7
     * per_page : 6
     * current_page : 1
     * last_page : 2
     * next_page_url : http://testapi.bjaiyouyou.com/api/v1/order/historyBuy?page=2
     * prev_page_url : null
     * from : 1
     * to : 6
     * data : [{"product_id":869,"product_size_id":282,"coupon_id":0,"price":"98.00","number":8,"get_gold":78,"deduct_integration":0,"amount":"784.00","updated_at":"2016-12-16 13:34:07","product":{"id":869,"name":"华格莱26cm复底单篦蒸锅","score":0,"onsell":0,"product_type":1,"deleted_at":null,"updated_at":"2016-12-26 16:12:50"},"product_size":{"id":282,"name":"华格莱26cm复底单篦蒸锅","bar_code":"6941133500880","price":"98.00","rush_price":"0.00","weight":"0","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":0,"presented_gold":9,"sale_state":1,"deleted_at":null,"updated_at":"2016-12-26 16:12:50"},"product_image":{"deleted_at":null,"image_path":"http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/10/product_550","image_base_name":"default_0.jpg","updated_at":"2016-10-10 10:25:37"}},{"product_id":997,"product_size_id":408,"coupon_id":0,"price":"159.90","number":1,"get_gold":15,"deduct_integration":0,"amount":"159.90","updated_at":"2016-12-07 14:13:29","product":{"id":997,"name":"鲁花花生油","score":0,"onsell":1,"product_type":1,"deleted_at":null,"updated_at":"2016-10-15 14:03:28"},"product_size":{"id":408,"name":"鲁花花生油5L","bar_code":"6916168616554","price":"159.90","rush_price":"0.00","weight":"5","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":99,"presented_gold":15,"sale_state":1,"deleted_at":null,"updated_at":"2016-10-25 10:46:04"},"product_image":{"deleted_at":null,"image_path":"http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/15/product_858","image_base_name":"default_0.jpg","updated_at":"2016-10-15 14:02:29"}},{"product_id":1020,"product_size_id":427,"coupon_id":0,"price":"28.00","number":1,"get_gold":2,"deduct_integration":0,"amount":"28.00","updated_at":"2016-12-07 13:56:38","product":{"id":1020,"name":"九三非转基因大豆油1.8L","score":0,"onsell":1,"product_type":1,"deleted_at":null,"updated_at":"2016-10-23 11:19:46"},"product_size":{"id":427,"name":"九三非转基因大豆油1.8L","bar_code":"","price":"28.00","rush_price":"0.00","weight":"1.8","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":99,"presented_gold":2,"sale_state":1,"deleted_at":null,"updated_at":"2016-10-25 09:42:23"},"product_image":{"deleted_at":null,"image_path":"http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/23/product_1073","image_base_name":"default_0.jpg","updated_at":"2016-10-23 11:16:55"}},{"product_id":982,"product_size_id":490,"coupon_id":0,"price":"25.00","number":2,"get_gold":5,"deduct_integration":0,"amount":"50.00","updated_at":"2016-11-23 15:58:46","product":{"id":982,"name":"维达233节卫生卷纸","score":0,"onsell":1,"product_type":1,"deleted_at":null,"updated_at":"2016-10-25 17:27:08"},"product_size":{"id":490,"name":"维达233节卫生卷纸10卷","bar_code":"6939871860717","price":"25.00","rush_price":"0.00","weight":"0","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":99,"presented_gold":2,"sale_state":1,"deleted_at":null,"updated_at":"2016-10-18 09:36:32"},"product_image":{"deleted_at":null,"image_path":"http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/25/product_1096","image_base_name":"default_0.jpg","updated_at":"2016-10-25 17:26:36"}},{"product_id":1102,"product_size_id":562,"coupon_id":0,"price":"3799.00","number":1,"get_gold":379,"deduct_integration":0,"amount":"3799.00","updated_at":"2016-11-23 15:58:46","product":{"id":1102,"name":"TCL液晶平板电视","score":0,"onsell":0,"product_type":1,"deleted_at":null,"updated_at":"2016-12-23 11:28:28"},"product_size":{"id":562,"name":"TCL L55E5800A-UD 55寸高端智能4K内置WIFI液晶平板电视50","bar_code":"","price":"3799.00","rush_price":"0.00","weight":"21","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":97,"presented_gold":379,"sale_state":1,"deleted_at":null,"updated_at":"2016-12-23 11:26:48"},"product_image":{"deleted_at":null,"image_path":"http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/24/product_1088","image_base_name":"default_0.jpg","updated_at":"2016-10-24 13:56:29"}},{"product_id":1103,"product_size_id":564,"coupon_id":0,"price":"25.00","number":1,"get_gold":2,"deduct_integration":0,"amount":"25.00","updated_at":"2016-12-07 13:56:38","product":{"id":1103,"name":"维达全新一代柔韧升级","score":0,"onsell":1,"product_type":1,"deleted_at":null,"updated_at":"2016-10-25 17:11:58"},"product_size":{"id":564,"name":"维达蓝色经典卷纸 220节/卷*10卷","bar_code":"6939871860861","price":"25.00","rush_price":"0.00","weight":"0","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":99,"presented_gold":2,"sale_state":1,"deleted_at":null,"updated_at":null},"product_image":{"deleted_at":null,"image_path":"http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/25/product_1095","image_base_name":"default_0.jpg","updated_at":"2016-10-25 17:06:49"}}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private String next_page_url;
    private Object prev_page_url;
    private int from;
    private int to;
    /**
     * product_id : 869
     * product_size_id : 282
     * coupon_id : 0
     * price : 98.00
     * number : 8
     * get_gold : 78
     * deduct_integration : 0
     * amount : 784.00
     * updated_at : 2016-12-16 13:34:07
     * product : {"id":869,"name":"华格莱26cm复底单篦蒸锅","score":0,"onsell":0,"product_type":1,"deleted_at":null,"updated_at":"2016-12-26 16:12:50"}
     * product_size : {"id":282,"name":"华格莱26cm复底单篦蒸锅","bar_code":"6941133500880","price":"98.00","rush_price":"0.00","weight":"0","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":0,"presented_gold":9,"sale_state":1,"deleted_at":null,"updated_at":"2016-12-26 16:12:50"}
     * product_image : {"deleted_at":null,"image_path":"http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/10/product_550","image_base_name":"default_0.jpg","updated_at":"2016-10-10 10:25:37"}
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

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
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
        private int product_id;
        private int product_size_id;
        private int coupon_id;
        private String price;
        private int number;
        private int get_gold;
        private int deduct_integration;
        private String amount;
        private String updated_at;
        /**
         * id : 869
         * name : 华格莱26cm复底单篦蒸锅
         * score : 0
         * onsell : 0
         * product_type : 1
         * deleted_at : null
         * updated_at : 2016-12-26 16:12:50
         */

        private ProductBean product;
        /**
         * id : 282
         * name : 华格莱26cm复底单篦蒸锅
         * bar_code : 6941133500880
         * price : 98.00
         * rush_price : 0.00
         * weight : 0
         * jd_price : 0.00
         * tb_price : 0.00
         * supper_market_price : 0.00
         * stock : 0
         * integration_price : 0
         * sales_volume : 0
         * stocks : 0
         * presented_gold : 9
         * sale_state : 1
         * deleted_at : null
         * updated_at : 2016-12-26 16:12:50
         */

        private ProductSizeBean product_size;
        /**
         * deleted_at : null
         * image_path : http://testapi.bjaiyouyou.com/storage/app/public/product/2016/10/10/product_550
         * image_base_name : default_0.jpg
         * updated_at : 2016-10-10 10:25:37
         */

        private ProductImageBean product_image;

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

        public int getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(int coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getGet_gold() {
            return get_gold;
        }

        public void setGet_gold(int get_gold) {
            this.get_gold = get_gold;
        }

        public int getDeduct_integration() {
            return deduct_integration;
        }

        public void setDeduct_integration(int deduct_integration) {
            this.deduct_integration = deduct_integration;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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

        public ProductSizeBean getProduct_size() {
            return product_size;
        }

        public void setProduct_size(ProductSizeBean product_size) {
            this.product_size = product_size;
        }

        public ProductImageBean getProduct_image() {
            return product_image;
        }

        public void setProduct_image(ProductImageBean product_image) {
            this.product_image = product_image;
        }

        public static class ProductBean {
            private int id;
            private String name;
            private int score;
            private int onsell;
            private int product_type;
            private Object deleted_at;
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
        }

        public static class ProductSizeBean {
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
        }

        public static class ProductImageBean {
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
    }
}
