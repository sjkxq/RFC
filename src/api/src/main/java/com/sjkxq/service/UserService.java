package com.sjkxq.service;


import com.sjkxq.annotation.Retryable;
import com.sjkxq.pojo.User;

/**
 * @InterfaceName UserService
 * @Description 接口
 */

public interface UserService {

    // 查询
    @Retryable
    User getUserByUserId(Integer id);

    // 新增
    @Retryable
    Integer insertUserId(User user);
}
