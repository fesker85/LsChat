package cc.lzsou.lschat.data.bean;

/**
 * 用户好友信息
 */
public class FriendEntity {
    public static final int MODE_PERSION = 0; //人员
    public static final int MODE_GROUP = 1;//群
    public static final int MODE_NOTICE = 2;//通知

    public static final int RELATION_FRIEND_YES=1;
    public static final int RELATION_FRIEND_NO=0;
    private int id;
    private String exid;//扩展id 作为群和通知的id
    private int mode;
    private String platid;
    private String name;
    private String username;
    private String idnumber;
    private String nickname;
    private int sex;
    private String mobile;
    private String email;
    private String avatar;
    private String region;
    private String regionid;
    private String address;
    private String jobunit;
    private String phone;
    private int vip;
    private String vipexpire;
    private String remark;
    private int relation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExid() {
        return exid;
    }

    public void setExid(String exid) {
        this.exid = exid;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }
}
