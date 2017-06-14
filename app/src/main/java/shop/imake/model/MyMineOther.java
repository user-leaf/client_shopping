package shop.imake.model;

import java.util.List;

/**
 *
 * @author Alice
 *Creare 2016/6/7 15:11
 * 个人中心其他服务数据类型类
 *
 */
public class MyMineOther {


    private List<ThreeServicesBean> three_services;

    public List<ThreeServicesBean> getThree_services() {
        return three_services;
    }

    public void setThree_services(List<ThreeServicesBean> three_services) {
        this.three_services = three_services;
    }

    public static class ThreeServicesBean {
        /**
         * id : 3
         * icon : storage/app/public/serviceicon/2017/06/14/app_3/default_0_1497447096.jpg
         * service_name : 博盛租车
         * request_url : https://www.baidu.com
         * is_open : 1
         * deleted_at : null
         * sort : 1
         */

        private int id;
        private String icon;
        private String service_name;
        private String request_url;
        private int is_open;
        private Object deleted_at;
        private int sort;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getRequest_url() {
            return request_url;
        }

        public void setRequest_url(String request_url) {
            this.request_url = request_url;
        }

        public int getIs_open() {
            return is_open;
        }

        public void setIs_open(int is_open) {
            this.is_open = is_open;
        }

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }
}
