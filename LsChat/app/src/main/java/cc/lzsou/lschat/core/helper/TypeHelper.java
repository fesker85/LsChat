package cc.lzsou.lschat.core.helper;

public class TypeHelper {

    public static int objectToInt(Object o){
        return  objectToInt(o,0);
    }

    public static int objectToInt(Object o,int defaultValue){
        if(o==null)return defaultValue;
        String s = o.toString();
        if(s==null||s.equals(""))return defaultValue;
        return Integer.parseInt(s);
    }
}
