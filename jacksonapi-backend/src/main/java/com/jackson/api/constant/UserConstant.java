package com.jackson.api.constant;

/**
 * 用户常量
 *
 * @author jackson
 */
public interface UserConstant {

    /**
     * 用户登录态
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 系统用户 id（虚拟用户）
     */
    long SYSTEM_USER_ID = 0;

    /**
     * 默认权限
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员权限
     */
    String ADMIN_ROLE = "admin";
}
