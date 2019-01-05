package cc.lzsou.media;

/**
 * Created by dmcBig on 2017/6/9.
 */

public class PickerConfig {
    public static final String LOG_TAG = "MediaPicker";
    /**
     * 最大图片选择次数，int类型，默认9
     */
    public static final String MAX_SELECT_COUNT = "max_select_count";

    public static final int DEFAULT_SELECTED_MAX_COUNT = 9;
    /**
     * 最大文件大小，int类型，默认99m
     */
    public static final String MAX_SELECT_SIZE = "max_select_size";

    public static final long DEFAULT_SELECTED_MAX_SIZE = 188743680;

    /**
     * 图片选择模式，默认选视频和图片
     */
    public static final String SELECT_MODE = "select_mode";
    /**
     * 默认选择集
     */
    public static final String DEFAULT_SELECTED_LIST = "default_list";
    /**
     *截图参数
     */
    public static final String DEFAULT_CROP_FILEPATH="crop_file";
    /**
     * 预览集
     */
    public static final String PRE_RAW_LIST = "pre_raw_List";
    public static final int RESULT_CODE = 19901026;
    public static final int RESULT_UPDATE_CODE = 1990;
    public static final int PICKER_IMAGE = 100;
    public static final int PICKER_VIDEO = 102;
    public static final int PICKER_IMAGE_VIDEO = 101;
    public static final int REQUEST_CODE_TAKE_PHOTO=1985;
    public static int GridSpanCount = 3;
    public static int GridSpace = 4;
}
