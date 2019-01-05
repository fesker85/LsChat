package cc.lzsou.lschat.data.bean;

/**
 * 地图POI地址信息
 */
public class POIEntity {
    private String address;//当前 POI 的地址
    private String catalog;//当前 POI 的类别
    private String direction;//当前位置(定位中心点)的方向关系
    private double distance;//当前 POI 跟当前位置的距离, 单位为 m(米)
    private double 	latitude;//当前 POI 的纬度.
    private double longitude;//当前 POI 的经度.
    private String name;//当前 POI 的名称
    private String uid;//当前 POI 的 uid
    private int select;//是否选中当前poi
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }
}
