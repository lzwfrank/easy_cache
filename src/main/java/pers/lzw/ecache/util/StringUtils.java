package pers.lzw.ecache.util;

import com.google.common.base.Defaults;

import java.security.MessageDigest;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

public final class StringUtils {


    /**
     * 判断{@code string} 是否为空，包括空格等空串的判断
     * @param string
     * @return null或者空值返回true
     */
    public static boolean isEmpty(String string){
        return string==null || string.trim().length()==0;
    }

    /**
     * 如果{@code string}为null，则返回空值
     * @param string
     * @return null返回空值，否则原值
     */
    public static String nullToEmpty(String string){

        return string==null ? "" : string;
    }

    /**
     * 如果{@code string}为空值，则返回null
     * @param string
     * @return 空值返回null，否则原值
     */
    public static String emptyToNull(String string){

        return string.length()==0 ? null : string;
    }
                                                                                                                                                 
    /**
     * 从{@code s1}和{@code s2}的头部到尾部，找到最大共有部分
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 返回最大的共有部分
     */
    public static String commonPrefix(CharSequence s1, CharSequence s2){

        checkNotNull(s1);
        checkNotNull(s2);
        int minIndexNumber = Math.min(s1.length(), s2.length());
        String comms = Defaults.defaultValue(String.class);
        int commsPosition = 0;
        while(commsPosition < minIndexNumber && s1.charAt(commsPosition) == s2.charAt(commsPosition)){
            commsPosition++;
        }
        if(commsPosition > 0)
            comms = s1.subSequence(0,commsPosition).toString();
        return comms;
    }

    /**
     * false when either content is null or comparedString is null.
     * if ignoring case , both parameters are to upper case first, then equals them.
     *
     * @param content original string
     * @param comparedString compared string
     * @return
     */
    public static boolean equals(String content, String comparedString, boolean ignoreCase)
    {
        if(content == null || comparedString == null)
            return false;

        if(ignoreCase)
            return content.trim().toUpperCase().equals(comparedString.trim().toUpperCase());
        else
            return content.trim().equals(comparedString.trim());
    }

    /**
     * get getter method name
     * @param fieldName
     * @param prefix customize prefix name , using "get" when it's null
     * @return
     */
    public static String createGetterMethodName(String fieldName, String prefix)
    {
        String pre = prefix == null ? "get" : prefix;
        if(isNullOrEmpty(fieldName))
            return null;
        StringBuffer result = new StringBuffer();
        char c = fieldName.charAt(0);
        if(c >= 'a' && c <= 'z')	//using char because substring creates new string,
            c = (char)(c - 32);
        result.append(pre).append(c)
                .append(fieldName.substring(1, fieldName.length()));
        return result.toString();
    }

    /**
     * get setter method name
     * @param fieldName
     * @param prefix customize prefix name
     * @return
     */
    public static String createSetterMethodName(String fieldName, String prefix)
    {
        String pre = prefix ==null ? "set" : prefix;
        if(isNullOrEmpty(fieldName))
            return null;
        String result = createGetterMethodName(fieldName, prefix);	//tricky method
        return result;

    }


    /**
     * 将字符串转换成MD5,如果传入source为空,或者转换时出现异常,将得到长度为0的空字符串
     * @param source
     * @return
     */
    public static String convertToMD5(String source) {
        if(isNullOrEmpty(source))
            return Defaults.defaultValue(String.class);
        try {
            byte[] btInput = source.getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16){
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            return Defaults.defaultValue(String.class);
        }
    }

    public static String toStringValue(Object object) {

        return toStringValue(object, false);
    }

    public static String toStringValue(Object object, boolean needNullToString) {
        String result = Defaults.defaultValue(String.class);
        return needNullToString ?
                String.valueOf(object) :
                object == null ? result : String.valueOf(object);
    }

}
