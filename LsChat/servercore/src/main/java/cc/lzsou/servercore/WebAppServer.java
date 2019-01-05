package cc.lzsou.servercore;

public class WebAppServer {
    /**
     * 服务器地址
     */
    private static final String server_url="http://192.168.31.28:8080/TApp";
    /**
     * 会员界面
     */
    public static final String WEB_URL_VIP=server_url+"/vip";
    /**
     * 商城界面
     */
    public static final String WEB_URL_STORE=server_url+"/store";

    /**
     * 用户相关
     */
    public static class MemberWeb{
        /**
         * 账单
         */
        public static final String WEB_URL_BILL_LIST=server_url+"/member/bill";
        /**
         * 红包
         */
        public static final String WEB_URL_REDENVELOPE_LIST=server_url+"/member/envelope";
        /**
         * 礼物
         */
        public static final String WEB_URL_GIFT_LIST=server_url+"/member/gift";
        /**
         * 收款
         */
        public static final String WEB_URL_RECEIPT_MONEY=server_url+"/member/receiptmoney";

    }

}
