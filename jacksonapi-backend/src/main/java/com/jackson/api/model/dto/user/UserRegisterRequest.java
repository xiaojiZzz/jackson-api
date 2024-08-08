package com.jackson.api.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author jackson
 */
@Data
public class UserRegisterRequest implements Serializable {

    private String userName;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private static final long serialVersionUID = 3191241716373120793L;
}
