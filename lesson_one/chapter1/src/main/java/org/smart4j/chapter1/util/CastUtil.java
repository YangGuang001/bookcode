package org.smart4j.chapter1.util;

/**
 * Created by yz on 2017/7/16.
 */
public class CastUtil {
    public static String castString(Object obj){
        return castString(obj, "");
    }

    public static String castString(Object obj, String defalutValue){
        return obj != null ? String.valueOf(obj) : defalutValue;
    }

    public static double castDouble(Object obj){
        return castDouble(obj, 0);
    }

    public static double castDouble(Object obj, double defaultVaule){
        double doubleVaule = defaultVaule;
        if (obj != null){
            String strValue = castString(obj);
            if (StringUtil.isNotEmpty(strValue)){
                try {
                    doubleVaule = Double.parseDouble(strValue);
                }catch (NumberFormatException e) {
                    doubleVaule = defaultVaule;
                }
            }
        }
        return doubleVaule;
    }

    public static long castLong(Object obj){
        return castLong(obj, 0);
    }

    public static long castLong(Object obj, long defaultVaule){
        long longVaule = defaultVaule;
        if (obj != null){
            String strValue = castString(obj);
            if (StringUtil.isNotEmpty(strValue)){
                try {
                    longVaule = Long.parseLong(strValue);
                }catch (NumberFormatException e) {
                    longVaule = defaultVaule;
                }
            }
        }
        return longVaule;
    }

    public static int castInt(Object obj){
        return castInt(obj, 0);
    }

    public static int castInt(Object obj, int defaultVaule){
        int intValue = defaultVaule;
        if (obj != null){
            String strValue = castString(obj);
            if (StringUtil.isNotEmpty(strValue)){
                try {
                    intValue = Integer.parseInt(strValue);
                }catch (NumberFormatException e) {
                    intValue = defaultVaule;
                }
            }
        }
        return intValue;
    }

    public static Boolean castBoolean(Object obj){
        return castBoolean(obj, false);
    }

    public static Boolean castBoolean(Object obj, boolean defaultVaule){
        boolean booleanValue = defaultVaule;
        if (obj != null){
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }
}
