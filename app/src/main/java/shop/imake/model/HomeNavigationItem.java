package shop.imake.model;

import java.util.List;

/**
 *
 * @author Alice
 *Creare 2016/7/3 17:55
 * 首页导航页面简介类
 *
 */
public class HomeNavigationItem {

    /**
     * total : 15
     * per_page : 5
     * current_page : 1
     * last_page : 3
     * next_page_url : http://139.129.167.60/api/v1/product/allRushToPurchase?page=2
     * prev_page_url : null
     * from : 1
     * to : 5
     * data : [{"id":1,"name":"产品_0","price":860.9,"score":4,"product_type":0,"updated_at":"2016-07-09 09:57:25","image":{"image_path":"http://139.129.167.60/storage/app/private/2016/06/03/product_100","image_base_name":"iamge_1.jpg","updated_at":"2016-07-09 09:57:25"},"size":{"id":1,"name":"规格_0","price":964.91,"jd_price":965.91,"tb_price":963.91,"supper_market_price":962.91,"stock":8,"updated_at":"2016-07-09 09:57:25"}},{"id":5,"name":"产品_4","price":247.41,"score":4,"product_type":0,"updated_at":"2016-07-09 09:57:25","image":{"image_path":"http://139.129.167.60/storage/app/private/2016/06/03/product_100","image_base_name":"iamge_1.jpg","updated_at":"2016-07-09 09:57:25"},"size":{"id":13,"name":"规格_0","price":304.32,"jd_price":305.32,"tb_price":303.32,"supper_market_price":302.32,"stock":12,"updated_at":"2016-07-09 09:57:25"}},{"id":6,"name":"产品_5","price":344.82,"score":5,"product_type":0,"updated_at":"2016-07-09 09:57:25","image":{"image_path":"http://139.129.167.60/storage/app/private/2016/06/03/product_100","image_base_name":"iamge_1.jpg","updated_at":"2016-07-09 09:57:25"},"size":{"id":15,"name":"规格_0","price":550.62,"jd_price":551.62,"tb_price":549.62,"supper_market_price":548.62,"stock":0,"updated_at":"2016-07-09 09:57:25"}},{"id":9,"name":"产品_8","price":129.43,"score":1,"product_type":0,"updated_at":"2016-07-09 09:57:25","image":{"image_path":"http://139.129.167.60/storage/app/private/2016/06/03/product_100","image_base_name":"iamge_1.jpg","updated_at":"2016-07-09 09:57:25"},"size":{"id":25,"name":"规格_0","price":140.22,"jd_price":141.22,"tb_price":139.22,"supper_market_price":138.22,"stock":41,"updated_at":"2016-07-09 09:57:25"}},{"id":12,"name":"产品_11","price":825.45,"score":4,"product_type":0,"updated_at":"2016-07-09 09:57:25","image":{"image_path":"http://139.129.167.60/storage/app/private/2016/06/03/product_100","image_base_name":"iamge_1.jpg","updated_at":"2016-07-09 09:57:25"},"size":{"id":35,"name":"规格_0","price":527.09,"jd_price":528.09,"tb_price":526.09,"supper_market_price":525.09,"stock":13,"updated_at":"2016-07-09 09:57:25"}}]
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
     * id : 1
     * name : 产品_0
     * price : 860.9
     * score : 4
     * product_type : 0
     * updated_at : 2016-07-09 09:57:25
     * image : {"image_path":"http://139.129.167.60/storage/app/private/2016/06/03/product_100","image_base_name":"iamge_1.jpg","updated_at":"2016-07-09 09:57:25"}
     * size : {"id":1,"name":"规格_0","price":964.91,"jd_price":965.91,"tb_price":963.91,"supper_market_price":962.91,"stock":8,"updated_at":"2016-07-09 09:57:25"}
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
        private int id;
        private String name;
        private double price;
        private int score;
        private int product_type;
        private String updated_at;
        /**
         * image_path : http://139.129.167.60/storage/app/private/2016/06/03/product_100
         * image_base_name : iamge_1.jpg
         * updated_at : 2016-07-09 09:57:25
         */

        private ImageBean image;
        /**
         * id : 1
         * name : 规格_0
         * price : 964.91
         * jd_price : 965.91
         * tb_price : 963.91
         * supper_market_price : 962.91
         * stock : 8
         * updated_at : 2016-07-09 09:57:25
         */

        private SizeBean size;

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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
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

        public static class SizeBean {
            private int id;
            private String name;
            private double price;
            private double jd_price;
            private double tb_price;
            private double supper_market_price;
            private int stock;
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

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getJd_price() {
                return jd_price;
            }

            public void setJd_price(double jd_price) {
                this.jd_price = jd_price;
            }

            public double getTb_price() {
                return tb_price;
            }

            public void setTb_price(double tb_price) {
                this.tb_price = tb_price;
            }

            public double getSupper_market_price() {
                return supper_market_price;
            }

            public void setSupper_market_price(double supper_market_price) {
                this.supper_market_price = supper_market_price;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
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
