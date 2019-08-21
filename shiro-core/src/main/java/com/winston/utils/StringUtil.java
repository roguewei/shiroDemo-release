package com.winston.utils;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author weigaosheng
 * @description
 * @CalssName StringUtil
 * @date 2018/12/25
 * @params
 * @return
 */
public class StringUtil {

    /**
     * 数值类型前面补零（共13位）
     * @param num
     * @return
     */
    public static String supplementZeroGenerateThirteen(int num){
        String str = String.format("%013d", num);

        return str;
    }

    /**
     * 数值类型前面补零（共16位）
     * @param num
     * @return
     */
    public static String supplementZeroGenerateSixteen(int num){
        String str = String.format("%016d", num);

        return str;
    }
    /**
     * 数值类型前面补零（共3位）
     * @param num
     * @return
     */
    public static String supplementZeroGenerateThree(int num){
        String str = String.format("%03d", num);

        return str;
    }

    /**
     * 判断字符串是不是double型
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    public static String getMapToXML(Map<String,String> param){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String,String> entry : param.entrySet()) {
            sb.append("<"+ entry.getKey() +">");
            sb.append(entry.getValue());
            sb.append("</"+ entry.getKey() +">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    //随机字符串生成
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = UUID.randomUUID()+"";
        base = base.replaceAll("-","");
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
