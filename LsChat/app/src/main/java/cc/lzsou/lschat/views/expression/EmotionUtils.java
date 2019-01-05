package cc.lzsou.lschat.views.expression;

import java.util.LinkedHashMap;

import cc.lzsou.lschat.R;

public class EmotionUtils {
    /**
     * key-表情文字;
     * value-表情图片资源
     */
    public static LinkedHashMap<String, Integer> EMPTY_GIF_MAP;
    public static LinkedHashMap<String, Integer> EMOTION_STATIC_MAP;


    static {
        EMPTY_GIF_MAP = new LinkedHashMap<>();
        EMPTY_GIF_MAP.put("[/g000]", R.mipmap.g000);
        EMPTY_GIF_MAP.put("[/g001]", R.mipmap.g001);
        EMPTY_GIF_MAP.put("[/g002]", R.mipmap.g002);
        EMPTY_GIF_MAP.put("[/g003]", R.mipmap.g003);
        EMPTY_GIF_MAP.put("[/g004]", R.mipmap.g004);
        EMPTY_GIF_MAP.put("[/g005]", R.mipmap.g005);
        EMPTY_GIF_MAP.put("[/g006]", R.mipmap.g006);
        EMPTY_GIF_MAP.put("[/g007]", R.mipmap.g007);
        EMPTY_GIF_MAP.put("[/g008]", R.mipmap.g008);
        EMPTY_GIF_MAP.put("[/g009]", R.mipmap.g009);
        EMPTY_GIF_MAP.put("[/g010]", R.mipmap.g010);
        EMPTY_GIF_MAP.put("[/g011]", R.mipmap.g011);
        EMPTY_GIF_MAP.put("[/g012]", R.mipmap.g012);
        EMPTY_GIF_MAP.put("[/g013]", R.mipmap.g013);
        EMPTY_GIF_MAP.put("[/g015]", R.mipmap.g014);
        EMPTY_GIF_MAP.put("[/g015]", R.mipmap.g015);
        EMPTY_GIF_MAP.put("[/g016]", R.mipmap.g016);
        EMPTY_GIF_MAP.put("[/g017]", R.mipmap.g017);
        EMPTY_GIF_MAP.put("[/g018]", R.mipmap.g018);
        EMPTY_GIF_MAP.put("[/g019]", R.mipmap.g019);
        EMPTY_GIF_MAP.put("[/g020]", R.mipmap.g020);
        EMPTY_GIF_MAP.put("[/g021]", R.mipmap.g021);
        EMPTY_GIF_MAP.put("[/g022]", R.mipmap.g022);
        EMPTY_GIF_MAP.put("[/g023]", R.mipmap.g023);
        EMPTY_GIF_MAP.put("[/g025]", R.mipmap.g025);
        EMPTY_GIF_MAP.put("[/g026]", R.mipmap.g026);
        EMPTY_GIF_MAP.put("[/g027]", R.mipmap.g027);
        EMPTY_GIF_MAP.put("[/g028]", R.mipmap.g028);
        EMPTY_GIF_MAP.put("[/g029]", R.mipmap.g029);
        EMPTY_GIF_MAP.put("[/g030]", R.mipmap.g030);
        EMPTY_GIF_MAP.put("[/g031]", R.mipmap.g031);
        EMPTY_GIF_MAP.put("[/g032]", R.mipmap.g032);
        EMPTY_GIF_MAP.put("[/g033]", R.mipmap.g033);
        EMPTY_GIF_MAP.put("[/g034]", R.mipmap.g034);
        EMPTY_GIF_MAP.put("[/g035]", R.mipmap.g035);


        EMOTION_STATIC_MAP = new LinkedHashMap<>();

        EMOTION_STATIC_MAP.put("[/f000]", R.mipmap.f000);
        EMOTION_STATIC_MAP.put("[/f001]", R.mipmap.f001);
        EMOTION_STATIC_MAP.put("[/f002]", R.mipmap.f002);
        EMOTION_STATIC_MAP.put("[/f003]", R.mipmap.f003);
        EMOTION_STATIC_MAP.put("[/f004]", R.mipmap.f004);
        EMOTION_STATIC_MAP.put("[/f005]", R.mipmap.f005);
        EMOTION_STATIC_MAP.put("[/f006]", R.mipmap.f006);
        EMOTION_STATIC_MAP.put("[/f007]", R.mipmap.f007);
        EMOTION_STATIC_MAP.put("[/f008]", R.mipmap.f008);
        EMOTION_STATIC_MAP.put("[/f009]", R.mipmap.f009);
        EMOTION_STATIC_MAP.put("[/f010]", R.mipmap.f010);
        EMOTION_STATIC_MAP.put("[/f011]", R.mipmap.f011);
        EMOTION_STATIC_MAP.put("[/f012]", R.mipmap.f012);
        EMOTION_STATIC_MAP.put("[/f013]", R.mipmap.f013);
        EMOTION_STATIC_MAP.put("[/f014]", R.mipmap.f014);
        EMOTION_STATIC_MAP.put("[/f015]", R.mipmap.f015);
        EMOTION_STATIC_MAP.put("[/f016]", R.mipmap.f016);
        EMOTION_STATIC_MAP.put("[/f017]", R.mipmap.f017);
        EMOTION_STATIC_MAP.put("[/f018]", R.mipmap.f018);
        EMOTION_STATIC_MAP.put("[/f019]", R.mipmap.f019);
        EMOTION_STATIC_MAP.put("[/f020]", R.mipmap.f020);
        EMOTION_STATIC_MAP.put("[/f021]", R.mipmap.f021);
        EMOTION_STATIC_MAP.put("[/f022]", R.mipmap.f022);
        EMOTION_STATIC_MAP.put("[/f023]", R.mipmap.f023);
        EMOTION_STATIC_MAP.put("[/f024]", R.mipmap.f024);
        EMOTION_STATIC_MAP.put("[/f025]", R.mipmap.f025);
        EMOTION_STATIC_MAP.put("[/f026]", R.mipmap.f026);
        EMOTION_STATIC_MAP.put("[/f027]", R.mipmap.f027);
        EMOTION_STATIC_MAP.put("[/f028]", R.mipmap.f028);
        EMOTION_STATIC_MAP.put("[/f029]", R.mipmap.f029);
        EMOTION_STATIC_MAP.put("[/f030]", R.mipmap.f030);
        EMOTION_STATIC_MAP.put("[/f031]", R.mipmap.f031);
        EMOTION_STATIC_MAP.put("[/f032]", R.mipmap.f032);
        EMOTION_STATIC_MAP.put("[/f033]", R.mipmap.f033);
        EMOTION_STATIC_MAP.put("[/f034]", R.mipmap.f034);
        EMOTION_STATIC_MAP.put("[/f035]", R.mipmap.f035);
        EMOTION_STATIC_MAP.put("[/f036]", R.mipmap.f036);
        EMOTION_STATIC_MAP.put("[/f037]", R.mipmap.f037);
        EMOTION_STATIC_MAP.put("[/f038]", R.mipmap.f038);
        EMOTION_STATIC_MAP.put("[/f039]", R.mipmap.f039);
        EMOTION_STATIC_MAP.put("[/f040]", R.mipmap.f040);
        EMOTION_STATIC_MAP.put("[/f041]", R.mipmap.f041);
        EMOTION_STATIC_MAP.put("[/f042]", R.mipmap.f042);
        EMOTION_STATIC_MAP.put("[/f043]", R.mipmap.f043);
    }
}
