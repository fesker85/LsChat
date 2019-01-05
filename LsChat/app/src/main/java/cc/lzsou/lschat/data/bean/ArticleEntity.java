package cc.lzsou.lschat.data.bean;

/**
 * 简单的文章实体
 */
public class ArticleEntity {

    public static final int MODE_NORMAL = 0;
    public static final int MODE_IMAGE = 1;
    public static final int MODE_VIDEO = 2;
    public static final int MODE_ADIMAGE=3;
    public static final int MODE_ADVIDEO=4;

    private long id; //id
    private String title; //标题
    private String classid;//分类
    private String source; //来源
    private String hits;//点击量
    private String image1;//图片1
    private String image2;//图片2
    private String image3;//图片3
    private String mark;//标签
    private int mode;//模式

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
