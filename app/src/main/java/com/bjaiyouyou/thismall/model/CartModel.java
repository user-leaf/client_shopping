package com.bjaiyouyou.thismall.model;

/**
 * 购物车接口数据中的CartModel类
 *
 * @author JackB
 * @date 2016/7/17
 */
public class CartModel {
    private long product_id; //,
    private long product_size_id; //,
    private int number; //,
    private String updated_at; //2016-07-09 12:21:14",
    private ProductBean product; //bject{...},
    private ProductSizeBean product_size; //bject{...},
    private ProductImagesBean product_images; //bject{...}

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

    public ProductImagesBean getProduct_images() {
        return product_images;
    }

    public void setProduct_images(ProductImagesBean product_images) {
        this.product_images = product_images;
    }

    @Override
    public String toString() {
        return "CartModel{" +
                "product_id=" + product_id +
                ", product=" + product;
    }

    /**
     * 购物车接口数据中的Product类
     *
     * @author JackB
     * @date 2016/7/17
     */
    public static class ProductBean {

        /**
         * id : 1101
         * name : TCL液晶平板电视
         * score : 0
         * onsell : 0
         * product_type : 1
         * deleted_at :
         * updated_at : 2016-10-25 10:18:08
         */

        private long id;
        private String name;
        private int score;
        private int onsell;
        private int product_type;
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

        @Override
        public String toString() {
            return "[ProductBean] name: " + name;
        }
    }

    /**
     * 购物车接口数据中的ProductSize类
     *
     * @author JackB
     * @date 2016/7/17
     */
    public static class ProductSizeBean {

        /**
         * id : 330
         * name : 多芬密集滋养修护洗发水700ml
         * bar_code : 6902088113846
         * price : 72.00
         * rush_price : 0.00
         * weight : 0.9
         * jd_price : 0.00
         * tb_price : 0.00
         * supper_market_price : 0.00
         * stock : 0
         * integration_price : 0
         * sales_volume : 0
         * stocks : 0
         * presented_gold : 7
         * sale_state : 1
         * deleted_at :
         * updated_at : 2016-10-27 14:08:41
         * product_time_frame : {"id":111,"rush_to_purchase_time_frame_id":3,"product_id":917,"product_size_id":330,"deleted_at":"","created_at":"","updated_at":"","if_rush_to_purchasing":true,"time_frame":{"id":3,"time_frame":"15:00","updated_at":"2016-07-26 02:47:21"}}
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
        /**
         * id : 111
         * rush_to_purchase_time_frame_id : 3
         * product_id : 917
         * product_size_id : 330
         * deleted_at :
         * created_at :
         * updated_at :
         * if_rush_to_purchasing : true
         * time_frame : {"id":3,"time_frame":"15:00","updated_at":"2016-07-26 02:47:21"}
         */

        private ProductTimeFrameBean product_time_frame;

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

        public ProductTimeFrameBean getProduct_time_frame() {
            return product_time_frame;
        }

        public void setProduct_time_frame(ProductTimeFrameBean product_time_frame) {
            this.product_time_frame = product_time_frame;
        }

        public static class ProductTimeFrameBean {
            private long id;
            private long rush_to_purchase_time_frame_id;
            private long product_id;
            private long product_size_id;
            private String deleted_at;
            private String created_at;
            private String updated_at;
            private boolean if_rush_to_purchasing;
            /**
             * id : 3
             * time_frame : 15:00
             * updated_at : 2016-07-26 02:47:21
             */

            private TimeFrameBean time_frame;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getRush_to_purchase_time_frame_id() {
                return rush_to_purchase_time_frame_id;
            }

            public void setRush_to_purchase_time_frame_id(long rush_to_purchase_time_frame_id) {
                this.rush_to_purchase_time_frame_id = rush_to_purchase_time_frame_id;
            }

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

            public String getDeleted_at() {
                return deleted_at;
            }

            public void setDeleted_at(String deleted_at) {
                this.deleted_at = deleted_at;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
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

            public TimeFrameBean getTime_frame() {
                return time_frame;
            }

            public void setTime_frame(TimeFrameBean time_frame) {
                this.time_frame = time_frame;
            }

            public static class TimeFrameBean {
                private long id;
                private String time_frame;
                private String updated_at;

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getTime_frame() {
                    return time_frame;
                }

                public void setTime_frame(String time_frame) {
                    this.time_frame = time_frame;
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

    /**
     * 购物车接口数据中的ProductImageModel类
     *
     * @author JackB
     * @date 2016/7/17
     */
    public static class ProductImagesBean {
        private String deleted_at;
        private String image_path; //http://139.129.167.60/storage/app/private/2016/06/03/product_100",
        private String image_base_name; //iamge_1.jpg",
        private String updated_at; //2016-07-09 09:57:25"

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
}
