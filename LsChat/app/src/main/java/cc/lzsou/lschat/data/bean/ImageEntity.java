package cc.lzsou.lschat.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片信息
 */
public class ImageEntity implements Parcelable {
    private String path;
    private boolean isSelect = false;


    public static final Parcelable.Creator<ImageEntity> CREATOR = new Parcelable.Creator<ImageEntity>() {
        @Override
        public ImageEntity createFromParcel(Parcel in) {
            ImageEntity bean = new ImageEntity();
            bean.path = in.readString();
            //1: true  0:false
            bean.isSelect = in.readByte() != 0;
            return bean;
        }

        @Override
        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "path='" + path + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        //1: true  0:false
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
