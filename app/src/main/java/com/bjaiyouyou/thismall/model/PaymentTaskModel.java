package com.bjaiyouyou.thismall.model;

/**
 * Created by Administrator on 2017/4/21.
 */

public class PaymentTaskModel {

    /**
     * id : 2011705040000089911
     * object : order
     * created : 1493889758
     * livemode : true
     * paid : true
     * status : paid
     */

    private String id;
    private String object;
    private int created;
    private boolean livemode;
    private boolean paid;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
