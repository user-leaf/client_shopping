package shop.imake.utils;

import android.Manifest;
import android.app.Activity;
import android.media.Ringtone;
import android.widget.Toast;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.OnUnreadmsgListener;
import cn.xiaoneng.uiapi.XNErrorListener;
import shop.imake.MainApplication;
import shop.imake.model.User;
import shop.imake.user.CurrentUserManager;

/**
 * 小能在线客服
 * Created by SONY on 2017/6/27.
 */
public class NTalkerUtils implements OnUnreadmsgListener, XNErrorListener {
    public static final String TAG = NTalkerUtils.class.getSimpleName();

    private static NTalkerUtils instance = new NTalkerUtils();

    private int startChat = 0;
    // 用户
    private String userid = "";     // 客户唯一标识，切忌不同客户使用同一个userid，未登录访客请传"",不能传除 . - @ 三个之外的特殊字符
    private String username = "";   // 顾客名字
    private String settingid1 = "kf_9003_1499140862639";    // 客服组id示例kf_9979_1452750735837,kf_9979_1452750735837
    private String groupName = "";  // 客服组默认名称
    private int userlevel = 0;      // 用户级别,级别分为1-5,5为最高级,默认为0 非会员
    // 聊天信息实例
    private ChatParamsBody chatparams = null;
    private Ringtone ringtonenotification;
    private Activity activity;

    private NTalkerUtils() {
    }

    public static NTalkerUtils getInstance() {
        return instance;
    }

    /**
     * 如果商城处于登录状态就调用小能login，如果商城未登录就不调用小能login
     */
    public void login() {
        User.MemberBean currentUser = CurrentUserManager.getCurrentUser();
        if (currentUser != null) {
            userid = String.valueOf(currentUser.getId());
            username = currentUser.getNick_name();
            Ntalker.getBaseInstance().login(userid, username, userlevel);
        }
    }

    public void startChat(Activity activity) {
        this.activity = activity;

        setNtalkerListener();
        requestPermissions();   // 适配Android6.0权限

        // 开启会话
        Ntalker.getBaseInstance().startChat(MainApplication.getContext(), settingid1, groupName, chatparams);
    }

    private void setNtalkerListener() {
        Ntalker.getExtendInstance().message().setOnUnreadmsgListener(this);
        Ntalker.getBaseInstance().setOnErrorListener(this);
    }

    private void requestPermissions() {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
        Ntalker.getExtendInstance().ntalkerSystem().requestPermissions(activity, permissions);
    }

    @Override
    public void onUnReadMsg(final String settingid, String username, final String msgcontent, final int messagecount) {
        // XNSDKListener 方法2/6 未读聊天消息监听
    }

    @Override
    public void onErrorCode(int errorcode) {
        Toast.makeText(MainApplication.getContext(), "发生错误" + errorcode, Toast.LENGTH_SHORT).show();
    }
}
