package com.bjaiyouyou.thismall.model;

/**
 * 
 * @author Alice
 *Creare 2016/6/22 16:44
 * 用户邀请好友类
 * 
 */
public class MineFriend {
    private String friendId;
    private String friendName;

    public MineFriend(String friendName, String friendId) {
        this.friendName = friendName;
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
