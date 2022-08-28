package com.lyz.service.impl;

import com.lyz.entity.Group;
import com.lyz.mapper.GroupMapper;
import com.lyz.service.GroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyz
 * @since 2022-01-30
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

}
