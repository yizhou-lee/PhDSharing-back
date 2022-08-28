package com.lyz.service.impl;

import com.lyz.entity.User;
import com.lyz.mapper.UserMapper;
import com.lyz.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyz
 * @since 2021-12-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
