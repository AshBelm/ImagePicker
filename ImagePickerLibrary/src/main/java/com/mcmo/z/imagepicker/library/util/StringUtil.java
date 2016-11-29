package com.mcmo.z.imagepicker.library.util;


/**
 * Created by weizhang210142 on 2016/5/9.
 */
public class StringUtil {
    public static boolean empty(String str){
        return str==null || str.trim().length()==0 || str.equals("null");
    }
    public static String string(String str){
        if(empty(str)){
            return "";
        }else{
            return str;
        }
    }


}
