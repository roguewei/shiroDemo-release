package com.winston.utils.result;

import lombok.Data;

/**
 * @author weigaosheng
 * @description
 * @CalssName CodeMsg
 * @date 2019/2/28
 * @params
 * @return
 */
@Data
public class CodeMsg {
    private int status;
    private String msg;

    public static CodeMsg SUCCESS = new CodeMsg(0, "success");

    // 通用异常5001XX
    public static CodeMsg USERNAME_NOT_EMPTY = new CodeMsg(500100, "用户名不能为空");
    public static CodeMsg PASSWORD_NOT_EMPTY = new CodeMsg(500101, "密码不能为空");
    public static CodeMsg ACCOUNT_NOT_EXIST = new CodeMsg(500102, "用户不存在");
    public static CodeMsg ACCOUNT_IS_LOCKED = new CodeMsg(500103, "用户被锁定");
    public static CodeMsg USERNAME_OR_PASSWORD_ERROR = new CodeMsg(500104, "用户名或密码错误");
    public static CodeMsg IS_NOT_LOGIN = new CodeMsg(500105, "未登录，请先登录");
    public static CodeMsg USER_UPDATE_FAILED = new CodeMsg(500106, "用户修改失败");
    public static CodeMsg USER_ALREADY_EXIST = new CodeMsg(500107, "用户已存在");
    public static CodeMsg USER_ADD_FAILE = new CodeMsg(500108, "用户新增失败");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500109, "服务端异常");
    public static CodeMsg SERVER_UPDATE_ERROR = new CodeMsg(500110, "系统信息修改失败");
    public static CodeMsg SERVER_UPDATE_NOT_ID = new CodeMsg(500111, "请传递资源id");
    public static CodeMsg WECHAT_GET_ACCESSTOKEN_ERROR = new CodeMsg(500112, "获取ACCESS_TOKEN失败");
    public static CodeMsg WECHAT_GET_TEMPLATEID_ERROR = new CodeMsg(500113  , "获取模板id失败");
    public static CodeMsg WECHAT_SEND_TEMPLATEID_ERROR = new CodeMsg(500114  , "模板消息发送失败");
    public static CodeMsg FILE_YASUO_ERROR = new CodeMsg(500115  , "图片压缩失败");
    public static CodeMsg FILE_TOO_BIG = new CodeMsg(500116  , "上传图片过大，请重新上传");

    // 登录模块5002XX
    public static CodeMsg USER_IS_NULL = new CodeMsg(500200, "操作对象不存在");
    public static CodeMsg UPDATE_IS_FAILED = new CodeMsg(500201, "修改失败");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500202, "输入密码错误");
    public static CodeMsg ACCOUNT_LOCKED = new CodeMsg(500203, "此用户已被被冻结,如有疑问请联系管理员");
    public static CodeMsg ACCOUNT_STOP = new CodeMsg(500204, "用户已停止使用");
    public static CodeMsg LOGIN_TIME_PASS = new CodeMsg(500205, "登陆过期,请重新登录");

    // 角色模块5003XX
    public static CodeMsg ROLE_USER_NO_CHOOES = new CodeMsg(500300, "请选择用户和角色");
    public static CodeMsg ROLE_USER_SAVE_ERROR = new CodeMsg(500301, "分配角色错误");
    public static CodeMsg ROLE_ADD_ERROR = new CodeMsg(500302, "角色新增失败");
    public static CodeMsg ROLE_UPDATE_ERROR = new CodeMsg(500303, "角色修改失败");
    public static CodeMsg ROLE_DEL_ERROR = new CodeMsg(500304, "角色删除失败");
    public static CodeMsg SELECT_ROLE_ERROR = new CodeMsg(500305, "请选择角色");
    public static CodeMsg SELECT_PERMOSSION_ERROR = new CodeMsg(500306, "请选择角色");
    public static CodeMsg SELECT__ERROR = new CodeMsg(500307, "分配角色出现异常");
    public static CodeMsg ROLE_HAS_USER_USE = new CodeMsg(500308, "该角色仍有用户使用,请先移除该角色用户");

    // 权限模块5004XX
    public static CodeMsg HAS_NOT_PERMISSION = new CodeMsg(500400, "您没有该权限");
    public static CodeMsg PERMISSION_ALERADY_EXIST = new CodeMsg(500401, "该权限已存在，请勿重复添加");
    public static CodeMsg PERMISSION_PARAM_NULL = new CodeMsg(500402, "请填写权限名称和路径");
    public static CodeMsg PERMISSION_ADD_ERROR = new CodeMsg(500403, "权限新增失败");
    public static CodeMsg PERMISSION_UPDATE_ERROR = new CodeMsg(500404, "权限更新失败");
    public static CodeMsg PERMISSION_DEL_ERROR = new CodeMsg(500405, "权限删除失败");
    public static CodeMsg PERMISSION_HAS_ROLE_USE = new CodeMsg(500406, "该权限正在被其他角色使用，请先移除该角色权限");

    // 村镇模块5005XX
    public static CodeMsg TOWNS_ALREADY_EXIST = new CodeMsg(500500, "该镇村已存在");
    public static CodeMsg TOWNS_NAME_NULL = new CodeMsg(500501, "请填写镇村名称");
    public static CodeMsg TOWNS_UPDATE_ERROR = new CodeMsg(500502, "镇村修改失败");
    public static CodeMsg TOWNS_DEL_HAVE_USER = new CodeMsg(500503, "请先移除该镇村的用户");

    // 轮播图模块5006XX
    public static CodeMsg WHEEL_NAME_NULL = new CodeMsg(500600, "请填写图片标题");
    public static CodeMsg WHEEL_UPDATE_ERROR = new CodeMsg(500601, "轮播图修改失败");

    // 景区模块5007XX
    public static CodeMsg SCENERY_NAME_NULL = new CodeMsg(500700, "请填写景区标题");
    public static CodeMsg SCENERY_UPDATE_ERROR = new CodeMsg(500701, "景区信息修改失败");
    public static CodeMsg SCENERY_ALREADY_EXIST = new CodeMsg(500702, "该景区已存在");
    public static CodeMsg SCENERY_DEL_ERROR = new CodeMsg(500703, "景区信息删除失败");
    public static CodeMsg SCENERY_ADD_ERROR = new CodeMsg(500704, "景区信息新增失败");

    // 美食模块5008XX
    public static CodeMsg FOOD_NAME_NULL = new CodeMsg(500800, "请填写美食标题");
    public static CodeMsg FOOD_UPDATE_ERROR = new CodeMsg(500801, "美食信息修改失败");
    public static CodeMsg FOOD_ALREADY_EXIST = new CodeMsg(500802, "该美食已存在");
    public static CodeMsg FOOD_DEL_ERROR = new CodeMsg(500803, "美食信息删除失败");
    public static CodeMsg FOOD_ADD_ERROR = new CodeMsg(500804, "美食信息新增失败");

    // 风土人情模块5009XX
    public static CodeMsg AMOROUS_NAME_NULL = new CodeMsg(500900, "请填写特色风情简介");
    public static CodeMsg AMOROUS_UPDATE_ERROR = new CodeMsg(500901, "特色风情信息修改失败");
    public static CodeMsg AMOROUS_ALREADY_EXIST = new CodeMsg(500902, "该特色风情已存在");
    public static CodeMsg AMOROUS_DEL_ERROR = new CodeMsg(500903, "特色风情信息删除失败");
    public static CodeMsg AMOROUS_ADD_ERROR = new CodeMsg(500904, "特色风情信息新增失败");

    // 企业模块5010XX
    public static CodeMsg ENTERPRISE_NAME_NULL = new CodeMsg(501000, "请填写企业名称");
    public static CodeMsg ENTERPRISE_UPDATE_ERROR = new CodeMsg(501001, "企业信息修改失败");
    public static CodeMsg ENTERPRISE_ALREADY_EXIST = new CodeMsg(501002, "该企业已存在");
    public static CodeMsg ENTERPRISE_DEL_ERROR = new CodeMsg(501003, "企业信息删除失败");
    public static CodeMsg ENTERPRISE_ADD_ERROR = new CodeMsg(501004, "企业信息新增失败");

    // 名人模块5011XX
    public static CodeMsg FAMOUS_NAME_NULL = new CodeMsg(501100, "请填写名人姓名");
    public static CodeMsg FAMOUS_UPDATE_ERROR = new CodeMsg(501101, "名人信息修改失败");
    public static CodeMsg FAMOUS_ALREADY_EXIST = new CodeMsg(501102, "该名人已存在");
    public static CodeMsg FAMOUS_DEL_ERROR = new CodeMsg(501103, "名人信息删除失败");
    public static CodeMsg FAMOUS_ADD_ERROR = new CodeMsg(501104, "名人信息新增失败");

    // 资讯模块5012XX
    public static CodeMsg INFORMATION_NAME_NULL = new CodeMsg(501200, "请填写资讯标题和内容");
    public static CodeMsg INFORMATION_UPDATE_ERROR = new CodeMsg(501201, "资讯信息修改失败");
    public static CodeMsg INFORMATION_ALREADY_EXIST = new CodeMsg(501202, "该资讯已存在");
    public static CodeMsg INFORMATION_DEL_ERROR = new CodeMsg(501203, "资讯信息删除失败");
    public static CodeMsg INFORMATION_ADD_ERROR = new CodeMsg(501204, "资讯信息新增失败");

    // 消息推送模块5013XX
    public static CodeMsg MESSAGE_NAME_NULL = new CodeMsg(501300, "请填写消息标题和内容");
    public static CodeMsg MESSAGE_UPDATE_ERROR = new CodeMsg(501301, "消息修改失败");
    public static CodeMsg MESSAGE_ALREADY_EXIST = new CodeMsg(501302, "该消息已存在");
    public static CodeMsg MESSAGE_DEL_ERROR = new CodeMsg(501303, "消息删除失败");
    public static CodeMsg MESSAGE_ADD_ERROR = new CodeMsg(501304, "消息新增失败");

    // 消息推送模块5013XX
    public static CodeMsg DIFFAMILY_NAME_NULL = new CodeMsg(501300, "请填写户主姓名");
    public static CodeMsg DIFFAMILY_UPDATE_ERROR = new CodeMsg(501301, "家庭信息修改失败");
    public static CodeMsg DIFFAMILY_ALREADY_EXIST = new CodeMsg(501302, "该家庭信息已存在");
    public static CodeMsg DIFFAMILY_DEL_ERROR = new CodeMsg(501303, "家庭信息删除失败");
    public static CodeMsg DIFFAMILY_ADD_ERROR = new CodeMsg(501304, "家庭信息新增失败");

    public CodeMsg(int code, String msg) {
        this.status = code;
        this.msg = msg;
    }

    public CodeMsg(){

    }

    public CodeMsg fillArsg(Object... args){
        int code = this.status;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

}
