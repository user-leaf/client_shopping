package shop.imake.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/9/12.
 */
public class UpdateInfo2 {

    /**
     * id : 27
     * package_name : we
     * package_type : 0
     * is_upgrade : 1
     * package_version : we
     * package_size :
     * package_describe : we
     * package_link : http://localhost/publict
     * created_at : 2016-09-12 10:08:18
     * updated_at : 2016-09-12 10:08:18
     */

    @SerializedName("package")
    private PackageBean packageX;

    public PackageBean getPackageX() {
        return packageX;
    }

    public void setPackageX(PackageBean packageX) {
        this.packageX = packageX;
    }

    public static class PackageBean {
        private long id;
        private String package_name;
        private String package_type;
        private int is_upgrade;
        private String package_version;
        private String package_size;
        private String package_describe;
        private String package_link;
        private String created_at;
        private String updated_at;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }

        public String getPackage_type() {
            return package_type;
        }

        public void setPackage_type(String package_type) {
            this.package_type = package_type;
        }

        public int getIs_upgrade() {
            return is_upgrade;
        }

        public void setIs_upgrade(int is_upgrade) {
            this.is_upgrade = is_upgrade;
        }

        public String getPackage_version() {
            return package_version;
        }

        public void setPackage_version(String package_version) {
            this.package_version = package_version;
        }

        public String getPackage_size() {
            return package_size;
        }

        public void setPackage_size(String package_size) {
            this.package_size = package_size;
        }

        public String getPackage_describe() {
            return package_describe;
        }

        public void setPackage_describe(String package_describe) {
            this.package_describe = package_describe;
        }

        public String getPackage_link() {
            return package_link;
        }

        public void setPackage_link(String package_link) {
            this.package_link = package_link;
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

        @Override
        public String toString() {
            return "PackageBean{" +
                    "id=" + id +
                    ", package_name='" + package_name + '\'' +
                    ", package_type='" + package_type + '\'' +
                    ", is_upgrade=" + is_upgrade +
                    ", package_version='" + package_version + '\'' +
                    ", package_size='" + package_size + '\'' +
                    ", package_describe='" + package_describe + '\'' +
                    ", package_link='" + package_link + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    '}';
        }
    }
}
