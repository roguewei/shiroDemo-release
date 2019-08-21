package com.winston.constant;

public interface Commons {


    /**********************正则*************************/
    /***
     * 密码校验正则只能包含字母数字和下划线 6-20密码
     */
    String PWD_REGEXP = "^[0-9a-zA-Z_]{6,20}$";

    /***
     * 强密码正则 8- 20 喂不包含特殊字符
     *
     */
    String PWD_REGEXP_STRONG = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
    /***
     * 账号正则
     */
    String ACCOUNT_REGEXP = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
    /***
     * 邮箱正则
     */
    String EMAIL_REGEXP = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /***
     * 手机号码正则
     */
    String MOBOLE_REGEXP = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

    /**
     * 年龄正则
     */

    String AGE_REGEXP = "^((1[0-5])|[1-9])?\\d$";

    String IDENTIFY_NO_REGEXP = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$";


    String INIT_PWD = "11111111";

    String [] COLUMNS = {"no","contacts","sex","companyName","position","contactsMobile","area","mainBusiness","businessContent","userName","dateTime","state","alreadyDone","canDevelopble"};

    String USER_ID = "userId";
}
