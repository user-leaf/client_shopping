package shop.imake.model;

/**
 * 是否存在未读系统消息
 *author Alice
 *created at 2017/4/11 16:59
 */
public class IsHaveMessageNotRead {

    /**
     * count : 0
     * hasNoRead : 0
     * latestMessage : {"id":"34","title":"顶顶顶","desc":"测试","content":"<p>测试<\/p>","updated_at":"2017-04-14 16:03:16","member_id":"0","time":"16:03:16"}
     */

    private int count;
    private int hasNoRead;
    private LatestMessageBean latestMessage;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getHasNoRead() {
        return hasNoRead;
    }

    public void setHasNoRead(int hasNoRead) {
        this.hasNoRead = hasNoRead;
    }

    public LatestMessageBean getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(LatestMessageBean latestMessage) {
        this.latestMessage = latestMessage;
    }

    public static class LatestMessageBean {
        /**
         * id : 34
         * title : 顶顶顶
         * desc : 测试
         * content : <p>测试</p>
         * updated_at : 2017-04-14 16:03:16
         * member_id : 0
         * time : 16:03:16
         */

        private String id;
        private String title;
        private String desc;
        private String content;
        private String updated_at;
        private String member_id;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
