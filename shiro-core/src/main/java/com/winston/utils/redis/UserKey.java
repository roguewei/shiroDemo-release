package com.winston.utils.redis;

/**
 * @author weigaosheng
 * @description
 * @CalssName UserKey
 * @date 2019/3/4
 * @params
 * @return
 */
public class UserKey extends BasePrefix {

    private UserKey(int expireSeconds, String preFix) {
        super(expireSeconds, preFix);
    }

    private UserKey(String preFix){
        super(preFix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");

    public static UserKey getByIdOrTimeOut = new UserKey(20, "id");
    public static UserKey getByNameOrTimeOut = new UserKey(60*60,"name");
}
