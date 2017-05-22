package shop.imake.model;

import java.util.List;

/**
 * 订单类
 * @author Alice
 */

public class MyOrder {

    /**
     * total : 1
     * per_page : 6
     * current_page : 1
     * last_page : 1
     * next_page_url : null
     * prev_page_url : null
     * from : 1
     * to : 1
     * data : [{"pingpp_charge_id":"ch_bT4anLejPmX9TW9uDCTmLWXH","pingpp_refund_id":"","order_number":"2016091851995154","address":"北京市北京市东城区tyg","phone":"12345678901","addressee":"tyu","amount":0.01,"postage":20,"all_amount":20.01,"waybill_number":"","express_company_id":0,"number":1,"get_gold":0,"deduct_integration":0,"coupon_id":0,"payment_channel":"wx","payment_state":1,"express_state":2,"order_state":1,"buyer_message":"","refund_type":0,"refund_description":"","created_at":"2016-09-18 19:03:47","updated_at":"2016-09-18 19:04:34","show_state":5,"show_state_msg":"确认收货","order_detail":[{"product_id":805,"product_size_id":164,"coupon_id":0,"price":"0.01","number":1,"get_gold":0,"deduct_integration":0,"amount":"0.01","updated_at":"2016-09-18 19:03:47","product":{"id":805,"name":"测试商品","score":0,"product_type":1,"updated_at":"2016-09-12 15:15:43"},"product_size":{"id":164,"name":"一杯水","bar_code":"","price":"0.01","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"presented_gold":0,"sale_state":1,"updated_at":"2016-09-12 15:16:17"},"product_image":{"image_path":"http://139.129.167.60/storage/app/public/product/2016/09/12/product_200","image_base_name":"default_0.JPG","updated_at":"2016-09-12 15:16:33"}}]}]
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
     * pingpp_charge_id : ch_bT4anLejPmX9TW9uDCTmLWXH
     * pingpp_refund_id :
     * order_number : 2016091851995154
     * address : 北京市北京市东城区tyg
     * phone : 12345678901
     * addressee : tyu
     * amount : 0.01
     * postage : 20
     * all_amount : 20.01
     * waybill_number :
     * express_company_id : 0
     * number : 1
     * get_gold : 0
     * deduct_integration : 0
     * coupon_id : 0
     * payment_channel : wx
     * payment_state : 1
     * express_state : 2
     * order_state : 1
     * buyer_message :
     * refund_type : 0
     * refund_description :
     * created_at : 2016-09-18 19:03:47
     * updated_at : 2016-09-18 19:04:34
     * show_state : 5
     * show_state_msg : 确认收货
     * order_detail : [{"product_id":805,"product_size_id":164,"coupon_id":0,"price":"0.01","number":1,"get_gold":0,"deduct_integration":0,"amount":"0.01","updated_at":"2016-09-18 19:03:47","product":{"id":805,"name":"测试商品","score":0,"product_type":1,"updated_at":"2016-09-12 15:15:43"},"product_size":{"id":164,"name":"一杯水","bar_code":"","price":"0.01","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"presented_gold":0,"sale_state":1,"updated_at":"2016-09-12 15:16:17"},"product_image":{"image_path":"http://139.129.167.60/storage/app/public/product/2016/09/12/product_200","image_base_name":"default_0.JPG","updated_at":"2016-09-12 15:16:33"}}]
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
        private String pingpp_charge_id;
        private String pingpp_refund_id;
        private String order_number;
        private String address;
        private String phone;
        private String addressee;
        private double amount;
        private int postage;
        private double all_amount;
        private String waybill_number;
        private int express_company_id;
        private int number;
        private int get_gold;
        private int deduct_integration;
        private int coupon_id;
        private String payment_channel;
        private int payment_state;
        private int express_state;
        private int order_state;
        private String buyer_message;
        private int refund_type;
        private String refund_description;
        private String created_at;
        private String updated_at;
        private int show_state;
        private String show_state_msg;
        /**
         * product_id : 805
         * product_size_id : 164
         * coupon_id : 0
         * price : 0.01
         * number : 1
         * get_gold : 0
         * deduct_integration : 0
         * amount : 0.01
         * updated_at : 2016-09-18 19:03:47
         * product : {"id":805,"name":"测试商品","score":0,"product_type":1,"updated_at":"2016-09-12 15:15:43"}
         * product_size : {"id":164,"name":"一杯水","bar_code":"","price":"0.01","jd_price":"0.00","tb_price":"0.00","supper_market_price":"0.00","stock":0,"integration_price":0,"sales_volume":0,"presented_gold":0,"sale_state":1,"updated_at":"2016-09-12 15:16:17"}
         * product_image : {"image_path":"http://139.129.167.60/storage/app/public/product/2016/09/12/product_200","image_base_name":"default_0.JPG","updated_at":"2016-09-12 15:16:33"}
         */

        private List<OrderDetailBean> order_detail;

        public String getPingpp_charge_id() {
            return pingpp_charge_id;
        }

        public void setPingpp_charge_id(String pingpp_charge_id) {
            this.pingpp_charge_id = pingpp_charge_id;
        }

        public String getPingpp_refund_id() {
            return pingpp_refund_id;
        }

        public void setPingpp_refund_id(String pingpp_refund_id) {
            this.pingpp_refund_id = pingpp_refund_id;
        }

        public String getOrder_number() {
            return order_number;
        }

        public void setOrder_number(String order_number) {
            this.order_number = order_number;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddressee() {
            return addressee;
        }

        public void setAddressee(String addressee) {
            this.addressee = addressee;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getPostage() {
            return postage;
        }

        public void setPostage(int postage) {
            this.postage = postage;
        }

        public double getAll_amount() {
            return all_amount;
        }

        public void setAll_amount(double all_amount) {
            this.all_amount = all_amount;
        }

        public String getWaybill_number() {
            return waybill_number;
        }

        public void setWaybill_number(String waybill_number) {
            this.waybill_number = waybill_number;
        }

        public int getExpress_company_id() {
            return express_company_id;
        }

        public void setExpress_company_id(int express_company_id) {
            this.express_company_id = express_company_id;
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

        public int getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(int coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getPayment_channel() {
            return payment_channel;
        }

        public void setPayment_channel(String payment_channel) {
            this.payment_channel = payment_channel;
        }

        public int getPayment_state() {
            return payment_state;
        }

        public void setPayment_state(int payment_state) {
            this.payment_state = payment_state;
        }

        public int getExpress_state() {
            return express_state;
        }

        public void setExpress_state(int express_state) {
            this.express_state = express_state;
        }

        public int getOrder_state() {
            return order_state;
        }

        public void setOrder_state(int order_state) {
            this.order_state = order_state;
        }

        public String getBuyer_message() {
            return buyer_message;
        }

        public void setBuyer_message(String buyer_message) {
            this.buyer_message = buyer_message;
        }

        public int getRefund_type() {
            return refund_type;
        }

        public void setRefund_type(int refund_type) {
            this.refund_type = refund_type;
        }

        public String getRefund_description() {
            return refund_description;
        }

        public void setRefund_description(String refund_description) {
            this.refund_description = refund_description;
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

        public int getShow_state() {
            return show_state;
        }

        public void setShow_state(int show_state) {
            this.show_state = show_state;
        }

        public String getShow_state_msg() {
            return show_state_msg;
        }

        public void setShow_state_msg(String show_state_msg) {
            this.show_state_msg = show_state_msg;
        }

        public List<OrderDetailBean> getOrder_detail() {
            return order_detail;
        }

        public void setOrder_detail(List<OrderDetailBean> order_detail) {
            this.order_detail = order_detail;
        }

        public static class OrderDetailBean {
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
             * id : 805
             * name : 测试商品
             * score : 0
             * product_type : 1
             * updated_at : 2016-09-12 15:15:43
             */

            private ProductBean product;
            /**
             * id : 164
             * name : 一杯水
             * bar_code :
             * price : 0.01
             * jd_price : 0.00
             * tb_price : 0.00
             * supper_market_price : 0.00
             * stock : 0
             * integration_price : 0
             * sales_volume : 0
             * presented_gold : 0
             * sale_state : 1
             * updated_at : 2016-09-12 15:16:17
             */

            private ProductSizeBean product_size;
            /**
             * image_path : http://139.129.167.60/storage/app/public/product/2016/09/12/product_200
             * image_base_name : default_0.JPG
             * updated_at : 2016-09-12 15:16:33
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

            public static class ProductSizeBean {
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

            public static class ProductImageBean {
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
}