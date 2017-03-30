package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 分类页商品数据格式
 *
 * Created by Administrator on 2017/3/29.
 */
public class ClassifyProductModel {

    /**
     * total : 15
     * per_page : 6
     * current_page : 1
     * last_page : 3
     * next_page_url : https://testapi2.bjaiyouyou.com/api/v1/product/recommendProduct?page=2
     * prev_page_url :
     * from : 1
     * to : 6
     * data : [{"id":1116,"name":"大铁勺测试测试测试测试测试测试测试测试测试测试测试测试测试测试","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":"","updated_at":"2017-03-17 14:16:57","recommend":1,"choiceness":0,"product":{"id":1116,"name":"大铁勺测试测试测试测试测试测试测试测试测试测试测试测试测试测试","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":"","updated_at":"2017-03-17 14:16:57","recommend":1,"choiceness":0},"image":{"deleted_at":"","image_path":"https://testapi2.bjaiyouyou.com/storage/app/public/product/2017/03/17/product_1166","image_base_name":"default_0_1489731398.jpeg","updated_at":"2017-03-17 14:16:38"},"size":{"id":580,"name":"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试","bar_code":"123","price":"123.00","rush_price":"123.00","weight":"123","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":122,"presented_gold":12,"sale_state":1,"deleted_at":"","updated_at":"2017-03-17 14:37:28"}},{"id":1115,"name":"16号测试商品","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2017-03-16 15:36:15","recommend":1,"choiceness":0,"product":{"id":1115,"name":"16号测试商品","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2017-03-16 15:36:15","recommend":1,"choiceness":0},"image":{"deleted_at":null,"image_path":"https://testapi2.bjaiyouyou.com/storage/app/public/product/2017/03/16/product_1163","image_base_name":"default_0_1489649756.jpg","updated_at":"2017-03-16 15:35:56"},"size":{"id":579,"name":"大号的","bar_code":"1","price":"1.00","rush_price":"1.00","weight":"1","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":11,"presented_gold":0,"sale_state":1,"deleted_at":null,"updated_at":null}},{"id":1112,"name":"美国汇丰牌是拉差辣椒酱","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2016-12-21 17:47:45","recommend":1,"choiceness":0,"product":{"id":1112,"name":"美国汇丰牌是拉差辣椒酱","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2016-12-21 17:47:45","recommend":1,"choiceness":0},"image":{"deleted_at":null,"image_path":"https://testapi2.bjaiyouyou.com/storage/app/public/product/2016/10/27/product_1145","image_base_name":"default_0.jpg","updated_at":"2016-10-27 16:07:01"},"size":{"id":573,"name":"美国汇丰牌是拉差辣椒酱255g/瓶","bar_code":"","price":"28.00","rush_price":"0.00","weight":"0.255","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":39,"presented_gold":2,"sale_state":1,"deleted_at":null,"updated_at":"2017-03-08 14:50:08"}},{"id":1111,"name":"艾叶复方精油123","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2016-10-27 14:34:00","recommend":1,"choiceness":0,"product":{"id":1111,"name":"艾叶复方精油123","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2016-10-27 14:34:00","recommend":1,"choiceness":0},"image":{"deleted_at":null,"image_path":"https://testapi2.bjaiyouyou.com/storage/app/public/product/2016/10/27/product_1136","image_base_name":"default_0.jpg","updated_at":"2016-10-27 14:24:20"},"size":{"id":572,"name":"艾叶复方精油10ml/瓶 *3瓶","bar_code":"","price":"128.00","rush_price":"0.00","weight":"0","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":46,"presented_gold":12,"sale_state":1,"deleted_at":null,"updated_at":"2017-03-28 17:12:22"}},{"id":1104,"name":"火系列艾饼","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2016-10-27 14:33:57","recommend":1,"choiceness":0,"product":{"id":1104,"name":"火系列艾饼","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2016-10-27 14:33:57","recommend":1,"choiceness":0},"image":{"deleted_at":null,"image_path":"https://testapi2.bjaiyouyou.com/storage/app/public/product/2016/10/27/product_1097","image_base_name":"default_0.jpg","updated_at":"2016-10-27 13:33:25"},"size":{"id":565,"name":"火系列艾饼","bar_code":"","price":"150.00","rush_price":"0.00","weight":"0","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":61,"presented_gold":15,"sale_state":1,"deleted_at":null,"updated_at":"2017-03-28 16:27:08"}},{"id":1105,"name":"金系列艾饼","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2016-10-27 14:33:54","recommend":1,"choiceness":0,"product":{"id":1105,"name":"金系列艾饼","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":null,"updated_at":"2016-10-27 14:33:54","recommend":1,"choiceness":0},"image":{"deleted_at":null,"image_path":"https://testapi2.bjaiyouyou.com/storage/app/public/product/2016/10/27/product_1104","image_base_name":"default_0.jpg","updated_at":"2016-10-27 13:43:45"},"size":{"id":566,"name":"金系列艾饼","bar_code":"","price":"180.00","rush_price":"0.00","weight":"0","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":52,"presented_gold":18,"sale_state":1,"deleted_at":null,"updated_at":"2017-02-23 18:49:45"}}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private String next_page_url;
    private String prev_page_url;
    private int from;
    private int to;
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

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
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
        /**
         * id : 1116
         * name : 大铁勺测试测试测试测试测试测试测试测试测试测试测试测试测试测试
         * score : 0
         * onsell : 1
         * shop_id : 0
         * product_type : 1
         * deleted_at :
         * updated_at : 2017-03-17 14:16:57
         * recommend : 1
         * choiceness : 0
         * product : {"id":1116,"name":"大铁勺测试测试测试测试测试测试测试测试测试测试测试测试测试测试","score":0,"onsell":1,"shop_id":0,"product_type":1,"deleted_at":"","updated_at":"2017-03-17 14:16:57","recommend":1,"choiceness":0}
         * image : {"deleted_at":"","image_path":"https://testapi2.bjaiyouyou.com/storage/app/public/product/2017/03/17/product_1166","image_base_name":"default_0_1489731398.jpeg","updated_at":"2017-03-17 14:16:38"}
         * size : {"id":580,"name":"测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试","bar_code":"123","price":"123.00","rush_price":"123.00","weight":"123","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"stocks":122,"presented_gold":12,"sale_state":1,"deleted_at":"","updated_at":"2017-03-17 14:37:28"}
         */

        private long id;
        private String name;
        private int score;
        private int onsell;
        private int shop_id;
        private int product_type;
        private String deleted_at;
        private String updated_at;
        private int recommend;
        private int choiceness;
        private ProductBean product;
        private ImageBean image;
        private SizeBean size;

        public long getId() {
            return id;
        }

        public void setId(long id) {
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

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getProduct_type() {
            return product_type;
        }

        public void setProduct_type(int product_type) {
            this.product_type = product_type;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getRecommend() {
            return recommend;
        }

        public void setRecommend(int recommend) {
            this.recommend = recommend;
        }

        public int getChoiceness() {
            return choiceness;
        }

        public void setChoiceness(int choiceness) {
            this.choiceness = choiceness;
        }

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public SizeBean getSize() {
            return size;
        }

        public void setSize(SizeBean size) {
            this.size = size;
        }

        public static class ProductBean {
            /**
             * id : 1116
             * name : 大铁勺测试测试测试测试测试测试测试测试测试测试测试测试测试测试
             * score : 0
             * onsell : 1
             * shop_id : 0
             * product_type : 1
             * deleted_at :
             * updated_at : 2017-03-17 14:16:57
             * recommend : 1
             * choiceness : 0
             */

            private long id;
            private String name;
            private int score;
            private int onsell;
            private long shop_id;
            private int product_type;
            private String deleted_at;
            private String updated_at;
            private int recommend;
            private int choiceness;

            public long getId() {
                return id;
            }

            public void setId(long id) {
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

            public long getShop_id() {
                return shop_id;
            }

            public void setShop_id(long shop_id) {
                this.shop_id = shop_id;
            }

            public int getProduct_type() {
                return product_type;
            }

            public void setProduct_type(int product_type) {
                this.product_type = product_type;
            }

            public String getDeleted_at() {
                return deleted_at;
            }

            public void setDeleted_at(String deleted_at) {
                this.deleted_at = deleted_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public int getRecommend() {
                return recommend;
            }

            public void setRecommend(int recommend) {
                this.recommend = recommend;
            }

            public int getChoiceness() {
                return choiceness;
            }

            public void setChoiceness(int choiceness) {
                this.choiceness = choiceness;
            }
        }

        public static class ImageBean {
            /**
             * deleted_at :
             * image_path : https://testapi2.bjaiyouyou.com/storage/app/public/product/2017/03/17/product_1166
             * image_base_name : default_0_1489731398.jpeg
             * updated_at : 2017-03-17 14:16:38
             */

            private String deleted_at;
            private String image_path;
            private String image_base_name;
            private String updated_at;

            public String getDeleted_at() {
                return deleted_at;
            }

            public void setDeleted_at(String deleted_at) {
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

        public static class SizeBean {
            /**
             * id : 580
             * name : 测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试
             * bar_code : 123
             * price : 123.00
             * rush_price : 123.00
             * weight : 123
             * jd_price : 0.00
             * tb_price : 0.00
             * supper_market_price : 0.00
             * stock : 0
             * integration_price : 0
             * sales_volume : 0
             * stocks : 122
             * presented_gold : 12
             * sale_state : 1
             * deleted_at :
             * updated_at : 2017-03-17 14:37:28
             */

            private long id;
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
            private String deleted_at;
            private String updated_at;

            public long getId() {
                return id;
            }

            public void setId(long id) {
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

            public String getDeleted_at() {
                return deleted_at;
            }

            public void setDeleted_at(String deleted_at) {
                this.deleted_at = deleted_at;
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
