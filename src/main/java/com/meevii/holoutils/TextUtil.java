package com.meevii.holoutils;

import android.text.TextUtils;

/**
 * Created by DS on 12/15/16.
 */

public class TextUtil {
    public static boolean isTextEmpty(String string) {
        return TextUtils.isEmpty(string) || string.equals("null");
    }

    public static String upperFirstCase(String string) {
        String ret = "";
        if (!TextUtil.isTextEmpty(string)) {
            ret = string.toLowerCase();
            ret = Character.toString(ret.charAt(0)).toUpperCase() + ret.substring(1);
        }
        return ret;
    }
}
