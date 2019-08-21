package com.winston.utils.redis;

public class SessionRedisKey extends BasePrefix {



    public SessionRedisKey(int expireSeconds, String preFix) {
        super(expireSeconds, preFix);
    }

    public SessionRedisKey(String preFix) {
        super(preFix);
    }

    public static SessionRedisKey sessionKey = new SessionRedisKey("personCount");
    public static SessionRedisKey JSESSION_KEY = new SessionRedisKey(60*60*27*7, "shiro:session:");
    public static SessionRedisKey sessionKeyByEx = new SessionRedisKey(60*60,"personCount");
}
