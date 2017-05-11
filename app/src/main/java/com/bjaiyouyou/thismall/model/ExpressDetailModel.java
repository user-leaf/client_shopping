package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 物流信息
 *
 * User: JackB
 * Date: 2016/10/25
 */
public class ExpressDetailModel {

    /**
     * mailno : 3101128521680
     * result : true
     * remark : 运输中
     * status : signed
     * weight : []
     * traces : {"trace":[{"time":"2016-10-25 15:40:51","station":"黑龙江七台河公司","status":"signed","remark":"快件已被 已签收 签收"},{"time":"2016-10-25 09:30:08","station":"黑龙江七台河公司","status":"deliver","remark":"进行派件扫描；派送业务员：怡安；联系电话：18045788985"},{"time":"2016-10-25 07:54:42","station":"黑龙江七台河公司","status":"arrived","remark":"到达目的地网点，快件将很快进行派送"},{"time":"2016-10-24 20:58:02","station":"黑龙江哈尔滨分拨中心","status":"out","remark":"从站点发出，本次转运目的地：黑龙江七台河公司"},{"time":"2016-10-24 20:50:19","station":"黑龙江哈尔滨分拨中心","status":"in","remark":"在分拨中心进行卸车扫描"},{"time":"2016-10-23 23:20:01","station":"北京分拨中心","status":"out","remark":"进行装车扫描，即将发往：黑龙江哈尔滨分拨中心"},{"time":"2016-10-23 23:17:01","station":"北京分拨中心","status":"weight","remark":"在分拨中心进行称重扫描"},{"time":"2016-10-23 18:48:14","station":"北京东燕郊公司","status":"got","remark":"进行揽件扫描"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String mailno;
        private boolean result;
        private String remark;
        private TracesBean traces;

        public String getMailno() {
            return mailno;
        }

        public void setMailno(String mailno) {
            this.mailno = mailno;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public TracesBean getTraces() {
            return traces;
        }

        public void setTraces(TracesBean traces) {
            this.traces = traces;
        }

        public static class TracesBean {
            /**
             * time : 2016-10-25 15:40:51
             * station : 黑龙江七台河公司
             * status : signed
             * remark : 快件已被 已签收 签收
             */

            private List<TraceBean> trace;

            public List<TraceBean> getTrace() {
                return trace;
            }

            public void setTrace(List<TraceBean> trace) {
                this.trace = trace;
            }

            public static class TraceBean {
                private String time;
                private String station;
                private String status;
                private String remark;

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getStation() {
                    return station;
                }

                public void setStation(String station) {
                    this.station = station;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }
            }
        }
    }
}
