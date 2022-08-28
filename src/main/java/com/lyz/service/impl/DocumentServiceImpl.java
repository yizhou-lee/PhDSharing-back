package com.lyz.service.impl;

import com.lyz.entity.Document;
import com.lyz.mapper.DocumentMapper;
import com.lyz.service.DocumentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyz
 * @since 2022-01-08
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {

}
