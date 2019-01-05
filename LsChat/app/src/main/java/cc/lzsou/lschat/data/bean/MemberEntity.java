package cc.lzsou.lschat.data.bean;

/**
 * 用户信息
 */
public class MemberEntity {
    private int id;
    private String platid;
    private String name;//姓名
    private String idnumber;//身份证号
    private String nickname;//昵称
    private int sex;//性别
    private String mobile;//手机号码
    private String email;//电子邮箱
    private String username;//帐号
    private String password;//登录密码
    private String paypwd;//支付密码
    private String avatar;//头像地址
    private String region;//所在区域
    private String regionid;//所在区域id
    private String address;//详细地址
    private String jobunit;//工作单位
    private String phone;//固定电话
    private String wxid;
    private String qqid;
    private String wbid;
    private String aliid;
    private int vip;//会员
    private String vipexpire;//会员到期
    private String createtime;
    private String updatetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatid() {
        return platid;
    }

    public void setPlatid(String platid) {
        this.platid = platid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionid() {
        return regionid;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobunit() {
        return jobunit;
    }

    public void setJobunit(String jobunit) {
        this.jobunit = jobunit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getQqid() {
        return qqid;
    }

    public void setQqid(String qqid) {
        this.qqid = qqid;
    }

    public String getWbid() {
        return wbid;
    }

    public void setWbid(String wbid) {
        this.wbid = wbid;
    }

    public String getAliid() {
        return aliid;
    }

    public void setAliid(String aliid) {
        this.aliid = aliid;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getVipexpire() {
        return vipexpire;
    }

    public void setVipexpire(String vipexpire) {
        this.vipexpire = vipexpire;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getSexString(){
        if(sex==0)return "未知";
        if(sex==1)return "男";
        if(sex==2)return "女";
        return "";
    }
}
