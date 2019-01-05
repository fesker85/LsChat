package cc.lzsou.lschat.core.helper;

import java.util.Comparator;

import cc.lzsou.lschat.data.bean.FriendEntity;

public class LsPinyinComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        String str1 = LsPinyinHelper.getPingYin(((FriendEntity)o1).getNickname());
        String str2 = LsPinyinHelper.getPingYin(((FriendEntity)o2).getNickname());
        return str1.compareToIgnoreCase(str2);
    }

}
