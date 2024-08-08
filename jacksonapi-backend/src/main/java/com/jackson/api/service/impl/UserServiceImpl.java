package com.jackson.api.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackson.api.common.StatusCode;
import com.jackson.api.exception.BusinessException;
import com.jackson.api.mapper.UserInterfaceInvokeMapper;
import com.jackson.api.mapper.UserMapper;
import com.jackson.api.service.InterfaceInfoService;
import com.jackson.api.service.UserService;
import com.jackson.apicommon.model.entity.InterfaceInfo;
import com.jackson.apicommon.model.entity.User;
import com.jackson.apicommon.model.entity.UserInterfaceInvoke;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static com.jackson.api.constant.UserConstant.ADMIN_ROLE;
import static com.jackson.api.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 *
 * @author jackson
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInterfaceInvokeMapper userInterfaceInvokeMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "jackson_api_7788";

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return
     */
    @Override
    public long userRegister(String userName, String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        // 参数不能为空
        if (StringUtils.isAnyBlank(userName, userAccount, userPassword, checkPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "参数为空");
        }
        // 用户昵称要符合要求
        if (userName.length() < 1 || userName.length() > 10) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "昵称长度应该在 1-10 之间");
        }
        // 账号长度要符合要求
        if (userAccount.length() < 4 || userAccount.length() > 10) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号长度应该在 4-10 之间");
        }
        // 密码和确认密码要相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 密码长度要符合要求
        if (userPassword.length() < 6) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户密码不能少于 6 位");
        }
        // 加锁，防止多线程创建同一个账户，使用 intern() 保证字符串引用的唯一性
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(StatusCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 分配 accessKey secretKey
            String accessKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
            // 4. 插入数据
            User user = new User();
            user.setUserName(userName);
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(StatusCode.SYSTEM_ERROR, "注册失败，请重试");
            }

            // 5. 为用户增加 api 调用的次数
            InterfaceInfo interfaceInfo = new InterfaceInfo();
            QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>(interfaceInfo);
            List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(interfaceInfoQueryWrapper);
            for (InterfaceInfo info : interfaceInfoList) {
                UserInterfaceInvoke userInterfaceInvoke = new UserInterfaceInvoke();
                Long interfaceId = info.getId();
                userInterfaceInvoke.setUserId(user.getId());
                userInterfaceInvoke.setInterfaceId(interfaceId);
                userInterfaceInvoke.setLeftNum(100);
                userInterfaceInvokeMapper.insert(userInterfaceInvoke);
            }

            // save() 方法保存对象到数据库后，会自动将用户 id 赋值给 user
            return user.getId();
        }
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(StatusCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(StatusCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return user;
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(StatusCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && ADMIN_ROLE.equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(StatusCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }
}
