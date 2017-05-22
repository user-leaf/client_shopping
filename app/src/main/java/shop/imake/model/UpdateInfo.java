package shop.imake.model;

/**
 * 升级数据类
 *
 * User: JackB
 * Date: 2016/9/10
 */
public class UpdateInfo {
    private String version;
    private boolean isQ; // 是否强制升级
    private String description;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isQ() {
        return isQ;
    }

    public void setQ(boolean q) {
        isQ = q;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
