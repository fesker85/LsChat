package cc.lzsou.servercore;

import java.security.PublicKey;

/**
 * 服务器地址
 */
public class LinkServer {

    private static final String server_url="http://192.168.31.28:8080/TLink";//服务器地址
    /**
     * 帐号相关
     */
    public static class PassportAction{
        /**
         * 登录地址 POST
         */
        public static final String URL_LOGIN=server_url+"/passport/login";
        /**
         * 短信验证码 POST
         */
        public static final String URL_SMSCODE=server_url+"/passport/smscode";
        /**
         * 注册验证 POST
         */
        public static final String URL_REGISTER_CHECK=server_url+"/passport/registercheck";
        /**
         * 注册 POST
         */
        public static final String URL_REGISTER=server_url+"/passport/registermobile";
    }

    /**
     * 用户相关
     */
    public static class MemberAction{
        /**
         * 获取用户信息 POST
         */
        public static final String URL_MEMBER_INFO=server_url+"/member/info";
        /**
         * 查找用户 POST
         */
        public static final String URL_MEMBER_EXACT=server_url+"/member/exact";
        /**
         * 设置昵称 POST
         */
        public static final String URL_MEMBER_NICKNAME=server_url+"/member/nickname";
        /**
         * 设置帐号 POST
         */
        public static final String URL_MEMBER_USERNAME=server_url+"/member/username";
        /**
         * 设置性别 POST
         */
        public static final String URL_MEMBER_SEX=server_url+"/member/sex";
        /**
         * 设置地区 POST
         */
        public static final String URL_MEMBER_REGION=server_url+"/member/region";
        /**
         * 设置头像 POST
         */
        public static final String URL_MEMBER_AVATAR=server_url+"/member/avatar";
        /**
         * 会员信息 POST
         */
        public static final String URL_MEMBER_VIPINFO=server_url+"/member/vipinfo";
    }

    /**
     * 好友相关
     */
    public static class FriendAction{
        /**
         * 好友列表 POST
         */
        public static final String URL_FRIEND_LIST=server_url+"/friend/list";

        /**
         * 好友列表 POST
         */
        public static final String URL_FRIEND_INFO=server_url+"/friend/info";
    }

    /**
     * 广播相关
     */
    public static class BroadcastAction{
        /**
         * 发送广播 POST
         */
        public static final String URL_BROADCAST_SEND=server_url+"/broadcast/send";
        /**
         * 向所有好友 发送广播 POST
         */
        public static final String URL_BROADCAST_SEND_ALL_FRIEND=server_url+"/broadcast/sendtofriends";

        /**
         * 添加好友 发送广播 POST
         */
        public static final String URL_BROADCAST_SEND_ADD_FRIEND=server_url+"/broadcast/addfriend";
    }

    /**
     * 文章相关
     */
    public static class ArticleAction{
        /**
         * 获取文章分类 POST
         */
        public static final String URL_ARTICLE_CLASS_LIST=server_url+"/article/class/list";
        /**
         * 文章列表 POST
         */
        public static final String URL_ARTICLE_LIST=server_url+"/article/entity/list";
    }

    /**
     * 文件相关
     */
    public static class FileAction{
        /**
         * 图片上传 POST
         */
        public static final String URL_FILE_IMAGE_UPLOAD=server_url+"/file/image/upload";
        /**
         * 视频上传 POST
         */
        public static final String URL_FILE_VIDEO_UPLOAD=server_url+"/file/video/upload";

        /**
         * 获取图片存储路径
         * @param url
         * @return
         */
        public static String getImageAddress(String url){
            return  server_url+"/"+url;
        }
    }

}
