package cc.lzsou.lschat.data.bean;

/**
 * 发现分类
 */
public class ArticleClassEntity {
    public static final int MINE_YES=1;
    public static final int MINE_NO=0;
    public static final String ID_RECOMMAND="999";
    public static final String ID_SHCOOL="998";

    private String id; //id
    private String name;//名称
    private int sort;//排序
    private String updatetime;//更新时间
    private int mine;//是否自定义 0否 1是

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSort() {
        return sort;
    }
    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public int getMine() {
        return mine;
    }
    public void setMine(int mine) {
        this.mine = mine;
    }
}
