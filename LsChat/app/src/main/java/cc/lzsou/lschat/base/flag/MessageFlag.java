package cc.lzsou.lschat.base.flag;

/**
 * 消息标记
 */
public class MessageFlag {

    public static final String MESSAGE_FLAG_NOTICE = "[EAC0BE58AA7A44F59F1675B5E6NOTICE,";//通知 eg.[EAC0BE58AA7A44F59F1675B5E6NOTICE,请假通知,重要信息]

    public static final String MESSAGE_FLAG_FRIEND_ADD = "[EAC0BE58AA7A44F59F1675B5E6AE7ADD,";//添加好友 eg.[EAC0BE58AA7A44F59F1675B5E6AE7ADD,][from][to][msg][from对to的remark]
    public static final String MESSAGE_FLAG_FRIEND_AGREE = "[EAC0BE58AA7A44F59F1675B5E6AAGREE,";//同意添加 eg.[EAC0BE58AA7A44F59F1675B5E6AAGREE,][from][to][msg][from对to的remark]
    public static final String MESSAGE_FLAG_FRIEND_REFUSE = "[EAC0BE58AA7A44F59F1675B5E6AREFUSE,";//拒绝添加 eg.[EAC0BE58AA7A44F59F1675B5E6AREFUSE,验证信息]
    public static final String MESSAGE_FLAG_FRIEND_INFOCHANGE = "[EAC0BE58AA7A44F59F1675BINFOCHANGE,";//好友信息变更 eg[EAC0BE58AA7A44F59F1675BINFOCHANGE,][friendid]

    /**
     * 图片标记
     * 标记 [EAC0BE58AA7A44F59F1675B5E6ARIMAGE,][base64String][serverpath]
     * 发送中或失败 [EAC0BE58AA7A44F59F1675B5E6ARIMAGE,][base64String][localpath]
     */
    public static final String MESSAGE_FLAG_IMAGE = "[EAC0BE58AA7A44F59F1675B5E6ARIMAGE,";
    /**
     * 语音标记
     * [EAC0BE58AA7A44F59F1675B5E6ASOUND,][时长][base64String]
     */
    public static final String MESSAGE_FLAG_VOICE = "[EAC0BE58AA7A44F59F1675B5E6ASOUND,";
    /**
     * 视频标记
     * [EAC0BE58AA7A44F59F1675B5E6AVIDEO,][时长][base64String][serverpath]
     * 发送中或失败 [EAC0BE58AA7A44F59F1675B5E6ARIMAGE,][时长][base64String][localpath]
     */
    public static final String MESSAGE_FLAG_VIDEO = "[EAC0BE58AA7A44F59F1675B5E6AVIDEO,";
    /**
     * gif动图
     */
    public static final String MESSAGE_FLAG_GIFIMAGE = "[/g";//gif图片 eg.[/g001] 系统给定 用户可以手动输入
    /**
     * 表情
     */
    public static final String MESSAGE_FLAG_EXPRESSION = "[/f";//表情 eg.[/f000] 系统给定 用户可以手动输入
    /**
     * 位置标记
     * [EAC0BE58AA7A44F59F1675B5LOCATION,][名称][地址][lat,lng][base64String]
     */
    public static final String MESSAGE_FLAG_LOCATION = "[EAC0BE58AA7A44F59F1675B5LOCATION,";
    /**
     * 红包
     * [EAC0BE58AA7A44F59F1675B5ENVELOPE,][数额][人数][祝福语]
     */
    public static final String MESSAGE_FLAG_ENVELOPE = "[EAC0BE58AA7A44F59F1675B5ENVELOPE,";
    /**
     * 转账
     * [EAC0BE58AA7A44F59F1675BGIVEMONEY,][数额][备注]
     */
    public static final String MESSAGE_FLAG_GIVEMONEY = "[EAC0BE58AA7A44F59F1675BGIVEMONEY,";
    /**
     * 礼物
     * [EAC0BE58AA7A44F59F1675B5E6AEGIFT,][礼物代码][礼物金额]
     */
    public static final String MESSAGE_FLAG_GIFT = "[EAC0BE58AA7A44F59F1675B5E6AEGIFT,";
    public static final String MESSAGE_FLAG_PASSEDFRIEND="[EAC0BE58AA7A44F59F16PASSEDFRIEND,";//我同意好友的申请
    public static final String MESSAGE_FLAG_FRIENDAGREE="[EAC0BE58AA7A44F59F167FRIENDAGREE,";//好友同意我的申请
}
